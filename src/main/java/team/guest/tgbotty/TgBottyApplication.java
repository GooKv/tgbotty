package team.guest.tgbotty;

import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileUrlResource;

import java.net.MalformedURLException;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TgBottyApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBottyApplication.class, args);
    }

//    @Bean
//    public SpringProcessEngineConfiguration processEngineConfiguration(ApplicationContext applicationContext) throws MalformedURLException {
//        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
//        config.setDeploymentResources(new FileUrlResource[]{
//                new FileUrlResource("classpath*:/processes/*.bpmn20.xml")
//        });
//        config.setApplicationContext(applicationContext);
//        return config;
//    }
//
//    @Bean
//    public ProcessEngineFactoryBean processEngine(ApplicationContext applicationContext) throws MalformedURLException {
//        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
//        factoryBean.setProcessEngineConfiguration(processEngineConfiguration(applicationContext));
//        return factoryBean;
//    }
}
