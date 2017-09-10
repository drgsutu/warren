package io.sutu;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.sutu")
class Warren {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Warren.class);
        Application app = context.getBean(Application.class);
        app.run();
    }
}
