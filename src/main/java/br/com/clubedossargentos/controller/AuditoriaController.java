package br.com.clubedossargentos.controller;

import br.com.clubedossargentos.dto.AuditoriaAcaoResponseDTO;
import br.com.clubedossargentos.dto.UsuarioResponseDTO;
import br.com.clubedossargentos.service.AuditoriaService;
import br.com.clubedossargentos.service.AutenticacaoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/auditoria")
public class AuditoriaController {
    private final AuditoriaService auditoriaService;
    private final AutenticacaoService autenticacaoService;

    public AuditoriaController(AuditoriaService auditoriaService, AutenticacaoService autenticacaoService) {
        this.auditoriaService = auditoriaService;
        this.autenticacaoService = autenticacaoService;
    }

    @GetMapping
    public List<AuditoriaAcaoResponseDTO> listar(@RequestParam(required = false) Long usuarioId,
                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return auditoriaService.listar(usuarioId, data);
    }

    @GetMapping("/usuarios")
    public List<UsuarioResponseDTO> listarUsuarios() {
        return autenticacaoService.listarUsuarios();
    }
}
