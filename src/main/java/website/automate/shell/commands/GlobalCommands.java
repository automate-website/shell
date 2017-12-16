package website.automate.shell.commands;

import static website.automate.shell.support.SystemMessageUtils.getSuccessMessage;
import org.jline.utils.AttributedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import website.automate.shell.services.AuthenticationService;

@ShellComponent
public class GlobalCommands extends BaseCommands {

    @Autowired
    public GlobalCommands(AuthenticationService authenticationService) {
        super(authenticationService);
    }

    @ShellMethod("Authenticate at the site.")
    public AttributedString login(
            @ShellOption(help = "The username required to access the site.") String username,
            @ShellOption(help = "The password required to access the site") String password) {
        authenticationService.authenticate(username, password);
        return getSuccessMessage();
    }

}
