package com.clinicbilling.core;

public class App {
    static void main(String[] args) {
        System.out.println("Clinic billing core is running");
        System.out.println("Java " + System.getProperty("java.version"));
        for (String arg : args) {
            System.out.println("arg: " + arg);
        }
    }
}
