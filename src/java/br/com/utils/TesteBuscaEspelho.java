package br.com.utils;

import Facade.EspelhoPontoFacade;
import br.com.DAO.JPAConect;
import br.com.Model.EspelhoPonto;
import br.com.Model.PtoArquivo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

public class TesteBuscaEspelho {

    public static void main(String[] args) throws ParseException {
        EspelhoPontoFacade af = new EspelhoPontoFacade();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat periodo = new SimpleDateFormat("yyyyMM");

        final List<String> listaPis = new ArrayList<>();
        final List<String> listaDatas = new ArrayList<>();
        final List<String> lista4 = new ArrayList<>();
        final List<String> lista2 = new ArrayList<>();

        EntityManager manager = new JPAConect().getEntityManager();
        SimpleDateFormat sdfH = new SimpleDateFormat("HHmm");
        String dataBatida = "0";
        String PeriodoBatida = "0";
        String pis = null;
        String[] linha;

        StringBuilder nomes = new StringBuilder();
        nomes.append("Carlos>").append("Maria>").append("José>").append("Renata");

        System.out.println(nomes.toString());

// Inicia a busca das datas por periodo        
        List<PtoArquivo> arquivos1 = af.buscaBatidas("12738913034", "201708");
//
        for (PtoArquivo arquivo : arquivos1) {
            dataBatida = sdf.format(arquivo.getData_batida());
//            String horaBatida = sdfH.format(arquivo.getHora());

//            if () {
//
//            }
            listaPis.add(dataBatida);
        }

        Map<String, Integer> contador = new HashMap<>();
        for (String valor : listaPis) {
            if (!contador.containsKey(valor)) {
                contador.put(valor, 1);
            }
            contador.put(valor, contador.get(valor) + 1);
        }
//Exibe os que tiverem mais de 1.
        for (Map.Entry<String, Integer> item : contador.entrySet()) {
            if (item.getValue() >= 2) {
            }
            listaDatas.add(item.getKey());
            Collections.sort(listaDatas);

        }

// Inicia Busca por dia    
        manager.getTransaction().begin();
        for (String data1 : listaDatas) {

            List<PtoArquivo> arquivos = af.buscaBatidasByDate("12738913034", sdf.parse(data1));

            for (PtoArquivo arquivo : arquivos) {
                dataBatida = sdf.format(arquivo.getData_batida());
                PeriodoBatida = periodo.format(arquivo.getData_batida());
                String horaBatida = sdfH.format(arquivo.getHora());
                 pis = arquivo.getPis();

                if (horaBatida.length() == 4) {
                    lista4.add(horaBatida);
                } else {
                    lista2.add(horaBatida);
                }
            }
            Collections.sort(lista4);
            EspelhoPonto espelho = new EspelhoPonto();

            try {
                if (lista4.get(3).isEmpty()) {
                    System.out.println("vazio");
                    break;
                }
                espelho.setEntrada01(lista4.get(0));
                espelho.setSaida01(lista4.get(1));
                espelho.setEntrada02(lista4.get(2));
                espelho.setSaida02(lista4.get(3));
                espelho.setData(dataBatida);
                espelho.setPeriodo(PeriodoBatida);
                espelho.setPis(pis);
                System.out.println(lista4);
            } catch (Exception e) {
                System.out.println("Erro: " + e);
                espelho.setEntrada01(lista4.get(0));
                espelho.setSaida01(lista4.get(1));
                espelho.setEntrada02(lista4.get(2));
                espelho.setData(dataBatida);
                espelho.setPeriodo(PeriodoBatida);
                espelho.setPis(pis);
            }

            manager.persist(espelho);
            lista4.clear();

        }

        manager.getTransaction().commit();
        manager.close();
    }
}
