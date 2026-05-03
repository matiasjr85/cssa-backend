package br.com.clubedossargentos.controller;

import br.com.clubedossargentos.dto.*;
import br.com.clubedossargentos.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AutenticacaoService autenticacaoService;

    public AuthController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @GetMapping("/status-publico")
    public StatusPublicoDTO statusPublico() { return autenticacaoService.statusPublico(); }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) { return autenticacaoService.login(dto); }

    @PostMapping("/cadastrar-usuario")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO cadastrarUsuarioPublico(@Valid @RequestBody UsuarioRequestDTO dto) { return autenticacaoService.cadastrarUsuarioPublico(dto); }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() { autenticacaoService.logout(); }

    @GetMapping("/me")
    public LoginResponseDTO me() { return autenticacaoService.me(); }

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO criarUsuario(@Valid @RequestBody UsuarioRequestDTO dto) { return autenticacaoService.criarUsuario(dto); }

    @GetMapping("/usuarios")
    public List<UsuarioResponseDTO> listarUsuarios() { return autenticacaoService.listarUsuarios(); }
}
