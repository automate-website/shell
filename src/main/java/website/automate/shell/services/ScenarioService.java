package website.automate.shell.services;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.stream.Stream.concat;
import static website.automate.shell.support.SystemMessageUtils.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.ExitRequest;
import org.springframework.stereotype.Service;

import website.automate.manager.api.client.JobManagementRemoteService;
import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.ScenarioRetrievalRemoteService;
import website.automate.manager.api.client.model.Job;
import website.automate.manager.api.client.model.Job.JobProfile;
import website.automate.manager.api.client.model.Job.JobStatus;
import website.automate.manager.api.client.model.Job.TakeScreenshots;
import website.automate.manager.api.client.model.Project;
import website.automate.manager.api.client.model.Scenario;
import website.automate.manager.api.client.support.Constants;
import website.automate.shell.ex.ProjectNotFoundException;
import website.automate.shell.support.ShellContextHolder;

@Service
public class ScenarioService {

    private long jobStatusPollIntervalInMs = 15000;

    private ShellContextHolder shellContextHolder;

    private ProjectRetrievalRemoteService projectRemoteService;

    private ScenarioRetrievalRemoteService scenarioRemoteService;

    private JobManagementRemoteService jobRemoteService;

    @Autowired
    public ScenarioService(ShellContextHolder shellContextHolder,
            ProjectRetrievalRemoteService projectRemoteService,
            ScenarioRetrievalRemoteService scenarioRemoteService,
            JobManagementRemoteService jobRemoteService) {
        this.shellContextHolder = shellContextHolder;
        this.scenarioRemoteService = scenarioRemoteService;
        this.projectRemoteService = projectRemoteService;
        this.jobRemoteService = jobRemoteService;
    }

    public List<String> getScenarios(String projectName) {
        Project project = getProjectByName(projectName);
        return getScenarioNamesByProjectId(project.getId());
    }

    public void runScenarios(String projectName, List<String> scenarioNames, boolean parallel)
            throws Exception {
        Project project = getProjectByName(projectName);
        Collection<Scenario> projectScenarios = getScenariosByProjectId(project.getId());

        Collection<String> selectedScenarios = projectScenarios.parallelStream()
                .filter(scenario -> scenarioNames.contains(scenario.getName()))
                .map(scenario -> scenario.getId()).collect(Collectors.toList());

        Collection<Job> jobsToCreate = createJobs(selectedScenarios, parallel);

        log("Running scenarios...");
        Collection<Job> runningJobs =
                jobRemoteService.createJobs(jobsToCreate, shellContextHolder.getPrincipal());

        Collection<Job> doneJobs = new ArrayList<>();

        waitToBeAllCompleted(project, runningJobs, doneJobs);

        exitOnErrorOrFailure(doneJobs);
    }

    private void exitOnErrorOrFailure(Collection<Job> jobs) {
        JobStatus totalStatus = getTotalStatus(jobs);
        if (hasFailedOrErrored(totalStatus)) {
            throw new ExitRequest(1);
        }
    }

    private void waitToBeAllCompleted(Project project, Collection<Job> runningJobs,
            Collection<Job> doneJobs) throws InterruptedException {
        Thread.sleep(jobStatusPollIntervalInMs);

        log("Checking job statuses...");
        Collection<Job> completedOrInProgressJobs = jobRemoteService.getJobsByIdsAndPrincipal(
                getJobIds(runningJobs), shellContextHolder.getPrincipal(), JobProfile.MINIMAL);

        Collection<Job> completedJobs = completedOrInProgressJobs.stream()
                .filter(job -> jobIsNotScheduledOrRunning(job.getStatus()))
                .collect(Collectors.toList());

        Collection<Job> inProgressJobs = completedOrInProgressJobs.stream()
                .filter(job -> isScheduledOrRunning(job.getStatus())).collect(Collectors.toList());

        doneJobs.addAll(completedJobs);

        concat(doneJobs.stream(), inProgressJobs.stream())
                .forEach(job -> logJobStatus(project, job));

        if (inProgressJobs.isEmpty()) {
            return;
        }

        waitToBeAllCompleted(project, inProgressJobs, doneJobs);
    }

    private JobStatus getTotalStatus(Collection<Job> doneJobs) {
        return doneJobs.stream().filter(job -> hasFailedOrErrored(job.getStatus()))
                .map(job -> job.getStatus()).findFirst().orElse(JobStatus.SUCCESS);
    }

    private void logJobStatus(Project project, Job job) {
        log(format("[{0}] {1}.", job.getStatus(), getReportUrl(project, job)));
    }

    private String getReportUrl(Project project, Job job) {
        return format("{0} - {1}/project/{2}/report/{3}", job.getTitle(), Constants.getAppBaseUrl(),
                project.getId(), job.getId());
    }

    private boolean jobIsNotScheduledOrRunning(JobStatus status) {
        return !isScheduledOrRunning(status);
    }

    private boolean isScheduledOrRunning(JobStatus status) {
        return status == JobStatus.SCHEDULED || status == JobStatus.RUNNING;
    }

    private boolean hasFailedOrErrored(JobStatus status) {
        return status == JobStatus.FAILURE || status == JobStatus.ERROR;
    }


    private List<String> getJobIds(Collection<Job> jobs) {
        return jobs.stream().map(job -> job.getId()).collect(Collectors.toList());
    }

    private List<Job> createJobs(Collection<String> scenarioIds, boolean parallel) {
        if (parallel) {
            return asList(createJob(scenarioIds));
        }
        return scenarioIds.stream().map(scenarioId -> createJob(scenarioId))
                .collect(Collectors.toList());
    }

    private Job createJob(String scenarioId) {
        Job job = new Job();
        job.setScenarioIds(singleton(scenarioId));
        job.setTakeScreenshots(TakeScreenshots.ON_FAILURE);
        return job;
    }

    private Job createJob(Collection<String> scenarioIds) {
        Job job = new Job();
        job.setScenarioIds(new HashSet<>(scenarioIds));
        return job;
    }

    private Project getProjectByName(String name) {
        List<Project> projects =
                projectRemoteService.getProjectsByPrincipal(shellContextHolder.getPrincipal());

        return projects.stream()
                .filter(potentialProject -> potentialProject.getTitle().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new ProjectNotFoundException());
    }

    private List<Scenario> getScenariosByProjectId(String projectId) {
        return scenarioRemoteService.getExecutableScenariosByProjectIdAndPrincipal(projectId,
                shellContextHolder.getPrincipal());
    }

    private List<String> getScenarioNamesByProjectId(String projectId) {
        return getScenariosByProjectId(projectId).stream().map(scenario -> scenario.getName())
                .collect(Collectors.toList());

    }
}
