package io.sutu;

import com.google.gson.Gson;
import com.tictactec.ta.lib.Core;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.sutu")
public class AppConfig {

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public Core core() {
        return new Core();
    }

}
