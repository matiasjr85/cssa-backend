package br.com.clubedossargentos.service;

import br.com.clubedossargentos.dto.AluguelEspacoRequestDTO;
import br.com.clubedossargentos.entity.AluguelEspaco;
import br.com.clubedossargentos.entity.Pessoa;
import br.com.clubedossargentos.enums.EspacoLocacao;
import br.com.clubedossargentos.enums.StatusAluguel;
import br.com.clubedossargentos.enums.TipoPagamento;
import br.com.clubedossargentos.exception.RegraNegocioException;
import br.com.clubedossargentos.repository.AluguelEspacoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AluguelEspacoService {
    private final AluguelEspacoRepository aluguelEspacoRepository;
    private final PessoaService pessoaService;

    public AluguelEspacoService(AluguelEspacoRepository aluguelEspacoRepository, PessoaService pessoaService) {
        this.aluguelEspacoRepository = aluguelEspacoRepository;
        this.pessoaService = pessoaService;
    }

    public AluguelEspaco criar(AluguelEspacoRequestDTO dto) {
        if (dto.getHoraFim().isBefore(dto.getHoraInicio()) || dto.getHoraFim().equals(dto.getHoraInicio())) {
            throw new RegraNegocioException("Hora final deve ser maior que a hora inicial.");
        }

        validarEspacos(dto.getEspacosSelecionados());
        BigDecimal valorSugerido = calcularValorTotal(dto.getEspacosSelecionados());
        BigDecimal valorTotal = (dto.getValorCustom() != null && dto.getValorCustom().compareTo(BigDecimal.ZERO) > 0)
                ? dto.getValorCustom()
                : valorSugerido;
        validarParcelamento(valorTotal, dto.getValorPago(), dto.getParcelas());

        AluguelEspaco aluguel = new AluguelEspaco();
        aluguel.setSocio(dto.getSocio());
        aluguel.setDataEvento(dto.getDataEvento());
        aluguel.setHoraInicio(dto.getHoraInicio());
        aluguel.setHoraFim(dto.getHoraFim());
        aluguel.setEspacosSelecionados(dto.getEspacosSelecionados());
        aluguel.setValor(valorTotal);
        aluguel.setParcelas(dto.getParcelas());
        aluguel.setValorPago(dto.getValorPago());
        aluguel.setTipoPagamento(dto.getTipoPagamento());
        aluguel.setObservacao(dto.getObservacao());
        aluguel.setDataCriacao(LocalDateTime.now());
        aluguel.setStatus(dto.getValorPago().compareTo(valorTotal) >= 0 ? StatusAluguel.PAGO : StatusAluguel.PENDENTE);

        if (Boolean.TRUE.equals(dto.getSocio())) {
            if (dto.getPessoaId() == null) throw new RegraNegocioException("Para aluguel de sócio, o pessoaId é obrigatório.");
            Pessoa pessoa = pessoaService.buscarPorId(dto.getPessoaId());
            aluguel.setPessoa(pessoa);
            aluguel.setNomeResponsavel(pessoa.getNome());
            aluguel.setCpfResponsavel(pessoa.getCpf());
            aluguel.setEnderecoResponsavel(pessoa.getEndereco());
        } else {
            String cpf = dto.getCpfResponsavel() == null 
                ? "" 
                : dto.getCpfResponsavel().replaceAll("\\D", "").trim();

            if (cpf.isBlank() || !cpf.matches("\\d{11}")) {
                throw new RegraNegocioException("Informe um CPF com 11 números.");
            }

            aluguel.setNomeResponsavel(dto.getNomeResponsavel().trim());
            aluguel.setCpfResponsavel(cpf);
            aluguel.setEnderecoResponsavel(dto.getEnderecoResponsavel().trim());
        }
        return aluguelEspacoRepository.save(aluguel);
    }

    @Transactional
    public AluguelEspaco atualizar(Long id, AluguelEspacoRequestDTO dto) {
        AluguelEspaco aluguel = aluguelEspacoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Aluguel não encontrado."));

        if (dto.getHoraFim().isBefore(dto.getHoraInicio()) || dto.getHoraFim().equals(dto.getHoraInicio())) {
            throw new RegraNegocioException("Hora final deve ser maior que a hora inicial.");
        }

        validarEspacos(dto.getEspacosSelecionados());
        BigDecimal valorSugerido = calcularValorTotal(dto.getEspacosSelecionados());
        BigDecimal valorTotal = (dto.getValorCustom() != null && dto.getValorCustom().compareTo(BigDecimal.ZERO) > 0)
                ? dto.getValorCustom()
                : valorSugerido;
        validarParcelamento(valorTotal, dto.getValorPago(), dto.getParcelas());

        aluguel.setSocio(dto.getSocio());
        aluguel.setDataEvento(dto.getDataEvento());
        aluguel.setHoraInicio(dto.getHoraInicio());
        aluguel.setHoraFim(dto.getHoraFim());
        aluguel.getEspacosSelecionados().clear();
        aluguel.getEspacosSelecionados().addAll(dto.getEspacosSelecionados());
        aluguel.setValor(valorTotal);
        aluguel.setParcelas(dto.getParcelas());
        aluguel.setValorPago(dto.getValorPago());
        aluguel.setTipoPagamento(dto.getTipoPagamento());
        aluguel.setObservacao(dto.getObservacao());

        if (aluguel.getStatus() != StatusAluguel.CANCELADO) {
            aluguel.setStatus(dto.getValorPago().compareTo(valorTotal) >= 0 ? StatusAluguel.PAGO : StatusAluguel.PENDENTE);
        }

        if (Boolean.TRUE.equals(dto.getSocio())) {
            if (dto.getPessoaId() == null) throw new RegraNegocioException("Para aluguel de sócio, o pessoaId é obrigatório.");
            Pessoa pessoa = pessoaService.buscarPorId(dto.getPessoaId());
            aluguel.setPessoa(pessoa);
            aluguel.setNomeResponsavel(pessoa.getNome());
            aluguel.setCpfResponsavel(pessoa.getCpf());
            aluguel.setEnderecoResponsavel(pessoa.getEndereco());
        } else {
            String cpf = dto.getCpfResponsavel() == null
                    ? ""
                    : dto.getCpfResponsavel().replaceAll("\\D", "").trim();
            if (cpf.isBlank() || !cpf.matches("\\d{11}")) {
                throw new RegraNegocioException("Informe um CPF com 11 números.");
            }
            aluguel.setPessoa(null);
            aluguel.setNomeResponsavel(dto.getNomeResponsavel().trim());
            aluguel.setCpfResponsavel(cpf);
            aluguel.setEnderecoResponsavel(dto.getEnderecoResponsavel().trim());
        }

        return aluguelEspacoRepository.save(aluguel);
    }

    public List<AluguelEspaco> listar() { return aluguelEspacoRepository.findAll(); }

    public AluguelEspaco atualizarStatus(Long id, StatusAluguel status, TipoPagamento tipoPagamento) {
        AluguelEspaco aluguel = aluguelEspacoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Aluguel não encontrado."));
        aluguel.setStatus(status);
        if (tipoPagamento != null) {
            aluguel.setTipoPagamento(tipoPagamento);
        }
        if (status == StatusAluguel.PAGO) {
            aluguel.setDataPagamento(LocalDateTime.now());
            if (aluguel.getTipoPagamento() == null) {
                throw new RegraNegocioException("Informe o tipo de pagamento para marcar o aluguel como pago.");
            }
            if (aluguel.getValorPago() == null || aluguel.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
                aluguel.setValorPago(aluguel.getValor());
            }
        } else if (status == StatusAluguel.CANCELADO) {
            aluguel.setDataPagamento(null);
        }
        return aluguelEspacoRepository.save(aluguel);
    }

    private void validarEspacos(List<EspacoLocacao> espacosSelecionados) {
        if (espacosSelecionados == null || espacosSelecionados.isEmpty()) {
            throw new RegraNegocioException("Selecione pelo menos 1 espaço para o aluguel.");
        }
        Set<EspacoLocacao> unicos = new HashSet<>(espacosSelecionados);
        if (unicos.size() != espacosSelecionados.size()) {
            throw new RegraNegocioException("Não é permitido selecionar o mesmo espaço mais de uma vez.");
        }
    }

    private BigDecimal valorDoEspaco(EspacoLocacao espaco) {
        return switch (espaco) {
            case GINASIO_FRANCO_FERREIRA -> new BigDecimal("5000.00");
            case POLIESPORTIVO -> new BigDecimal("10000.00");
            case ESPACO_FUTVOLEI -> new BigDecimal("2400.00");
            case CHURRASQUEIRA_1, CHURRASQUEIRA_2 -> new BigDecimal("300.00");
            case SEGUNDO_ANDAR -> new BigDecimal("2000.00");
            case QUARTO_ANDAR -> new BigDecimal("3000.00");
        };
    }

    private BigDecimal calcularValorTotal(List<EspacoLocacao> espacosSelecionados) {
        BigDecimal total = BigDecimal.ZERO;
        for (EspacoLocacao espaco : espacosSelecionados) {
            total = total.add(valorDoEspaco(espaco));
        }
        return total;
    }

    private void validarParcelamento(BigDecimal valorTotal, BigDecimal valorPago, Integer parcelas) {
        if (parcelas == null || parcelas < 1) {
            throw new RegraNegocioException("A quantidade de parcelas deve ser no mínimo 1.");
        }
        if (valorPago == null) {
            throw new RegraNegocioException("Informe o valor pago.");
        }
        if (valorPago.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("O valor pago não pode ser negativo.");
        }
        if (parcelas > 1 && valorPago.compareTo(valorTotal) < 0) {
            throw new RegraNegocioException("Parcelamento só pode ser usado quando o valor total estiver quitado.");
        }
    }
}
