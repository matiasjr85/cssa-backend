package br.com.clubedossargentos.entity;

import br.com.clubedossargentos.enums.TipoParentesco;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;

@Entity
@Table(name = "dependentes")
public class Dependente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 13)
    private String matricula;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoParentesco tipoParentesco;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean universitario = false;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorMensalidadeCustom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    @JsonIgnore
    private Pessoa pessoa;

    public Dependente() {
    }

    public Dependente(String nome, String cpf, String endereco, LocalDate dataNascimento, TipoParentesco tipoParentesco) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.dataNascimento = dataNascimento;
        this.tipoParentesco = tipoParentesco;
    }

    public Long getId() { return id; }
    public String getMatricula() { return matricula; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEndereco() { return endereco; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public TipoParentesco getTipoParentesco() { return tipoParentesco; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public Boolean getUniversitario() { return universitario; }
    public BigDecimal getValorMensalidadeCustom() { return valorMensalidadeCustom; }
    public Pessoa getPessoa() { return pessoa; }
    public void setId(Long id) { this.id = id; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public void setTipoParentesco(TipoParentesco tipoParentesco) { this.tipoParentesco = tipoParentesco; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public void setUniversitario(Boolean universitario) { this.universitario = universitario; }
    public void setValorMensalidadeCustom(BigDecimal valorMensalidadeCustom) { this.valorMensalidadeCustom = valorMensalidadeCustom; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }

    public int getIdade() {
        if (dataNascimento == null) return 0;
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public boolean isFilhoOuFilha() {
        return tipoParentesco == TipoParentesco.FILHO || tipoParentesco == TipoParentesco.FILHA;
    }

    public boolean isConjuge() {
        return tipoParentesco == TipoParentesco.MARIDO || tipoParentesco == TipoParentesco.MULHER;
    }

    public boolean isSogroOuSogra() {
        return tipoParentesco == TipoParentesco.SOGRO || tipoParentesco == TipoParentesco.SOGRA;
    }

    public boolean isPaiOuMae() {
        return tipoParentesco == TipoParentesco.PAI || tipoParentesco == TipoParentesco.MAE;
    }

    public boolean isDayUse() {
        return tipoParentesco == TipoParentesco.DAY_USE;
    }

    public boolean eMenorDe14ConsiderandoInicioDaCobrancaNoMesSeguinte(YearMonth referencia) {
        if (dataNascimento == null) return false;
        YearMonth mesDo14Aniversario = YearMonth.from(dataNascimento.plusYears(14));
        return !referencia.isAfter(mesDo14Aniversario);
    }
}
