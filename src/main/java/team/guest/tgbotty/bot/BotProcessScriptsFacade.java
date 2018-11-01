package team.guest.tgbotty.bot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import team.guest.tgbotty.bot.callbacks.BotKeyboardCallback;
import team.guest.tgbotty.controller.CustomTgRestController;
import team.guest.tgbotty.entity.SenderType;
import team.guest.tgbotty.controller.ExampleProcessStarter;

import javax.inject.Named;
import java.sql.Timestamp;
import java.util.*;

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

        sendKeyboardAsync(chatId, message, map,
                (originalMessage, option) -> {
                    customTgRestController.saveChatInfo(chatId, map.get(option),
                            new Timestamp(System.currentTimeMillis()), null, SenderType.CUSTOMER);
                    
                    // TODO: replace with task finish
                    try {
                        processStarter.completeUserTask(chatId, ImmutableMap.of(key, option));
                        sendSimpleMessage(originalMessage.getChatId(), "Received " + option);
                    } catch (TelegramApiException ignored) {
                        System.err.println(ignored.getMessage());
                    }
                });
    }
    
    public void sendKeyboardAsync(Long chatId, String message, Map<String, String> options, 
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
        customTgRestController.registerKeyboardCallback(sent, callback);

        customTgRestController.saveChatInfo(chatId, message,
                new Timestamp(sent.getDate() * 1000L), null, SenderType.BOT);
    }
    
}
