package br.com.clubedossargentos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Embeddable
public class PagamentoRegistro {

    @Column(name = "pagamento_id", length = 36)
    private String pagamentoId;

    private LocalDateTime dataHora;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorSocio;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorDependentes;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorTotal;

    private String mesReferencia;

    @Column(length = 20)
    private String tipoPagamento;

    private Integer quantidadeParcelas;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorParcela;

    private String observacao;

    @Column(length = 4000)
    private String detalhesDependentes;

    @Column(length = 1000)
    private String mesesQuitados;

    public PagamentoRegistro() {
    }

    public PagamentoRegistro(
            String pagamentoId,
            LocalDateTime dataHora,
            BigDecimal valorSocio,
            BigDecimal valorDependentes,
            BigDecimal valorTotal,
            String mesReferencia,
            String tipoPagamento,
            Integer quantidadeParcelas,
            BigDecimal valorParcela,
            String observacao,
            String detalhesDependentes,
            String mesesQuitados
    ) {
        this.pagamentoId = pagamentoId;
        this.dataHora = dataHora;
        this.valorSocio = valorSocio;
        this.valorDependentes = valorDependentes;
        this.valorTotal = valorTotal;
        this.mesReferencia = mesReferencia;
        this.tipoPagamento = tipoPagamento;
        this.quantidadeParcelas = quantidadeParcelas;
        this.valorParcela = valorParcela;
        this.observacao = observacao;
        this.detalhesDependentes = detalhesDependentes;
        this.mesesQuitados = mesesQuitados;
    }

    public String getPagamentoId() { return pagamentoId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public BigDecimal getValorSocio() { return valorSocio; }
    public BigDecimal getValorDependentes() { return valorDependentes; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public String getMesReferencia() { return mesReferencia; }
    public String getTipoPagamento() { return tipoPagamento; }
    public Integer getQuantidadeParcelas() { return quantidadeParcelas; }
    public BigDecimal getValorParcela() { return valorParcela; }
    public String getObservacao() { return observacao; }
    public String getDetalhesDependentes() { return detalhesDependentes; }
    public String getMesesQuitados() { return mesesQuitados; }

    public void setPagamentoId(String pagamentoId) { this.pagamentoId = pagamentoId; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public void setValorSocio(BigDecimal valorSocio) { this.valorSocio = valorSocio; }
    public void setValorDependentes(BigDecimal valorDependentes) { this.valorDependentes = valorDependentes; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public void setMesReferencia(String mesReferencia) { this.mesReferencia = mesReferencia; }
    public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }
    public void setQuantidadeParcelas(Integer quantidadeParcelas) { this.quantidadeParcelas = quantidadeParcelas; }
    public void setValorParcela(BigDecimal valorParcela) { this.valorParcela = valorParcela; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public void setDetalhesDependentes(String detalhesDependentes) { this.detalhesDependentes = detalhesDependentes; }
    public void setMesesQuitados(String mesesQuitados) { this.mesesQuitados = mesesQuitados; }
}
