package com.estudo.reativo.repositories;

import java.util.Optional;

public interface BancoDados {

    <T> T salvar(final String chave, final T valor);
    <T> Optional<T> pegar(final String chave, final Class<T> clazz);
}
