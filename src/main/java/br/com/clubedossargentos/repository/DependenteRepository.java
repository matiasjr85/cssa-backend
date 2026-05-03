package br.com.clubedossargentos.repository;

import br.com.clubedossargentos.entity.Dependente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DependenteRepository extends JpaRepository<Dependente, Long> {

    List<Dependente> findByPessoaId(Long pessoaId);

    Optional<Dependente> findByIdAndPessoaId(Long id, Long pessoaId);

    boolean existsByCpf(String cpf);

    long countByPessoaId(Long pessoaId);
}
