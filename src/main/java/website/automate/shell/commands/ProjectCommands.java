package website.automate.shell.commands;

import static website.automate.shell.support.SystemMessageUtils.format;
import static website.automate.shell.support.SystemMessageUtils.getSuccessMessage;

import java.util.List;

import org.jline.utils.AttributedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import website.automate.shell.services.AuthenticationService;
import website.automate.shell.services.ProjectService;
import website.automate.shell.services.ScenarioService;

@ShellComponent
public class ProjectCommands {

    private ProjectService projectService;

    private ScenarioService scenarioService;

    private AuthenticationService authenticationService;

    @Autowired
    public ProjectCommands(ProjectService projectService,
            AuthenticationService authenticationService, ScenarioService scenarioService) {
        this.projectService = projectService;
        this.authenticationService = authenticationService;
        this.scenarioService = scenarioService;
    }

    @ShellMethod("Authenticate at the site.")
    public AttributedString login(
            @ShellOption(help = "The username required to access the site.") String username,
            @ShellOption(help = "The password required to access the site") String password) {
        authenticationService.authenticate(username, password);
        return getSuccessMessage();
    }


    @ShellMethodAvailability("listProjects")
    public Availability listProjectsAvailability() {
        return isAuthenticatedAvailable();
    }

    @ShellMethod("List available projects.")
    public String listProjects() {
        return format(projectService.getProjectTitles());
    }

    @ShellMethodAvailability("listScenarios")
    public Availability listScenariosAvailability() {
        return isAuthenticatedAvailable();
    }

    @ShellMethod("List executable scenarios for a project.")
    public String listScenarios(
            @ShellOption(help = "The project name to list scenarios for.") String projectName) {
        return format(scenarioService.getScenarios(projectName));
    }

    @ShellMethodAvailability("runScenarios")
    public Availability runScenariosAvailability() {
        return isAuthenticatedAvailable();
    }


    @ShellMethod("Run scenario from a certain project.")
    public void runScenarios(
            @ShellOption(help = "Name of the project the scenarios come from.") String projectName,
            @ShellOption(help = "Names of the scenarios to execute") List<String> scenarioNames,
            @ShellOption(help = "Whether to run scenarios in sequence or parallel",
                    defaultValue = "false") boolean parallel) throws Exception {
        scenarioService.runScenarios(projectName, scenarioNames, parallel);
    }

    private Availability isAuthenticatedAvailable() {
        return authenticationService.isAuthenticated() ? Availability.available()
                : Availability.unavailable("you are not authenticated.");
    }
}
