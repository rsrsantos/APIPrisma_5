package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.Model.Departamento;
import br.com.Model.Secretaria;
import br.com.Model.Usuario;
import br.com.utils.Msg;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class DepartamentoBean {

    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    Departamento departamento = new Departamento();

    public List<Departamento> getDepartamentos() {
        return new GenericDAO<>(Departamento.class).listaTodos();

    }

    public List<Secretaria> getSecretarias() {
        return new GenericDAO<>(Secretaria.class).listaTodos();

    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String gravarDepartamento() {
        try {
            if (departamento.getId() == 0) {
                new GenericDAO<>(Departamento.class).adiciona(this.departamento);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/departamento/lista-departamento/index?faces-redirect=true";

            } else {
                new GenericDAO<>(Departamento.class).atualiza(this.departamento);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/departamento/lista-departamento/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String novoDepartamento() {
//        if (usuarioLogado.getAdministrador().equals("true")) {
        return "/View/departamento/cadastrar-departamento/index?faces-redirect=true";
//        } else {
//        }
//        Msg.addMsgFatal("Somente Administrador");
//        return null;

    }

    public String excluir(Departamento departamento) {

        if (usuarioLogado.getAdministrador().equals("true")) {
            new GenericDAO<>(Departamento.class
            ).remove(departamento);
            return "/View/departamento/lista-departamento/index";
        } else {
            Msg.addMsgFatal("Não Autorizado");
        }
        return null;

    }

    public String editar(Departamento departamento) {

//        if (usuarioLogado.getAdministrador().equals("true")) {
        this.departamento = departamento;
        return "/View/departamento/cadastrar-departamento/index";
//        } else {
////            Msg.addMsgFatal("Não Autorizado");
//        }
//        return null;

    }
}
