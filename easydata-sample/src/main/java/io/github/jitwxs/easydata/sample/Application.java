package io.github.jitwxs.easydata.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-06-04 22:45
 */
@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.error(
                "\n\n-------------------------------- " +
                        "Application started successfully" +
                        " --------------------------------\n\n");
    }
}
