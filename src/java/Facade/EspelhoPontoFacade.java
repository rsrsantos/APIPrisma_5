package Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Departamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.Funcionario;
import br.com.Model.PtoArquivo;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class EspelhoPontoFacade extends GenericDAO<EspelhoPonto> implements Serializable{

    EntityManager em = new JPAConect().getEntityManager();

    public EspelhoPontoFacade() {
        super(EspelhoPonto.class);
    }

    public Funcionario buscaporDepartamento(Departamento departamento) {

        Query query = em.createQuery("select u from Usuario u where u.login = :login and u.senha = :senha");
        query.setParameter("departamento", departamento);
//        query.setParameter("funcionario", funcionario);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;

    }

    public List<Funcionario> buscadepartamento(Departamento departamento) {
        Query q = em.createQuery("select p from Funcionario p JOIN p.departamento d WHERE d.id =:id");
        q.setParameter("id", departamento.getId());
        return (List<Funcionario>) q.getResultList();
    }

    public List<Departamento> listaPorUsuario(String nome) {

        StringBuffer jpql = new StringBuffer();

        jpql.append(" SELECT l FROM Departamento l ");
        jpql.append(" WHERE l.usuario = :pUsuario ");

        TypedQuery<Departamento> query = em.createQuery(jpql.toString(), Departamento.class);
        query.setParameter("pUsuario", nome);

        return query.getResultList();
    }

//    public List<ServidorVinculoForaEscala> porSetor(Setor setorSelecionado, String periodo) {
//		return repository.porSetor(setorSelecionado, periodo);
//	}
    public List<Funcionario> porNomeSemelhante(String nome) {
        return em.createQuery("from Funcionario where nome like :nome", Funcionario.class)
                .setParameter("nome", "%" + nome + "%")
                .getResultList();
    }

    public Map<String, Funcionario> listaPorMesELocalTrabalhoEUsuario(Departamento departamento) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuffer jpql = new StringBuffer();

        jpql.append(" SELECT r FROM RegistroPonto r ");
        jpql.append(" WHERE r.localTrabalho.id = :pLocalTrabalho ");

        TypedQuery<Funcionario> query = em.createQuery(jpql.toString(), Funcionario.class);
        query.setParameter("pDepartamento", departamento.getId());

        Map<String, Funcionario> map = new HashMap<String, Funcionario>();
        List<Funcionario> lista = query.getResultList();
        for (Funcionario registroPonto : lista) {
        }

        return map;
    }

    public List<PtoArquivo> buscaBatidas(String pis,String periodo) {
        Query q = em.createQuery("select p from PtoArquivo p where p.pis = :pis and p.periodo =:periodo ORDER BY p.data_batida ASC");
        q.setParameter("pis", pis);
        q.setParameter("periodo", periodo);
        return (List<PtoArquivo>) q.getResultList();
    }
    public List<EspelhoPonto> buscaEspelhoPonto(String pis,String periodo) {
        Query q = em.createQuery("select p from EspelhoPonto p where p.pis = :pis and p.periodo =:periodo");
        q.setParameter("pis", pis);
        q.setParameter("periodo", periodo);
        return (List<EspelhoPonto>) q.getResultList();
    }
    
    public List<PtoArquivo> buscaBatidasByDate(String pis, Date data_batida) {
        Query q = em.createQuery("select p from PtoArquivo p where p.pis = :pis and p.data_batida =:data_batida");
        q.setParameter("pis", pis);
        q.setParameter("data_batida", data_batida);
        return (List<PtoArquivo>) q.getResultList();
    }
    
    
    //select u from Usuario u where u.login = :login and u.senha = :senha

    public List<PtoArquivo> buscaBatidas1(String inicio ,String fim) {
        Query q = em.createQuery("select p from PtoArquivo p where p.data_batida  between :inicio and :fim");
        q.setParameter("inicio", inicio);
        q.setParameter("fim", fim);
        return (List<PtoArquivo>) q.getResultList();
    }

    //select p from ptoarquivo  p where p.data_batida BETWEEN '2017-09-01' AND '2017-09-11' AND p.pis = '012659898660';
    //select p from Afastamento p JOIN p.justificativa j JOIN p.funcionario f Where p.id =:id
    //SELECT er from ExameRealizado er where er.data between :dataInicial and :dataFinal
    
    
//    select * 
//from cadastros
//where DataRegistro between to_date( '28/04/2014', 'dd/mm/yyyy') and to_date( '28/04/2014', 'dd/mm/yyyy')
}
