package util;

import model.Usuario;

public class SessaoUsuario {
    private static Usuario usuarioLogado;

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isAdmin() {
        return usuarioLogado != null && "ADMIN".equals(usuarioLogado.getPerfil());
    }

    public static boolean isFuncionario() {
        return usuarioLogado != null && "FUNCIONARIO".equals(usuarioLogado.getPerfil());
    }

    public static String getNomeUsuario() {
        return usuarioLogado != null ? usuarioLogado.getNome() : "Usuário";
    }

    public static String getEmailUsuario() {
        return usuarioLogado != null ? usuarioLogado.getEmail() : "";
    }
}