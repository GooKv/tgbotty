package team.guest.tgbotty.bot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import team.guest.tgbotty.bot.callbacks.BotKeyboardCallback;
import team.guest.tgbotty.bot.callbacks.BotLocationCallback;
import team.guest.tgbotty.bot.callbacks.BotMessageCallback;
import team.guest.tgbotty.controller.CustomTgRestController;
import team.guest.tgbotty.controller.ExampleProcessStarter;
import team.guest.tgbotty.entity.SenderType;

import javax.inject.Named;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("bot")
@Component
public class BotProcessScriptsFacade {
    
    private final CustomTgRestController customTgRestController;
    private final AbsSender sender;
    private final ExampleProcessStarter processStarter;

    @Autowired
    public BotProcessScriptsFacade(AbsSender sender, CustomTgRestController customTgRestController, ExampleProcessStarter processStarter) {
        this.sender = sender;
        this.customTgRestController = customTgRestController;
        this.processStarter = processStarter;
    }

    public void sendSimpleMessage(Long chatId, String message) throws TelegramApiException {
        Message sent = sender.execute(new SendMessage(chatId, message));

        customTgRestController.saveChatInfo(chatId, message,
                new Timestamp(sent.getDate() * 1000L), null, SenderType.BOT);
    }

    public void sendOptions(DelegateExecution execution, Long chatId, String message, String key,
                            String... options) throws TelegramApiException, IllegalArgumentException {
        if(options.length % 2 != 0)
            throw new IllegalArgumentException("Only even number of options allowed, " + options.length + " passed");
        
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < options.length / 2; i++) 
            map.put(options[i * 2], options[i * 2 + 1]);

        execution.setVariable(key + "Options", map);

        sendKeyboardAsync(chatId, message, map, new BotKeyboardCallback() {
            @Override
            public void answerReceived(Long chatId, User user, String received) {
                customTgRestController.saveChatInfo(chatId, map.get(received), 
                        new Timestamp(System.currentTimeMillis()), user.getUserName(), SenderType.CUSTOMER);
                processStarter.completeUserTask(chatId, ImmutableMap.of(key, received));
            }
        });
    }
    
    private void sendKeyboardAsync(Long chatId, String message, Map<String, String> options, 
                                  BotKeyboardCallback callback) throws TelegramApiException {
        SendMessage action = new SendMessage(chatId, message);
        
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>(options.size());
        for(Map.Entry<String, String> option : options.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton(option.getValue());
            button.setCallbackData(option.getKey());
            
            buttons.add(ImmutableList.of(button));
        }
        
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(buttons);
        
        action.setReplyMarkup(keyboardMarkup);
        
        Message sent = sender.execute(action);
        customTgRestController.registerCallback(sent.getChatId(), callback);
        
        callback.setOriginalMessageId(sent.getMessageId());
        
        customTgRestController.saveChatInfo(chatId, message,
                new Timestamp(sent.getDate() * 1000L), null, SenderType.BOT);
    }
    
    public void requestString(Long chatId, String message, String key) throws TelegramApiException {
        sendSimpleMessage(chatId, message);
        customTgRestController.registerCallback(chatId, new BotMessageCallback() {
            @Override
            public void answerReceived(Long chatId, User user, String received) {
                processStarter.completeUserTask(chatId, ImmutableMap.of(key, received));
            }
        });
    }
    
    public void requestLocation(Long chatId, String message, String key) throws TelegramApiException {
        sendSimpleMessage(chatId, message);
        customTgRestController.registerCallback(chatId, new BotLocationCallback() {
            @Override
            public void answerReceived(Long chatId, User user, String received) {
                processStarter.completeUserTask(chatId, ImmutableMap.of(key, received));
            }
        });
    }
    
}
