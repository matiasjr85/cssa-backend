package br.com.clubedossargentos.repository;

import br.com.clubedossargentos.entity.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Long> {
    boolean existsByNomeIgnoreCase(String nome);
    Optional<UsuarioSistema> findByNomeIgnoreCase(String nome);
    Optional<UsuarioSistema> findByTokenAcesso(String tokenAcesso);
}
