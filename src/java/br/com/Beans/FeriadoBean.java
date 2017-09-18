package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.Model.Departamento;
import br.com.Model.Feriado;
import br.com.Model.Usuario;
import br.com.utils.Msg;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class FeriadoBean {

    Feriado feriado = new Feriado();
    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");

    public List<Feriado> getFeriados() {
        return new GenericDAO<>(Feriado.class).listaTodos();
    }

    public List<Departamento> getDepartamentos() {
        return new GenericDAO<>(Departamento.class).listaTodos();
    }

    public String gravarFeriado() {
        try {
            if (feriado.getId() == 0) {
                new GenericDAO<>(Feriado.class).adiciona(this.feriado);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/feriado/lista-feriado/index?faces-redirect=true";

            } else {
                new GenericDAO<>(Feriado.class).atualiza(this.feriado);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/feriado/lista-feriado/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String excluir(Feriado feriado) {
        try {
            if (usuarioLogado.getAdministrador().equals("true")) {
                new GenericDAO<>(Feriado.class).remove(feriado);
                Msg.addMsgFatal("Excluido com Sucesso: " + feriado.getNome());
                return "/View/feriado/lista-feriado/index?faces-redirect=true";
            } else {
                Msg.addMsgFatal("Não Autorizado!");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String editar(Feriado feriado) {
//        if (usuario.getNome().equals(usuarioLogado.getNome())) {
        this.feriado = feriado;
        return "/View/feriado/cadastrar-feriado/index";
//        } else if (usuarioLogado.getAdministrador().equals("true")) {
//            this.usuario = usuario;
//            return "/View/usuario/cadastrarUsuario";
//        } else {
//            Msg.addMsgFatal("Não Autorizado");
//        }
//        return null;

    }

    public String novoFeriado(Feriado feriado) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            return "/View/feriado/cadastrar-feriado/index?faces-redirect=true";
        } else {
            Msg.addMsgFatal("Somente Administrador");
        }

        return null;
    }

    public Feriado getFeriado() {
        return feriado;
    }

    public void setFeriado(Feriado feriado) {
        this.feriado = feriado;
    }

}
