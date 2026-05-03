package br.com.clubedossargentos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReciboRenegociacaoDTO {
    private Long id;
    private String nomeSocio;
    private String matriculaSocio;
    private BigDecimal valorOriginal;
    private BigDecimal valorRenegociado;
    private Integer quantidadeParcelas;
    private BigDecimal valorParcela;
    private String motivo;
    private String observacao;
    private LocalDateTime dataCriacao;
    private List<String> mesesRenegociados;

    public ReciboRenegociacaoDTO(Long id, String nomeSocio, String matriculaSocio, BigDecimal valorOriginal,
                                 BigDecimal valorRenegociado, Integer quantidadeParcelas, BigDecimal valorParcela,
                                 String motivo, String observacao, LocalDateTime dataCriacao, List<String> mesesRenegociados) {
        this.id = id;
        this.nomeSocio = nomeSocio;
        this.matriculaSocio = matriculaSocio;
        this.valorOriginal = valorOriginal;
        this.valorRenegociado = valorRenegociado;
        this.quantidadeParcelas = quantidadeParcelas;
        this.valorParcela = valorParcela;
        this.motivo = motivo;
        this.observacao = observacao;
        this.dataCriacao = dataCriacao;
        this.mesesRenegociados = mesesRenegociados;
    }

    public Long getId() { return id; }
    public String getNomeSocio() { return nomeSocio; }
    public String getMatriculaSocio() { return matriculaSocio; }
    public BigDecimal getValorOriginal() { return valorOriginal; }
    public BigDecimal getValorRenegociado() { return valorRenegociado; }
    public Integer getQuantidadeParcelas() { return quantidadeParcelas; }
    public BigDecimal getValorParcela() { return valorParcela; }
    public String getMotivo() { return motivo; }
    public String getObservacao() { return observacao; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public List<String> getMesesRenegociados() { return mesesRenegociados; }
}
