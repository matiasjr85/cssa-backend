package br.com.clubedossargentos.dto;

import br.com.clubedossargentos.enums.PerfilUsuario;

import java.time.LocalDateTime;

public class AuditoriaAcaoResponseDTO {
    private Long id; private Long usuarioId; private String usuarioNome; private PerfilUsuario perfilUsuario; private String acao; private String entidade; private Long entidadeId; private String descricao; private LocalDateTime dataHora;
    public AuditoriaAcaoResponseDTO() {}
    public AuditoriaAcaoResponseDTO(Long id, Long usuarioId, String usuarioNome, PerfilUsuario perfilUsuario, String acao, String entidade, Long entidadeId, String descricao, LocalDateTime dataHora) { this.id=id; this.usuarioId=usuarioId; this.usuarioNome=usuarioNome; this.perfilUsuario=perfilUsuario; this.acao=acao; this.entidade=entidade; this.entidadeId=entidadeId; this.descricao=descricao; this.dataHora=dataHora; }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; } public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNome() { return usuarioNome; } public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
    public PerfilUsuario getPerfilUsuario() { return perfilUsuario; } public void setPerfilUsuario(PerfilUsuario perfilUsuario) { this.perfilUsuario = perfilUsuario; }
    public String getAcao() { return acao; } public void setAcao(String acao) { this.acao = acao; }
    public String getEntidade() { return entidade; } public void setEntidade(String entidade) { this.entidade = entidade; }
    public Long getEntidadeId() { return entidadeId; } public void setEntidadeId(Long entidadeId) { this.entidadeId = entidadeId; }
    public String getDescricao() { return descricao; } public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataHora() { return dataHora; } public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
