/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.Model.Modelo;
import br.com.Model.TipoCorte;
import br.com.utils.Msg;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author postgres
 */
@ManagedBean
public class TipoCorteBean {

    public List<TipoCorte> getTipoCortes() {
        return new GenericDAO<>(TipoCorte.class).listaTodos();

    }

    TipoCorte corte = new TipoCorte();

    public TipoCorte getCorte() {
        return corte;
    }

    public void setCorte(TipoCorte corte) {
        this.corte = corte;
    }

    public String tipocorte() {
        try {
            if (corte.getId() == 0) {
                new GenericDAO<>(TipoCorte.class).adiciona(this.corte);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");
                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            } else {
                new GenericDAO<>(TipoCorte.class).atualiza(this.corte);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");
                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String excluir(TipoCorte corte) {
        new GenericDAO<>(TipoCorte.class
        ).remove(corte);
        return "/View/equipamento/ModeloEquipamento";
    }

    public String editar(TipoCorte corte) {
        this.corte = corte;

        return "/View/equipamento/cadastrar-tipoCorte/index";

    }

}
