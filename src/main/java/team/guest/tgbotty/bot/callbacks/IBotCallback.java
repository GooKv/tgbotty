package team.guest.tgbotty.bot.callbacks;

import org.telegram.telegrambots.meta.api.objects.User;

public interface IBotCallback {
    
    void answerReceived(Long chatId, User user, String received);
    
}
