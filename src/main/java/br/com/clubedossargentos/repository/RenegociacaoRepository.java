package br.com.clubedossargentos.repository;

import br.com.clubedossargentos.entity.Renegociacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RenegociacaoRepository extends JpaRepository<Renegociacao, Long> {
    List<Renegociacao> findByPessoaIdOrderByDataCriacaoDesc(Long pessoaId);
}