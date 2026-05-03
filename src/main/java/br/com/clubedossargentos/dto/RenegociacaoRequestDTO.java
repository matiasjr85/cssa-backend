package br.com.clubedossargentos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class RenegociacaoRequestDTO {

    @NotNull(message = "Valor renegociado é obrigatório.")
    @DecimalMin(value = "0.01", message = "Valor renegociado deve ser maior que zero.")
    private BigDecimal valorRenegociado;

    @NotNull(message = "Quantidade de parcelas é obrigatória.")
    @Min(value = 1, message = "Parcelas mínimas: 1.")
    @Max(value = 10, message = "Parcelas máximas: 10.")
    private Integer quantidadeParcelas;

    @NotBlank(message = "Motivo é obrigatório.")
    private String motivo;

    private String observacao;

    @NotEmpty(message = "Selecione ao menos um mês para renegociação.")
    private List<String> mesesReferencia;

    public BigDecimal getValorRenegociado() {
        return valorRenegociado;
    }

    public Integer getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getObservacao() {
        return observacao;
    }

    public List<String> getMesesReferencia() {
        return mesesReferencia;
    }

    public void setValorRenegociado(BigDecimal valorRenegociado) {
        this.valorRenegociado = valorRenegociado;
    }

    public void setQuantidadeParcelas(Integer quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setMesesReferencia(List<String> mesesReferencia) {
        this.mesesReferencia = mesesReferencia;
    }
}
