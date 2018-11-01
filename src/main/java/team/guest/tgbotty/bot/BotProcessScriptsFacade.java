package team.guest.tgbotty.bot;

import com.google.common.collect.ImmutableList;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    
    public void sendKeyboardAsync(Long chatId, String message, String[] options, 
                                  BotKeyboardCallback callback) throws TelegramApiException {
        SendMessage action = new SendMessage(chatId, message);
        
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>(options.length);
        for(int i = 0; i < options.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton(options[i]);
            button.setCallbackData(Integer.toString(i));
            
            buttons.add(ImmutableList.of(button));
        }
        
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(buttons);
        
        action.setReplyMarkup(keyboardMarkup);
        
        customTgRestController.registerKeyboardCallback(sender.execute(action), callback);
    }

    public void sendOptions(Long chatId) throws TelegramApiException {
        sendKeyboardAsync(chatId, "Test menu", new String[] {"Option 1", "Option 2", "Option 3"}, 
                (message, option) -> {
                    try {
                        sendSimpleMessage(message.getChatId(), "Received " + option);
                    } catch (TelegramApiException ignored) { }
                });
    }
}
