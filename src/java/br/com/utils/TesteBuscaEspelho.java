/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.utils;

import Facade.EspelhoPontoFacade;
import br.com.DAO.JPAConect;
import br.com.Model.EspelhoPonto;
import br.com.Model.PtoArquivo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author postgres
 */
public class TesteBuscaEspelho {

    public static void main(String[] args) throws ParseException {
        EspelhoPontoFacade af = new EspelhoPontoFacade();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final List<String> listaPis = new ArrayList<>();
        EntityManager manager = new JPAConect().getEntityManager();

        List<PtoArquivo> arquivos = af.buscaBatidasByDate("12659898660", sdf.parse("23/08/2017"));

        for (PtoArquivo arquivo : arquivos) {
            String dataBatida = sdf.format(arquivo.getData_batida());
//            listaPis.add(arquivo.getHora());

            System.err.println("Funcionario: id " + arquivo.getId() + " " + arquivo.getPis() + " - " + "Hora: " + arquivo.getHora() + " - " + "Data: " + sdf.format(arquivo.getData_batida()));
        }

        Collections.sort(listaPis);

        EspelhoPonto espelhoPonto = new EspelhoPonto();
        espelhoPonto.setEntrada01(listaPis.get(0));
        espelhoPonto.setSaida01(listaPis.get(1));
        espelhoPonto.setEntrada02(listaPis.get(2));
//        espelhoPonto.setPis(arquivo.);
//        espelhoPonto.setSaida02(listaPis.get(3));

        System.out.println("Posição " + espelhoPonto.getEntrada01());
        System.out.println("Posição " + espelhoPonto.getEntrada02());
        System.out.println("Posição " + espelhoPonto.getEntrada01());

        manager.getTransaction().begin();
        manager.persist(espelhoPonto);
        manager.getTransaction().commit();
        manager.close();

    }
}
