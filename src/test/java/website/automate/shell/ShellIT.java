package website.automate.shell;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import website.automate.manager.api.client.JobManagementRemoteService;
import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.ScenarioRetrievalRemoteService;
import website.automate.manager.api.client.model.Job.JobProfile;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ShellIT extends TestBase {

    @Autowired
    private Shell shell;

    @MockBean
    private ProjectRetrievalRemoteService projectRemoteService;

    @MockBean
    private ScenarioRetrievalRemoteService scenarioRemoteService;

    @MockBean
    private JobManagementRemoteService jobRemoteService;

    @Before
    public void init() {
        when(projectRemoteService.getProjectsByPrincipal(authentication))
                .thenReturn(asList(project));
        when(scenarioRemoteService.getExecutableScenariosByProjectIdAndPrincipal(project.getId(),
                authentication)).thenReturn(asList(scenario));
        when(jobRemoteService.createJobs(asList(newJob), authentication))
                .thenReturn(asList(scheduledJob));
        when(jobRemoteService.getJobsByIdsAndPrincipal(asList(scheduledJob.getId()),
                authentication, JobProfile.MINIMAL)).thenReturn(asList(finishedJob));
    }

    @Test
    public void login() {
        executeAndVerify("login admin secret", "ok.");
    }
    
    @Test
    public void listProjects() {
        login();
        executeAndVerify("list-projects", project.getTitle());
    }
    
    @Test
    public void listScenarios() {
        login();
        executeAndVerify("list-scenarios " + project.getTitle(), scenario.getName());
    }
    
    @Test
    public void runScenarios() {
        login();
        execute("run-scenarios " + project.getTitle() + " " + scenario.getName());
    }
    
    private void assertEquals(Object actualValue, String expectedValue) {
        new ComparingResultHandler(expectedValue).handleResult(actualValue);
    }
    
    private Object execute(final String commandStr) {
        return shell.evaluate(new Input() {
            @Override
            public String rawText() {
                return commandStr;
            }

        });
    }
    
    private void executeAndVerify(final String commandStr, String expectedResult) {
        Object result = execute(commandStr);
        assertEquals(result, expectedResult);
    }
    
    public static class ComparingResultHandler extends DefaultResultHandler {
        
        private String expectedValue;
        
        public ComparingResultHandler(String expectedValue) {
            this.expectedValue = expectedValue;
        }
        @Override
        public void handleResult(Object result) {
            super.handleResult(result);
            Assert.assertEquals(expectedValue, result.toString());
        }
    }
}
