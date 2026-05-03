package br.com.clubedossargentos.security;

import br.com.clubedossargentos.enums.PerfilUsuario;

public class UsuarioLogado {
    private final Long id;
    private final String nome;
    private final PerfilUsuario perfil;
    private final String token;

    public UsuarioLogado(Long id, String nome, PerfilUsuario perfil, String token) {
        this.id = id;
        this.nome = nome;
        this.perfil = perfil;
        this.token = token;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public PerfilUsuario getPerfil() { return perfil; }
    public String getToken() { return token; }
    public boolean isAdmin() { return perfil == PerfilUsuario.ADMIN; }
}
