package com.SIMOD.SIMOD.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

/*
É responsável por inicializar o Firebase Admin SDK dentro do contexto de uma aplicação Spring.
Ela garante que a configuração do Firebase seja carregada uma única vez, logo após a criação do contexto da aplicação.
*/

@Configuration
public class FirebaseConfig {

    /*O método init() é executado automaticamente após o Spring instanciar o bean.
    Isso garante que o Firebase esteja pronto antes de qualquer uso por outros componentes da aplicação.*/
    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount =
                    getClass().getClassLoader()
                            .getResourceAsStream("firebase-service-account.json");

            if (serviceAccount == null) {
                throw new IllegalStateException("Arquivo firebase-service-account.json não encontrado");
            }

            //As credenciais são carregadas diretamente a partir do InputStream, utilizando a API oficial do Firebase
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar Firebase", e);
        }
    }
}

