package team.guest.tgbotty.bpm;

import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BpmnProcessIntrocpector {
    private final RepositoryService repositoryService;

    @Autowired
    public BpmnProcessIntrocpector(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }
}
