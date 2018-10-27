package team.guest.tgbotty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TgBottyApplication {
    private static final String URL_FORMAT = "https://api.telegram.org/bot%s/";

    @Value("${tg.bot.api.key}")
    private String tgBotApiKey;

    public static void main(String[] args) {
        SpringApplication.run(TgBottyApplication.class, args);
    }

    @Bean
    public RestTemplate tgRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(String.format(URL_FORMAT, tgBotApiKey))
                .build();
    }
}
