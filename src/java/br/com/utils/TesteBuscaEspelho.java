package br.com.utils;

import Facade.EspelhoPontoFacade;
import br.com.DAO.JPAConect;
import br.com.Model.EspelhoPonto;
import br.com.Model.PtoArquivo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

/**
 *
 * @author postgres
 */
public class TesteBuscaEspelho {

    public static void main(String[] args) throws ParseException {
        EspelhoPontoFacade af = new EspelhoPontoFacade();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        final List<String> listaPis = new ArrayList<>();
        final List<String> listaDatas = new ArrayList<>();
        EntityManager manager = new JPAConect().getEntityManager();
        SimpleDateFormat sdfH = new SimpleDateFormat("HHmm");
        Date data = sdf.parse("01/08/2017");
        String dataBatida = null;

        List<PtoArquivo> arquivos = af.buscaBatidasByDate("13744465275", data);

        for (PtoArquivo arquivo : arquivos) {
            dataBatida = sdf.format(arquivo.getData_batida());
            String horaBatida = sdfH.format(arquivo.getHora());

            listaPis.add(horaBatida);
        }
        Collections.sort(listaPis);
        System.out.println(listaPis);

        manager.getTransaction().begin();
        EspelhoPonto espelho = new EspelhoPonto();
        espelho.setEntrada01(listaPis.get(0));
        espelho.setSaida01(listaPis.get(1));
        espelho.setEntrada02(listaPis.get(2));
        espelho.setSaida02(listaPis.get(3));
        espelho.setData(dataBatida);

        manager.persist(espelho);
        manager.getTransaction().commit();
        manager.close();

//        Map<String, Integer> contador = new HashMap<String, Integer>();
//        for (String valor : listaPis) {
//            if (!contador.containsKey(valor)) {
//                contador.put(valor, 1);
//            }
//            contador.put(valor, contador.get(valor) + 1);
//        }
//Exibe os que tiverem mais de 1.
//        for (Map.Entry<String, Integer> item : contador.entrySet()) {
//            if (item.getValue() >= 2) {
//            }
//            listaDatas.add(item.getKey());
//            Collections.sort(listaDatas);
//
//        }
//
//        for (String itens : listaDatas) {
//
////            System.out.println("Datas Ordenadas " + itens);
//        }
    }
}
