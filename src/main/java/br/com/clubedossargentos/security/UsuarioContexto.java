package br.com.clubedossargentos.security;

public final class UsuarioContexto {
    private static final ThreadLocal<UsuarioLogado> ATUAL = new ThreadLocal<>();

    private UsuarioContexto() {}

    public static void definir(UsuarioLogado usuario) { ATUAL.set(usuario); }
    public static UsuarioLogado obter() { return ATUAL.get(); }
    public static void limpar() { ATUAL.remove(); }
}
