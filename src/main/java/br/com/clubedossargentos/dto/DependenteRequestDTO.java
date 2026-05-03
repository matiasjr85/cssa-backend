package br.com.clubedossargentos.dto;

import br.com.clubedossargentos.enums.TipoParentesco;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DependenteRequestDTO {

    @NotBlank(message = "Nome do dependente é obrigatório.")
    private String nome;
    
    @Pattern(regexp = "^$|\\d{11}", message = "CPF do dependente deve conter 11 números quando informado.")
    private String cpf;

    @NotBlank(message = "Endereço do dependente é obrigatório.")
    private String endereco;

    @NotNull(message = "Data de nascimento do dependente é obrigatória.")
    private LocalDate dataNascimento;

    @NotNull(message = "Tipo de parentesco é obrigatório.")
    private TipoParentesco tipoParentesco;

    private Boolean universitario = false;

    @DecimalMin(value = "0.00", message = "Valor mensal do dependente não pode ser negativo.")
    private BigDecimal valorMensalidadeCustom;

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public TipoParentesco getTipoParentesco() {
        return tipoParentesco;
    }

    public Boolean getUniversitario() {
        return universitario;
    }

    public BigDecimal getValorMensalidadeCustom() {
        return valorMensalidadeCustom;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setTipoParentesco(TipoParentesco tipoParentesco) {
        this.tipoParentesco = tipoParentesco;
    }

    public void setUniversitario(Boolean universitario) {
        this.universitario = universitario;
    }

    public void setValorMensalidadeCustom(BigDecimal valorMensalidadeCustom) {
        this.valorMensalidadeCustom = valorMensalidadeCustom;
    }
}
