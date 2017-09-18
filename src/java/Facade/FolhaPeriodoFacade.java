package Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.FolhaPeriodo;
import java.io.Serializable;
import javax.persistence.EntityManager;

public class FolhaPeriodoFacade extends GenericDAO<FolhaPeriodo> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public FolhaPeriodoFacade() {
        super(FolhaPeriodo.class);
    }

}
