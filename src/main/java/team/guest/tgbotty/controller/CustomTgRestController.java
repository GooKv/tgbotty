package team.guest.tgbotty.controller;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.WebhookBot;
import team.guest.tgbotty.bot.BotProcessScriptsFacade;
import team.guest.tgbotty.bot.callbacks.BotKeyboardCallback;
import team.guest.tgbotty.bot.callbacks.BotLocationCallback;
import team.guest.tgbotty.bot.callbacks.BotMessageCallback;
import team.guest.tgbotty.bot.callbacks.IBotCallback;
import team.guest.tgbotty.dao.ChatMessageRepository;
import team.guest.tgbotty.dao.ChatRepository;
import team.guest.tgbotty.dao.RequestRepository;
import team.guest.tgbotty.entity.Chat;
import team.guest.tgbotty.entity.ChatMessage;
import team.guest.tgbotty.entity.Request;
import team.guest.tgbotty.entity.SenderType;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController("/callback")
public class CustomTgRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomTgRestController.class);

    private final ExampleProcessStarter exampleProcessStarter;
    private final Map<String, WebhookBot> registeredBots;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RequestRepository requestRepository;
    private static final String START_PROCESS_COMMAND = "/sp";
    private static final String HELP_COMMAND = "/help";
    private final AbsSender sender;

    //private Map<Pair<Long, Integer>, BotKeyboardCallback> keyboardCallbacks = new HashMap<>();
    private Map<Long, IBotCallback> callbacks = new HashMap<>();

    private HashMap<Long, String> avatars = new HashMap<>();

    @Value("${tg.bot.key}")
    private String token;
    
    @Autowired
    public CustomTgRestController(ExampleProcessStarter exampleProcessStarter,
                                  ChatRepository chatRepository,
                                  ChatMessageRepository chatMessageRepository,
                                  AbsSender sender,
                                  RequestRepository requestRepository, WebhookBot... webHookBots) {
        this.exampleProcessStarter = exampleProcessStarter;
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.requestRepository = requestRepository;
        this.sender = sender;
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

            if (update.hasMessage())
                if (update.getMessage().hasText()) {
                    return handleChatMessage(update, update.getMessage());
                } else if (update.getMessage().hasLocation()) {
                    return handleLocation(update, update.getMessage());
                } else {
                    return handleWrongUserAction(update.getMessage().getChatId());
                }

            return null;
        } catch (Exception e) {
            LOGGER.error("Exception in custom controller", e);
            if (update.hasMessage()) {
                String message = e.getClass().getName();
                if (e.getMessage() != null) message += ": " + e.getMessage();

                return new SendMessage(update.getMessage().getChatId(), message);
            }
            return null;
        }
    }

    public void sendMessageFromSupporter(Long chatId, String message) throws TelegramApiException {
        Message sent = sender.execute(new SendMessage(chatId, message));
        saveChatInfo(chatId, message, new Timestamp(sent.getDate() * 1000L), null, SenderType.SUPPORT);
    }

    @Transactional
    public void startDialogWithHuman(Long chatId) throws TelegramApiException {
        Chat chat = chatRepository.findByChatId(chatId).orElseThrow(() -> new NoChatFoundException(chatId));
        exampleProcessStarter.deleteProcessInstance(chat.getActiveProcessId(), "Supporter interrupt dialog");
        
        chat.setActiveProcessId(null);
        chat.setDialogMode(true);
        
        sendMessageFromSupporter(chatId, "Сотрудник подключился к диалогу");
    }

    public String assignRequest(Long chatId) {
        Chat chat = chatRepository.findByChatId(chatId).orElseThrow(() -> new NoChatFoundException(chatId));
        String requestNumber = calculateRequestNumber(chat);
        Request request = new Request(requestNumber, "text");
        request.setChat(chat);
        requestRepository.save(request);
        List<Request> chatRequests = chat.getChatRequests();
        chatRequests.add(request);
        chat.setChatRequests(chatRequests);
        return requestNumber;
    }

    private String calculateRequestNumber(Chat chat) {
        List<ChatMessage> chatMessages = chat.getChatMessages();
        if (chatMessages.isEmpty()) {
            return String.valueOf(chat.getChatId() * new Random().nextLong());
        }
        ChatMessage message = chatMessages.get(chatMessages.size() - 1);
        return String.valueOf(message.getSendTime().getTime());
    }

    private String[] getArguments(String messageText, String command) {
        return messageText.substring(command.length()).trim().split(" ");
    }

    @GetMapping(value = "/callback/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testReceived(@PathVariable String path) {
        return this.registeredBots.containsKey(path) ? "Hi there " + path + "!" : "Callback not found for " + path;
    }

    private void saveChat(Long chatId, String processInstanceId, Update update) {
        Chat chat = getOrCreateChat(chatId);

        String activeProcessId = chat.getActiveProcessId();
        if (activeProcessId != null) {
            exampleProcessStarter.deleteProcessInstance(activeProcessId, "User start another process");
        }
        chat.setActiveProcessId(processInstanceId);
        
        chatRepository.save(chat);
        
        try {
            avatars.put(chatId, getAvatar(update.getMessage().getFrom()));
        } catch (Exception e) {
            LOGGER.error("Error setting avatar", e);
        }
    }

    public String getAvatar(User user) throws TelegramApiException {
        GetUserProfilePhotos method = new GetUserProfilePhotos();
        method.setUserId(user.getId());
        method.setOffset(0);
        method.setLimit(1);

        UserProfilePhotos photos = sender.execute(method);
        if(photos.getTotalCount() == 0) return null;

        PhotoSize photo = photos.getPhotos().get(0).get(0);

        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(photo.getFileId());

        File file = sender.execute(getFileMethod);
        return file.getFileUrl(token);
    }
    
    public String getAvatar(long id) {
        return avatars.get(id);
    }

    private void saveChatInfoCustomer(long chatId, Update update, String message) {
        Message updateMessage = update.getMessage();
        if (message == null) message = updateMessage.getText();

        saveChatInfo(chatId, message, new Timestamp(updateMessage.getDate() * 1000L),
                formUserName(updateMessage.getFrom()), SenderType.CUSTOMER);
    }

    public String formUserName(User user) {
        String name = user.getFirstName();
        if (user.getLastName() != null) name += " " + user.getLastName();
        return name;
    }

    public void saveChatInfo(long chatId, String message, Timestamp timestamp, String username, SenderType senderType) {
        Chat chat = getOrCreateChat(chatId);
        List<ChatMessage> chatMessages = chat.getChatMessages();

        ChatMessage chatMessage = new ChatMessage(chat, message, timestamp, username, senderType);
        chatMessageRepository.save(chatMessage);

        chatMessages.add(chatMessage);
        chat.setChatMessages(chatMessages);

        chatRepository.save(chat);
    }

    private Chat getOrCreateChat(long chatId) {
        return chatRepository.findByChatId(chatId).orElseGet(() -> chatRepository.save(new Chat(chatId)));
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
        if (!callbacks.containsKey(chatId)) {
            LOGGER.warn("Received message with no registered callback");
            return answerCallbackQuery;
        }

        IBotCallback callback = callbacks.get(chatId);
        if (!(callback instanceof BotKeyboardCallback)) {
            //handleWrongUserAction(originalMessage.getChatId());
            return answerCallbackQuery;
        }

        BotKeyboardCallback keyboardCallback = (BotKeyboardCallback) callback;
        if (keyboardCallback.getOriginalMessageId() != originalMessage.getMessageId()) {
            LOGGER.warn("Received message with no registered callback");
            return answerCallbackQuery;
        }

        callbacks.remove(chatId);
        keyboardCallback.answerReceived(chatId, callbackQuery.getFrom(), callbackQuery.getData());

        return answerCallbackQuery;
    }

    private BotApiMethod handleChatMessage(Update update, Message message) {
        Long chatId = message.getChatId();

        Chat chat = getOrCreateChat(chatId);
        chatRepository.save(chat);
        
        if (message.isCommand()) {
            String messageText = update.getMessage().getText();
            Command command = Command.fromMessage(messageText);

            switch (command.getName()) {
                case START_PROCESS_COMMAND:
                    startProcess(chatId, command.getArguments()[0], update);
                    break;
                case HELP_COMMAND:
                    startProcess(chatId, "help", update);
            }
            
            return null;
        }

        if(chat.getDialogMode()) {
            saveChatInfoCustomer(chatId, update, null);
            return null;
        }
        
        if (!callbacks.containsKey(chatId)) {
            // new dialog
            startProcess(chatId, "help", update);
            return null;
        }
        
        IBotCallback callback = callbacks.get(chatId);
        if (!(callback instanceof BotMessageCallback)) {
            return handleWrongUserAction(chatId);
        }
        
        saveChatInfoCustomer(chatId, update, null);
        BotMessageCallback messageCallback = (BotMessageCallback) callback;

        callbacks.remove(chatId);
        messageCallback.answerReceived(chatId, message.getFrom(), message.getText());

        return null;
    }

    private void startProcess(Long chatId, String processSchemeId, Update update) {
        ProcessInstance processInstance = exampleProcessStarter.startProcess(processSchemeId, update);
        
        getOrCreateChat(chatId).setDialogMode(false);
        saveChatInfoCustomer(chatId, update, "Пользователь подключился к диалогу");
        
        saveChat(chatId, processInstance.getId(), update);
    }

    private BotApiMethod handleLocation(Update update, Message message) {
        Long chatId = message.getChatId();

        if (!callbacks.containsKey(chatId)) {
            return handleWrongUserAction(chatId);
        }

        IBotCallback callback = callbacks.get(chatId);
        if (!(callback instanceof BotLocationCallback)) {
            return handleWrongUserAction(chatId);
        }

        BotLocationCallback locationCallback = (BotLocationCallback) callback;
        
        Location loc = message.getLocation();
        
        String value;
        try {
            value = Resources.toString(CustomTgRestController.class.getResource("/map.html"), Charsets.UTF_8);
            value = value.replaceAll("\\$Lat\\$", loc.getLatitude().toString());
            value = value.replaceAll("\\$Lon\\$", loc.getLatitude().toString());
            value = value.replaceAll("\\$Id\\$", message.getMessageId().toString());
        } catch (IOException e) {
            value = "{ " + loc.getLatitude() + ", " + loc.getLongitude() + " }";
            LOGGER.error("Error loading map", e);
        }
        
        saveChatInfoCustomer(chatId, update, value);

        callbacks.remove(chatId);
        locationCallback.answerReceived(chatId, message.getFrom(), value);

        return null;
    }

    private SendMessage handleWrongUserAction(Long chatId) {
        return new SendMessage(chatId, "Неверный ввод");
    }

}
