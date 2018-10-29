package team.guest.tgbotty.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Bot not found")
public class BotNotFoundException extends RuntimeException {
    public BotNotFoundException(String bot) {
        super("Bot not found: " + bot);
    }
}
