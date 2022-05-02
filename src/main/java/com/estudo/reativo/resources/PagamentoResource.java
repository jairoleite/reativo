package com.estudo.reativo.resources;

import com.estudo.reativo.entities.Pagamento;
import com.estudo.reativo.repositories.BancoEmMemoria;
import com.estudo.reativo.repositories.PagamentoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("pagamentos")
@RequiredArgsConstructor
@Slf4j
public class PagamentoResource {

    private final PagamentoRepository pagamentoRepository;

    @PostMapping
    public Mono<Pagamento> criaPagamento(@RequestBody final NovoPagamento novo) {
        final String usuarioId = novo.getUsuarioId();
        return this.pagamentoRepository.criaPagamento(usuarioId)
                .doOnNext(next -> log.info("Pagamento processado {}", usuarioId));
    }

    @GetMapping("/usuarios")
    public Flux<Pagamento> buscaTodosPorId(@RequestParam String ids) {
        final List<String> _ids = Arrays.asList(ids.split(","));
        log.info("Tamanho lista pagamento {}", _ids.size());

        return Flux.fromIterable(_ids)
                .flatMap(id -> this.pagamentoRepository.pegaPagamento(id));
    }

    @GetMapping("/ids")
    public Mono<String> pegaIds() {
        return Mono.fromCallable(() -> {
                    return String.join(",", BancoEmMemoria.BANCO_DADOS.keySet());
                })
                .subscribeOn(Schedulers.parallel());
    }

    @Data
    @AllArgsConstructor
    public static class NovoPagamento {
        private String usuarioId;
    }

}
