package br.com.clubedossargentos.entity;

import br.com.clubedossargentos.enums.PerfilUsuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_acoes")
public class AuditoriaAcao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private String usuarioNome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfilUsuario;

    @Column(nullable = false)
    private String acao;

    @Column(nullable = false)
    private String entidade;

    private Long entidadeId;

    @Column(length = 1200)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
    public PerfilUsuario getPerfilUsuario() { return perfilUsuario; }
    public void setPerfilUsuario(PerfilUsuario perfilUsuario) { this.perfilUsuario = perfilUsuario; }
    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }
    public String getEntidade() { return entidade; }
    public void setEntidade(String entidade) { this.entidade = entidade; }
    public Long getEntidadeId() { return entidadeId; }
    public void setEntidadeId(Long entidadeId) { this.entidadeId = entidadeId; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
