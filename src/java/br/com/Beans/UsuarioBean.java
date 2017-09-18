package br.com.Beans;

import br.com.Model.Usuario;
import br.com.DAO.GenericDAO;
import Facade.UsuarioFacade;
//import br.com.Core.BeanUtils;
import br.com.Core.CoreUtils;
import br.com.DAO.JPAConect;
import br.com.Model.Funcionario;
//import static br.com.Model.Usuario_.senha;
import br.com.utils.Msg;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

@ManagedBean
//@SessionScoped
@ViewScoped
public class UsuarioBean {

    private boolean exibeAdm = Boolean.TRUE; // get/set

    FacesContext context = FacesContext.getCurrentInstance();
    EntityManager manager = new JPAConect().getEntityManager();
    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    private Usuario usuario = new Usuario();
    private UsuarioFacade usuariodao = new UsuarioFacade();
    String emailrecupera;
    private String confirmaSenha;
  

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Funcionario> getFuncionarios() {
        return new GenericDAO<>(Funcionario.class).listaTodos();
    }

    public List<Usuario> getUsuarios() {
        return new GenericDAO<>(Usuario.class).listaTodos();
    }

    public boolean isExibeAdm() {
        return exibeAdm;
    }

    public void setExibeAdm(boolean exibeAdm) {
        this.exibeAdm = exibeAdm;
    }

    public boolean oculta() {

        if (usuarioLogado.getAdministrador().equals("false")) {
            exibeAdm = Boolean.TRUE;
        } else {
            exibeAdm = Boolean.FALSE;
        }
        return exibeAdm;
    }

    public boolean oculta1() {

        if (usuarioLogado.getAdministrador().equals("false")) {
            exibeAdm = Boolean.FALSE;
        } else {
            exibeAdm = Boolean.TRUE;
        }
        return exibeAdm;
    }

    public String gravar() {
        manager.getTransaction().begin();
        try {
            if (usuario.getId() == 0) {
//                new GenericDAO<>(Usuario.class).adiciona(this.usuario);
//                usuario.setSenha(criptografa.criptografa(usuario.getSenha()));
                usuario.setUsuario(usuarioLogado);
                usuario.setUltima_alteracao(datahora);
                manager.persist(usuario);
                manager.getTransaction().commit();
                manager.close();
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/usuario/lista-usuario/index?faces-redirect=true";

            } else {
//                usuario.setSenha(criptografa.criptografa(usuario.getSenha()));
                usuario.setUsuario(usuarioLogado);
                usuario.setUltima_alteracao(datahora);
                manager.merge(usuario);
                manager.getTransaction().commit();
                manager.close();
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/usuario/lista-usuario/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String novoUsuario(Usuario usuario) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            return "/View/usuario/cadastrar-usuario/index?faces-redirect=true";
        } else {
            Msg.addMsgFatal("Somente Administrador");
        }

        return null;
    }

    public String excluir(Usuario usuario) {
        try {
            if (usuarioLogado.getAdministrador().equals("true")) {
                new GenericDAO<>(Usuario.class).remove(usuario);
                Msg.addMsgFatal("Usuario Excluido com Sucesso");
                return "/View/usuario/lista-usuario/index";
            } else {
                Msg.addMsgFatal("Não Autorizado!");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String editar(Usuario usuario) {
//        if (usuario.getNome().equals(usuarioLogado.getNome())) {
        this.usuario = usuario;
        return "/View/usuario/cadastrar-usuario/index";
//        } else if (usuarioLogado.getAdministrador().equals("true")) {
//            this.usuario = usuario;
//            return "/View/usuario/cadastrarUsuario";
//        } else {
//            Msg.addMsgFatal("Não Autorizado");
//        }
//        return null;

    }

    public Usuario pegaUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
        return usuarioLogado;
    }

    public String minhaConta() {
        return "/View/usuario/Usuarios?faces-redirect=true";
    }

    public void isValidaEmail() {
        usuario = usuariodao.buscaEmail(this.usuario.getEmail());
        if (usuario.getEmail() != emailrecupera) {
            Msg.addMsgFatal("Matricula já Cadastrado!");
        }
    }

    public void recuperarSenha(Usuario usuario) {

        try {
            Properties props = new Properties();

            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("pontoeletronicopmbv@gmail.com", "@#monitor2016");
                }
            });
            /**
             * Ativa Debug para sessão //
             */
//        usuario = usuariodao.buscaEmail(this.usuario.getEmail());
//        if (usuario.getEmail().equals(emailrecupera)) {
            session.setDebug(true);
            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("pontoeletronicopmbv@gmail.com")); //Remetente

                Address[] toUser = InternetAddress //Destinatário(s)
                        .parse(usuario.getEmail());
                message.setRecipients(Message.RecipientType.TO, toUser);
                message.setSubject("Recuperação de Senha");//Assunto
                message.setText("Conforme Solicitado sua atual senha é :" + usuario.getSenha());
                /**
                 * Método para enviar a mensagem criada
                 */
                Transport.send(message);
                System.out.println("Feito!!!");
                Msg.addMsgInfo("Senha Enviada com Sucesso !");

            } catch (MessagingException e) {
                Msg.addMsgFatal("Operação não Realizada");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação Não Realizada !");
        }
    }

    public String getNomeUsuario(Usuario usuario) {
        String nome = null;

        LoginBean usuariologado = new LoginBean();

        if (usuario != null) {
//            nome = usuariologado.getUsuario().getNome();
            JOptionPane.showMessageDialog(null, nome);
        }

        return nome;
    }

    public void isValidaLogin() {
        usuario = usuariodao.buscaLogin(this.usuario.getLogin());
        if (usuario.getLogin() != null) {
            Msg.addMsgFatal("Login já cadastrado!");
        }
    }

    public void isValidaEmailRecupera() {
        usuario = usuariodao.buscaEmail(this.usuario.getEmail());
        if (usuario.getEmail() == null) {
            Msg.addMsgFatal("Email Não Localizado!");
            System.out.println("Email Não Encontrado");
        }
    }

    public String home() {
        return "/home?faces-redirect=true";
    }

    public String getEmailrecupera() {
        return emailrecupera;
    }

    public void setEmailrecupera(String emailrecupera) {
        this.emailrecupera = emailrecupera;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    

}
