package team.guest.tgbotty.controller;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
    private final TaskService taskService;
    private final FormService formService;
    private final ChatRepository chatRepository;

    @Autowired
    public ExampleProcessStarter(RuntimeService runtimeService,
                                 TaskService taskService,
                                 FormService formService,
                                 ChatRepository chatRepository) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.formService = formService;
        this.chatRepository = chatRepository;
    }

    private List<Task> getTaskList() {
        return taskService.createTaskQuery().taskUnassigned().includeProcessVariables().list();
    }

    public boolean hasIncompleteProcess(Long chatId) {
        return getTaskList()
                .stream()
                .anyMatch(task -> task.getProcessVariables().get("chatId") == chatId);
    }

    @Transactional
    public void startRepeaterProcess(Long chatId, Update update) {
        Map<String, Object> env = ImmutableMap.of(
                "chatId", chatId,
                "initialUpdate", update
        );

        startProcess("repeat-message-process", env);
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

    public void completeUserTask(Long chatId, Update update) {
        Task userTask = getCurrentUserTask(chatId);

        FormData formData = formService.getTaskFormData(userTask.getId());
        String propertyName = formData.getFormProperties()
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getId();
        Map<String, Object> variables = ImmutableMap.of(propertyName, update.getMessage().getText());

        completeUserTask(userTask, variables);
    }

    private void completeUserTask(Task userTask, Map<String, Object> variables) {
        taskService.complete(userTask.getId(), variables);
    }

    public void completeUserTask(Long chatId, Map<String, Object> variables) {
        completeUserTask(getCurrentUserTask(chatId), variables);
    }

    public Task getCurrentUserTask(Long chatId) {
        return getTaskList().stream()
                .filter(task -> task.getProcessVariables().get("chatId") == chatId)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public void startProcess(String processId, Update update) {
        Map<String, Object> env = ImmutableMap.of(
                "chatId", update.getMessage().getChatId(),
                "initialUpdate", update
        );
        startProcess(processId, env);
    }

    public ProcessInstance startProcess(String processId, Map<String, Object> env) {
        Long chatId = (Long) env.get("chatId");
        if (chatId != null) {
            saveProcessIdInChat(chatId, processId);
        } else {
            System.err.println("No chat id for process " + processId);
        }

        return runtimeService.startProcessInstanceByKey(processId, env);
    }
}
