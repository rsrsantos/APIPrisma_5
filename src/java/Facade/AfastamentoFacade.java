package Facade;

import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class AfastamentoFacade {

    EntityManager em = new JPAConect().getEntityManager();

    public Afastamento buscaAfastamento(long id) {
        Query query = em.createQuery("select u from Afastamento u where u.funcionario_id = :funcionario_id");
        query.setParameter("id", id);
        Afastamento afastamento = (Afastamento) query.getSingleResult();
        return afastamento;
    }

    public List<Afastamento> buscaFuncionariosAfastamento(Long afastamento) {
        Query q = em.createQuery("select p from Afastamento p JOIN p.justificativa j JOIN p.funcionario f Where p.id =:id");
        q.setParameter("id", afastamento);
        return (List<Afastamento>) q.getResultList();
    }

    public Afastamento buscaJustificativaAfastamento(Afastamento afastamento) {
        Query q = em.createQuery("select p from Afastamento p JOIN p.justificativa d WHERE d.id =:id");
        q.setParameter("id", afastamento.getJustificativa());
        return (Afastamento) q.getResultList();
    }

//    public List<EspelhoPonto> getValorEntreDatas(Date data1, Date data2,long id) {
//        Query query = em.createQuery("select p from EspelhoPonto where  between :inicio and :fim");
//        query.setParameter("data1 ", data1);
//        query.setParameter("data2", data2);
//        query.setParameter("id",id);
//        return (List<EspelhoPonto>) query.getResultList();
//    }

}
