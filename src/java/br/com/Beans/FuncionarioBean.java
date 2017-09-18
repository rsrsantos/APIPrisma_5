package br.com.Beans;

import Facade.FuncionarioFacade;
import br.com.Core.CoreUtils;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Departamento;
import br.com.Model.Funcao;
import br.com.Model.Funcionario;
import br.com.Model.Horario;
import br.com.Model.PtoEquipamento;
import br.com.Model.Usuario;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.caelum.stella.validation.NITValidator;
import br.com.utils.Msg;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import tcpcom.TcpClient;

@ManagedBean
//@ViewScoped
//@SessionScoped
@RequestScoped
public class FuncionarioBean {

    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    private Funcionario funcionario = new Funcionario();
    private FuncionarioFacade dao = new FuncionarioFacade();
    PtoEquipamento ptoequipamento = new PtoEquipamento();
//    private DataModel<Funcionario> Item;
    private org.apache.log4j.Logger logger = Logger.getLogger(PtoEquipamentoBeanHenry.class.getName());
    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
    EntityManager manager = new JPAConect().getEntityManager();

    private TcpClient client;
    private Boolean conectado;
    final List<String> listaRegistros = new ArrayList<>();

    private boolean isValidMatricula = false;

//    private List<Funcionario> selectedFuncionario;
//    public List<Funcionario> getSelectedFuncionario() {
//        return selectedFuncionario;
//    }
//    
//    public void setSelectedFuncionario(List<Funcionario> selectedFuncionario) {
//        this.selectedFuncionario = selectedFuncionario;
//    }
    public Funcionario getFuncionario() {
        return funcionario;
    }

    public List<Funcionario> getFuncionarios() {
        return new GenericDAO<>(Funcionario.class).listaTodos();

    }

    public List<Funcao> getFuncaos() {
        return new GenericDAO<>(Funcao.class).listaTodos();
    }

    public List<Departamento> getDepartamentos() {
        return new GenericDAO<>(Departamento.class).listaTodos();

    }

    public List<Horario> getHorarios() {
        return new GenericDAO<>(Horario.class).listaTodos();
    }

    public void imprimir() {

    }

    public String gravarFuncionario() {
        manager.getTransaction().begin();
        try {

            if (funcionario.getId() == 0) {
                System.out.println("Valida Matricula: " + isValidMatricula);
                if (isValidMatricula == false) {
//                    new GenericDAO<>(Funcionario.class).adiciona(funcionario);
                    funcionario.setUsuario(usuarioLogado);
                    funcionario.setUltima_alteracao(datahora);
                    manager.persist(funcionario);
                    manager.getTransaction().commit();
                    manager.close();
                    Msg.addMsgInfo("Cadastro realizado com sucesso! " + funcionario.getNome());

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/funcionario/lista-funcionario/index?faces-redirect=true";
                } else {
                    Msg.addMsgFatal("Matricula já Cadastrada");
                }
            } else {
//                new GenericDAO<>(Funcionario.class).atualiza(funcionario);
                funcionario.setUsuario(usuarioLogado);
                funcionario.setUltima_alteracao(datahora);
                manager.merge(funcionario);
                manager.getTransaction().commit();
                manager.close();
                Msg.addMsgInfo("Cadastro atualizado com sucesso! " + funcionario.getNome());

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/funcionario/lista-funcionario/index?faces-redirect=true";
            }

        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;

    }

    public String excluir(Funcionario funcionario) {
        try {
//        if (usuarioLogado.getAdministrador().equals("true")) {
            new GenericDAO<>(Funcionario.class).remove(funcionario);
            Msg.addMsgFatal("Funcionário Excluido: " + funcionario.getNome());
            return "/View/funcionario/lista-funcionario/index";
        } catch (Exception e) {
            Msg.addMsgFatal("Operação Não Realizada !");
        }
//        } else {
//            Msg.addMsgFatal("Não Autorizado!");
//        }
        return null;
    }

    public String editar(Funcionario funcionario) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            this.funcionario = funcionario;
            return "/View/funcionario/cadastrar-funcionario/index";
        } else {
            Msg.addMsgFatal("Não Autorizado!");
        }
        return null;
    }

