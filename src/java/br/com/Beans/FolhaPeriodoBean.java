package br.com.Beans;

import br.com.Core.CoreUtils;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.EspelhoPonto;
import br.com.Model.FolhaPeriodo;
import br.com.Model.FolhaPeriodoDias;
import br.com.Model.Funcionario;
import br.com.Model.Usuario;
import br.com.utils.Msg;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

@ManagedBean
public class FolhaPeriodoBean {

    FolhaPeriodo periodoFolha = new FolhaPeriodo();
    FacesContext context = FacesContext.getCurrentInstance();
    EntityManager manager = new JPAConect().getEntityManager();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
    List<String> listaFoo = new ArrayList<>();

    public List<FolhaPeriodo> getFolhaPeriodos() {
        return new GenericDAO<>(FolhaPeriodo.class).listaTodos();
    }

    public String gravarPeriodoFolha() {

        try {

            if (periodoFolha.getId() == 0) {
                periodoFolha.setUsuario(usuarioLogado);
                periodoFolha.setUltima_Alteracao(datahora);
                new GenericDAO<>(FolhaPeriodo.class).adiciona(periodoFolha);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/folha/lista-periodo/index?faces-redirect=true";

            } else {
                periodoFolha.setUsuario(usuarioLogado);
                periodoFolha.setUltima_Alteracao(datahora);
                new GenericDAO<>(FolhaPeriodo.class).atualiza(periodoFolha);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/folha/lista-periodo/index?faces-redirect=true";
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;

    }

    public void gerardatas(FolhaPeriodo periodo) throws ParseException {
        Date dt = null;

        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        DateFormat dff = new SimpleDateFormat("EEEEEE");

        Date dt1 = periodo.getInicioFolha();
        Date dt2 = periodo.getFimFolha();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt1);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(dt1);

        for (dt = dt1; dt.compareTo(dt2) <= 0;) {
            if (dt.compareTo(dt2) <= 0) {
                listaFoo.add(df.format(dt) + " " + dff.format(dt).substring(0, 3));
            }
            cal1.add(Calendar.DAY_OF_WEEK, +1);
            dt = cal1.getTime();
            System.out.println("Datas - " + listaFoo);

        }
        manager.getTransaction().begin();

        for (String item : listaFoo) {
            FolhaPeriodoDias folha = new FolhaPeriodoDias();
            
            folha.setDatas(item);

            manager.persist(folha);

        }
        manager.getTransaction().commit();
        manager.close();
        Msg.addMsgInfo("Afastamento Processado");

    }

    public String excluir(FolhaPeriodo periodo) {
        try {
            if (usuarioLogado.getAdministrador().equals("true")) {

                new GenericDAO<>(FolhaPeriodo.class).remove(periodo);
                Msg.addMsgFatal("Periodo Excluido:  " + periodo.getPeriodo());

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/folha/lista-periodo/index?faces-redirect=true";
            } else {
                Msg.addMsgFatal("Não Autorizado!");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String editar(FolhaPeriodo periodoFolha) {
//        if (usuario.getNome().equals(usuarioLogado.getNome())) {
        this.periodoFolha = periodoFolha;
        return "/View/folha/cadastrar-periodo/index";
//        } else if (usuarioLogado.getAdministrador().equals("true")) {
//            this.usuario = usuario;
//            return "/View/usuario/cadastrarUsuario";
//        } else {
//            Msg.addMsgFatal("Não Autorizado");
//        }
//        return null;

    }

    public String novoPeriodo(FolhaPeriodo periodoFolha) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            return "/View/folha/cadastrar-periodo/index";
        } else {
            Msg.addMsgFatal("Somente Administrador");
        }

        return null;
    }

    public FolhaPeriodo getPeriodoFolha() {
        return periodoFolha;
    }

    public void setPeriodoFolha(FolhaPeriodo periodoFolha) {
        this.periodoFolha = periodoFolha;
    }

}
