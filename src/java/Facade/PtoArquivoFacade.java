package Facade;

import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.Funcionario;
import br.com.Model.PtoArquivo;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class PtoArquivoFacade {

    EntityManager em = new JPAConect().getEntityManager();

    public boolean existe(int nsr) {
        Query query = em.createQuery("select p from PtoArquivo p where p.nsr =:nsr");
        query.setParameter("nsr", nsr);
        try {
            PtoArquivo resultado = (PtoArquivo) query.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        em.close();
        return true;
    }

    
    
    

}
