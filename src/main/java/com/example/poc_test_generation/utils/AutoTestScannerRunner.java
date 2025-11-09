package com.example.poc_test_generation.utils;


import com.example.poc_test_generation.annotation.AutoTest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@SpringBootApplication
public class AutoTestScannerRunner implements CommandLineRunner {

    private final ApplicationContext context;
    public AutoTestScannerRunner(ApplicationContext context) { this.context = context; }

    public static void main(String[] args) {
        SpringApplication.run(AutoTestScannerRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String[] beans = context.getBeanNamesForAnnotation(RestController.class);
        for (String beanName : beans) {
            Object controller = context.getBean(beanName);
            for (Method m : controller.getClass().getDeclaredMethods()) {
                if (m.isAnnotationPresent(AutoTest.class)) {
                    // Output fully qualified name
                    System.out.println(controller.getClass().getName() + "." + m.getName());
                }
            }
        }
    }
}
