package website.automate.shell.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.model.Project;
import website.automate.shell.TestBase;
import website.automate.shell.support.ShellContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest extends TestBase {

    @Mock
    private ShellContextHolder shellContextHolder;

    @Mock
    private ProjectRetrievalRemoteService projectRemoteService;

    @Mock
    private Project project;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void authenticationSucceeds() {
        authenticationService.authenticate("admin", "secret");

        verify(projectRemoteService).getProjectsByPrincipal(authentication);
        verify(shellContextHolder).setPrincipal(authentication);
    }

    @Test
    public void authenticationChecked() {
        when(shellContextHolder.getPrincipal()).thenReturn(authentication);

        authenticationService.isAuthenticated();
    }
}