//    public void isValidPis(String pis) {
//        NITValidator valida = new NITValidator();
//        try {
//            valida.assertValid(pis);
//            Msg.addMsgWarn("PIS Válido");
//
//        } catch (InvalidStateException e) {
//            Msg.addMsgFatal("PIS Inválido");
//        }
//    }
    public void isValidaMatri(String matricula) {
        funcionario = dao.buscaMatricu(this.funcionario.getMatricula());
        if (funcionario.getMatricula() != null) {
            isValidMatricula = true;
            Msg.addMsgFatal("Matricula já Cadastrado!");
        }
    }

//=========Envio de Funcionarios=======================================================================================================================
    public void conectarFuncionario(PtoEquipamento ptoequipamento, Funcionario funcionario) {

        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
                enviarFuncionario(client, ptoequipamento, funcionario);
                Msg.addMsgInfo("Enviado : " + funcionario.getNome());
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada!" + e);

        }

    }

    public void enviarFuncionario(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        String codigo = "01+EU+00+1+I[";
        String nome = funcionario.getNome();
        String cod01 = "[";
        String matricula = funcionario.getMatricula();
        String cod02 = "[0[1[";
        String pis = funcionario.getPis();
        String str1 = codigo + pis + cod01 + nome + cod02 + matricula;

        char[] data = str1.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());
        int dados = client.availableData();

    }

    //========================Excluir Funcionário ===================================================================================
    public void conectarFuncionarioExcluir(PtoEquipamento ptoequipamento, Funcionario funcionario) {
        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
                enviarFuncionarioExcluir(client, ptoequipamento, funcionario);
                Msg.addMsgFatal("Funcionário Excluido! " + funcionario.getNome());
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }
    }

    public void enviarFuncionarioExcluir(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        String codigo = "01+EU+00+1+E[";
        String nome = funcionario.getNome();
        String cod01 = "[";
        String matricula = funcionario.getMatricula();
        String cod02 = "[0[1[";
        String pis = funcionario.getPis();
        String str1 = codigo + pis + cod01 + nome + cod02 + matricula;
        System.out.println("Funcionário : " + str1);

        char[] data = str1.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());

    }
    //======================Excluir Biometria ==============================================================================

    public void ExcluirBiometria(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {

                String codigo = "01+ED+00+E]";
                String matricula = funcionario.getMatricula();
                String str1 = codigo + matricula;
                char[] data = str1.toCharArray();
                String str = textFormat(data);
                client.sendData(str.toCharArray());
                Msg.addMsgFatal("Biometria Excluida: " + funcionario.getNome());
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }

    }

    //======================Pesquisa Funcionário ===========================================================================
    public void conectarFuncionarioPesquisa(PtoEquipamento ptoequipamento, Funcionario funcionario) {
        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
//                enviarFuncionarioPesquisa(client, ptoequipamento, funcionario);
                procedimentoPesquisa(ptoequipamento, client, conectado, funcionario);
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }
    }

    public int enviarFuncionarioPesquisa(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        String codigo = "01+RU+00+-2]";
        String pis = funcionario.getPis();
        String str1 = codigo + pis;

        char[] data = str1.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());
        int dados = client.availableData();
        return dados;
    }

    public String procedimentoPesquisa(final PtoEquipamento ptoequipamento, final TcpClient client, final Boolean solicitarRegistros, Funcionario funcionario) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (conectado) {

//                        if (solicitarRegistros == true) {
                        int temDados = enviarFuncionarioPesquisa(client, ptoequipamento, funcionario);

                        if (temDados > 0) {

                            char[] temp = client.receiveData(temDados); //RECEBENDO DADOS
//                            Msg.addMsgInfo("Funcionário Encontrado");
                            String str8 = "", aux = "";
                            for (char chr : temp) {
                                str8 += chr;
                                System.out.println(str8);

                            }

                            conectado = false;
                            client.disconnect();

                        } else {

                            logger.error("Funcionário não encontrado!" + funcionario.getNome());

                        }

                        Thread.sleep(5000);  //esperando resposta

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    conectado = false;
                    logger.info(" ### problema na conexão ###.");

                }
                logger.info("Conexão finalizada.");

            }

        }
        ).start();
        return null;

    }

    //=========================RecebeBiometria=======================================
    public void conectarBiometria(PtoEquipamento ptoequipamento, Funcionario funcionario) {
        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
//                enviarFuncionarioPesquisa(client, ptoequipamento, funcionario);
                procedimentoBiometria(ptoequipamento, client, conectado, funcionario);
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }
    }

    public int recebeBiometria(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        String codigo = "01+RD+00+D]";
        String pis = funcionario.getMatricula();
        String str1 = codigo + pis;

        char[] data = str1.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());
        int dados = client.availableData();
        return dados;
    }

    public String procedimentoBiometria(final PtoEquipamento ptoequipamento, final TcpClient client, final Boolean solicitarRegistros, Funcionario funcionario) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (conectado) {
//                        if (solicitarRegistros == true) {
                        int temDados = recebeBiometria(client, ptoequipamento, funcionario);

                        if (temDados > 0) {

                            char[] temp = client.receiveData(temDados); //RECEBENDO DADOS
                            String str8 = "";
                            for (char chr : temp) {
                                str8 += chr;
                                System.out.println(str8);
                            }
                            conectado = false;
                            client.disconnect();

                        } else {

                            logger.error("Funcionário não encontrado!" + funcionario.getNome());

                        }

                        Thread.sleep(500);  //esperando resposta

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    conectado = false;
                    logger.info(" ### problema na conexão ###.");

                }
                logger.info("Conexão finalizada.");

            }

        }
        ).start();
        return null;

    }

    public String textFormat(char[] data) {
        String aux = "", aux2 = "", str = "";
        char BYTE_INIT, BYTE_END, BYTE_TAM[] = {0, 0}, BYTE_CKSUM;
        BYTE_INIT = (char) Integer.valueOf("2", 16).intValue();// conf. bit
        // inicial
        BYTE_END = (char) Integer.valueOf("3", 16).intValue();// conf. bit final
        BYTE_TAM[0] = (char) data.length;// conf. tamanho dos dados
        BYTE_TAM[1] = (char) Integer.valueOf("0", 16).intValue();
        aux2 += BYTE_INIT; // Inserindo byte inicial
        aux2 += BYTE_TAM[0]; // Inserindo byte do tamanho
        aux2 += BYTE_TAM[1];
        for (char chr : data) {
            str += chr;
        }
        aux = new String(aux2 + str); // concatenando com a informaÃ§Ã£o

        BYTE_CKSUM = aux.charAt(1);// Calculo do Checksum
        for (int a = 2; a < aux.length(); a++) {
            BYTE_CKSUM = (char) (BYTE_CKSUM ^ aux.charAt(a));
        }
        aux += BYTE_CKSUM; // Inserindo Checksum
        aux += BYTE_END; // Inserindo byte Final
        return aux;

    }

    public String stringHexFormat(String str) {
        String aux = "", temp = "";
        for (char ch : str.toCharArray()) {
            temp += Integer.toHexString(ch).toUpperCase();
            //Converte Hexa em String
            if (temp.length() == 1) {
                aux += "0" + temp + " ";//se tiver 1 digito complementa com 0
            } else {
                aux += temp + " ";
            }
            temp = new String();
        }
        return aux;
    }

    private int currentTab = 0;

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }

    public void onTabChange(org.primefaces.event.TabChangeEvent event) {
        String id = event.getTab().getId();
        if (id.equals("tab01")) {
            this.setCurrentTab(01);
        } else if (id.equals("tab02")) {
            this.setCurrentTab(02);
        } else if (id.equals("tab03")) {
            this.setCurrentTab(03);
        }
    }

    public boolean isIsValidMatricula() {
        return isValidMatricula;
    }

    public void setIsValidMatricula(boolean isValidMatricula) {
        this.isValidMatricula = isValidMatricula;
    }

}
