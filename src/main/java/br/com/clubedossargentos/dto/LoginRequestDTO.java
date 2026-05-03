package br.com.clubedossargentos.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
    @NotBlank(message = "Nome é obrigatório.")
    private String nome;
    @NotBlank(message = "Senha é obrigatória.")
    private String senha;
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
