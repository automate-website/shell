package website.automate.shell;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class ApplicationConfigTest {

    private ApplicationConfig config = new ApplicationConfig();
    
    @Test
    public void jobRemoteServiceReturned() {
        assertNotNull(config.jobManagementRemoteService());
    }
    
    @Test
    public void scenarioRemoteServiceReturned() {
        assertNotNull(config.scenarioRemoteService());
    }
    
    @Test
    public void projectRemoteServiceReturned() {
        assertNotNull(config.projectRemoteService());
    }
}
