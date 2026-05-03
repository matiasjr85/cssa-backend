package br.com.clubedossargentos.repository;

import br.com.clubedossargentos.entity.AuditoriaAcao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaAcaoRepository extends JpaRepository<AuditoriaAcao, Long> {
    List<AuditoriaAcao> findByUsuarioIdAndDataHoraBetweenOrderByDataHoraDesc(Long usuarioId, LocalDateTime inicio, LocalDateTime fim);
    List<AuditoriaAcao> findByDataHoraBetweenOrderByDataHoraDesc(LocalDateTime inicio, LocalDateTime fim);
}
