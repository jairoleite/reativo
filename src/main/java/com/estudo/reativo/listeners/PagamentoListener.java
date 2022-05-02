package com.estudo.reativo.listeners;

import com.estudo.reativo.entities.PubSubMensagem;
import com.estudo.reativo.repositories.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoListener implements InitializingBean {

    private final Sinks.Many<PubSubMensagem> sink;
    private final PagamentoRepository pagamentoRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.sink.asFlux()
                .subscribe(
                        next -> {

                        },
                        error -> {},
                        () -> {}
                );
    }
}
