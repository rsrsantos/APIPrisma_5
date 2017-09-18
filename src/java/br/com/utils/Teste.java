package br.com.utils;

import Facade.EspelhoPontoFacade;
import br.com.Model.PtoArquivo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Teste {

    public static void main(String[] args) throws ParseException {
        EspelhoPontoFacade af = new EspelhoPontoFacade();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final List<String> listaPis = new ArrayList<>();

        List<PtoArquivo> arquivos = af.buscaBatidas("12659898660", "201708");
//
        for (PtoArquivo arquivo : arquivos) {
            System.err.println("Funcionario: id "  + arquivo.getPis() + " - " + "Hora: " + arquivo.getHora() + " - " + "Data: " + sdf.format(arquivo.getData_batida()));
        }
//        

// Pesquisa Funcionario pelo PIS
//        FuncionarioFacade f = new FuncionarioFacade();
//        Funcionario pessoa = new Funcionario();
//
//        pessoa = f.buscapis("12659898660");
//        System.out.println("PIS Selecionado: " + pessoa.getNome());
//        Date a = new Date("05/09/2017");
////        a.setDate(a.getDate() - 10);
//        String formato = "/MM/";
//        SimpleDateFormat dataFormatada = new SimpleDateFormat(formato);
//        System.out.println("Daqui há dez dias: " + dataFormatada.format(a));
//    }
//        Date data = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTime(data);
//        // formata e exibe a data e hora
//        Format format = new SimpleDateFormat("dd/MM/yyyy");
//        String mes = format.format(c.getTime());
//        System.out.println(mes);
//        List<PtoArquivo> arquivos = af.buscaBatidas("012659898660");
//        for (PtoArquivo arquivo : arquivos) {
//            Date data = arquivo.getData_batida();
//            c.setTime(data);
//            String Data_final = format.format(c.getTime());
//
//            System.err.println("Afastamento: " + arquivo.getHora() + " - " + Data_final + " - " + arquivo.getPis());
//
//        }
    }
}
