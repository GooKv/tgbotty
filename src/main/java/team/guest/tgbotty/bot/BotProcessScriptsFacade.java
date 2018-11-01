package team.guest.tgbotty.bot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import team.guest.tgbotty.bot.callbacks.BotKeyboardCallback;
import team.guest.tgbotty.controller.CustomTgRestController;

import javax.inject.Named;
import java.util.*;

@Named("bot")
@Component
public class BotProcessScriptsFacade {
    
    private final CustomTgRestController customTgRestController;
    private final AbsSender sender;

    @Autowired
    public BotProcessScriptsFacade(AbsSender sender, CustomTgRestController customTgRestController) {
        this.sender = sender;
        this.customTgRestController = customTgRestController;
    }

    public void sendSimpleMessage(Long chatId, String message) throws TelegramApiException {
        sender.execute(new SendMessage(chatId, message));
    }

    public void sendOptions(Long chatId, String message, String key, 
                            String... options) throws TelegramApiException, IllegalArgumentException {
        if(options.length % 2 != 0)
            throw new IllegalArgumentException("Only even number of options allowed, " + options.length + " passed");
        
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < options.length / 2; i++) 
            map.put(options[i * 2], options[i * 2 + 1]);
        
        sendKeyboardAsync(chatId, message, map,
                (originalMessage, option) -> {
                    try {
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
        
        customTgRestController.registerKeyboardCallback(sender.execute(action), callback);
    }
    
}
