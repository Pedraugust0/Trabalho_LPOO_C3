package com.devs.trabalho.dao.contato;

import com.devs.trabalho.dao.BaseDAO;
import com.devs.trabalho.model.pessoa.Contato;

public class ContatoDAO extends BaseDAO<Contato> {
    public ContatoDAO() {
        super(Contato.class);
    }
}