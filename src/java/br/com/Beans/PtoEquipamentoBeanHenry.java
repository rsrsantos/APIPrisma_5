package br.com.Beans;

import br.com.Core.AgendamentoJob;
import br.com.Core.AgendamentoJobExecucao;
import br.com.Core.CoreUtils;
import br.com.Model.PtoEquipamento;
import br.com.Model.PtoArquivo;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Funcionario;
import br.com.Model.TipoCorte;
import br.com.Model.Usuario;
import br.com.utils.Msg;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import tcpcom.TcpClient;

@ManagedBean
public class PtoEquipamentoBeanHenry {

    private TcpClient client;
    String u;
    private PtoEquipamento ptoequipamento = new PtoEquipamento();
    private TipoCorte tipo = new TipoCorte();
    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    private Boolean conectado;
    private int lastNSR = 0;
    private org.apache.log4j.Logger logger = Logger.getLogger(PtoEquipamentoBeanHenry.class.getName());
    private static final SimpleDateFormat sdfH = new SimpleDateFormat("HHmm");

    final List<String> listaRegistros = new ArrayList<>();

    private  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    EntityManager manager = new JPAConect().getEntityManager();

    public PtoEquipamento getPtoEquipamento() {
        return ptoequipamento;
    }

    public List<PtoEquipamento> getPtoEquipamentos() {
        return new GenericDAO<>(PtoEquipamento.class).listaTodos();

    }

    public void atualizar(PtoEquipamento ptoequipamento) {

        new GenericDAO<>(PtoEquipamento.class).atualiza(ptoequipamento);
    }

    public List<TipoCorte> getTipoCortes() {
        return new GenericDAO<>(TipoCorte.class).listaTodos();

    }

