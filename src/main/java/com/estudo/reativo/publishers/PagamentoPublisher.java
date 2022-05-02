package com.estudo.reativo.publishers;

import com.estudo.reativo.entities.Pagamento;
import com.estudo.reativo.entities.PubSubMensagem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Flow;

@Component
@RequiredArgsConstructor
public class PagamentoPublisher {

    private final Sinks.Many<PubSubMensagem> sink;
    private final ObjectMapper mapper;

    public Mono<Pagamento> onPagamentoCriado(final Pagamento pagamento) {

        return Mono.fromCallable(() -> {
            final String usuarioId = pagamento.getUsuarioId();
            final String dado = mapper.writeValueAsString(pagamento);
            return new PubSubMensagem(usuarioId, dado);
        })
                .subscribeOn(Schedulers.parallel())
                .doOnNext(next -> this.sink.tryEmitNext(next))
                .thenReturn(pagamento);
    }
}
