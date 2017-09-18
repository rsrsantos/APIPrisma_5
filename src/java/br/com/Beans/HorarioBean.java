package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.Departamento;
import br.com.Model.Horario;
import br.com.Model.Usuario;
import br.com.utils.GeraTabelas;
import br.com.utils.Msg;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

@ManagedBean
public class HorarioBean {
    
    Horario horario = new Horario();
    EntityManager manager = new JPAConect().getEntityManager();
    
    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    
    public List<Horario> getHorarios() {
        return new GenericDAO<>(Horario.class).listaTodos();
    }
    
    public String gravarHorario() {
        
        try {
            
            if (horario.getId() == 0) {
                new GenericDAO<>(Horario.class).adiciona(this.horario);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");
                
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);
                
                return "/View/horario/lista-horario/index?faces-redirect=true";
            } else {
                new GenericDAO<>(Horario.class).atualiza(this.horario);
                
                Msg.addMsgInfo("Horário Atualizado! ");
                
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);
                
                return "/View/horario/lista-horario/index?faces-redirect=true";
                
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!" + e);
        }
        return null;
    }
    
    public Horario getHorario() {
        return horario;
    }
    
    public void setHorario(Horario horario) {
        this.horario = horario;
    }
    
    public String excluir(Horario horario) {
        
        if (usuarioLogado.getAdministrador().equals("true")) {
            new GenericDAO<>(Horario.class
            ).remove(horario);
            Msg.addMsgFatal("Horário Excluido: " + horario.getNome());
            return "/View/horario/lista-horario/index";
        } else {
            Msg.addMsgFatal("Não Autorizado");
        }
        return null;
        
    }
    
    public String editar(Horario horario) {

//        if (usuarioLogado.getAdministrador().equals("true")) {
        this.horario = horario;
        return "/View/horario/cadastrar-horario/index";
//        } else {
////            Msg.addMsgFatal("Não Autorizado");
//        }
//        return null;

    }
    
}
