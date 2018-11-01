package team.guest.tgbotty.controller;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Service
public class ExampleProcessStarter {
    private final RuntimeService runtimeService;

    @Autowired
    public ExampleProcessStarter(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Transactional
    public void startRepeaterProcess(Long chatId, Update update) {
        Map<String, Object> env = ImmutableMap.of(
                "chatId", chatId,
                "initialUpdate", update
        );
        runtimeService.startProcessInstanceByKey("repeat-message-process", env);
    }
}
