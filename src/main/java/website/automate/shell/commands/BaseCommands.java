package website.automate.shell.commands;

import org.springframework.shell.Availability;
import website.automate.shell.services.AuthenticationService;

public abstract class BaseCommands {

    protected AuthenticationService authenticationService;

    public BaseCommands(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    protected Availability isAuthenticatedAvailable() {
        return authenticationService.isAuthenticated() ? Availability.available()
                : Availability.unavailable("you are not authenticated.");
    }
}
