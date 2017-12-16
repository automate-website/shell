package website.automate.shell.commands;

import static website.automate.shell.support.SystemMessageUtils.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import website.automate.shell.services.AuthenticationService;
import website.automate.shell.services.ScenarioService;

@ShellComponent
public class ScenarioCommands extends BaseCommands {

    private ScenarioService scenarioService;

    @Autowired
    public ScenarioCommands(AuthenticationService authenticationService, ScenarioService scenarioService) {
        super(authenticationService);
        this.scenarioService = scenarioService;
    }

    @ShellMethodAvailability("listScenarios")
    public Availability listScenariosAvailability() {
        return isAuthenticatedAvailable();
    }

    @ShellMethod("List executable scenarios for a project.")
    public String listScenarios(
            @ShellOption(help = "The project name to list scenarios for.") String project) {
        return format(scenarioService.getScenarios(project));
    }

    @ShellMethodAvailability("runScenarios")
    public Availability runScenariosAvailability() {
        return isAuthenticatedAvailable();
    }

    @ShellMethod("Run scenario from a certain project.")
    public void runScenarios(
            @ShellOption(help = "Name of the project the scenarios come from.") String project,
            @ShellOption(help = "Names of the scenarios to execute") List<String> scenarios,
            @ShellOption(help = "Whether to run scenarios in sequence or parallel",
                    defaultValue = "false") boolean parallel) throws Exception {
        scenarioService.runScenarios(project, scenarios, parallel);
    }

}
