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

import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.model.Authentication;
import website.automate.manager.api.client.model.Project;
import website.automate.shell.support.ShellContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {

    @Mock
    private ShellContextHolder shellContextHolder;

    @Mock
    private ProjectRetrievalRemoteService projectRemoteService;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private Project project;
    
    @InjectMocks
    private ProjectService projectService;
    
    @Test
    public void projectTitlesAreReturned(){
        when(shellContextHolder.getPrincipal()).thenReturn(authentication);
        when(projectRemoteService.getProjectsByPrincipal(authentication)).thenReturn(asList(project));
        when(project.getTitle()).thenReturn("foo");
        
        List<String> actualProjectTitles = projectService.getProjectTitles();
        
        assertThat(actualProjectTitles, is(asList("foo")));
    }
}
