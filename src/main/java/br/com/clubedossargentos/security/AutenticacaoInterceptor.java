package br.com.clubedossargentos.security;

import br.com.clubedossargentos.entity.UsuarioSistema;
import br.com.clubedossargentos.exception.NaoAutenticadoException;
import br.com.clubedossargentos.repository.UsuarioSistemaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AutenticacaoInterceptor implements HandlerInterceptor {

    private final UsuarioSistemaRepository usuarioSistemaRepository;

    public AutenticacaoInterceptor(UsuarioSistemaRepository usuarioSistemaRepository) {
        this.usuarioSistemaRepository = usuarioSistemaRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        if (path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/status-publico")
                || path.startsWith("/api/auth/cadastrar-usuario")
                || path.startsWith("/api/saude")
                || path.startsWith("/error")) {
            return true;
        }

        String token = request.getHeader("X-Auth-Token");
        if (token == null || token.isBlank()) {
            throw new NaoAutenticadoException("Faça login para acessar o sistema.");
        }

        UsuarioSistema usuario = usuarioSistemaRepository.findByTokenAcesso(token)
                .filter(item -> Boolean.TRUE.equals(item.getAtivo()))
                .orElseThrow(() -> new NaoAutenticadoException("Sessão inválida ou expirada. Faça login novamente."));

        UsuarioContexto.definir(new UsuarioLogado(
                usuario.getId(),
                usuario.getNome(),
                usuario.getPerfil(),
                usuario.getTokenAcesso()
        ));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UsuarioContexto.limpar();
    }
}
