package br.com.clubedossargentos.service;

import br.com.clubedossargentos.dto.*;
import br.com.clubedossargentos.entity.UsuarioSistema;
import br.com.clubedossargentos.enums.PerfilUsuario;
import br.com.clubedossargentos.exception.AcessoNegadoException;
import br.com.clubedossargentos.exception.NaoAutenticadoException;
import br.com.clubedossargentos.exception.RegraNegocioException;
import br.com.clubedossargentos.repository.UsuarioSistemaRepository;
import br.com.clubedossargentos.security.UsuarioContexto;
import br.com.clubedossargentos.security.UsuarioLogado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AutenticacaoService {
    private static final String ADMIN_FIXO_NOME = "admin";

    @Value("${admin.password:admin123}")
    private String adminFixoSenha;

    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public AutenticacaoService(UsuarioSistemaRepository usuarioSistemaRepository, PasswordEncoder passwordEncoder, AuditoriaService auditoriaService) {
        this.usuarioSistemaRepository = usuarioSistemaRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }



    @PostConstruct
    public void garantirAdministradorPadrao() {
        UsuarioSistema admin = usuarioSistemaRepository.findByNomeIgnoreCase(ADMIN_FIXO_NOME).orElseGet(UsuarioSistema::new);
        admin.setNome(ADMIN_FIXO_NOME);
        admin.setSenhaHash(passwordEncoder.encode(adminFixoSenha));
        admin.setPerfil(PerfilUsuario.ADMIN);
        admin.setAtivo(true);
        if (admin.getDataCriacao() == null) {
            admin.setDataCriacao(LocalDateTime.now());
        }
        usuarioSistemaRepository.save(admin);
    }

    public StatusPublicoDTO statusPublico() {
        return new StatusPublicoDTO(usuarioSistemaRepository.count() > 0);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        UsuarioSistema usuario = usuarioSistemaRepository.findByNomeIgnoreCase(normalizarNome(dto.getNome()))
                .filter(item -> Boolean.TRUE.equals(item.getAtivo()))
                .orElseThrow(() -> new NaoAutenticadoException("Usuário ou senha inválidos."));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash())) {
            throw new NaoAutenticadoException("Usuário ou senha inválidos.");
        }

        usuario.setTokenAcesso(UUID.randomUUID().toString());
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioSistemaRepository.save(usuario);
        return new LoginResponseDTO(usuario.getId(), usuario.getNome(), usuario.getPerfil(), usuario.getTokenAcesso());
    }

    public void logout() {
        UsuarioLogado atual = auditoriaService.exigirUsuarioLogado();
        UsuarioSistema usuario = usuarioSistemaRepository.findById(atual.getId())
                .orElseThrow(() -> new NaoAutenticadoException("Usuário não encontrado."));
        usuario.setTokenAcesso(null);
        usuarioSistemaRepository.save(usuario);
    }

    public LoginResponseDTO me() {
        UsuarioLogado atual = auditoriaService.exigirUsuarioLogado();
        return new LoginResponseDTO(atual.getId(), atual.getNome(), atual.getPerfil(), atual.getToken());
    }

    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO dto) {
        UsuarioLogado atual = auditoriaService.exigirAdmin();
        String nome = normalizarNome(dto.getNome());
        if (usuarioSistemaRepository.existsByNomeIgnoreCase(nome)) throw new RegraNegocioException("Já existe usuário com esse nome.");
        if (dto.getPerfil() == PerfilUsuario.ADMIN) throw new RegraNegocioException("Não é permitido cadastrar outro administrador. O administrador do sistema é fixo.");

        UsuarioSistema usuario = new UsuarioSistema();
        usuario.setNome(nome);
        usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        usuario.setPerfil(PerfilUsuario.USUARIO);
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());
        usuarioSistemaRepository.save(usuario);

        auditoriaService.registrar("CRIAR_USUARIO", "USUARIO_SISTEMA", usuario.getId(), "Usuário cadastrado: " + usuario.getNome() + " por " + atual.getNome() + ".");
        return toResponse(usuario);
    }

    public UsuarioResponseDTO cadastrarUsuarioPublico(UsuarioRequestDTO dto) {
        String nome = normalizarNome(dto.getNome());
        if (usuarioSistemaRepository.existsByNomeIgnoreCase(nome)) throw new RegraNegocioException("Já existe usuário com esse nome.");
        if (ADMIN_FIXO_NOME.equalsIgnoreCase(nome)) throw new RegraNegocioException("Esse nome é reservado para o administrador fixo do sistema.");

        UsuarioSistema usuario = new UsuarioSistema();
        usuario.setNome(nome);
        usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        usuario.setPerfil(PerfilUsuario.USUARIO);
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());
        usuarioSistemaRepository.save(usuario);

        return toResponse(usuario);
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        auditoriaService.exigirAdmin();
        return usuarioSistemaRepository.findAll().stream()
                .sorted((a,b) -> a.getNome().compareToIgnoreCase(b.getNome()))
                .map(this::toResponse)
                .toList();
    }

    private UsuarioResponseDTO toResponse(UsuarioSistema usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getPerfil(), usuario.getAtivo(), usuario.getDataCriacao(), usuario.getUltimoLogin());
    }

    private String normalizarNome(String nome) {
        if (nome == null || nome.isBlank()) throw new RegraNegocioException("Nome do usuário é obrigatório.");
        return nome.trim();
    }
}
