package br.com.clubedossargentos.dto;

import br.com.clubedossargentos.enums.StatusAluguel;
import br.com.clubedossargentos.enums.TipoPagamento;
import jakarta.validation.constraints.NotNull;

public class AtualizarStatusAluguelRequestDTO {
    @NotNull(message = "Status do aluguel é obrigatório.")
    private StatusAluguel status;

    private TipoPagamento tipoPagamento;

    public StatusAluguel getStatus() { return status; }
    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public void setStatus(StatusAluguel status) { this.status = status; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
}
