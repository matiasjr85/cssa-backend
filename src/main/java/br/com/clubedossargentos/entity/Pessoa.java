package br.com.clubedossargentos.entity;

import br.com.clubedossargentos.enums.StatusSocio;
import br.com.clubedossargentos.enums.TipoSocio;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pessoas")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String matricula;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSocio tipoSocio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSocio status = StatusSocio.ATIVO;

    @Column(nullable = false)
    private Boolean isento = false;

    @Column(nullable = false)
    private Boolean sinal = false;

    @Column(nullable = false)
    private Boolean joiaCobrada = false;

    @Column(nullable = false)
    private Boolean semMargem = false;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorMensalidadeCustom;

    @JsonIgnoreProperties({"dependentes", "pagamentos", "cpf", "endereco", "isento", "sinal", "joiaCobrada", "semMargem", "valorMensalidadeCustom", "dataCadastro", "socioTitular"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_titular_id")
    private Pessoa socioTitular;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dependente> dependentes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "pessoa_pagamentos", joinColumns = @JoinColumn(name = "pessoa_id"))
    private List<PagamentoRegistro> pagamentos = new ArrayList<>();

    public Pessoa() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public TipoSocio getTipoSocio() { return tipoSocio; }
    public void setTipoSocio(TipoSocio tipoSocio) { this.tipoSocio = tipoSocio; }
    public StatusSocio getStatus() { return status; }
    public void setStatus(StatusSocio status) { this.status = status; }
    public Boolean getIsento() { return isento; }
    public void setIsento(Boolean isento) { this.isento = isento; }
    public Boolean getSinal() { return sinal; }
    public void setSinal(Boolean sinal) { this.sinal = sinal; }
    public Boolean getJoiaCobrada() { return joiaCobrada; }
    public BigDecimal getValorMensalidadeCustom() { return valorMensalidadeCustom; }
    public Boolean getSemMargem() { return semMargem; }
    public void setJoiaCobrada(Boolean joiaCobrada) { this.joiaCobrada = joiaCobrada; }
    public void setValorMensalidadeCustom(BigDecimal valorMensalidadeCustom) { this.valorMensalidadeCustom = valorMensalidadeCustom; }
    public void setSemMargem(Boolean semMargem) { this.semMargem = semMargem; }
    public Pessoa getSocioTitular() { return socioTitular; }
    public void setSocioTitular(Pessoa socioTitular) { this.socioTitular = socioTitular; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public List<Dependente> getDependentes() { return dependentes; }
    public void setDependentes(List<Dependente> dependentes) { this.dependentes = dependentes; }
    public List<PagamentoRegistro> getPagamentos() { return pagamentos; }
    public void setPagamentos(List<PagamentoRegistro> pagamentos) { this.pagamentos = pagamentos; }
}
