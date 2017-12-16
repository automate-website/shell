package website.automate.shell.commands;

import static website.automate.shell.support.SystemMessageUtils.format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import website.automate.shell.services.AuthenticationService;
import website.automate.shell.services.ProjectService;
import website.automate.shell.services.ScenarioService;

@ShellComponent
public class ProjectCommands extends BaseCommands {

    private ProjectService projectService;

    @Autowired
    public ProjectCommands(ProjectService projectService,
            AuthenticationService authenticationService, ScenarioService scenarioService) {
        super(authenticationService);
        this.projectService = projectService;
    }

    @ShellMethodAvailability("listProjects")
    public Availability listProjectsAvailability() {
        return isAuthenticatedAvailable();
    }

    @ShellMethod("List available projects.")
    public String listProjects() {
        return format(projectService.getProjectTitles());
    }
}
