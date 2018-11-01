package team.guest.tgbotty.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.WebhookBot;
import team.guest.tgbotty.bot.callbacks.BotKeyboardCallback;
import team.guest.tgbotty.dao.ChatMessageRepository;
import team.guest.tgbotty.dao.ChatRepository;
import team.guest.tgbotty.entity.Chat;
import team.guest.tgbotty.entity.ChatMessage;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController("/callback")
public class CustomTgRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomTgRestController.class);

    private final ExampleProcessStarter exampleProcessStarter;
    private final Map<String, WebhookBot> registeredBots;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private String START_PROCESS_COMMAND;

    private Map<Pair<Long, Integer>, BotKeyboardCallback> keyboardCallbacks = new HashMap<>();

    @Autowired
    public CustomTgRestController(ExampleProcessStarter exampleProcessStarter,
                                  ChatRepository chatRepository,
                                  ChatMessageRepository chatMessageRepository,
                                  WebhookBot... webHookBots) {
        this.exampleProcessStarter = exampleProcessStarter;
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        registeredBots = Arrays.stream(webHookBots)
                .collect(Collectors.toMap(WebhookBot::getBotPath, Function.identity()));
    }

    @PostMapping(value = "/callback/{path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateReceived(@PathVariable String path, @RequestBody Update update) {
        try {
            WebhookBot webhookBot = this.registeredBots.get(path);

            if (webhookBot == null) {
                throw new BotNotFoundException(path);
            }

            if (update.hasCallbackQuery()) {
                return handleCallbackQueryUpdate(update.getCallbackQuery());
            }

            Long chatId = update.getMessage().getChatId();

            saveChatInfo(chatId, update);

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
        } catch (Exception e) {
            LOGGER.error("Exception in custom controller", e);
            if(update.hasMessage()) {
                String message = e.getClass().getName();
                if(e.getMessage() != null) message += ": " + e.getMessage();
                
                return new SendMessage(update.getMessage().getChatId(), message);
            }
            return null;
        }
    }

    private String[] getArguments(String messageText, String command) {
        return messageText.substring(command.length()).trim().split(" ");
    }

    @GetMapping(value = "/callback/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testReceived(@PathVariable String path) {
        return this.registeredBots.containsKey(path) ? "Hi there " + path + "!" : "Callback not found for " + path;
    }

    private void saveChatInfo(long chatId, Update update) {
        Chat chat = chatRepository.findByChatId(chatId).orElseGet(() -> chatRepository.save(new Chat(chatId)));
        List<ChatMessage> chatMessages = chat.getChatMessages();
        Message updateMessage = update.getMessage();
        Timestamp timestamp = new Timestamp(updateMessage.getDate() * 1000L);
        ChatMessage chatMessage = new ChatMessage(chat,
                                                  updateMessage.getText(),
                                                  timestamp,
                                                  updateMessage.getFrom().getBot() ? "bot" : updateMessage.getFrom().getUserName());
        chatMessageRepository.save(chatMessage);
        chatMessages.add(chatMessage);
        chat.setChatMessages(chatMessages);
        chatRepository.save(chat);

    }

    @PostMapping(value = "/startprocess/{processName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object startProcess(@PathVariable String processName, @RequestBody Map<String, Object> env) {
        return exampleProcessStarter.startProcess(processName, env).getId();
    }

    public void registerKeyboardCallback(Message message, BotKeyboardCallback callback) {
        keyboardCallbacks.put(Pair.of(message.getChatId(), message.getMessageId()), callback);
    }

    private BotApiMethod handleCallbackQueryUpdate(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        
        Message originalMessage = callbackQuery.getMessage();
        if (originalMessage == null) {
            LOGGER.warn("There are no original message for callback");
            return answerCallbackQuery;
        }

        Pair<Long, Integer> key = Pair.of(originalMessage.getChatId(), originalMessage.getMessageId());
        if (!keyboardCallbacks.containsKey(key)) {
            LOGGER.warn("Received message with no registered callback");
            return answerCallbackQuery;
        }

        BotKeyboardCallback callback = keyboardCallbacks.get(key);
        keyboardCallbacks.remove(key);

        int selectedOption;
        try {
            selectedOption = Integer.parseInt(callbackQuery.getData());
        } catch (NumberFormatException e) {
            LOGGER.warn("Unable to parse callback data", e);
            return answerCallbackQuery;
        }

        callback.answerReceived(originalMessage, selectedOption);
        
        return answerCallbackQuery;
    }

}
