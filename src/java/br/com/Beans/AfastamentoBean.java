package br.com.Beans;

import Facade.AfastamentoFacade;
import br.com.Core.CoreUtils;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.Funcionario;
import br.com.Model.Justificativa;
import br.com.Model.Usuario;
import br.com.utils.Msg;
import java.text.DateFormat;
import java.text.Format;
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
public class AfastamentoBean {

    Afastamento afastamento = new Afastamento();
    FacesContext context = FacesContext.getCurrentInstance();
    EntityManager manager = new JPAConect().getEntityManager();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private List<Afastamento> afastamentoselecionado;
    AfastamentoFacade dao1 = new AfastamentoFacade();
    List<String> listaFoo = new ArrayList<>();
    private boolean disable;
    private static final SimpleDateFormat periodo = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat sdg = new SimpleDateFormat("dd/MM/yyyy");

    ///Listas de Acesso
    public List<Afastamento> getAfastamentos() {
        return new GenericDAO<>(Afastamento.class).listaTodos();
    }

    public List<Funcionario> getFuncionarios() {
        return new GenericDAO<>(Funcionario.class).listaTodos();
    }

    public List<Justificativa> getJustificativas() {
        return new GenericDAO<>(Justificativa.class).listaTodos();
    }

    //Metodos
    public String gravarAfastamento() {
        manager.getTransaction().begin();
        try {
            boolean existe = true;

            if (afastamento.getId() == 0) {
                if (existe == true) {

                    afastamento.setData_criacao(new Date());
                    afastamento.setUsuario(usuarioLogado);
                    afastamento.setUltima_alteracao(datahora);
                    manager.persist(afastamento);
                    manager.getTransaction().commit();
                    manager.close();

                    Msg.addMsgInfo("Cadastro realizado com Secusso! ");

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/afastamento/lista-afastamento/index?faces-redirect=true";
                } else {
                    Msg.addMsgFatal("Periodo já cadastrado para o Funcionário");
                }
            } else {
                new GenericDAO<>(Afastamento.class).atualiza(this.afastamento);
                afastamento.setUsuario(usuarioLogado);
                afastamento.setUltima_alteracao(datahora);
                manager.merge(afastamento);
                manager.getTransaction().commit();
                manager.close();

                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/afastamento/lista-afastamento/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!" + e);
        }
        return null;
    }

    public String excluir(Afastamento afastamento) {
        try {
            if (usuarioLogado.getAdministrador().equals("true")) {
                new GenericDAO<>(Afastamento.class).remove(afastamento);
                Msg.addMsgFatal("Afastamento Excluido:  " + afastamento.getDescricao());

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/afastamento/lista-afastamento/index?faces-redirect=true";
            } else {
                Msg.addMsgFatal("Não Autorizado!");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String editar(Afastamento afastamento) {
//        if (usuario.getNome().equals(usuarioLogado.getNome())) {
        this.afastamento = afastamento;
        return "/View/afastamento/cadastrar-afastamento/index";
//        } else if (usuarioLogado.getAdministrador().equals("true")) {
//            this.usuario = usuario;
//            return "/View/usuario/cadastrarUsuario";
//        } else {
//            Msg.addMsgFatal("Não Autorizado");
//        }
//        return null;

    }

    public String novoAfastamento(Afastamento afastamento) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            return "/View/afastamento/cadastrar-afastamento/index";
        } else {
            Msg.addMsgFatal("Somente Administrador");
        }

        return null;
    }

    public void processaAfastamento(Afastamento afastado) throws ParseException {

        Date data_inicio = null;
        Date data_fim = null;

        AfastamentoFacade af = new AfastamentoFacade();

        List<Afastamento> afastamentos = af.buscaFuncionariosAfastamento(afastado.getId());
        for (Afastamento afasta : afastamentos) {
            System.err.println("Afastamento: " + afasta.getId());
            System.err.println("Justificativa: " + afasta.getJustificativa().getDescricao());

            List<Funcionario> funcionarios = afasta.getFuncionario();

            data_inicio = afasta.getData_inicio();
            System.out.println("Data Inicio" + sdf.format(data_inicio));

            data_fim = afasta.getData_fim();
            System.out.println("Data Inicio" + sdf.format(data_fim));
            System.err.println("======================================================");

            Calendar c = Calendar.getInstance();
            Calendar c1 = Calendar.getInstance();
            Date data = new Date();

            c.setTime(data_inicio);
            c1.setTime(data);
            // formata e exibe a data e hora
            Format format = new SimpleDateFormat("MM");
            String mes_informado = format.format(c.getTime());
            String mes_atual = format.format(c1.getTime());

            //Gerando as Datas
            Date dt = null;

            DateFormat df = new SimpleDateFormat("dd/MM/yy");
            DateFormat dff = new SimpleDateFormat("EEEEEE");

            Date dt1 = data_inicio;
            Date dt2 = data_fim;

            Calendar cal = Calendar.getInstance();
            cal.setTime(dt1);

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(dt1);
            manager.getTransaction().begin();
            for (dt = dt1; dt.compareTo(dt2) <= 0;) {
                if (dt.compareTo(dt2) <= 0) {
                    listaFoo.add(df.format(dt));
                }
                cal1.add(Calendar.DAY_OF_WEEK, +1);
                dt = cal1.getTime();

            }
//            if (mes_informado.equals(mes_atual)) {

            if (afastado.isProcessado() == false) {

                for (Funcionario funcionario : funcionarios) {

                    boolean existe_afastamento = false;
                    for (String item : listaFoo) {

                        String dataPeriodo = item.replace("/", "");
                        System.out.println(dataPeriodo);
                        String periodoString = "";

                        periodoString = dataPeriodo.substring(2, 6);

                        EspelhoPonto espelho = new EspelhoPonto();

                        if (existe_afastamento == false) {

                            espelho.setPeriodo(periodoString);
                            espelho.setData(item);
                            espelho.setEntrada01(afasta.getJustificativa().getNome());
                            espelho.setEntrada02(afasta.getJustificativa().getNome());
                            espelho.setSaida01(afasta.getJustificativa().getNome());
                            espelho.setSaida02(afasta.getJustificativa().getNome());
                            espelho.setFuncionario(funcionario);
                            espelho.setUsuario(usuarioLogado);
                            espelho.setNome_Funcionario(funcionario.getNome());
                            espelho.setPis(funcionario.getPis());
                            afastado.setProcessado(true);
                            manager.persist(espelho);
                            manager.merge(afastado);

                        } else {
                            Msg.addMsgError("");
                            System.out.println("Excessão");
                        }

                    }
                    Msg.addMsgInfo("Afastamento Processado");
                }
            } else {
                Msg.addMsgError("Erro, Já Processado");
            }

//            } else {
//                Msg.addMsgError("Periodo informado inválido");
//            }
        }

        manager.getTransaction().commit();
        manager.close();

    }

    public String dataParaPeriodo(Date data) throws ParseException {
        if (data != null) {
            return periodo.format(data);
        }
        return null;
    }

    //Gets and Seters
    public Afastamento getAfastamento() {
        return afastamento;
    }

    public void setAfastamento(Afastamento afastamento) {
        this.afastamento = afastamento;
    }

    public String getDatahora() {
        return datahora;
    }

    public void setDatahora(String datahora) {
        this.datahora = datahora;
    }

    public List<Afastamento> getAfastamentoselecionado() {
        return afastamentoselecionado;
    }

    public void setAfastamentoselecionado(List<Afastamento> afastamentoselecionado) {
        this.afastamentoselecionado = afastamentoselecionado;
    }

    public List<String> getListaFoo() {
        return listaFoo;
    }

    public void setListaFoo(List<String> listaFoo) {
        this.listaFoo = listaFoo;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

}
