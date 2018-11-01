package team.guest.tgbotty.controller;

public class NoRequestFoundException extends RuntimeException {

    private static final String MESSAGE = "Не найдена заявка (id = %d)";

    public NoRequestFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
