package br.com.clubedossargentos.dto;

import br.com.clubedossargentos.enums.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioRequestDTO {
    @NotBlank(message = "Nome é obrigatório.")
    private String nome;
    @NotBlank(message = "Senha é obrigatória.")
    private String senha;
    @NotNull(message = "Perfil é obrigatório.")
    private PerfilUsuario perfil;
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }
}
