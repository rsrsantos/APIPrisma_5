package Facade;

import br.com.DAO.JPAConect;
import br.com.Model.Funcao;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class FuncaoFacade {

    EntityManager em = new JPAConect().getEntityManager();

    
    public Funcao buscaFuncao(String descricao) {
        Query query = em.createQuery("select u from Funcao u where u.descricao = :descricao");
        query.setParameter("descricao", descricao);
        Funcao funcao = (Funcao) query.getSingleResult();
        return funcao;
    }

}
