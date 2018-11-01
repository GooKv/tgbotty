package team.guest.tgbotty.controller;

public class Command {
    public String getName() {
        return name;
    }

    public String[] getArguments() {
        return arguments;
    }

    private final String name;
    private final String[] arguments;

    public Command(String name, String[] arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public static Command fromMessage(String message) {
        String commandName = message.split(" ")[0];
        return new Command(commandName, getArguments(message, commandName));
    }

    public static String[] getArguments(String messageText, String command) {
        return messageText.substring(command.length()).trim().split(" ");
    }
}
