package website.automate.shell.services;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import website.automate.manager.api.client.JobManagementRemoteService;
import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.ScenarioRetrievalRemoteService;
import website.automate.manager.api.client.model.Authentication;
import website.automate.manager.api.client.model.Job.JobProfile;
import website.automate.shell.TestBase;
import website.automate.shell.ex.ProjectNotFoundException;
import website.automate.shell.factories.JobFactory;
import website.automate.shell.support.ShellContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioServiceTest extends TestBase {

    @Mock
    private ShellContextHolder shellContextHolder;

    @Mock
    private ProjectRetrievalRemoteService projectRemoteService;

    @Mock
    private ScenarioRetrievalRemoteService scenarioRemoteService;

    @Mock
    private JobManagementRemoteService jobRemoteService;

    @Mock
    private Authentication authentication;

    @Mock
    private JobFactory jobFactory;
    
    @InjectMocks
    private ScenarioService scenarioService;

    @Test
    public void scenariosAreRetrieved() {
        when(shellContextHolder.getPrincipal()).thenReturn(authentication);
        when(projectRemoteService.getProjectsByPrincipal(authentication))
                .thenReturn(asList(project));
        when(scenarioRemoteService.getExecutableScenariosByProjectIdAndPrincipal(project.getId(),
                authentication)).thenReturn(asList(scenario));

        List<String> scenarios = scenarioService.getScenarios(project.getTitle());

        assertThat(scenarios, is(asList(scenario.getName())));
    }

    @Test
    public void scenariosAreExecuted() throws Exception {
        when(shellContextHolder.getPrincipal()).thenReturn(authentication);
        when(projectRemoteService.getProjectsByPrincipal(authentication))
                .thenReturn(asList(project));
        when(scenarioRemoteService.getExecutableScenariosByProjectIdAndPrincipal(project.getId(),
                authentication)).thenReturn(asList(scenario));
        when(jobRemoteService.createJobs(asList(newJob), authentication))
                .thenReturn(asList(scheduledJob));
        when(jobRemoteService.getJobsByIdsAndPrincipal(asList(scheduledJob.getId()), authentication,
                JobProfile.MINIMAL)).thenReturn(asList(finishedJob));
        when(jobFactory.createInstances(asList(scenario.getId()), true)).thenReturn(asList(newJob));

        scenarioService.runScenarios(project.getTitle(), asList(scenario.getName()), true);
    }

    @Test(expected = ProjectNotFoundException.class)
    public void projectNotFound() throws Exception {
        scenarioService.runScenarios(project.getTitle(), asList(scenario.getName()), true);
    }
}
