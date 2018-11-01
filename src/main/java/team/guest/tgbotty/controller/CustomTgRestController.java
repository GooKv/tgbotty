package team.guest.tgbotty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.WebhookBot;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController("/callback")
public class CustomTgRestController {
    private final ExampleProcessStarter exampleProcessStarter;
    private final Map<String, WebhookBot> registeredBots;

    @Autowired
    public CustomTgRestController(ExampleProcessStarter exampleProcessStarter, WebhookBot... webHookBots) {
        this.exampleProcessStarter = exampleProcessStarter;
        registeredBots = Arrays.stream(webHookBots)
                .collect(Collectors.toMap(WebhookBot::getBotPath, Function.identity()));
    }

    @PostMapping(value = "/callback/{path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateReceived(@PathVariable String path, @RequestBody Update update) {
        WebhookBot webhookBot = this.registeredBots.get(path);

        if (webhookBot == null) {
            throw new BotNotFoundException(path);
        }

        exampleProcessStarter.startRepeaterProcess(update.getMessage().getChatId(), update);
        return null;
    }

    @GetMapping(value = "/callback/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testReceived(@PathVariable String path) {
        return this.registeredBots.containsKey(path) ? "Hi there " + path + "!" : "Callback not found for " + path;
    }
}