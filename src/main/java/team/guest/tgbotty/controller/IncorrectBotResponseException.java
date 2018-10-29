package team.guest.tgbotty.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.util.Objects;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Incorrect bot response")
public class IncorrectBotResponseException extends RuntimeException {
    public IncorrectBotResponseException(String path, BotApiMethod response, TelegramApiValidationException caused) {
        super("Incorrect bot response: " + path + ": " + Objects.toString(response), caused);
    }
}
