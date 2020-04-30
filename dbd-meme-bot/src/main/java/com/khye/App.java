package com.khye;

public class App {
    public static void main(String[] args) {
        JDAConnector jdaConnector = new JDAConnector(new Configuration());
        jdaConnector.connect();
    }
}
