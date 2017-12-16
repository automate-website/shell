package website.automate.shell.factories;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import website.automate.manager.api.client.model.Job;
import website.automate.manager.api.client.model.Job.TakeScreenshots;

@Service
public class JobFactory {

    public List<Job> createInstances(Collection<String> scenarioIds, boolean parallel) {
        if (parallel) {
            return asList(createInstance(scenarioIds));
        }
        return scenarioIds.stream().map(scenarioId -> createInstance(scenarioId))
                .collect(Collectors.toList());
    }

    private Job createInstance(String scenarioId) {
        Job job = new Job();
        job.setScenarioIds(singleton(scenarioId));
        job.setTakeScreenshots(TakeScreenshots.ON_FAILURE);
        return job;
    }

    private Job createInstance(Collection<String> scenarioIds) {
        Job job = new Job();
        job.setScenarioIds(new HashSet<>(scenarioIds));
        return job;
    }
}
