package team.guest.tgbotty.bot.callbacks;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotKeyboardCallback {
    
    void answerReceived(Message originalMessage, String selectedOption);
    
}
