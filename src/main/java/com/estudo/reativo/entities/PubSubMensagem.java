package com.estudo.reativo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PubSubMensagem {

    String chave;
    String valor;
}
