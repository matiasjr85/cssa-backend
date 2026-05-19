package br.com.clubedossargentos.service;

import br.com.clubedossargentos.dto.HistoricoPagamentoDTO;
import br.com.clubedossargentos.dto.PagamentoHistoricoDetalhadoDTO;
import br.com.clubedossargentos.dto.ReciboDTO;
import br.com.clubedossargentos.dto.ReciboDependenteDTO;
import br.com.clubedossargentos.dto.ReciboRenegociacaoDTO;
import br.com.clubedossargentos.dto.RenegociacaoRequestDTO;
import br.com.clubedossargentos.entity.Dependente;
import br.com.clubedossargentos.entity.PagamentoRegistro;
import br.com.clubedossargentos.entity.Pessoa;
import br.com.clubedossargentos.entity.Renegociacao;
import br.com.clubedossargentos.enums.StatusSocio;
import br.com.clubedossargentos.enums.TipoPagamento;
import br.com.clubedossargentos.enums.TipoSocio;
import br.com.clubedossargentos.exception.DependenteNaoEncontradoException;
import br.com.clubedossargentos.exception.PessoaNaoEncontradaException;
import br.com.clubedossargentos.exception.RegraNegocioException;
import br.com.clubedossargentos.repository.DependenteRepository;
import br.com.clubedossargentos.repository.PessoaRepository;
import br.com.clubedossargentos.repository.RenegociacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PessoaService {
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal TREZE = new BigDecimal("13.00");
    private static final BigDecimal QUINZE = new BigDecimal("15.00");
    private static final BigDecimal QUARENTA = new BigDecimal("40.00");
    private static final BigDecimal SESSENTA = new BigDecimal("60.00");
    private static final BigDecimal VALOR_JOIA_RECREATIVO = new BigDecimal("255.00");
    private static final DateTimeFormatter MES_FMT = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final DateTimeFormatter MATRICULA_FMT = DateTimeFormatter.ofPattern("yyyyMM");

    private final PessoaRepository pessoaRepository;
    private final DependenteRepository dependenteRepository;
    private final RenegociacaoRepository renegociacaoRepository;

    public PessoaService(PessoaRepository pessoaRepository, DependenteRepository dependenteRepository, RenegociacaoRepository renegociacaoRepository) {
        this.pessoaRepository = pessoaRepository;
        this.dependenteRepository = dependenteRepository;
        this.renegociacaoRepository = renegociacaoRepository;
    }

    public Pessoa criar(Pessoa pessoa, Long socioTitularId) {
        validarPessoa(pessoa);
        aplicarRelacionamentosERegras(pessoa, socioTitularId);
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new RegraNegocioException("Já existe um sócio cadastrado com este CPF.");
        }
        pessoa.setDataCadastro(LocalDateTime.now());
        pessoa.setMatricula(gerarMatriculaSocio(pessoa.getDataCadastro()));
        pessoa.setStatus(StatusSocio.ATIVO);
        if (pessoa.getDependentes() == null) pessoa.setDependentes(new ArrayList<>());
        if (pessoa.getPagamentos() == null) pessoa.setPagamentos(new ArrayList<>());
        return pessoaRepository.save(pessoa);
    }

    public List<Pessoa> listar() {
        return pessoaRepository.findAll().stream()
                .sorted(Comparator.comparing(Pessoa::getNome, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public Pessoa buscarPorId(Long id) {
        return pessoaRepository.findById(id).orElseThrow(() -> new PessoaNaoEncontradaException("Pessoa não encontrada."));
    }

    public Pessoa atualizar(Long id, Pessoa dados, Long socioTitularId) {
        Pessoa pessoa = buscarPorId(id);
        validarPessoa(dados);
        if (!pessoa.getCpf().equals(dados.getCpf()) && pessoaRepository.existsByCpf(dados.getCpf())) {
            throw new RegraNegocioException("Já existe um sócio cadastrado com este CPF.");
        }
        boolean joiaAntes = Boolean.TRUE.equals(pessoa.getSinal());
        boolean joiaNova = Boolean.TRUE.equals(dados.getSinal());

        pessoa.setNome(dados.getNome().trim());
        pessoa.setCpf(dados.getCpf());
        pessoa.setEndereco(dados.getEndereco().trim());
        pessoa.setTipoSocio(dados.getTipoSocio());
        pessoa.setIsento(Boolean.TRUE.equals(dados.getIsento()));
        pessoa.setSinal(joiaNova);
        pessoa.setSemMargem(Boolean.TRUE.equals(dados.getSemMargem()));
        pessoa.setValorMensalidadeCustom(normalizarValor(dados.getValorMensalidadeCustom()));

        if (!joiaNova) {
            pessoa.setJoiaCobrada(false);
        } else if (!joiaAntes || pessoa.getJoiaCobrada() == null) {
            pessoa.setJoiaCobrada(false);
        }

        aplicarRelacionamentosERegras(pessoa, socioTitularId);
        recalcularStatusSocio(pessoa);
        return pessoaRepository.save(pessoa);
    }

    public void excluir(Long id) {
        pessoaRepository.delete(buscarPorId(id));
    }

    public List<Dependente> listarDependentes(Long id) {
        buscarPorId(id);
        return dependenteRepository.findByPessoaId(id).stream()
                .sorted(Comparator.comparing(Dependente::getNome, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    @Transactional
    public Pessoa adicionarDependente(Long id, Dependente dependente) {
        Pessoa pessoa = buscarPorId(id);
        validarDependente(dependente);

        dependente.setCpf(normalizarCpfOpcionalDependente(dependente.getCpf()));

        if (dependente.getCpf() != null && dependenteRepository.existsByCpf(dependente.getCpf())) {
            throw new RegraNegocioException("Já existe um dependente cadastrado com este CPF.");
        }

        dependente.setPessoa(pessoa);
        dependente.setDataCadastro(LocalDateTime.now());
        dependente.setMatricula(gerarMatriculaDependente(pessoa));
        dependente.setUniversitario(dependente.isDayUse() ? false : Boolean.TRUE.equals(dependente.getUniversitario()));
        dependente.setValorMensalidadeCustom(normalizarValor(dependente.getValorMensalidadeCustom()));
        dependenteRepository.save(dependente);
        return buscarPorId(id);
    }

    @Transactional
    public Dependente atualizarDependente(Long pessoaId, Long dependenteId, Dependente dados) {
        buscarPorId(pessoaId);
        Dependente dependente = dependenteRepository.findByIdAndPessoaId(dependenteId, pessoaId)
                .orElseThrow(() -> new DependenteNaoEncontradoException("Dependente não encontrado para essa pessoa."));

        validarDependente(dados);

        String cpfNovo = normalizarCpfOpcionalDependente(dados.getCpf());
        String cpfAtual = normalizarCpfOpcionalDependente(dependente.getCpf());

        if (cpfNovo != null && !Objects.equals(cpfAtual, cpfNovo) && dependenteRepository.existsByCpf(cpfNovo)) {
            throw new RegraNegocioException("Já existe um dependente cadastrado com este CPF.");
        }

        dependente.setNome(dados.getNome().trim());
        dependente.setCpf(cpfNovo);
        dependente.setEndereco(dados.getEndereco().trim());
        dependente.setDataNascimento(dados.getDataNascimento());
        dependente.setTipoParentesco(dados.getTipoParentesco());
        dependente.setUniversitario(dependente.isDayUse() ? false : Boolean.TRUE.equals(dados.getUniversitario()));
        dependente.setValorMensalidadeCustom(normalizarValor(dados.getValorMensalidadeCustom()));
        return dependenteRepository.save(dependente);
    }

    @Transactional
    public void excluirDependente(Long pessoaId, Long dependenteId) {
        buscarPorId(pessoaId);
        Dependente dependente = dependenteRepository.findByIdAndPessoaId(dependenteId, pessoaId)
                .orElseThrow(() -> new DependenteNaoEncontradoException("Dependente não encontrado para essa pessoa."));
        dependenteRepository.delete(dependente);
    }

    public boolean jaPagouNoMes(Long id) {
        return jaPagouNoMesInterno(buscarPorId(id), YearMonth.now());
    }

    private boolean jaPagouNoMesInterno(Pessoa pessoa, YearMonth mes) {
        return pessoa.getPagamentos() != null && pessoa.getPagamentos().stream()
                .flatMap(p -> obterMesesQuitados(p).stream())
                .anyMatch(m -> m.equals(mes.format(MES_FMT)));
    }

    public BigDecimal calcularValorDependentes(Pessoa pessoa) {
        return montarDetalhesDependentes(pessoa).stream()
                .map(ReciboDependenteDTO::getValor)
                .reduce(ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValorIndividualDependente(Pessoa pessoa, Dependente dependente) {
        if (dependente.getValorMensalidadeCustom() != null) {
            return normalizarValor(dependente.getValorMensalidadeCustom());
        }

        YearMonth referencia = YearMonth.now();

        if (dependente.isDayUse()) {
            return dependente.eMenorDe14ConsiderandoInicioDaCobrancaNoMesSeguinte(referencia) ? TREZE : QUARENTA;
        }
        if (dependente.isConjuge()) return QUINZE;
        if (dependente.isSogroOuSogra()) return QUINZE;
        if (dependente.isPaiOuMae()) return QUINZE;
        if (dependente.isFilhoOuFilha()) {
            if (dependente.eMenorDe14ConsiderandoInicioDaCobrancaNoMesSeguinte(referencia)) return ZERO;
            if (dependente.getIdade() <= 21) return QUINZE;
            if (Boolean.TRUE.equals(dependente.getUniversitario()) && dependente.getIdade() <= 24) return QUINZE;
            return SESSENTA;
        }
        return SESSENTA;
    }

    private boolean deveCobrarJoia(Pessoa pessoa) {
        return pessoa.getTipoSocio() == TipoSocio.RECREATIVO
                && Boolean.TRUE.equals(pessoa.getSinal())
                && !Boolean.TRUE.equals(pessoa.getJoiaCobrada());
    }

    public BigDecimal calcularValorSocio(Pessoa pessoa) {
        return calcularValorSocio(pessoa, true);
    }

    public BigDecimal calcularValorSocio(Pessoa pessoa, boolean incluirJoiaSePrimeiroPagamento) {
        if (pessoa.getTipoSocio() == null) {
            throw new RegraNegocioException("Tipo do sócio é obrigatório.");
        }
        BigDecimal valorBase = pessoa.getValorMensalidadeCustom() != null
                ? normalizarValor(pessoa.getValorMensalidadeCustom())
                : pessoa.getTipoSocio().getValorMensalidade();
        if (incluirJoiaSePrimeiroPagamento && deveCobrarJoia(pessoa)) {
            return valorBase.add(VALOR_JOIA_RECREATIVO);
        }
        return valorBase;
    }

    public BigDecimal calcularValorTotalMensalidade(Pessoa pessoa) {
        return calcularValorSocio(pessoa).add(calcularValorDependentes(pessoa));
    }

    @Transactional
    public Pessoa pagarMensalidade(Long id, Integer quantidadeParcelas, String observacao, BigDecimal valorSocioPago, BigDecimal valorDependentesPago, TipoPagamento tipoPagamento, List<String> mesesReferencia) {
        Pessoa pessoa = buscarPorId(id);
        if (pessoa.getStatus() == StatusSocio.DEMITIDO) {
            throw new RegraNegocioException("Sócio demitido não pode realizar pagamento.");
        }
        aplicarRelacionamentosERegras(pessoa, pessoa.getSocioTitular() != null ? pessoa.getSocioTitular().getId() : null);

        if (quantidadeParcelas == null) quantidadeParcelas = 1;
        if (quantidadeParcelas < 1 || quantidadeParcelas > 10) {
            throw new RegraNegocioException("O parcelamento deve ser entre 1 e 10 vezes.");
        }
        if (tipoPagamento == null) {
            throw new RegraNegocioException("Informe o tipo de pagamento.");
        }
        List<String> mesesSelecionados = normalizarMesesPagamento(mesesReferencia);
        for (String mesReferenciaSelecionado : mesesSelecionados) {
            if (jaPagouNoMesInterno(pessoa, parseMesReferencia(mesReferenciaSelecionado))) {
                throw new RegraNegocioException("O mês " + mesReferenciaSelecionado + " já está quitado.");
            }
        }

        List<ReciboDependenteDTO> detalhados = montarDetalhesDependentes(pessoa);
        int quantidadeMeses = mesesSelecionados.size();
        BigDecimal valorSocioMensal = valorSocioPago != null
                ? valorSocioPago.setScale(2, RoundingMode.HALF_UP)
                : calcularValorSocio(pessoa, false);
        BigDecimal valorDependentesMensal = valorDependentesPago != null
                ? valorDependentesPago.setScale(2, RoundingMode.HALF_UP)
                : detalhados.stream().map(ReciboDependenteDTO::getValor).reduce(ZERO, BigDecimal::add);
        BigDecimal valorSocio = valorSocioMensal.multiply(BigDecimal.valueOf(quantidadeMeses)).setScale(2, RoundingMode.HALF_UP);
        if (valorSocioPago == null && deveCobrarJoia(pessoa)) {
            valorSocio = valorSocio.add(VALOR_JOIA_RECREATIVO);
        }
        BigDecimal valorDependentes = valorDependentesMensal.multiply(BigDecimal.valueOf(quantidadeMeses)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorSocio.add(valorDependentes);
        BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(quantidadeParcelas), 2, RoundingMode.HALF_UP);

        String mesPrincipal = mesesSelecionados.get(0);
        PagamentoRegistro pagamento = new PagamentoRegistro(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                valorSocio,
                valorDependentes,
                valorTotal,
                mesPrincipal,
                tipoPagamento.name(),
                quantidadeParcelas,
                valorParcela,
                observacao,
                serializarDetalhesDependentes(detalhados),
                String.join(",", mesesSelecionados)
        );

        pessoa.getPagamentos().add(pagamento);
        if (deveCobrarJoia(pessoa)) {
            pessoa.setJoiaCobrada(true);
            pessoa.setSinal(false);
        }
        pessoa.setStatus(StatusSocio.ATIVO);
        return pessoaRepository.save(pessoa);
    }

    public ReciboDTO gerarReciboPrevia(Long id, Integer quantidadeParcelas, BigDecimal valorSocioPago, BigDecimal valorDependentesPago, String tipoPagamento, List<String> mesesReferencia) {
        Pessoa pessoa = buscarPorId(id);
        if (quantidadeParcelas == null || quantidadeParcelas < 1 || quantidadeParcelas > 10) quantidadeParcelas = 1;
        return montarRecibo(pessoa, null, quantidadeParcelas, true, valorSocioPago, valorDependentesPago, tipoPagamento, normalizarMesesPagamento(mesesReferencia));
    }

    public ReciboDTO gerarRecibo(Long id) {
        Pessoa pessoa = buscarPorId(id);
        PagamentoRegistro pagamento = pessoa.getPagamentos().stream()
                .max(Comparator.comparing(PagamentoRegistro::getDataHora, Comparator.nullsLast(Comparator.naturalOrder())))
                .orElseThrow(() -> new RegraNegocioException("Nenhum pagamento encontrado para este sócio."));
        return montarRecibo(pessoa, pagamento, pagamento.getQuantidadeParcelas(), false, null, null, pagamento.getTipoPagamento(), obterMesesQuitados(pagamento));
    }

    private ReciboDTO montarRecibo(Pessoa pessoa, PagamentoRegistro pagamento, Integer quantidadeParcelas, boolean previa,
                                   BigDecimal valorSocioPago, BigDecimal valorDependentesPago, String tipoPagamento, List<String> mesesQuitadosSelecionados) {
        List<Dependente> dependentes = dependenteRepository.findByPessoaId(pessoa.getId());
        List<String> nomesDependentes = dependentes.stream().map(Dependente::getNome).collect(Collectors.toList());
        List<ReciboDependenteDTO> detalhados = pagamento != null && pagamento.getDetalhesDependentes() != null && !pagamento.getDetalhesDependentes().isBlank()
                ? desserializarDetalhesDependentes(pagamento.getDetalhesDependentes())
                : montarDetalhesDependentes(pessoa);

        int quantidadeMesesSelecionados = mesesQuitadosSelecionados == null || mesesQuitadosSelecionados.isEmpty() ? 1 : mesesQuitadosSelecionados.size();
        BigDecimal valorSocio = pagamento != null
                ? pagamento.getValorSocio()
                : (valorSocioPago != null
                    ? valorSocioPago.setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(quantidadeMesesSelecionados)).setScale(2, RoundingMode.HALF_UP)
                    : calcularValorSocio(pessoa, false).multiply(BigDecimal.valueOf(quantidadeMesesSelecionados)).setScale(2, RoundingMode.HALF_UP).add(deveCobrarJoia(pessoa) ? VALOR_JOIA_RECREATIVO : ZERO));

        BigDecimal valorDependentes = pagamento != null
                ? pagamento.getValorDependentes()
                : (valorDependentesPago != null
                    ? valorDependentesPago.setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(quantidadeMesesSelecionados)).setScale(2, RoundingMode.HALF_UP)
                    : detalhados.stream().map(ReciboDependenteDTO::getValor).reduce(ZERO, BigDecimal::add).multiply(BigDecimal.valueOf(quantidadeMesesSelecionados)).setScale(2, RoundingMode.HALF_UP));

        BigDecimal valorTotal = pagamento != null ? pagamento.getValorTotal() : valorSocio.add(valorDependentes);
        BigDecimal valorParcela = pagamento != null
                ? pagamento.getValorParcela()
                : valorTotal.divide(BigDecimal.valueOf(quantidadeParcelas), 2, RoundingMode.HALF_UP);
        LocalDateTime data = pagamento != null ? pagamento.getDataHora() : LocalDateTime.now();
        List<String> mesesQuitados = pagamento != null
                ? obterMesesQuitados(pagamento)
                : (mesesQuitadosSelecionados == null || mesesQuitadosSelecionados.isEmpty()
                    ? List.of(YearMonth.now().format(MES_FMT))
                    : mesesQuitadosSelecionados);
        String mes = mesesQuitados.get(0);

        return new ReciboDTO(
                pessoa.getNome(),
                pessoa.getMatricula(),
                pessoa.getTipoSocio().name(),
                valorSocio,
                detalhados.size(),
                detalhados.stream().map(ReciboDependenteDTO::getNome).toList(),
                detalhados,
                valorDependentes,
                valorTotal,
                quantidadeParcelas,
                valorParcela,
                data,
                tipoPagamento,
                mes,
                mesesQuitados,
                previa
        );
    }

    public List<HistoricoPagamentoDTO> listarHistoricoPagamentos(Long id) {
        Pessoa pessoa = buscarPorId(id);
        Map<String, String> mesesPagos = new HashMap<>();
        if (pessoa.getPagamentos() != null) {
            for (PagamentoRegistro pagamento : pessoa.getPagamentos()) {
                for (String mesPago : obterMesesQuitados(pagamento)) {
                    mesesPagos.put(mesPago, pagamento.getPagamentoId());
                }
            }
        }
        Set<String> mesesRenegociados = renegociacaoRepository.findByPessoaIdOrderByDataCriacaoDesc(id).stream()
                .filter(item -> Boolean.TRUE.equals(item.getAtiva()))
                .flatMap(item -> item.getMesesRenegociados().stream())
                .collect(Collectors.toSet());
        List<HistoricoPagamentoDTO> historico = new ArrayList<>();
        YearMonth inicio = YearMonth.of(2026, 1);
        YearMonth fim = determinarMesFinalHistorico(mesesPagos.keySet(), mesesRenegociados);
        YearMonth cursor = inicio;
        while (!cursor.isAfter(fim)) {
            String referencia = cursor.format(MES_FMT);
            historico.add(new HistoricoPagamentoDTO(referencia, mesesPagos.containsKey(referencia), mesesRenegociados.contains(referencia), mesesPagos.get(referencia)));
            cursor = cursor.plusMonths(1);
        }
        return historico;
    }

    public List<PagamentoHistoricoDetalhadoDTO> listarTodosPagamentos() {
        List<PagamentoHistoricoDetalhadoDTO> itens = new ArrayList<>();
        for (Pessoa pessoa : pessoaRepository.findAll()) {
            if (pessoa.getPagamentos() == null) continue;
            for (PagamentoRegistro pagamento : pessoa.getPagamentos()) {
                itens.add(new PagamentoHistoricoDetalhadoDTO(
                        pessoa.getId(),
                        pessoa.getNome(),
                        pessoa.getMatricula(),
                        pessoa.getCpf(),
                        pagamento.getMesReferencia(),
                        pagamento.getDataHora(),
                        pagamento.getTipoPagamento(),
                        pagamento.getValorSocio(),
                        pagamento.getValorDependentes(),
                        pagamento.getValorTotal(),
                        pagamento.getQuantidadeParcelas(),
                        pagamento.getValorParcela(),
                        pagamento.getObservacao(),
                        desserializarDetalhesDependentes(pagamento.getDetalhesDependentes()),
                        obterMesesQuitados(pagamento)
                ));
            }
        }
        return itens.stream()
                .sorted(Comparator.comparing(PagamentoHistoricoDetalhadoDTO::getDataPagamento, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    public Pessoa atualizarStatus(Long id, StatusSocio status) {
        Pessoa pessoa = buscarPorId(id);
        pessoa.setStatus(status);
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public Renegociacao renegociar(Long id, RenegociacaoRequestDTO dto) {
        Pessoa pessoa = buscarPorId(id);
        List<String> mesesSelecionados = normalizarMesesReferencia(dto.getMesesReferencia());
        if (mesesSelecionados.isEmpty()) {
            throw new RegraNegocioException("Selecione ao menos um mês para renegociação.");
        }
        for (String mes : mesesSelecionados) {
            if (jaPagouNoMesInterno(pessoa, parseMesReferencia(mes))) {
                throw new RegraNegocioException("O mês " + mes + " já foi quitado e não pode ser renegociado.");
            }
        }

        BigDecimal valorMensalBase = calcularValorSocio(pessoa, false).add(calcularValorDependentes(pessoa));
        BigDecimal valorOriginal = valorMensalBase.multiply(BigDecimal.valueOf(mesesSelecionados.size())).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorParcela = dto.getValorRenegociado().divide(BigDecimal.valueOf(dto.getQuantidadeParcelas()), 2, RoundingMode.HALF_UP);

        Renegociacao renegociacao = new Renegociacao();
        renegociacao.setPessoa(pessoa);
        renegociacao.setValorOriginal(valorOriginal);
        renegociacao.setValorRenegociado(dto.getValorRenegociado());
        renegociacao.setQuantidadeParcelas(dto.getQuantidadeParcelas());
        renegociacao.setValorParcela(valorParcela);
        renegociacao.setMotivo(dto.getMotivo().trim());
        renegociacao.setObservacao(dto.getObservacao());
        renegociacao.setDataCriacao(LocalDateTime.now());
        renegociacao.setAtiva(true);
        renegociacao.setMesesRenegociados(mesesSelecionados);

        pessoa.setStatus(StatusSocio.INADIMPLENTE);
        pessoaRepository.save(pessoa);
        return renegociacaoRepository.save(renegociacao);
    }

    public List<Renegociacao> listarRenegociacoes(Long pessoaId) {
        buscarPorId(pessoaId);
        return renegociacaoRepository.findByPessoaIdOrderByDataCriacaoDesc(pessoaId);
    }

    public ReciboRenegociacaoDTO gerarReciboRenegociacao(Long pessoaId, Long renegociacaoId) {
        Pessoa pessoa = buscarPorId(pessoaId);
        Renegociacao renegociacao = renegociacaoRepository.findById(renegociacaoId)
                .filter(item -> item.getPessoa() != null && Objects.equals(item.getPessoa().getId(), pessoaId))
                .orElseThrow(() -> new RegraNegocioException("Renegociação não encontrada para este sócio."));

        return new ReciboRenegociacaoDTO(
                renegociacao.getId(),
                pessoa.getNome(),
                pessoa.getMatricula(),
                renegociacao.getValorOriginal(),
                renegociacao.getValorRenegociado(),
                renegociacao.getQuantidadeParcelas(),
                renegociacao.getValorParcela(),
                renegociacao.getMotivo(),
                renegociacao.getObservacao(),
                renegociacao.getDataCriacao(),
                renegociacao.getMesesRenegociados()
        );
    }


    @Transactional
    public void excluirPagamento(Long pessoaId, String pagamentoId) {
        Pessoa pessoa = buscarPorId(pessoaId);
        PagamentoRegistro pagamento = pessoa.getPagamentos().stream()
                .filter(item -> Objects.equals(item.getPagamentoId(), pagamentoId))
                .findFirst()
                .orElseThrow(() -> new RegraNegocioException("Pagamento não encontrado para este sócio."));
        pessoa.getPagamentos().remove(pagamento);
        recalcularStatusSocio(pessoa);
        pessoaRepository.save(pessoa);
    }

    public Pessoa recalcularStatus(Long id) {
        Pessoa pessoa = buscarPorId(id);
        recalcularStatusSocio(pessoa);
        return pessoaRepository.save(pessoa);
    }

    private void recalcularStatusSocio(Pessoa pessoa) {
        if (pessoa.getStatus() == StatusSocio.DEMITIDO) return;
        YearMonth atual = YearMonth.now();
        YearMonth cadastro = YearMonth.from(pessoa.getDataCadastro());
        if (cadastro.equals(atual)) {
            pessoa.setStatus(StatusSocio.ATIVO);
            return;
        }
        pessoa.setStatus(jaPagouNoMesInterno(pessoa, atual) ? StatusSocio.ATIVO : StatusSocio.INADIMPLENTE);
    }

    private String gerarMatriculaSocio(LocalDateTime dataCadastro) {
        String prefixo = dataCadastro.format(MATRICULA_FMT);
        LocalDateTime inicio = YearMonth.from(dataCadastro).atDay(1).atStartOfDay();
        LocalDateTime fim = YearMonth.from(dataCadastro).atEndOfMonth().atTime(23, 59, 59);
        long sequencia = pessoaRepository.countByDataCadastroBetween(inicio, fim) + 1;
        return prefixo + String.format("%04d", sequencia) + "100";
    }

    private String gerarMatriculaDependente(Pessoa pessoa) {
        if (pessoa.getMatricula() == null || pessoa.getMatricula().isBlank() || pessoa.getMatricula().length() < 13) {
            throw new RegraNegocioException("Matrícula do sócio inválida para geração da matrícula do dependente.");
        }

        long ordemNoSocio = dependenteRepository.countByPessoaId(pessoa.getId()) + 1;
        long sufixo = 200 + ordemNoSocio;

        String matriculaSocio = pessoa.getMatricula();
        String baseMatriculaSocio = matriculaSocio.substring(0, matriculaSocio.length() - 3);

        return baseMatriculaSocio + sufixo;
    }

    private void validarPessoa(Pessoa pessoa) {
        if (pessoa.getNome() == null || pessoa.getNome().isBlank()) throw new RegraNegocioException("Nome é obrigatório.");
        if (pessoa.getCpf() == null || !pessoa.getCpf().matches("\\d{11}")) throw new RegraNegocioException("CPF deve conter 11 números.");
        if (pessoa.getEndereco() == null || pessoa.getEndereco().isBlank()) throw new RegraNegocioException("Endereço é obrigatório.");
        if (pessoa.getTipoSocio() == null) throw new RegraNegocioException("Tipo do sócio é obrigatório.");
        if (pessoa.getValorMensalidadeCustom() != null && pessoa.getValorMensalidadeCustom().compareTo(ZERO) < 0) {
            throw new RegraNegocioException("Valor mensal do sócio não pode ser negativo.");
        }
    }

    private void validarDependente(Dependente dependente) {
        if (dependente.getNome() == null || dependente.getNome().isBlank()) throw new RegraNegocioException("Nome do dependente é obrigatório.");
        if (dependente.getCpf() != null && !dependente.getCpf().isBlank() && !dependente.getCpf().matches("\\d{11}")) {
            throw new RegraNegocioException("CPF do dependente deve conter 11 números quando informado.");
        }
        if (dependente.getEndereco() == null || dependente.getEndereco().isBlank()) throw new RegraNegocioException("Endereço do dependente é obrigatório.");
        if (dependente.getDataNascimento() == null) throw new RegraNegocioException("Data de nascimento do dependente é obrigatória.");
        if (dependente.getTipoParentesco() == null) throw new RegraNegocioException("Tipo de parentesco é obrigatório.");
        if (dependente.getValorMensalidadeCustom() != null && dependente.getValorMensalidadeCustom().compareTo(ZERO) < 0) {
            throw new RegraNegocioException("Valor mensal do dependente não pode ser negativo.");
        }
    }

    private void aplicarRelacionamentosERegras(Pessoa pessoa, Long socioTitularId) {
        pessoa.setIsento(Boolean.TRUE.equals(pessoa.getIsento()));
        pessoa.setSinal(Boolean.TRUE.equals(pessoa.getSinal()));
        pessoa.setJoiaCobrada(Boolean.TRUE.equals(pessoa.getJoiaCobrada()));
        pessoa.setSemMargem(Boolean.TRUE.equals(pessoa.getSemMargem()));
        pessoa.setValorMensalidadeCustom(normalizarValor(pessoa.getValorMensalidadeCustom()));

        if (pessoa.getTipoSocio() == TipoSocio.REMIDO) {
            pessoa.setIsento(false);
            pessoa.setSinal(false);
            pessoa.setJoiaCobrada(false);
            pessoa.setSemMargem(false);
            pessoa.setSocioTitular(null);
            pessoa.setValorMensalidadeCustom(null);
            return;
        }

        if (pessoa.getTipoSocio() == TipoSocio.RECREATIVO) {
            if (!pessoa.getIsento() && !pessoa.getSinal()) {
                throw new RegraNegocioException("Sócio recreativo deve ser isento ou ter joia.");
            }
            if (pessoa.getIsento() && pessoa.getSinal()) {
                throw new RegraNegocioException("Sócio recreativo não pode ser isento e joia ao mesmo tempo.");
            }
            if (pessoa.getIsento()) {
                pessoa.setJoiaCobrada(false);
                pessoa.setSinal(false);
            } else if (!Boolean.TRUE.equals(pessoa.getJoiaCobrada())) {
                pessoa.setJoiaCobrada(false);
            }
            pessoa.setSemMargem(false);
            pessoa.setSocioTitular(null);
            return;
        }

        if (pessoa.getTipoSocio() == TipoSocio.CONTRIBUINTE) {
            pessoa.setIsento(false);
            pessoa.setSinal(false);
            pessoa.setJoiaCobrada(false);
            pessoa.setSocioTitular(null);
            return;
        }

        pessoa.setIsento(false);
        pessoa.setSinal(false);
        pessoa.setJoiaCobrada(false);
        pessoa.setSemMargem(false);
        if (socioTitularId == null) throw new RegraNegocioException("Sócio recreativo vinculado deve possuir um sócio titular.");
        Pessoa titular = buscarPorId(socioTitularId);
        if (titular.getTipoSocio() != TipoSocio.RECREATIVO) throw new RegraNegocioException("O titular do sócio recreativo vinculado deve ser recreativo.");
        pessoa.setSocioTitular(titular);
    }

    private List<ReciboDependenteDTO> montarDetalhesDependentes(Pessoa pessoa) {
        return dependenteRepository.findByPessoaId(pessoa.getId()).stream()
                .sorted(Comparator.comparing(Dependente::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(dep -> new ReciboDependenteDTO(dep.getNome(), dep.getMatricula(), calcularValorIndividualDependente(pessoa, dep)))
                .toList();
    }

    private String serializarDetalhesDependentes(List<ReciboDependenteDTO> itens) {
        return itens.stream()
                .map(item -> sanitizar(item.getMatricula()) + "|" + sanitizar(item.getNome()) + "|" + item.getValor().setScale(2, RoundingMode.HALF_UP))
                .collect(Collectors.joining(";"));
    }

    private List<ReciboDependenteDTO> desserializarDetalhesDependentes(String valor) {
        if (valor == null || valor.isBlank()) {
            return new ArrayList<>();
        }
        List<ReciboDependenteDTO> itens = new ArrayList<>();
        for (String parte : valor.split(";")) {
            if (parte.isBlank()) continue;
            String[] colunas = parte.split("\\|", -1);
            if (colunas.length < 3) continue;
            try {
                itens.add(new ReciboDependenteDTO(colunas[1], colunas[0], new BigDecimal(colunas[2]).setScale(2, RoundingMode.HALF_UP)));
            } catch (NumberFormatException ignored) {
            }
        }
        return itens;
    }

    private String sanitizar(String texto) {
        return texto == null ? "" : texto.replace("|", " ").replace(";", " ");
    }

    private YearMonth determinarMesFinalHistorico(Set<String> mesesPagos, Set<String> mesesRenegociados) {
        YearMonth fim = YearMonth.now().plusMonths(12);
        for (String referencia : mesesPagos) {
            YearMonth mes = parseMesReferencia(referencia);
            if (mes.isAfter(fim)) fim = mes;
        }
        for (String referencia : mesesRenegociados) {
            YearMonth mes = parseMesReferencia(referencia);
            if (mes.isAfter(fim)) fim = mes;
        }
        return fim;
    }

    private BigDecimal normalizarValor(BigDecimal valor) {
        return valor == null ? null : valor.setScale(2, RoundingMode.HALF_UP);
    }

    private String normalizarCpfOpcionalDependente(String cpf) {
        if (cpf == null) return null;
        String valor = cpf.trim();
        return valor.isBlank() ? null : valor;
    }

    private List<String> obterMesesQuitados(PagamentoRegistro pagamento) {
        if (pagamento.getMesesQuitados() == null || pagamento.getMesesQuitados().isBlank()) {
            return pagamento.getMesReferencia() == null ? new ArrayList<>() : List.of(pagamento.getMesReferencia());
        }
        return Arrays.stream(pagamento.getMesesQuitados().split(","))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .toList();
    }

    private List<String> normalizarMesesPagamento(List<String> meses) {
        List<String> normalizados = normalizarMesesReferencia(meses);
        return normalizados.isEmpty() ? List.of(YearMonth.now().format(MES_FMT)) : normalizados;
    }

    private List<String> normalizarMesesReferencia(List<String> meses) {
        if (meses == null) return new ArrayList<>();
        return meses.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .map(item -> parseMesReferencia(item).format(MES_FMT))
                .distinct()
                .sorted(Comparator.comparing(this::parseMesReferencia))
                .toList();
    }

    private YearMonth parseMesReferencia(String valor) {
        try {
            return YearMonth.parse(valor, MES_FMT);
        } catch (DateTimeParseException ex) {
            throw new RegraNegocioException("Mês de referência inválido: " + valor + ". Use o formato MM/aaaa.");
        }
    }
}