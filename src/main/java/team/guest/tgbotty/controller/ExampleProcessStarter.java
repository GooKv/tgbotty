package team.guest.tgbotty.controller;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import team.guest.tgbotty.dao.ChatRepository;
import team.guest.tgbotty.entity.Chat;

import java.util.List;
import java.util.Map;

@Service
public class ExampleProcessStarter {
    private final RuntimeService runtimeService;
    private final ChatRepository chatRepository;

    @Autowired
    public ExampleProcessStarter(RuntimeService runtimeService, ChatRepository chatRepository) {
        this.runtimeService = runtimeService;
        this.chatRepository = chatRepository;
    }

    @Transactional
    public void startRepeaterProcess(Long chatId, Update update) {
        Map<String, Object> env = ImmutableMap.of(
                "chatId", chatId,
                "initialUpdate", update
        );
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("repeat-message-process", env);
        saveProcessIdInChat(chatId, processInstance.getProcessDefinitionId());
    }

    private void saveProcessIdInChat(Long chatId, String processId) {
//        Chat chat = chatRepository.findByChatId(chatId).orElse(chatRepository.save(new Chat(chatId)));
        Chat chat = chatRepository.findByChatId(chatId).orElseThrow(() -> new NoChatFoundException(chatId));
        chat.setActiveProcessId(processId);
        List<String> processList = chat.getProcessList();
        processList.add(processId);
        chat.setProcessList(processList);
        chatRepository.save(chat);
    }


    public void startProcess(String processId, Update update) {
        Map<String, Object> env = ImmutableMap.of(
                "chatId", update.getMessage().getChatId(),
                "initialUpdate", update
        );
        startProcess(processId, env);
    }

    public ProcessInstance startProcess(String processId, Map<String, Object> env) {
        return runtimeService.startProcessInstanceByKey(processId, env);
    }
}
