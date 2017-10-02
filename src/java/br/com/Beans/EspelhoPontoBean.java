package br.com.Beans;

import Facade.EspelhoPontoFacade;
import Facade.FolhaPeriodoFacade;
import Facade.FuncionarioFacade;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Departamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.FolhaPeriodo;
import br.com.Model.FolhaPeriodoDias;
import br.com.Model.Funcionario;
import br.com.Model.PtoArquivo;
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
import org.primefaces.event.SelectEvent;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

@ViewScoped
@ManagedBean
public class EspelhoPontoBean {

    private List<Funcionario> listapordepartamento;
    private DataModel<Funcionario> dataModeloFuncionarioSlecionado;
    private Departamento departamento;
    private Funcionario funcionarioSelecionado;
    FolhaPeriodoDias folhaDias = new FolhaPeriodoDias();
    EspelhoPonto espelho;
    Date inicioPeriodo = null;
    Date fimPeriodo = null;
    List<String> listaFoo = new ArrayList<>();
    List<String> ListaPeriodo = new ArrayList<>();
    List<EspelhoPonto> espelhoPontos = new ArrayList<>();
    EntityManager manager = new JPAConect().getEntityManager();
    FuncionarioFacade f = new FuncionarioFacade();
    EspelhoPontoFacade espelhofacade = new EspelhoPontoFacade();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    Usuario usuario = new Usuario();
    String teste;

    private DataModel<FolhaPeriodo> dataModel;
    private List<FolhaPeriodo> folhaPeriodosTeste;
    private List<FolhaPeriodoDias> folhaPeriodoDias;
    private List<EspelhoPonto> tabelaEspelho;
    FolhaPeriodoFacade folhaPeriodoFacade = new FolhaPeriodoFacade();

    public Date getHoje() {
        return new Date();
    }

    public EspelhoPontoBean() {
//        folhaPeriodoFacade = new FolhaPeriodoFacade();
        funcionarioSelecionado = new Funcionario();
    }

    private boolean carregado;

    public List<Departamento> getDepartamentos() {
        return new GenericDAO<>(Departamento.class).listaTodos();
    }

    public List<FolhaPeriodoDias> getFolhaPeriodoDias() {
        return new GenericDAO<>(FolhaPeriodoDias.class).listaTodos();
    }

    public List<FolhaPeriodo> getFolhaPeriodos() {
        return new GenericDAO<>(FolhaPeriodo.class).listaTodos();
    }

    public List<EspelhoPonto> getEspelhoPontos() {
        return espelhofacade.buscaEspelhoPonto("12738913034", "201708");
    }

    public void carregarFuncionarios() {

        if (this.departamento == null) {
            Msg.addMsgError("Selecione o Departamento");
            return;
        }
        try {
            espelhofacade = new EspelhoPontoFacade();
            listapordepartamento = espelhofacade.buscadepartamento(departamento);
            carregado = true;
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada: " + e);

        }

    }

//    public void gerardatas() throws ParseException {
//        Date dt = null;
//
//        DateFormat df = new SimpleDateFormat("dd/MM/yy");
//        DateFormat dff = new SimpleDateFormat("EEEEEE");
//
//        Date dt1 = this.getInicioPeriodo();
//        Date dt2 = this.getFimPeriodo();
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dt1);
//
//        Calendar cal1 = Calendar.getInstance();
//        cal1.setTime(dt1);
//
//        for (dt = dt1; dt.compareTo(dt2) <= 0;) {
//            if (dt.compareTo(dt2) <= 0) {
//                listaFoo.add(df.format(dt) + " " + dff.format(dt).substring(0, 3));
//            }
//            cal1.add(Calendar.DAY_OF_WEEK, +1);
//            dt = cal1.getTime();
//            System.out.println("Datas - " + listaFoo);
//
//        }
//
//        for (String item : listaFoo) {
//            teste = item;
//            System.out.println("Dias - " + teste);
//
//        }
//
//    }
    public void onRowSelect(SelectEvent event) {
        funcionarioSelecionado = (Funcionario) event.getObject();
        System.out.println(funcionarioSelecionado.getNome());

    }

    public Funcionario onRowSelect1(SelectEvent event) {
        return funcionarioSelecionado = (Funcionario) event.getObject();

    }

