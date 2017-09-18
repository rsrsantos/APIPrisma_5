package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.Model.Departamento;
import br.com.Model.Empresa;
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
public class SecretariaBean {

    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    Secretaria secretaria = new Secretaria();

    public List<Secretaria> getSecretarias() {
        return new GenericDAO<>(Secretaria.class).listaTodos();

    }

    public List<Empresa> getEmpresas() {
        return new GenericDAO<>(Empresa.class).listaTodos();

    }

    public String gravarSecretaria() {
        try {
            if (secretaria.getId() == 0) {
                new GenericDAO<>(Secretaria.class).adiciona(this.secretaria);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/secretaria/lista-secretaria/index?faces-redirect=true";

            } else {
                new GenericDAO<>(Secretaria.class).atualiza(this.secretaria);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/secretaria/lista-secretaria/index.xhtml?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String novaSecretaria() {
//        if (usuarioLogado.getAdministrador().equals("true")) {
        return "/View/secretaria/cadastrar-secretaria/index?faces-redirect=true";
//        } else {
//        }
//        Msg.addMsgFatal("Somente Administrador");
//        return null;

    }

    public String excluir(Secretaria secretaria) {

        if (usuarioLogado.getAdministrador().equals("true")) {
            new GenericDAO<>(Secretaria.class
            ).remove(secretaria);
            return "/View/secretaria/lista-secretaria/index";
        } else {
            Msg.addMsgFatal("Não Autorizado");
        }
        return null;

    }

    public String editar(Secretaria secretaria) {

//        if (usuarioLogado.getAdministrador().equals("true")) {
        this.secretaria = secretaria;
        return "/View/secretaria/cadastrar-secretaria/index";
//        } else {
////            Msg.addMsgFatal("Não Autorizado");
//        }
//        return null;

    }

    public Secretaria getSecretaria() {
        return secretaria;
    }

    public void setSecretaria(Secretaria secretaria) {
        this.secretaria = secretaria;
    }

}
