package br.com.clubedossargentos.enums;

import java.math.BigDecimal;

public enum TipoSocio {
    RECREATIVO(new BigDecimal("85.00")),
    CONTRIBUINTE(new BigDecimal("119.75")),
    RECREATIVO_VINCULADO(new BigDecimal("65.00")),
    REMIDO(BigDecimal.ZERO);

    private final BigDecimal valorMensalidade;

    TipoSocio(BigDecimal valorMensalidade) {
        this.valorMensalidade = valorMensalidade;
    }

    public BigDecimal getValorMensalidade() {
        return valorMensalidade;
    }
}
