/** Clasa principală a aplicației Spring Boot
 * Punctul de intrare pentru Subscription Tracker
 * @author Nedelcu Bianca-Nicoleta
 * @version 2 Decembrie 2025
 */
package com.awj.proiect.subscription_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SubscriptionTrackerApplication {
    public static void main(String[] args) {
        System.out.println("SUBSCRIPTION TRACKER - Starting...   ");

        long startTime = System.currentTimeMillis();
        SpringApplication.run(SubscriptionTrackerApplication.class, args);
        long duration = System.currentTimeMillis() - startTime;

        System.out.println("Aplicația a pornit în " + duration + " ms");
        System.out.println("Accesează: http://localhost:8081");
        System.out.println("H2 Console: http://localhost:8081/h2-console");
    }
}