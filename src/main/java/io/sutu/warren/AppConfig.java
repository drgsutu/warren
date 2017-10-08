package io.sutu.warren;

import com.google.gson.Gson;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.sutu.warren")
public class AppConfig {

    @Bean
    public Config config() {
        return ConfigFactory.load();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

}
