package br.com.clubedossargentos.dto;

import java.math.BigDecimal;

public class ReciboDependenteDTO {
    private String nome;
    private String matricula;
    private BigDecimal valor;

    public ReciboDependenteDTO() {}

    public ReciboDependenteDTO(String nome, String matricula, BigDecimal valor) {
        this.nome = nome;
        this.matricula = matricula;
        this.valor = valor;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
