package br.com.clubedossargentos.dto;

import br.com.clubedossargentos.enums.PerfilUsuario;

public class LoginResponseDTO {
    private Long id; private String nome; private PerfilUsuario perfil; private String token;
    public LoginResponseDTO() {}
    public LoginResponseDTO(Long id, String nome, PerfilUsuario perfil, String token) { this.id=id; this.nome=nome; this.perfil=perfil; this.token=token; }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public PerfilUsuario getPerfil() { return perfil; } public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }
    public String getToken() { return token; } public void setToken(String token) { this.token = token; }
}
