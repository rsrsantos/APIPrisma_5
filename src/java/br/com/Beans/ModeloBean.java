package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.Model.Funcionario;
import br.com.Model.Modelo;
import br.com.Model.Usuario;
import br.com.utils.Msg;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class ModeloBean {

    Modelo modelo = new Modelo();

    public List<Modelo> getModelos() {
        return new GenericDAO<>(Modelo.class).listaTodos();

    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String gravarModelo() {
        try {
            if (modelo.getId() == 0) {
                new GenericDAO<>(Modelo.class).adiciona(this.modelo);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            } else {
                new GenericDAO<>(Modelo.class).atualiza(this.modelo);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String excluir(Modelo modelo) {
        new GenericDAO<>(Modelo.class
        ).remove(modelo);
        return "/View/equipamento/lista-modelo/index";
    }

    public String editar(Modelo modelo) {
        this.modelo = modelo;

        return "/View/equipamento/cadastrar-modelo/index";

    }

}
