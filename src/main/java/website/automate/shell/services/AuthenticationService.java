package website.automate.shell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.model.Authentication;
import website.automate.shell.support.ShellContextHolder;

@Service
public class AuthenticationService {

    private ShellContextHolder shellContextHolder;

    private ProjectRetrievalRemoteService projectRemoteService;
    
    @Autowired
    public AuthenticationService(ShellContextHolder shellContextHolder,
            ProjectRetrievalRemoteService projectRemoteService){
        this.shellContextHolder = shellContextHolder;
        this.projectRemoteService = projectRemoteService;
    }
    
    public boolean isAuthenticated(){
        return shellContextHolder.getPrincipal() != null;
    }
    
    public void authenticate(String username, String password){
        Authentication principal = Authentication.of(username, password);
        projectRemoteService.getProjectsByPrincipal(principal);
        shellContextHolder.setPrincipal(principal);
    }
}
