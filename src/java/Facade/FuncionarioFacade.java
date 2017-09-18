package Facade;

import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.Departamento;
import br.com.Model.Funcionario;
import br.com.Model.Justificativa;
import java.util.List;
import static javafx.scene.input.KeyCode.T;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class FuncionarioFacade  {

    EntityManager em = new JPAConect().getEntityManager();

    public Funcionario buscaMatricu(String matricula) {
        Query query = em.createQuery("select u from Funcionario u where u.matricula = :matricula");
        query.setParameter("matricula", matricula);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;
    }

    public Funcionario buscaEquipamentoId(Long id) {
        Query query = em.createQuery("select u from Funcionario u JOIN u.ptoequipamento e where e.id = :id");
        query.setParameter("id", id);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;
    }

    public Funcionario buscaFuncionarioId(Long id) {
        Query query = em.createQuery("select u from Funcionario u where u.id = :id");
        query.setParameter("id", id);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;
    }

    public List<Afastamento> buscaJustificativa(Afastamento afastamento) {
        Query q = em.createQuery("select p from Afastamento p JOIN p.justificativa d WHERE d.id =:id");
        q.setParameter("id", afastamento.getId());
        return (List<Afastamento>) q.getResultList();
    }


    public Funcionario buscapis(String pis) {
        Query query = em.createQuery("select u from Funcionario u where u.pis = :pis");
        query.setParameter("pis", pis);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;
    }

}
