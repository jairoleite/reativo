package com.estudo.reativo.repositories;

import com.estudo.reativo.entities.Pagamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * Tipos de schedulers
 Schedulers.parallel() => scheduler exclusivamente para uso de CPU
 Schedulers.boundedElastic(); => scheduler para uso bloqueante I/O
 Schedulers.immediate();
 Schedulers.single();
 */
public class PagamentoRepository {

    private static final ThreadFactory THREAD_FACTORY = new CustomizableThreadFactory("banco-dados");
    private static final Scheduler DB_SCHEDULER = Schedulers.fromExecutor(Executors.newFixedThreadPool(8, THREAD_FACTORY));

    private final BancoDados bancoDados;

    public Mono<Pagamento> criaPagamento(final String usuarioId) {

        final Pagamento pagamento = Pagamento.builder()
                .id(UUID.randomUUID().toString())
                .usuarioId(usuarioId)
                .status(Pagamento.PagamentoStatus.PENDENTE)
                .build();

        return Mono.fromCallable(() -> {
                    log.info("Salvando transação de pagamento para o usuário {}", usuarioId);
                    return this.bancoDados.salvar(usuarioId, pagamento);
                })
                .subscribeOn(DB_SCHEDULER)
                .doOnNext(next -> log.info("Pagamento efetudo {}", next.getUsuarioId()));
    }

    public Mono<Pagamento> pegaPagamento(final String usuarioId) {
        return Mono.defer(() -> {
                    final Optional<Pagamento> pagamento = this.bancoDados.pegar(usuarioId, Pagamento.class);
                    return Mono.justOrEmpty(pagamento);
                })
                .subscribeOn(DB_SCHEDULER);
    }
}
