package team.guest.tgbotty.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Named;

@Named("bot")
@Component
public class BotProcessScriptsFacade {
    private final AbsSender sender;

    @Autowired
    public BotProcessScriptsFacade(AbsSender sender) {
        this.sender = sender;
    }

    public void sendSimpleMessage(Long chatId, String message) throws TelegramApiException {
        sender.execute(new SendMessage(chatId, message));
    }

    public void sendOptions(Long chatId) {
        System.err.println("Sending options to chat " + chatId);
    }
}
