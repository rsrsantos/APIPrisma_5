package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Empresa;
import br.com.Model.Usuario;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.caelum.stella.validation.NITValidator;
import br.com.utils.Msg;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

@ManagedBean
public class EmpresaBean {

    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    private Empresa empresa = new Empresa();

    EntityManager manager = new JPAConect().getEntityManager();

    public Empresa getEmpresa() {
        return empresa;
    }

    public List<Empresa> getEmpresas() {
        return new GenericDAO<>(Empresa.class).listaTodos();

    }

    public String gravarEmpresa() {

        try {

            if (empresa.getId() == 0) {
                new GenericDAO<>(Empresa.class
                ).adiciona(empresa);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/empresa/lista-empresa/index?faces-redirect=true";

            } else {
                new GenericDAO<>(Empresa.class
                ).atualiza(empresa);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/empresa/lista-empresa/index?faces-redirect=true";
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;

    }

    public String novaEmpresa() {
        if (usuarioLogado.getAdministrador().equals("true")) {
            return "/View/empresa/cadastrar-empresa/index?faces-redirect=true";
        } else {
            Msg.addMsgFatal("Somente Administrador");
        }
        return null;
    }

    public String excluir(Empresa empresa) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            new GenericDAO<>(Empresa.class
            ).remove(empresa);
            Msg.addMsgFatal("Empresa Excluida");
            return "/View/empresa/Empresa";
        } else {
            Msg.addMsgFatal("Não Autorizado!");
        }
        return null;

    }

    public String editar(Empresa empresa) {

        if (usuarioLogado.getAdministrador().equals("true")) {
            this.empresa = empresa;
            return "/View/empresa/cadastrar-empresa/index";
        } else {
            Msg.addMsgFatal("Não Autorizado");
        }
        return null;
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            results.add(query + i);
        }

        return results;
    }

    public void isValidCNPJ(String cnpj) {
        CNPJValidator valida = new CNPJValidator();

        try {
            valida.assertValid(cnpj);
            Msg.addMsgWarn("CNPJ Válido");

        } catch (InvalidStateException e) {
            Msg.addMsgFatal("CNPJ Inválido");
        }

    }

}
