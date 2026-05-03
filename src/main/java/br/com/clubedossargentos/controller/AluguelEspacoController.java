package br.com.clubedossargentos.controller;

import br.com.clubedossargentos.dto.AluguelEspacoRequestDTO;
import br.com.clubedossargentos.dto.AtualizarStatusAluguelRequestDTO;
import br.com.clubedossargentos.entity.AluguelEspaco;
import br.com.clubedossargentos.service.AluguelEspacoService;
import br.com.clubedossargentos.service.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alugueis")
public class AluguelEspacoController {
    private final AluguelEspacoService aluguelEspacoService;
    private final AuditoriaService auditoriaService;

    public AluguelEspacoController(AluguelEspacoService aluguelEspacoService, AuditoriaService auditoriaService) {
        this.aluguelEspacoService = aluguelEspacoService;
        this.auditoriaService = auditoriaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AluguelEspaco criar(@Valid @RequestBody AluguelEspacoRequestDTO dto) {
        AluguelEspaco aluguel = aluguelEspacoService.criar(dto);
        auditoriaService.registrar("CRIAR_ALUGUEL", "ALUGUEL", aluguel.getId(), "Aluguel cadastrado para " + aluguel.getNomeResponsavel() + " no valor de R$ " + aluguel.getValor() + " com pagamento " + aluguel.getTipoPagamento() + ".");
        return aluguel;
    }

    @GetMapping
    public List<AluguelEspaco> listar() {
        return aluguelEspacoService.listar();
    }

    @PutMapping("/{id}")
    public AluguelEspaco atualizar(@PathVariable Long id, @Valid @RequestBody AluguelEspacoRequestDTO dto) {
        AluguelEspaco aluguel = aluguelEspacoService.atualizar(id, dto);
        auditoriaService.registrar("ATUALIZAR_ALUGUEL", "ALUGUEL", aluguel.getId(), "Aluguel de " + aluguel.getNomeResponsavel() + " atualizado.");
        return aluguel;
    }

    @PatchMapping("/{id}/status")
    public AluguelEspaco atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusAluguelRequestDTO dto) {
        AluguelEspaco aluguel = aluguelEspacoService.atualizarStatus(id, dto.getStatus(), dto.getTipoPagamento());
        auditoriaService.registrar("ATUALIZAR_STATUS_ALUGUEL", "ALUGUEL", aluguel.getId(), "Status do aluguel de " + aluguel.getNomeResponsavel() + " alterado para " + aluguel.getStatus() + ".");
        return aluguel;
    }
}
