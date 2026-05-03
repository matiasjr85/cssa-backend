package br.com.clubedossargentos.controller;

import br.com.clubedossargentos.dto.*;
import br.com.clubedossargentos.entity.Dependente;
import br.com.clubedossargentos.entity.Pessoa;
import br.com.clubedossargentos.entity.Renegociacao;
import br.com.clubedossargentos.service.AuditoriaService;
import br.com.clubedossargentos.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {
    private final PessoaService service;
    private final AuditoriaService auditoriaService;

    public PessoaController(PessoaService service, AuditoriaService auditoriaService) {
        this.service = service;
        this.auditoriaService = auditoriaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa criar(@Valid @RequestBody PessoaRequestDTO dto) {
        Pessoa criada = service.criar(mapearPessoa(dto), dto.getSocioTitularId());
        auditoriaService.registrar("CRIAR_SOCIO", "SOCIO", criada.getId(), "Sócio cadastrado: " + criada.getNome() + " (" + criada.getMatricula() + ").");
        return criada;
    }

    @GetMapping
    public List<Pessoa> listar() { return service.listar(); }

    @GetMapping("/pagamentos")
    public List<PagamentoHistoricoDetalhadoDTO> listarTodosPagamentos() { return service.listarTodosPagamentos(); }

    @GetMapping("/{id}")
    public Pessoa buscarPorId(@PathVariable Long id) { return service.buscarPorId(id); }

    @PutMapping("/{id}")
    public Pessoa atualizar(@PathVariable Long id, @Valid @RequestBody PessoaRequestDTO dto) {
        Pessoa atualizada = service.atualizar(id, mapearPessoa(dto), dto.getSocioTitularId());
        auditoriaService.registrar("ATUALIZAR_SOCIO", "SOCIO", atualizada.getId(), "Sócio atualizado: " + atualizada.getNome() + " (" + atualizada.getMatricula() + ").");
        return atualizada;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        Pessoa pessoa = service.buscarPorId(id);
        service.excluir(id);
        auditoriaService.registrar("EXCLUIR_SOCIO", "SOCIO", id, "Sócio excluído: " + pessoa.getNome() + " (" + pessoa.getMatricula() + ").");
    }

    @GetMapping("/{id}/status-pagamento")
    public boolean statusPagamento(@PathVariable Long id) { return service.jaPagouNoMes(id); }

    @GetMapping("/{id}/recibo-previa")
    public ReciboDTO gerarReciboPrevia(@PathVariable Long id,
                                       @RequestParam(defaultValue = "1") Integer parcelas,
                                       @RequestParam(required = false) BigDecimal valorSocioPago,
                                       @RequestParam(required = false) BigDecimal valorDependentesPago,
                                       @RequestParam(required = false) String tipoPagamento,
                                       @RequestParam(required = false) List<String> mesesReferencia) {
        return service.gerarReciboPrevia(id, parcelas, valorSocioPago, valorDependentesPago, tipoPagamento, mesesReferencia);
    }

    @PatchMapping("/{id}/status")
    public Pessoa atualizarStatus(@PathVariable Long id, @Valid @RequestBody StatusSocioRequestDTO dto) {
        Pessoa pessoa = service.atualizarStatus(id, dto.getStatus());
        auditoriaService.registrar("ATUALIZAR_STATUS_SOCIO", "SOCIO", pessoa.getId(), "Status do sócio " + pessoa.getNome() + " alterado para " + pessoa.getStatus() + ".");
        return pessoa;
    }

    @PostMapping("/{id}/recalcular-status")
    public Pessoa recalcularStatus(@PathVariable Long id) {
        Pessoa pessoa = service.recalcularStatus(id);
        auditoriaService.registrar("RECALCULAR_STATUS_SOCIO", "SOCIO", pessoa.getId(), "Status recalculado para o sócio " + pessoa.getNome() + ": " + pessoa.getStatus() + ".");
        return pessoa;
    }

    @GetMapping("/{id}/historico-pagamentos")
    public List<HistoricoPagamentoDTO> listarHistoricoPagamentos(@PathVariable Long id) { return service.listarHistoricoPagamentos(id); }

    @DeleteMapping("/{id}/pagamentos/{pagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirPagamento(@PathVariable Long id, @PathVariable String pagamentoId) {
        auditoriaService.exigirAdmin();
        service.excluirPagamento(id, pagamentoId);
        auditoriaService.registrar("EXCLUIR_PAGAMENTO", "PAGAMENTO", id, "Pagamento " + pagamentoId + " excluído para o sócio " + id + ".");
    }

    @GetMapping("/{id}/dependentes")
    public List<Dependente> listarDependentes(@PathVariable Long id) { return service.listarDependentes(id); }

    @PostMapping("/{id}/dependentes")
    public Pessoa adicionarDependente(@PathVariable Long id, @Valid @RequestBody DependenteRequestDTO dto) {
        Dependente dependente = criarDependenteDeDTO(dto);
        Pessoa pessoa = service.adicionarDependente(id, dependente);
        Dependente criado = service.listarDependentes(id).stream().max((a, b) -> Long.compare(a.getId(), b.getId())).orElse(null);
        auditoriaService.registrar("CRIAR_DEPENDENTE", "DEPENDENTE", criado != null ? criado.getId() : null, "Dependente cadastrado para o sócio " + pessoa.getNome() + ": " + dependente.getNome() + ".");
        return pessoa;
    }

    @PutMapping("/{pessoaId}/dependentes/{dependenteId}")
    public Dependente atualizarDependente(@PathVariable Long pessoaId, @PathVariable Long dependenteId, @Valid @RequestBody DependenteRequestDTO dto) {
        Dependente atualizado = service.atualizarDependente(pessoaId, dependenteId, criarDependenteDeDTO(dto));
        auditoriaService.registrar("ATUALIZAR_DEPENDENTE", "DEPENDENTE", atualizado.getId(), "Dependente atualizado: " + atualizado.getNome() + ".");
        return atualizado;
    }

    @DeleteMapping("/{pessoaId}/dependentes/{dependenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirDependente(@PathVariable Long pessoaId, @PathVariable Long dependenteId) {
        String nomeDependente = service.listarDependentes(pessoaId).stream()
                .filter(item -> item.getId().equals(dependenteId))
                .findFirst()
                .map(Dependente::getNome)
                .orElse(String.valueOf(dependenteId));
        service.excluirDependente(pessoaId, dependenteId);
        auditoriaService.registrar("EXCLUIR_DEPENDENTE", "DEPENDENTE", dependenteId, "Dependente excluído: " + nomeDependente + ".");
    }

    @PostMapping("/{id}/pagar")
    public Pessoa pagar(@PathVariable Long id, @Valid @RequestBody PagamentoRequestDTO dto) {
        Pessoa pessoa = service.pagarMensalidade(id, dto.getQuantidadeParcelas(), dto.getObservacao(), dto.getValorSocioPago(), dto.getValorDependentesPago(), dto.getTipoPagamento(), dto.getMesesReferencia());
        auditoriaService.registrar("PAGAR_MENSALIDADE", "SOCIO", pessoa.getId(), "Pagamento registrado para o sócio " + pessoa.getNome() + ".");
        return pessoa;
    }

    @GetMapping("/{id}/recibo")
    public ReciboDTO gerarRecibo(@PathVariable Long id) { return service.gerarRecibo(id); }

    @PostMapping("/{id}/renegociacoes")
    public Renegociacao renegociar(@PathVariable Long id, @Valid @RequestBody RenegociacaoRequestDTO dto) {
        Renegociacao renegociacao = service.renegociar(id, dto);
        Pessoa pessoa = service.buscarPorId(id);
        auditoriaService.registrar("RENEGOCIAR", "RENEGOCIACAO", renegociacao.getId(), "Renegociação criada para o sócio " + pessoa.getNome() + ".");
        return renegociacao;
    }

    @GetMapping("/{id}/renegociacoes")
    public List<Renegociacao> listarRenegociacoes(@PathVariable Long id) { return service.listarRenegociacoes(id); }

    @GetMapping("/{id}/renegociacoes/{renegociacaoId}/recibo")
    public ReciboRenegociacaoDTO gerarReciboRenegociacao(@PathVariable Long id, @PathVariable Long renegociacaoId) {
        return service.gerarReciboRenegociacao(id, renegociacaoId);
    }

    private Pessoa mapearPessoa(PessoaRequestDTO dto) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(dto.getNome().trim());
        pessoa.setCpf(dto.getCpf());
        pessoa.setEndereco(dto.getEndereco().trim());
        pessoa.setTipoSocio(dto.getTipoSocio());
        pessoa.setIsento(dto.getIsento());
        pessoa.setSinal(dto.getSinal());
        pessoa.setSemMargem(dto.getSemMargem());
        pessoa.setValorMensalidadeCustom(dto.getValorMensalidadeCustom());
        return pessoa;
    }

    private Dependente criarDependenteDeDTO(DependenteRequestDTO dto) {
        Dependente dependente = new Dependente(dto.getNome().trim(), dto.getCpf(), dto.getEndereco().trim(), dto.getDataNascimento(), dto.getTipoParentesco());
        dependente.setUniversitario(dto.getUniversitario());
        dependente.setValorMensalidadeCustom(dto.getValorMensalidadeCustom());
        return dependente;
    }
}
