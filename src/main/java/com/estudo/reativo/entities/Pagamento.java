package com.estudo.reativo.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Pagamento {

    String id;
    String usuarioId;
    PagamentoStatus status;

    public enum PagamentoStatus {
        PENDENTE, APROVADO
    }
}
