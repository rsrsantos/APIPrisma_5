package br.com.Core;

import br.com.Beans.ColetaHenry;
import br.com.DAO.JpaUtil;
import br.com.Model.PtoEquipamento;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.EntityManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AgendamentoJob implements Job {

    EquipamentoBuscarConexao conectarRelogios = new EquipamentoBuscarConexao();
    ColetaHenry coleta = new ColetaHenry();
    public void execute(JobExecutionContext context) throws JobExecutionException {

        EntityManager manager = JpaUtil.getEntityManager();
        PtoEquipamento ptoequipamento = new PtoEquipamento();

        System.out.println("... executando tarefa agendada para hora: " + (new Date()));

        Date hora = context.getFireTime();

        try {
            EquipamentoBuscarConexao.pegarEventosRelogio(manager, hora);
            coleta.teste2(ptoequipamento);
            

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
