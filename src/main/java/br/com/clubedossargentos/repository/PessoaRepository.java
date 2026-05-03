package br.com.clubedossargentos.repository;

import br.com.clubedossargentos.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    boolean existsByCpf(String cpf);
    long countByDataCadastroBetween(LocalDateTime inicio, LocalDateTime fim);
}