//    public void carregaCalendario(Funcionario funcionario) {
//
//        espelhoPontos = espelhofacade.buscaEspelhoPonto(funcionario.getPis(), "201708");
//        System.out.println(funcionarioSelecionado.getNome());
//    }

    //Gets and Setrs
    public List<Funcionario> getListapordepartamento() {
        return listapordepartamento;
    }

    public void setListapordepartamento(List<Funcionario> listapordepartamento) {
        this.listapordepartamento = listapordepartamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Funcionario getFuncionarioSelecionado() {
        return funcionarioSelecionado;
    }

    public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
        this.funcionarioSelecionado = funcionarioSelecionado;
    }

    public EspelhoPonto getEspelho() {
        return espelho;
    }

    public void setEspelho(EspelhoPonto espelho) {
        this.espelho = espelho;
    }

    public List<String> getListaFoo() {
        return listaFoo;
    }

    public void setListaFoo(List<String> listaFoo) {
        this.listaFoo = listaFoo;
    }

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }

    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public FuncionarioFacade getF() {
        return f;
    }

    public void setF(FuncionarioFacade f) {
        this.f = f;
    }

    public boolean isCarregado() {
        return carregado;
    }

    public void setCarregado(boolean carregado) {
        this.carregado = carregado;
    }

    public List<String> getListaPeriodo() {
        return ListaPeriodo;
    }

    public void setListaPeriodo(List<String> ListaPeriodo) {
        this.ListaPeriodo = ListaPeriodo;
    }

//    public List<EspelhoPonto> getEspelhoPontos() {
////        espelhofacade = new EspelhoPontoFacade();
////      
////        funcionarioSelecionado = dataModeloFuncionarioSlecionado.getRowData();
////        System.out.println(funcionarioSelecionado.getNome());
//        return espelhoPontos = espelhofacade.buscaEspelhoPonto("13744465275", "201708");
//
//    }
    public void setEspelhoPontos(List<EspelhoPonto> espelhoPontos) {
        this.espelhoPontos = espelhoPontos;
    }

    public DataModel<FolhaPeriodo> getDataModel() {
        getFolhaPeriodosTeste();
        dataModel = new ListDataModel<>(folhaPeriodosTeste);
        return dataModel;
    }

    public void setDataModel(DataModel<FolhaPeriodo> dataModel) {
        this.dataModel = dataModel;
    }

    public List<FolhaPeriodo> getFolhaPeriodosTeste() {
        folhaPeriodoFacade = new FolhaPeriodoFacade();
        folhaPeriodosTeste = folhaPeriodoFacade.listaTodos();
        return folhaPeriodosTeste;
    }

    public void setFolhaPeriodosTeste(List<FolhaPeriodo> folhaPeriodosTeste) {
        this.folhaPeriodosTeste = folhaPeriodosTeste;
    }

    public EspelhoPontoFacade getEspelhofacade() {
        return espelhofacade;
    }

    public void setEspelhofacade(EspelhoPontoFacade espelhofacade) {
        this.espelhofacade = espelhofacade;
    }

    public FolhaPeriodoFacade getFolhaPeriodoFacade() {
        return folhaPeriodoFacade;
    }

    public DataModel<Funcionario> getDataModeloFuncionarioSlecionado() {
        getListapordepartamento();
        dataModeloFuncionarioSlecionado = new ListDataModel<>(listapordepartamento);
        return dataModeloFuncionarioSlecionado;
    }

    public void setDataModeloFuncionarioSlecionado(DataModel<Funcionario> dataModeloFuncionarioSlecionado) {
        this.dataModeloFuncionarioSlecionado = dataModeloFuncionarioSlecionado;
    }

    public Date getInicioPeriodo() {
        return inicioPeriodo;
    }

    public void setInicioPeriodo(Date inicioPeriodo) {
        this.inicioPeriodo = inicioPeriodo;
    }

    public Date getFimPeriodo() {
        return fimPeriodo;
    }

    public void setFimPeriodo(Date fimPeriodo) {
        this.fimPeriodo = fimPeriodo;
    }

    public FolhaPeriodoDias getFolhaDias() {
        return folhaDias;
    }

    public void setFolhaDias(FolhaPeriodoDias folhaDias) {
        this.folhaDias = folhaDias;
    }

    public List<EspelhoPonto> getTabelaEspelho() {
        return tabelaEspelho;
    }

    public void setTabelaEspelho(List<EspelhoPonto> tabelaEspelho) {
        this.tabelaEspelho = tabelaEspelho;
    }

}
