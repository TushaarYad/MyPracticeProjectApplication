package com.tushaar.MyPracticeProject.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/*
@Component: Registers BeanInspector as a Spring-managed bean itself.
Spring automatically scans, instantiates, and manages this class.

implements CommandLineRunner: CommandLineRunner is a special Spring Boot interface with a
single run method. Spring Boot automatically executes the run method once, right after the application context has loaded, but before the main startup finishes.

The run Method Logic
When Spring Boot triggers the run method, and the code below executes.
*/



@Component
public class BeanInspector implements CommandLineRunner {
    private final ApplicationContext applicationContext;

    //Injecting the ApplicationContext since it is a bean too
    public BeanInspector(ApplicationContext context) {
        this.applicationContext = context;
    }

    @Override
    public void run(String... args){
        System.out.println("\n========== ALL BEANS IN THE IOC CONTAINER ==========");

        //Saving all the names in the array beanNames
        String[] beanNames = applicationContext.getBeanDefinitionNames();

        //Sorting the array
        Arrays.sort(beanNames);

        for (String name : beanNames) {
            System.out.println(name);
        }

        System.out.println("===================================================");
        System.out.println("Total beans: " + beanNames.length);
        System.out.println("===================================================\n");

    }
}
