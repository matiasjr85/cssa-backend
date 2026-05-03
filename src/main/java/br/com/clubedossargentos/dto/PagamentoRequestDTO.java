package br.com.clubedossargentos.dto;

import jakarta.validation.constraints.DecimalMin;
import br.com.clubedossargentos.enums.TipoPagamento;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PagamentoRequestDTO {

    @Min(value = 1, message = "Parcelas mínimas: 1.")
    @Max(value = 10, message = "Parcelas máximas: 10.")
    private Integer quantidadeParcelas = 1;

    private String observacao;

    @DecimalMin(value = "0.00", message = "Valor do sócio não pode ser negativo.")
    private BigDecimal valorSocioPago;

    @DecimalMin(value = "0.00", message = "Valor dos dependentes não pode ser negativo.")
    private BigDecimal valorDependentesPago;

    private List<String> mesesReferencia = new ArrayList<>();

    @NotNull(message = "Tipo de pagamento é obrigatório.")
    private TipoPagamento tipoPagamento;

    public Integer getQuantidadeParcelas() { return quantidadeParcelas; }
    public String getObservacao() { return observacao; }
    public BigDecimal getValorSocioPago() { return valorSocioPago; }
    public BigDecimal getValorDependentesPago() { return valorDependentesPago; }
    public List<String> getMesesReferencia() { return mesesReferencia; }
    public TipoPagamento getTipoPagamento() { return tipoPagamento; }

    public void setQuantidadeParcelas(Integer quantidadeParcelas) { this.quantidadeParcelas = quantidadeParcelas; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public void setValorSocioPago(BigDecimal valorSocioPago) { this.valorSocioPago = valorSocioPago; }
    public void setValorDependentesPago(BigDecimal valorDependentesPago) { this.valorDependentesPago = valorDependentesPago; }
    public void setMesesReferencia(List<String> mesesReferencia) { this.mesesReferencia = mesesReferencia; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
}
