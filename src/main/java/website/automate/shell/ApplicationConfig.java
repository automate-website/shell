package website.automate.shell;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import website.automate.manager.api.client.JobManagementRemoteService;
import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.ScenarioRetrievalRemoteService;

@Configuration
public class ApplicationConfig {

    @Bean
    public ProjectRetrievalRemoteService projectRemoteService(){
        return ProjectRetrievalRemoteService.getInstance();
    }
    
    @Bean
    public ScenarioRetrievalRemoteService scenarioRemoteService(){
        return ScenarioRetrievalRemoteService.getInstance();
    }
    
    @Bean
    public JobManagementRemoteService jobManagementRemoteService(){
        return JobManagementRemoteService.getInstance();
    }
}
