package com.korczak.plsql1.controller;

import com.korczak.plsql1.spring.ControllersConfiguration;
import com.korczak.plsql1.spring.DatabaseConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class GenericController {

    protected final ApplicationContext applicationContext;

    public GenericController() {
        applicationContext = new AnnotationConfigApplicationContext(DatabaseConfiguration.class, ControllersConfiguration.class);
    }
}
