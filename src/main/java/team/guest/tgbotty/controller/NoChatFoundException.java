package team.guest.tgbotty.controller;

public class NoChatFoundException extends RuntimeException {

    private static final String MESSAGE = "Не найден час с id %d";

    public NoChatFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
