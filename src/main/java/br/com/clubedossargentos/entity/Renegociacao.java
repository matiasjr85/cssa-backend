package br.com.clubedossargentos.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "renegociacoes")
public class Renegociacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorOriginal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorRenegociado;

    @Column(nullable = false)
    private Integer quantidadeParcelas;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorParcela;

    @Column(nullable = false)
    private String motivo;

    private String observacao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private Boolean ativa = true;

    @ElementCollection
    @CollectionTable(name = "renegociacao_meses", joinColumns = @JoinColumn(name = "renegociacao_id"))
    @Column(name = "mes_referencia")
    private List<String> mesesRenegociados = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public BigDecimal getValorOriginal() {
        return valorOriginal;
    }

    public BigDecimal getValorRenegociado() {
        return valorRenegociado;
    }

    public Integer getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getObservacao() {
        return observacao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public List<String> getMesesRenegociados() {
        return mesesRenegociados;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public void setValorRenegociado(BigDecimal valorRenegociado) {
        this.valorRenegociado = valorRenegociado;
    }

    public void setQuantidadeParcelas(Integer quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public void setMesesRenegociados(List<String> mesesRenegociados) {
        this.mesesRenegociados = mesesRenegociados;
    }
}
