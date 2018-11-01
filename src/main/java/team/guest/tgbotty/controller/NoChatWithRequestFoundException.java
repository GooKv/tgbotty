package team.guest.tgbotty.controller;

public class NoChatWithRequestFoundException extends RuntimeException {

    private static final String MESSAGE = "Не найден чат с заявкой (id = %d)";

    public NoChatWithRequestFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
