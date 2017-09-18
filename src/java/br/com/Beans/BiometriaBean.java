package br.com.Beans;

import br.com.Model.Funcionario;
import br.com.Model.PtoEquipamento;
import br.com.utils.Msg;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import tcpcom.TcpClient;

@ManagedBean
public class BiometriaBean {

    private org.apache.log4j.Logger logger = Logger.getLogger(PtoEquipamentoBeanHenry.class.getName());
    private Boolean conectado;
    private TcpClient client;

    PtoEquipamento ptoequipamento = new PtoEquipamento();

    public boolean procedimentobiometria(final PtoEquipamento ptoequipamento, final TcpClient client, final Boolean solicitarRegistros) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (conectado) {

//                        if (solicitarRegistros == true) {
                        int temDados = recebebiometria(ptoequipamento, client);

                        if (temDados > 0) {

                            char[] temp = client.receiveData(temDados); //RECEBENDO DADOS

                            String str8 = "", aux = "";
                            for (char chr : temp) {
                                str8 += chr;

                                System.out.println(str8);

                            }

                            conectado = false;
                            client.disconnect();

                        } else {

                            logger.error("Equipamento IP: " + ptoequipamento.getIp() + " Não POSSUI dados de Biometria." + (new Date()));

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

    public boolean conectarbiometria(final PtoEquipamento ptoequipamento, final Boolean solicitarRegistros) {

        final String EQUIPAMENTO_IP = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();
        boolean retorno = false;

        client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));

        conectado = client.connect();

        if (client.isConnected()) {

            retorno = procedimentobiometria(ptoequipamento, client, solicitarRegistros);
            return true;
        }
        return retorno;
    }

    public int recebebiometria(final PtoEquipamento ptoequipamento, TcpClient client) {

        String str5 = "01+RD+00+D]847010";
        char[] data = str5.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());

        int dados = client.availableData();

        return dados;

    }

    public String funcionario(PtoEquipamento ptoequipamento) {

         String EQUIPAMENTO_IPs = ptoequipamento.getIp();
         String EQUIPAMENTO_PORTAs = ptoequipamento.getPorta();

        client = new TcpClient(EQUIPAMENTO_IPs, Integer.valueOf(EQUIPAMENTO_PORTAs));

        client.connect();

        if (client.isConnected()) {
            Msg.addMsgInfo("Conectado");
//            enviafunciario(ptoequipamento, funcionario);
        }
        return "/View/receAfd?faces-redirect=true";
    }

    
}
