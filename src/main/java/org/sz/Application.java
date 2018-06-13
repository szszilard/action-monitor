package org.sz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class to launch the application.
 */
@SpringBootApplication
public class Application {

    /**
     * Entry point of the application.
     *
     * @param args potential arguments for the execution of the application
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
