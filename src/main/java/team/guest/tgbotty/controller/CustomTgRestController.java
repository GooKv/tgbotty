package team.guest.tgbotty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.WebhookBot;
import team.guest.tgbotty.dao.ChatRepository;
import team.guest.tgbotty.entity.Chat;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController("/callback")
public class CustomTgRestController {
    private final ExampleProcessStarter exampleProcessStarter;
    private final Map<String, WebhookBot> registeredBots;
    private final ChatRepository chatRepository;
    private String START_PROCESS_COMMAND;

    @Autowired
    public CustomTgRestController(ExampleProcessStarter exampleProcessStarter,
                                  ChatRepository chatRepository,
                                  WebhookBot... webHookBots) {
        this.exampleProcessStarter = exampleProcessStarter;
        this.chatRepository = chatRepository;
        registeredBots = Arrays.stream(webHookBots)
                .collect(Collectors.toMap(WebhookBot::getBotPath, Function.identity()));
    }

    @PostMapping(value = "/callback/{path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateReceived(@PathVariable String path, @RequestBody Update update) {
        WebhookBot webhookBot = this.registeredBots.get(path);

        if (webhookBot == null) {
            throw new BotNotFoundException(path);
        }

        Long chatId = update.getMessage().getChatId();

        if (!isChatExisted(chatId)) {
            chatRepository.save(new Chat(chatId));
        }

        exampleProcessStarter.startRepeaterProcess(chatId, update);

        if (update.hasMessage() && update.getMessage().isCommand()) {
            START_PROCESS_COMMAND = "/startprocess";
            String messageText = update.getMessage().getText();
            if (messageText.startsWith(START_PROCESS_COMMAND)) {
                String[] args = getArguments(messageText, START_PROCESS_COMMAND);
                exampleProcessStarter.startProcess(args[0], update);
            }
        }

        exampleProcessStarter.startRepeaterProcess(update.getMessage().getChatId(), update);
        return null;
    }

    private String[] getArguments(String messageText, String command) {
        return messageText.substring(command.length()).trim().split(" ");
    }

    @GetMapping(value = "/callback/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testReceived(@PathVariable String path) {
        return this.registeredBots.containsKey(path) ? "Hi there " + path + "!" : "Callback not found for " + path;
    }

    private boolean isChatExisted(long chatId) {
        return chatRepository.findById(chatId).isPresent();
    }

    @PostMapping(value = "/startprocess/{processName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object startProcess(@PathVariable String processName, @RequestBody Map<String, Object> env) {
        return exampleProcessStarter.startProcess(processName, env).getId();
    }
}
