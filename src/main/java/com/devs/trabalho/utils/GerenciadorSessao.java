package com.devs.trabalho.utils;

import com.devs.trabalho.exceptions.usuario.UsuarioNotLoggedException;
import com.devs.trabalho.model.usuario.Usuario;

/**
 * Classe utilitária para representar a sessão do usuário que está logado no sistema
 * */
public class GerenciadorSessao {

    private static Usuario usuarioLogado;

    /**
     * Método para pegar o usuário logado
     * (se der null é porque não tem ninguém logado
     * */
    public static Usuario getUsuarioLogado() throws UsuarioNotLoggedException {
        if(usuarioLogado == null) {
            throw new UsuarioNotLoggedException("Usuário não está logado");
        }

        return usuarioLogado;
    }

    /**
     * Método que seta o usuário logado no sistema
     * */
    public static void setUsuarioLogado(Usuario usuarioLogado) {
        GerenciadorSessao.usuarioLogado = usuarioLogado;
    }
}
