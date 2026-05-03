package br.com.clubedossargentos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReciboDTO {
    private String nomeSocio;
    private String matriculaSocio;
    private String tipoSocio;
    private BigDecimal valorSocio;
    private Integer quantidadeDependentes;
    private List<String> nomesDependentes;
    private List<ReciboDependenteDTO> dependentesDetalhados;
    private BigDecimal valorDependentes;
    private BigDecimal valorTotal;
    private Integer quantidadeParcelas;
    private BigDecimal valorParcela;
    private LocalDateTime dataPagamento;
    private String tipoPagamento;
    private String mesReferencia;
    private List<String> mesesQuitados;
    private boolean previa;

    public ReciboDTO(String nomeSocio, String matriculaSocio, String tipoSocio, BigDecimal valorSocio,
                     Integer quantidadeDependentes, List<String> nomesDependentes,
                     List<ReciboDependenteDTO> dependentesDetalhados, BigDecimal valorDependentes,
                     BigDecimal valorTotal, Integer quantidadeParcelas, BigDecimal valorParcela,
                     LocalDateTime dataPagamento, String tipoPagamento, String mesReferencia, List<String> mesesQuitados, boolean previa) {
        this.nomeSocio = nomeSocio;
        this.matriculaSocio = matriculaSocio;
        this.tipoSocio = tipoSocio;
        this.valorSocio = valorSocio;
        this.quantidadeDependentes = quantidadeDependentes;
        this.nomesDependentes = nomesDependentes;
        this.dependentesDetalhados = dependentesDetalhados;
        this.valorDependentes = valorDependentes;
        this.valorTotal = valorTotal;
        this.quantidadeParcelas = quantidadeParcelas;
        this.valorParcela = valorParcela;
        this.dataPagamento = dataPagamento;
        this.tipoPagamento = tipoPagamento;
        this.mesReferencia = mesReferencia;
        this.mesesQuitados = mesesQuitados;
        this.previa = previa;
    }

    public String getNomeSocio() { return nomeSocio; }
    public String getMatriculaSocio() { return matriculaSocio; }
    public String getTipoSocio() { return tipoSocio; }
    public BigDecimal getValorSocio() { return valorSocio; }
    public Integer getQuantidadeDependentes() { return quantidadeDependentes; }
    public List<String> getNomesDependentes() { return nomesDependentes; }
    public List<ReciboDependenteDTO> getDependentesDetalhados() { return dependentesDetalhados; }
    public BigDecimal getValorDependentes() { return valorDependentes; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public Integer getQuantidadeParcelas() { return quantidadeParcelas; }
    public BigDecimal getValorParcela() { return valorParcela; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public String getTipoPagamento() { return tipoPagamento; }
    public String getMesReferencia() { return mesReferencia; }
    public List<String> getMesesQuitados() { return mesesQuitados; }
    public boolean isPrevia() { return previa; }
}
