package br.com.Beans;

import br.com.Core.AgendamentoJob;
import br.com.DAO.JPAConect;
import br.com.Model.PtoArquivo;
import br.com.Model.PtoEquipamento;
import br.com.utils.Msg;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
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
public class ColetaHenry {

    private int nsrFora;
    private Boolean conectado;
    private TcpClient client;
    private org.apache.log4j.Logger logger = Logger.getLogger(ColetaHenry.class.getName());
    final List<String> listaRegistros = new ArrayList<>();
    private int lastNSR = 0;
    EntityManager manager = new JPAConect().getEntityManager();
    private  SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    private static final SimpleDateFormat sdfH = new SimpleDateFormat("HHmm");
    
    
    

    public void conectarManual(final PtoEquipamento ptoequipamento, final Boolean solicitarRegistros1) {

        final String EQUIPAMENTO_IP = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

        try {
            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
                procedimentoManual(ptoequipamento, client, solicitarRegistros1);

            } else {
            }
        } catch (Exception e) {
        }
    }

    public int registroManual(TcpClient client, PtoEquipamento ptoequipamento) {

        String codigo = "01+RR+00+N]";
        String qtdeRegistros = "20]";
        String ultimoNSR = String.valueOf(ptoequipamento.getUltimonsr());
        String str1 = codigo + qtdeRegistros + ultimoNSR;

        char[] data = str1.toCharArray();
        String str = textFormat(data);

        client.sendData(str.toCharArray());

        int dados = client.availableData();
        return dados;
    }

    public void procedimentoManual(final PtoEquipamento ptoequipamento, final TcpClient client, final Boolean solicitarRegistros1) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (conectado) {
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
                                        nsrFora = Integer.valueOf(item.substring(0, 9));

                                        int tipo = Integer.valueOf(item.substring(9, 10));
                                        PtoArquivo arquivo = new PtoArquivo();

                                        Boolean existe = false;

                                        // verifica se nsr já existe
                                        // para
                                        // este equipamento
//                                        existe = porEquipamentoNSR(ptoequipamento, nsr);
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

                                                logger.info(" Registro - NSR: " + nsr + " Equipamento: " + ptoequipamento.getDescricao() + " - " + (new Date()));
                                                manager.persist(arquivo);
                                                manager.merge(ptoequipamento);

                                            }
                                        }

                                    }
                                    manager.getTransaction().commit();
                                    manager.close();

                                } catch (Exception e) {
                                    logger.info("## Problema no Try Coleta ##" + e);
                                }

                            }
                            conectado = false;
                            client.disconnect();

                        } else {

                            logger.error("Equipamento IP: " + ptoequipamento.getIp() + " Não POSSUI dados." + (new Date()));

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

    }

    public String test() throws SchedulerException {

        try {

            // Registrando a classe que execurÃ¡ meus mÃ©todos de negÃ³cio
            JobDetail job = JobBuilder.newJob(AgendamentoJob.class)
                    .withIdentity("AgendamentoJob", "envioJSon").build();

            // Criado um objeto de intervalo de repetiÃ§Ã£o
            // No nosso caso serÃ¡ de 2 segundos
            SimpleScheduleBuilder intervalo = SimpleScheduleBuilder
                    .simpleSchedule().withIntervalInSeconds(2).repeatForever();

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

    public void teste2(final PtoEquipamento ptoequipamento) {

        Timer timer = null;
        if (timer == null) {
            timer = new Timer();                                                                   
            TimerTask tarefa = new TimerTask() {
                
                public void run() {
                    try {
                        conectarManual(ptoequipamento, conectado);
                        setManager(manager);
                        //chamar metodo
                    } catch (Exception e) {
                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, 0, 5000);
            
        }
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
//        if (typedQuery.getResultList().isEmpty()) {
//            retorno = false;
//        }else{
//            retorno = true;
//        }
//        
//        logger.info(retorno);
//        return retorno;
        Boolean retorno = false;

        int n2 = ptoequipamento.getUltimonsr();

        if (n2 == nsr) {

            retorno = true;
            client.disconnect();

        } else {
            retorno = false;
        }

        return retorno;

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

    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }
    
    

}
