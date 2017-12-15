package website.automate.shell.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.model.Project;
import website.automate.shell.support.ShellContextHolder;

@Service
public class ProjectService {

    private ShellContextHolder shellContextHolder;

    private ProjectRetrievalRemoteService projectRemoteService;

    @Autowired
    public ProjectService(ShellContextHolder shellContextHolder,
            ProjectRetrievalRemoteService projectRemoteService) {
        this.shellContextHolder = shellContextHolder;
        this.projectRemoteService = projectRemoteService;
    }

    public List<String> getProjectTitles() {
        List<Project> projects = projectRemoteService
                .getProjectsByPrincipal(shellContextHolder.getPrincipal());

        return projects.stream().map(project -> project.getTitle()).collect(Collectors.toList());
    }

}
