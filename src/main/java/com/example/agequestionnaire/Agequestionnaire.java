
package com.example.agequestionnaire;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@SpringBootApplication
public class Agequestionnaire {

    public static void main(String[] args) {
        SpringApplication.run(Agequestionnaire.class, args);
    }

    @Configuration
    static class FirestoreConfiguration {

        @Bean
        public Firestore firestore() throws IOException {
            return FirestoreOptions
                    .getDefaultInstance()
                    .getService();
        }
    }
}

