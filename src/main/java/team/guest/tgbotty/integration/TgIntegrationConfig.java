package team.guest.tgbotty.integration;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Profile("tg-integration")
@Component
public class TgIntegrationConfig {
    @Value("${tg.bot.webhook.url}")
    private String webhookUrl;

    @Autowired
    private RestTemplate tgRestTemplate;

    @PostConstruct
    public void intializeWebHook() {
        tgRestTemplate.postForEntity(
                "/setWebhook",
                ImmutableMap.of("url", webhookUrl),
                HashMap.class
        );
    }

}
