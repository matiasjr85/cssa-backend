package br.com.clubedossargentos.service;

import br.com.clubedossargentos.dto.AuditoriaAcaoResponseDTO;
import br.com.clubedossargentos.entity.AuditoriaAcao;
import br.com.clubedossargentos.exception.AcessoNegadoException;
import br.com.clubedossargentos.exception.NaoAutenticadoException;
import br.com.clubedossargentos.repository.AuditoriaAcaoRepository;
import br.com.clubedossargentos.security.UsuarioContexto;
import br.com.clubedossargentos.security.UsuarioLogado;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaService {
    private final AuditoriaAcaoRepository auditoriaAcaoRepository;

    public AuditoriaService(AuditoriaAcaoRepository auditoriaAcaoRepository) {
        this.auditoriaAcaoRepository = auditoriaAcaoRepository;
    }

    public void registrar(String acao, String entidade, Long entidadeId, String descricao) {
        UsuarioLogado usuario = UsuarioContexto.obter();
        if (usuario == null) return;

        AuditoriaAcao item = new AuditoriaAcao();
        item.setUsuarioId(usuario.getId());
        item.setUsuarioNome(usuario.getNome());
        item.setPerfilUsuario(usuario.getPerfil());
        item.setAcao(acao);
        item.setEntidade(entidade);
        item.setEntidadeId(entidadeId);
        item.setDescricao(descricao);
        item.setDataHora(LocalDateTime.now());
        auditoriaAcaoRepository.save(item);
    }

    public UsuarioLogado exigirUsuarioLogado() {
        UsuarioLogado usuario = UsuarioContexto.obter();
        if (usuario == null) throw new NaoAutenticadoException("Faça login para acessar o sistema.");
        return usuario;
    }

    public UsuarioLogado exigirAdmin() {
        UsuarioLogado usuario = exigirUsuarioLogado();
        if (!usuario.isAdmin()) throw new AcessoNegadoException("Apenas administradores podem acessar este recurso.");
        return usuario;
    }

    public List<AuditoriaAcaoResponseDTO> listar(Long usuarioId, LocalDate data) {
        exigirAdmin();
        LocalDate filtro = data != null ? data : LocalDate.now();
        LocalDateTime inicio = filtro.atStartOfDay();
        LocalDateTime fim = filtro.plusDays(1).atStartOfDay().minusNanos(1);

        List<AuditoriaAcao> itens = usuarioId != null
                ? auditoriaAcaoRepository.findByUsuarioIdAndDataHoraBetweenOrderByDataHoraDesc(usuarioId, inicio, fim)
                : auditoriaAcaoRepository.findByDataHoraBetweenOrderByDataHoraDesc(inicio, fim);

        return itens.stream()
                .map(item -> new AuditoriaAcaoResponseDTO(item.getId(), item.getUsuarioId(), item.getUsuarioNome(), item.getPerfilUsuario(), item.getAcao(), item.getEntidade(), item.getEntidadeId(), item.getDescricao(), item.getDataHora()))
                .toList();
    }
}
