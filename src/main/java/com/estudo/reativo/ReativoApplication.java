package com.estudo.reativo;

import com.estudo.reativo.entities.PubSubMensagem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Sinks;

@SpringBootApplication
public class ReativoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReativoApplication.class, args);
    }

    @Bean
    public Sinks.Many<PubSubMensagem> sink() {
        return Sinks.many().multicast()
                .onBackpressureBuffer(1000);
    }

}
