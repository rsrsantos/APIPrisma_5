package br.com.Beans;

import Facade.FuncaoFacade;
import br.com.DAO.GenericDAO;
import br.com.Model.Funcao;
import br.com.Model.Usuario;
import br.com.utils.Msg;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class FuncaoBean {

    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    Funcao funcao = new Funcao();
    private FuncaoFacade dao = new FuncaoFacade();

    public List<Funcao> getFuncaos() {
        return new GenericDAO<>(Funcao.class).listaTodos();

    }

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    public String gravarFuncao() {
        try {
            if (funcao.getId() == 0) {
                new GenericDAO<>(Funcao.class).adiciona(this.funcao);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/funcao/lista-funcao/index?faces-redirect=true";

            } else {
                new GenericDAO<>(Funcao.class).atualiza(this.funcao);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);
                return "/View/funcao/lista-funcao/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String excluir(Funcao funcao) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            new GenericDAO<>(Funcao.class
            ).remove(funcao);
            return "/View/lista-funcao/index";
        } else {
            Msg.addMsgFatal("Não Autorizado!");
        }
        return null;
    }

    public String novaFuncao() {
        if (usuarioLogado.getAdministrador().equals("true")) {
            return "/View/funcao/cadastrar-funcao/index?faces-redirect=true";
        } else {
            Msg.addMsgFatal("Somente Administrador");
        }
        return null;

    }

    public String editar(Funcao funcao) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            this.funcao = funcao;
            return "/View/funcao/cadastrar-funcao/index";
        } else {
            Msg.addMsgFatal("Não Autorizado");
        }
        return null;

    }

    public void isValidaFuncao() {
        funcao = dao.buscaFuncao(this.funcao.getDescricao());
        if (funcao.getDescricao() != null) {
            Msg.addMsgFatal("Função já Cadastrado!");
        }
    }
}
