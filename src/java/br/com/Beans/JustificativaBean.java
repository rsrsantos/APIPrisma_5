package br.com.Beans;

import br.com.Core.CoreUtils;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Funcionario;
import br.com.Model.Justificativa;
import br.com.Model.Usuario;
import br.com.utils.Msg;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

@ManagedBean
public class JustificativaBean {

    Justificativa justificativa = new Justificativa();
    FacesContext context = FacesContext.getCurrentInstance();
    EntityManager manager = new JPAConect().getEntityManager();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");

    public List<Justificativa> getJustificativas() {
        return new GenericDAO<>(Justificativa.class).listaTodos();
    }

    public List<Funcionario> getFuncionarios() {
        return new GenericDAO<>(Funcionario.class).listaTodos();
    }

    public String gravarJustificativa() {

        manager.getTransaction().begin();
        try {
            if (justificativa.getId() == 0) {
//                new GenericDAO<>(Justificativa.class).adiciona(this.justificativa);
                justificativa.setUsuario(usuarioLogado);
                justificativa.setUltima_alteracao(datahora);
                manager.persist(justificativa);
                manager.getTransaction().commit();
                manager.close();
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/justificativa/lista-justificativa/index?faces-redirect=true";

            } else {
//                new GenericDAO<>(Justificativa.class).atualiza(this.justificativa);
                justificativa.setUsuario(usuarioLogado);
                justificativa.setUltima_alteracao(datahora);
                manager.merge(justificativa);
                manager.getTransaction().commit();
                manager.close();
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/justificativa/lista-justificativa/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String excluir(Justificativa justificativa) {
        try {
            if (usuarioLogado.getAdministrador().equals("true")) {
                new GenericDAO<>(Justificativa.class).remove(justificativa);
                Msg.addMsgFatal("Excluido com Sucesso: " + justificativa.getNome());
                return "/View/justificativa/lista-justificativa/index?faces-redirect=true";
            } else {
                Msg.addMsgFatal("Não Autorizado!");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String editar(Justificativa justificativa) {
//        if (usuario.getNome().equals(usuarioLogado.getNome())) {
        this.justificativa = justificativa;
        return "/View/justificativa/cadastrar-justificativa/index";
//        } else if (usuarioLogado.getAdministrador().equals("true")) {
//            this.usuario = usuario;
//            return "/View/usuario/cadastrarUsuario";
//        } else {
//            Msg.addMsgFatal("Não Autorizado");
//        }
//        return null;

    }

    public String novaJustificativa(Justificativa justificativa) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            return "/View/justificativa/cadastrar-justificativa/index?faces-redirect=true";
        } else {
            Msg.addMsgFatal("Somente Administrador");
        }

        return null;
    }

    public Justificativa getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(Justificativa justificativa) {
        this.justificativa = justificativa;
    }

}
