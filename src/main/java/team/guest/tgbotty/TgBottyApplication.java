package team.guest.tgbotty;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.SQLException;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaRepositories("team.guest.tgbotty.dao")
public class TgBottyApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBottyApplication.class, args);
    }
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}
