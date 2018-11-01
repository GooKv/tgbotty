package team.guest.tgbotty.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.WebhookBot;
import team.guest.tgbotty.bot.callbacks.BotKeyboardCallback;
import team.guest.tgbotty.bot.callbacks.BotLocationCallback;
import team.guest.tgbotty.bot.callbacks.BotMessageCallback;
import team.guest.tgbotty.bot.callbacks.IBotCallback;
import team.guest.tgbotty.dao.ChatMessageRepository;
import team.guest.tgbotty.dao.ChatRepository;
import team.guest.tgbotty.entity.Chat;
import team.guest.tgbotty.entity.ChatMessage;
import team.guest.tgbotty.entity.SenderType;

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
    private static final String START_PROCESS_COMMAND = "/sp";
    private static final String HELP_COMMAND = "/help";

    //private Map<Pair<Long, Integer>, BotKeyboardCallback> keyboardCallbacks = new HashMap<>();
    private Map<Long, IBotCallback> callbacks = new HashMap<>();

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
            
            if(update.hasMessage())
                if(update.getMessage().hasText()) {
                    return handleChatMessage(update, update.getMessage());
                } else if(update.getMessage().hasLocation()) {
                    return handleLocation(update, update.getMessage());
                } else {
                    return handleWrongUserAction(update.getMessage().getChatId());
                }
            
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

    public void sendMessageFromSupporter(Long id, String message) {
        
    }

    public void startDialogWithHuman() {

    }

    private String[] getArguments(String messageText, String command) {
        return messageText.substring(command.length()).trim().split(" ");
    }

    @GetMapping(value = "/callback/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testReceived(@PathVariable String path) {
        return this.registeredBots.containsKey(path) ? "Hi there " + path + "!" : "Callback not found for " + path;
    }
    
    private void saveChatInfoCustomer(long chatId, Update update, String message) {
        Message updateMessage = update.getMessage();
        if(message == null) message = updateMessage.getText();
        
        saveChatInfo(chatId, message, new Timestamp(updateMessage.getDate() * 1000L), 
                updateMessage.getFrom().getUserName(), SenderType.CUSTOMER);
    }
    
    public void saveChatInfo(long chatId, String message, Timestamp timestamp, String username, SenderType senderType) {
        Chat chat = chatRepository.findByChatId(chatId).orElseGet(() -> chatRepository.save(new Chat(chatId)));
        List<ChatMessage> chatMessages = chat.getChatMessages();
        
        ChatMessage chatMessage = new ChatMessage(chat, message, timestamp, username, senderType);
        chatMessageRepository.save(chatMessage);
        
        chatMessages.add(chatMessage);
        chat.setChatMessages(chatMessages);
        
        chatRepository.save(chat);
    }

    @PostMapping(value = "/startprocess/{processName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object startProcess(@PathVariable String processName, @RequestBody Map<String, Object> env) {
        return exampleProcessStarter.startProcess(processName, env).getId();
    }

    public void registerCallback(Long messageId, IBotCallback callback) {
        callbacks.put(messageId, callback);
    }

    private BotApiMethod handleCallbackQueryUpdate(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        
        Message originalMessage = callbackQuery.getMessage();
        if (originalMessage == null) {
            LOGGER.warn("There are no original message for callback");
            return answerCallbackQuery;
        }
        
        Long chatId = originalMessage.getChatId();
        if(!callbacks.containsKey(chatId)) {
            LOGGER.warn("Received message with no registered callback");
            return answerCallbackQuery;
        }
        
        IBotCallback callback = callbacks.get(chatId);
        if(!(callback instanceof BotKeyboardCallback)) {
            //handleWrongUserAction(originalMessage.getChatId());
            return answerCallbackQuery;
        }
        
        BotKeyboardCallback keyboardCallback = (BotKeyboardCallback)callback;
        if(keyboardCallback.getOriginalMessageId() != originalMessage.getMessageId()) {
            LOGGER.warn("Received message with no registered callback");
            return answerCallbackQuery;
        }
        
        callbacks.remove(chatId);
        keyboardCallback.answerReceived(chatId, callbackQuery.getFrom(), callbackQuery.getData());
        
        return answerCallbackQuery;
    }
    
    private BotApiMethod handleChatMessage(Update update, Message message) { 
        Long chatId = message.getChatId();

        saveChatInfoCustomer(chatId, update, null);

        if(message.isCommand()) {
            String messageText = update.getMessage().getText();
            Command command = Command.fromMessage(messageText);

            switch (command.getName()) {
                case START_PROCESS_COMMAND:
                    exampleProcessStarter.startProcess(command.getArguments()[0], update);
                    break;
                case HELP_COMMAND:
                    exampleProcessStarter.startProcess("help", update);
            }
        }
        
        if(!callbacks.containsKey(chatId)) {
            // new dialog
            exampleProcessStarter.startProcess("help", update);
            return null;
        }

        IBotCallback callback = callbacks.get(chatId);
        if(!(callback instanceof BotMessageCallback)) {
            return handleWrongUserAction(chatId);
        }
        
        BotMessageCallback messageCallback = (BotMessageCallback)callback;

        callbacks.remove(chatId);
        messageCallback.answerReceived(chatId, message.getFrom(), message.getText());
        
        return null;
    }
    
    private BotApiMethod handleLocation(Update update, Message message) {
        Long chatId = message.getChatId();

        if(!callbacks.containsKey(chatId)) {
            return handleWrongUserAction(chatId);
        }

        IBotCallback callback = callbacks.get(chatId);
        if(!(callback instanceof BotLocationCallback)) {
            return handleWrongUserAction(chatId);
        }
        
        BotLocationCallback locationCallback = (BotLocationCallback) callback;
        
        Location loc = message.getLocation();
        String value = "{ " + loc.getLatitude() + ", " + loc.getLongitude() + " }";
        
        saveChatInfoCustomer(chatId, update, value);
        
        callbacks.remove(chatId);
        locationCallback.answerReceived(chatId, message.getFrom(), value);
        
        return null;
    }
    
    private SendMessage handleWrongUserAction(Long chatId) {
        return new SendMessage(chatId, "Неверный ввод");
    }

}
