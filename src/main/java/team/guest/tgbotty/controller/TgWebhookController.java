package team.guest.tgbotty.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TgWebhookController {
    @RequestMapping("/webhooks/tg/**")
    public Object onWebhook(Map<String, String> requestParams) {
        System.out.println(requestParams);
        return null;
    }
}
