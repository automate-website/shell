package website.automate.shell;

import static java.util.Collections.singleton;
import website.automate.manager.api.client.model.Authentication;
import website.automate.manager.api.client.model.Job;
import website.automate.manager.api.client.model.Project;
import website.automate.manager.api.client.model.Scenario;
import website.automate.manager.api.client.model.Job.JobStatus;
import website.automate.manager.api.client.model.Job.TakeScreenshots;

public class TestBase {

    protected Authentication authentication = Authentication.of("admin", "secret");
    
    protected Project project = createProject();

    protected Scenario scenario = createScenario();
    
    protected Job newJob = createJob("scenarioId1");
    
    protected Job scheduledJob = createJob("scenarioId1", "jobId1");
    
    protected Job finishedJob = createJob("scenarioId1", "jobId1", JobStatus.SUCCESS);
    
    protected Job createJob(String scenarioId) {
        return createJob(scenarioId, null, null);
    }

    protected Job createJob(String scenarioId, String id) {
        return createJob(scenarioId, id, null);
    }

    protected Job createJob(String scenarioId, String id, JobStatus status) {
        Job job = new Job();
        job.setId(id);
        job.setTitle("jobTitle1");
        job.setScenarioIds(singleton(scenarioId));
        job.setTakeScreenshots(TakeScreenshots.ON_FAILURE);
        if (status != null) {
            job.setStatus(status);
        }
        return job;
    }

    protected Scenario createScenario() {
        return createScenario("scenarioId1", "scenarioName1");
    }
    

    protected Scenario createScenario(String id, String name) {
        Scenario scenario = new Scenario();
        scenario.setId(id);
        scenario.setName(name);
        return scenario;
    }

    protected Project createProject() {
        return createProject("projectId1", "projectTitle1");
    }
    
    protected Project createProject(String id, String title) {
        Project project = new Project();
        project.setTitle(title);
        project.setId(id);
        return project;
    }
}
