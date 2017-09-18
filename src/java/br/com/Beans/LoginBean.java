package br.com.Beans;

import br.com.Model.Usuario;
import Facade.UsuarioFacade;
//import br.com.Core.BeanUtils;
import br.com.Core.CoreUtils;
import br.com.DAO.JPAConect;
import br.com.utils.Msg;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

@ManagedBean
@SessionScoped
public class LoginBean {

    private String senha;
    private String login;
    private Date acesso;
    private Integer sessionTimeout;
    private Usuario usuario;
    private UsuarioFacade usuarioDao;
    EntityManager manager = new JPAConect().getEntityManager();
    private Boolean isAdmin;
//    BeanUtils criptografa = new BeanUtils();
    private List<String> listaOnline = new ArrayList<>();

    public String efetuaLogin() {

        try {
            boolean existe = new UsuarioFacade().existe(usuario);
            usuario = usuarioDao.logar(usuario.getLogin(), usuario.getSenha());

            if (existe) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getSessionMap().put("usuarioLogado", usuario);
                retorna();
                Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
                
                if (usuarioLogado.getAdministrador().equals("true")) {
                    isAdmin = true;
                }
                return "/home?faces-redirect=true";
            } else {
            }
        } catch (Exception e) {
            Msg.addErrorMessage("Login ou Senha errado, tente novamente !");
        }
        return null;
    }

    public Integer sessionTimeoutInterval() {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = facesCtx.getExternalContext();

        HttpSession session = (HttpSession) extCtx.getSession(false);
        int val = ((session.getMaxInactiveInterval()) - 5) * 1000;

        return val;
    }

//    public String efetuaLogin() {
//
//        try {
//
//            usuario = usuarioDao.logar(this.login);
//
//            if (usuario.getId() != 0) {
//
//                if (usuario.getSenha().equals(senha)) {
//                    FacesContext context = FacesContext.getCurrentInstance();
//                    context.getExternalContext().getSessionMap().put("usuarioLogado", usuario);
//                    retorna();
//                    
//                    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
//                    if (usuarioLogado.getAdministrador().equals("true")) {
//                        isAdmin = true;
//                    }
//
//                    return "/home?faces-redirect=true";
//                } else {
//                    mensagem.addError("Login ou Senha errado, tente novamente !", FacesMessage.SEVERITY_ERROR);
//                    FacesContext.getCurrentInstance().validationFailed();
//                    
//                }
//            } else {
//                mensagem.addError("Login ou Senha errado, tente novamente !", FacesMessage.SEVERITY_ERROR);
//                FacesContext.getCurrentInstance().validationFailed();
//            }
//        } catch (Exception e) {
//            Msg.addMsgFatal("Operação não realizada!");
//            System.out.println("erro..." + e.getMessage());
//        }
//
//        return "/index?faces-redirect=true";
//    }
//    public String efetuaLogin1() {
//
//        usuario = usuarioDao.logar(usuario.getLogin(), usuario.getSenha());
//
//        if (usuario == null) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login ou Senha errado, tente novamente !", "Erro no Login!"));
//            FacesContext.getCurrentInstance().validationFailed();
//            return "";
//        } else {
//            return "/home?faces-redirect=true";
//        }
////        try {
////        usuario = usuarioDao.logar(usuario.getLogin(), usuario.getSenha());
////
////        if (usuario.getId() != 0) {
////            FacesContext context = FacesContext.getCurrentInstance();
////            context.getExternalContext().getSessionMap().put("usuarioLogado", usuario);
////            retorna();
////
////            Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
////            if (usuarioLogado.getAdministrador().equals("true")) {
////                isAdmin = true;
////            }
////            return "/home?faces-redirect=true";
////        } else {
////            Msg.addMsgWarn("Usuário Inválido!");
////            return null;
////        }
////        } catch (Exception e) {
////            Msg.addMsgFatal("Login ou Senha errado, tente novamente !");
////        }
//
//    }
    public void retorna() {

        try {
            manager.getTransaction().begin();
            String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
//            System.out.println("logando: " + usuario.getNome() + datahora);
            usuario.setUltimologin(datahora);
            manager.merge(usuario);
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
        }

    }

    public void sessionDestroyed(HttpSessionEvent event) {
        String ultimoAcesso = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date(event.getSession().getLastAccessedTime()));
        System.out.println("Sessão expirada " + event.getSession().getId() + ". Ultimo Acesso = " + ultimoAcesso);
    }

    public String pesquisarLogin() {
        return "/View/usuario/MudarSenha.xhtml?faces-redirect=true";
    }

    public LoginBean() {
        usuario = new Usuario();
        usuarioDao = new UsuarioFacade();
    }

    public Date getAcesso() {
        return acesso;
    }

    public void setAcesso(Date acesso) {
        this.acesso = acesso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioFacade getUsuarioDao() {
        return usuarioDao;
    }

    public void setUsuarioDao(UsuarioFacade usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getListaOnline() {
        return listaOnline;
    }

    public void setListaOnline(List<String> listaOnline) {
        this.listaOnline = listaOnline;
    }
    
    

}