    public boolean executarProcedimento(final PtoEquipamento ptoequipamento, final TcpClient client, final Boolean solicitarRegistros) {

//        conectado = seConectado;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (conectado) {

                        if (solicitarRegistros == true) {

                            int temDados = enviarSolicitacaoRegistroPonto(client, ptoequipamento);

                            if (temDados > 0) {

                                char[] temp = client.receiveData(temDados); //RECEBENDO DADOS

                                String str = "";
                                for (char chr : temp) {
                                    if (chr == ']') {
                                        str = "";
                                    }
                                    if (Character.isDigit(chr)) {
                                        str += chr;

                                        if (str.length() == 34) {
                                            listaRegistros.add(str);

                                            int var = Integer.valueOf(str.substring(0, 9));
                                            int lastNSRInt = lastNSR;
                                            if (var > lastNSRInt) {
                                                lastNSR = Integer.valueOf(str.substring(0, 9));

                                            }
                                            str = "";
                                        }
                                    }
                                }

                                if (listaRegistros.size() > 0) {

                                    try {

                                        manager.getTransaction().begin();

                                        for (String item : listaRegistros) {
                                            int nsr = Integer.valueOf(item.substring(0, 9));

                                            int tipo = Integer.valueOf(item.substring(9, 10));
                                            PtoArquivo arquivo = new PtoArquivo();

                                            Boolean existe = false;

                                            // verifica se nsr já existe
                                            // para
                                            // este equipamento
                                            existe = porEquipamentoNSR(ptoequipamento, nsr);
                                            if (existe == false) {
                                                Date HrsString = sdfH.parse(item.substring(18, 22));
                                                String pisString = item.substring(22, 34);
                                                Date data = sdf.parse(item.substring(10,18));
                                                String Equipam = ptoequipamento.getDescricao();
                                                ptoequipamento.setUltimonsr(nsr);
                                                String modelo = ptoequipamento.getModelo();

                                                if (tipo != 2) {

                                                    arquivo.setModelo(modelo);
                                                    arquivo.setEquipamento(Equipam);
                                                    arquivo.setData_batida(data);
                                                    arquivo.setTipo(tipo);
                                                    arquivo.setNsr(nsr);
                                                    arquivo.setHora(HrsString);
                                                    arquivo.setPis(pisString);
                                                    arquivo.setPtooriginal(item);

                                                    logger.info(" ###################### gravando registro de PONTO com NSR: " + nsr
                                                            + " para equipamento: " + ptoequipamento.getId() + (new Date()));
                                                    manager.persist(arquivo);
                                                    manager.merge(ptoequipamento);

                                                }

                                            }
                                        }
                                        manager.getTransaction().commit();
                                        manager.close();

                                    } catch (Exception e) {
                                    }

                                }
                                conectado = false;
                                client.disconnect();

                            } else {

                                logger.error("Equipamento IP: " + ptoequipamento.getIp() + " Não POSSUI dados." + (new Date()));
                            }

                            Thread.sleep(500);  //esperando resposta

                        } else {

                            conectado = false;
                            client.disconnect();
                            manager.close();

                        }

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

        return true;
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

    private Boolean porEquipamentoNSR(PtoEquipamento ptoequipamento, int nsr) {

//        Boolean retorno = false;
//
//        CriteriaBuilder cb = manager.getCriteriaBuilder();
//        CriteriaQuery<PtoArquivo> cq = cb.createQuery(PtoArquivo.class);
//        Root<PtoArquivo> c = cq.from(PtoArquivo.class);
//        cq.where(cb.and(cb.equal(c.<PtoEquipamento>get("ptoequipamento"), ptoequipamento), cb.equal(c.<Integer>get("nsr"), nsr)));
//
//        TypedQuery<PtoArquivo> typedQuery = manager.createQuery(cq);
//
//        if (!typedQuery.getResultList().isEmpty()) {
//            retorno = false;
//        }
//
//        return retorno;
        Boolean retorno = false;

        int n2 = ptoequipamento.getUltimonsr();

        if (n2 == nsr) {

            retorno = true;
            client.disconnect();

        } else if (n2 != nsr) {
            retorno = false;
        }
        {

        }
        return retorno;
    }

    public String test() {

        try {
            Msg.addMsgInfo("Coleta Iniciada !");
            // Registrando a classe que execurÃ¡ meus mÃ©todos de negÃ³cio
            JobDetail job = JobBuilder.newJob(AgendamentoJob.class)
                    .withIdentity("AgendamentoJob", "envioJSon").build();

            // Criado um objeto de intervalo de repetiÃ§Ã£o
            // No nosso caso serÃ¡ de 2 segundos
            SimpleScheduleBuilder intervalo = SimpleScheduleBuilder
                    .simpleSchedule().withIntervalInSeconds(5).repeatForever();

            // Criado um disparador
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("disparaTrigger", "envioJSon")
                    .withSchedule(intervalo).build();

            // Finalmente Ã© criado um objeto de agendamento
            // que recebe o JOB e o disparador!
            org.quartz.Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String parar() throws SchedulerException {

        try {
            Msg.addMsgFatal("Coleta Parada !");
            org.quartz.Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public boolean conectar(final PtoEquipamento ptoequipamento, final Boolean solicitarRegistros) {

        final String EQUIPAMENTO_IP = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();
        boolean retorno = false;

        client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));

        conectado = client.connect();

        if (client.isConnected()) {

            retorno = executarProcedimento(ptoequipamento, client, solicitarRegistros);

            return true;
        }
        return retorno;
    }

    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public int enviarSolicitacaoRegistroPonto(TcpClient client, PtoEquipamento ptoequipamento) {

        String codigo = "01+RR+00+N]";
        String qtdeRegistros = "500]";
        String ultimoNSR = String.valueOf(ptoequipamento.getUltimonsr());
        String str1 = codigo + qtdeRegistros + ultimoNSR;

        char[] data = str1.toCharArray();
        String str = textFormat(data);

        client.sendData(str.toCharArray());

        int dados = client.availableData();

        return dados;
    }

    public void hora(PtoEquipamento ptoequipamento) {

        final String EQUIPAMENTO_IP = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();
        try {
            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));

            conectado = client.connect();

            if (client.isConnected()) {

                String str;     //Recebendo data do sistema e formatando string
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                str = new String("01+EH+00+" + dateFormat.format(new Date()) + "]00/00/00]00/00/00");
                str = textFormat(str.toCharArray());//formatando texto (cabeçalho, checksum e Byte final)
                client.sendData(str.toCharArray());
                Msg.addMsgInfo("Data e Hora Atualizados com Sucesso!");
                logger.info("####-Hora Enviada com Sucesso-### :" + EQUIPAMENTO_IP);
            } else {
                Msg.addMsgFatal("Problema na Conexão");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }

    }

    public String senhaMenu(PtoEquipamento ptoequipamento) {

        final String EQUIPAMENTO_IPp = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTAa = ptoequipamento.getPorta();
        try {
            client = new TcpClient(EQUIPAMENTO_IPp, Integer.valueOf(EQUIPAMENTO_PORTAa));

            conectado = client.connect();

            if (client.isConnected()) {

                String str1 = "01+EC+00+SENHA_MENU[";
                String password = ptoequipamento.getSenhaMenu();
                String corte = ptoequipamento.getTipoCorte();
                String cd2 = "]";
                String codigo = str1 + password + cd2;
                char[] data = codigo.toCharArray();
                String str = textFormat(data);
                client.sendData(str.toCharArray());
                Msg.addMsgInfo("Configurações Enviadas");

            } else {
                Msg.addMsgFatal("Problema na Conexão");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada!" + e);
            return "/View/receAfd?faces-redirect=true";
        }
        return null;

    }

    public String conectarFuncionarios(PtoEquipamento ptoequipamento, final Funcionario funcionario) {

        String ip = ptoequipamento.getIp();
        String porta = ptoequipamento.getPorta();

        client = new TcpClient(ip, Integer.valueOf(porta));

        client.connect();

        if (client.isConnected()) {

            String str1 = "01+RD+00+D]847010";
            char[] data = str1.toCharArray();
            String str = textFormat(data);
            client.sendData(str.toCharArray());
            int dados = client.availableData();
            System.out.println(dados);
        }

        return "/View/equipamento/receAfd";
    }

    //=================================Receber Manual===============================================
    public void conectarManual1() {

        final String EQUIPAMENTO_IP = "192.168.148.9";
        final String EQUIPAMENTO_PORTA = "3000";
//        boolean retorno = false;

        try {
            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
                procedimentoManual(ptoequipamento, client);

//                Msg.addMsgInfo("Coletado com Sucesso!" + item + " Registros");
//                item = "Coletado com Secesso" + item;
//                return null;
            } else {
                Msg.addMsgFatal("Problema na Conexão");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada!" + e);
//            return "/View/receAfd?faces-redirect=true";
        }
    }

    public void conectarManual(final PtoEquipamento ptoequipamento, final Boolean solicitarRegistros1) {

        final String EQUIPAMENTO_IP = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();
//        boolean retorno = false;

        try {
            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
                procedimentoManual(ptoequipamento, client);

            } else {
                Msg.addMsgFatal("Problema na Conexão");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada!" + e);
//            return "/View/receAfd?faces-redirect=true";
        }
    }

    public int registroManual(TcpClient client, PtoEquipamento ptoequipamento) {

        String codigo = "01+RR+00+N]";
        String qtdeRegistros = "500]";
        String ultimoNSR = String.valueOf(ptoequipamento.getUltimonsr());
        String str1 = codigo + qtdeRegistros + ultimoNSR;

        char[] data = str1.toCharArray();
        String str = textFormat(data);

        client.sendData(str.toCharArray());

        int dados = client.availableData();
        return dados;
    }

    public void procedimentoManual(final PtoEquipamento ptoequipamento, final TcpClient client) {

        //conectado = seConectado;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (conectado) {

//                        if (solicitarRegistros1 == true) {
                        int temDados1 = registroManual(client, ptoequipamento);

                        if (temDados1 > 0) {

                            char[] temp = client.receiveData(temDados1); //RECEBENDO DADOS

                            String str = "";
                            for (char chr : temp) {
                                if (chr == ']') {
                                    str = "";
                                }
                                if (Character.isDigit(chr)) {
                                    str += chr;

                                    if (str.length() == 34) {
                                        listaRegistros.add(str);

                                        int var = Integer.valueOf(str.substring(0, 9));
                                        int lastNSRInt = lastNSR;
                                        if (var > lastNSRInt) {
                                            lastNSR = Integer.valueOf(str.substring(0, 9));
                                        }
                                        str = "";
                                    }
                                }
                            }

                            if (listaRegistros.size() > 0) {

                                try {

                                    manager.getTransaction().begin();

                                    for (String item : listaRegistros) {

                                        int nsr = Integer.valueOf(item.substring(0, 9));

                                        int tipo = Integer.valueOf(item.substring(9, 10));
                                        PtoArquivo arquivo = new PtoArquivo();

                                        Boolean existe = false;

                                        // verifica se nsr já existe
                                        // para
                                        // este equipamento
                                        existe = porEquipamentoNSR(ptoequipamento, nsr);
                                        if (existe == false) {
                                            Date HrsString = sdfH.parse(item.substring(18, 22));
                                            String pisString = item.substring(22, 34);
                                            Date data = sdf.parse(item.substring(10,18));
                                            String Equipam = ptoequipamento.getDescricao();
                                            ptoequipamento.setUltimonsr(nsr);
                                            String modelo = ptoequipamento.getModelo();

                                            if (tipo != 2) {

                                                arquivo.setModelo(modelo);
                                                arquivo.setEquipamento(Equipam);
                                                arquivo.setData_batida(data);
                                                arquivo.setTipo(tipo);
                                                arquivo.setNsr(nsr);
                                                arquivo.setHora(HrsString);
                                                arquivo.setPis(pisString);
                                                arquivo.setPtooriginal(item);

                                                logger.info(" ###################### gravando registro de PONTO com NSR: " + nsr
                                                        + " para equipamento: " + ptoequipamento.getId() + (new Date()));
                                                manager.persist(arquivo);
                                                manager.merge(ptoequipamento);

                                            }

                                        }
                                    }
                                    manager.getTransaction().commit();
                                    manager.close();
                                    Msg.addInfoMessage("Coleta Concluida");

                                } catch (Exception e) {
                                    System.out.println(e);
                                }

                            }
                            conectado = false;
                            client.disconnect();

                        } else {

                            logger.error("Equipamento IP: " + ptoequipamento.getIp() + " Não POSSUI dados." + (new Date()));

                        }

                        Thread.sleep(500);  //esperando resposta

//                        } else {
//
//                            conectado = false;
//                            client.disconnect();
//                            manager.close();
//
//                        }
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

    }

    public String
            gravarEquipamento() {
        try {
            if (ptoequipamento.getId() == 0) {
                new GenericDAO<>(PtoEquipamento.class
                ).adiciona(ptoequipamento);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/equipamento/equipamento-Henry/index?faces-redirect=true";

            } else {
                new GenericDAO<>(PtoEquipamento.class
                ).atualiza(ptoequipamento);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/equipamento/equipamento-Henry/index?faces-redirect=true";
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!" + e);
        }
        return null;

    }

    public String excluir(PtoEquipamento ptoequipamento) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            new GenericDAO<>(PtoEquipamento.class
            ).remove(ptoequipamento);
            Msg.addMsgFatal("Exclusão Realizada");
            return "/View/equipamento/receAfd";
        } else {
            Msg.addMsgFatal("Não Autorizado!");
        }
        return null;
    }

    public String editar(PtoEquipamento ptoequipamento) {
        if (usuarioLogado.getAdministrador().equals("true")) {
            this.ptoequipamento = ptoequipamento;
            return "/View/equipamento/cadastrar-equipamentoHenry/index";
        } else {
            Msg.addMsgFatal("Não Autorizado!");
        }
        return null;

    }

    private HttpSession getContext() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        return session;
    }

    public String deslogar() {

        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.clear();

        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove("usuarioLogado");
        HttpSession session = getContext();
        session.setAttribute("usuarioLogado", null);

        context.renderResponse();

        String datahora = CoreUtils.dateParaString(new Date(), "dd/MM/yyyy - HH:mm");
        System.out.println("encerrando sessao as " + datahora + ".");

        return "/index?faces-redirect=true";
    }

    public void log(String item) {

//        try {
//
//            Runtime.getRuntime().exec("cmd.exe /C start  C:\\Users\\postgres\\Documents\\NetBeansProjects\\APIPrisma\\Logs.log");
//
//        } catch (Exception e) {
//
//            Msg.addMsgInfo("Arquivo de Log não localizado");
//        }
        System.out.println(item);

    }

    public TipoCorte getTipo() {
        return tipo;
    }

    public void setTipo(TipoCorte tipo) {
        this.tipo = tipo;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }
    
    
    

}
