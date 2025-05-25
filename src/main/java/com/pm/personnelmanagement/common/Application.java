package com.pm.personnelmanagement.common;

import com.pm.personnelmanagement.config.DefaultAppConfigPropertyController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Application implements CommandLineRunner {
    private final DefaultAppConfigPropertyController appConfigPropertyController;
    public Application(DefaultAppConfigPropertyController appConfigPropertyController) {
        this.appConfigPropertyController = appConfigPropertyController;
    }

    @Override
    public void run(String... args) {
        appConfigPropertyController.init();
    }
}
