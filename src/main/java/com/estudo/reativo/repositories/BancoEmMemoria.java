package com.estudo.reativo.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class BancoEmMemoria implements BancoDados {

    public static final Map<String, String> BANCO_DADOS = new ConcurrentHashMap<>();

    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public <T> T salvar(final String chave, final T valor) {
        final var valorString = this.mapper.writeValueAsString(valor);
        BANCO_DADOS.put(chave, valorString);
        return valor;
    }

    @Override
    public <T> Optional<T> pegar(final String chave, final Class<T> clazz) {
        final String json = BANCO_DADOS.get(chave);

        return Optional.ofNullable(json)
                .map(dado -> {
                    try {
                        return this.mapper.readValue(dado, clazz);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
