package br.com.clubedossargentos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PagamentoHistoricoDetalhadoDTO {
    private Long pessoaId;
    private String nomeSocio;
    private String matriculaSocio;
    private String cpfSocio;
    private String mesReferencia;
    private LocalDateTime dataPagamento;
    private String tipoPagamento;
    private BigDecimal valorSocio;
    private BigDecimal valorDependentes;
    private BigDecimal valorTotal;
    private Integer quantidadeParcelas;
    private BigDecimal valorParcela;
    private String observacao;
    private List<ReciboDependenteDTO> dependentesDetalhados;
    private List<String> mesesQuitados;

    public PagamentoHistoricoDetalhadoDTO(Long pessoaId, String nomeSocio, String matriculaSocio, String cpfSocio,
                                          String mesReferencia, LocalDateTime dataPagamento, String tipoPagamento, BigDecimal valorSocio,
                                          BigDecimal valorDependentes, BigDecimal valorTotal, Integer quantidadeParcelas,
                                          BigDecimal valorParcela, String observacao,
                                          List<ReciboDependenteDTO> dependentesDetalhados, List<String> mesesQuitados) {
        this.pessoaId = pessoaId;
        this.nomeSocio = nomeSocio;
        this.matriculaSocio = matriculaSocio;
        this.cpfSocio = cpfSocio;
        this.mesReferencia = mesReferencia;
        this.dataPagamento = dataPagamento;
        this.tipoPagamento = tipoPagamento;
        this.valorSocio = valorSocio;
        this.valorDependentes = valorDependentes;
        this.valorTotal = valorTotal;
        this.quantidadeParcelas = quantidadeParcelas;
        this.valorParcela = valorParcela;
        this.observacao = observacao;
        this.dependentesDetalhados = dependentesDetalhados;
        this.mesesQuitados = mesesQuitados;
    }

    public Long getPessoaId() { return pessoaId; }
    public String getNomeSocio() { return nomeSocio; }
    public String getMatriculaSocio() { return matriculaSocio; }
    public String getCpfSocio() { return cpfSocio; }
    public String getMesReferencia() { return mesReferencia; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public String getTipoPagamento() { return tipoPagamento; }
    public BigDecimal getValorSocio() { return valorSocio; }
    public BigDecimal getValorDependentes() { return valorDependentes; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public Integer getQuantidadeParcelas() { return quantidadeParcelas; }
    public BigDecimal getValorParcela() { return valorParcela; }
    public String getObservacao() { return observacao; }
    public List<ReciboDependenteDTO> getDependentesDetalhados() { return dependentesDetalhados; }
    public List<String> getMesesQuitados() { return mesesQuitados; }
}
