package team.guest.tgbotty.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Component
public class TgBottyBot extends TelegramWebhookBot {

    @Value("${tg.bot.username}")
    private String username;
    @Value("${tg.bot.path}")
    private String path;
    @Value("${tg.bot.key}")
    private String token;
    @Value("${tg.bot.webhook}")
    private String webhook;
    @Value("${tg.bot.webhook.enabled}")
    private boolean webhookEnabled;

    @PostConstruct
    public void init() throws TelegramApiRequestException {
        if (webhookEnabled) {
            setWebhook(webhook, null);
        }
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        Message message = update.getMessage();
        return new SendMessage(message.getChatId(), "No u! " + message.getText());
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotPath() {
        return path;
    }
}
