package br.com.clubedossargentos.dto;

import br.com.clubedossargentos.enums.PerfilUsuario;

import java.time.LocalDateTime;

public class UsuarioResponseDTO {
    private Long id; private String nome; private PerfilUsuario perfil; private Boolean ativo; private LocalDateTime dataCriacao; private LocalDateTime ultimoLogin;
    public UsuarioResponseDTO() {}
    public UsuarioResponseDTO(Long id, String nome, PerfilUsuario perfil, Boolean ativo, LocalDateTime dataCriacao, LocalDateTime ultimoLogin) { this.id=id; this.nome=nome; this.perfil=perfil; this.ativo=ativo; this.dataCriacao=dataCriacao; this.ultimoLogin=ultimoLogin; }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public PerfilUsuario getPerfil() { return perfil; } public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }
    public Boolean getAtivo() { return ativo; } public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; } public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getUltimoLogin() { return ultimoLogin; } public void setUltimoLogin(LocalDateTime ultimoLogin) { this.ultimoLogin = ultimoLogin; }
}
