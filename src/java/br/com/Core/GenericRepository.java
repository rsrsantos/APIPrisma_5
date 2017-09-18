package br.com.Core;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

@SuppressWarnings("rawtypes")
public abstract class GenericRepository<T, ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class classePersistente;

	@Inject
	@PersistenceUnit(unitName = "iridioPU") 
	private EntityManager manager;


	public GenericRepository() {
		this.classePersistente = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	@SuppressWarnings("unchecked")
	public List<T> listarTodos() {
		Criteria c = criaCriteria();
		c.addOrder(Order.desc("id"));
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List) c.list();

	}

	@SuppressWarnings("unchecked")
	public T porId(Integer id) {
		return (T) buscaSession().get(getClassePersistente().getName(), id);
	}

	public Class getClassePersistente() {
		return classePersistente;
	}

	public void setClassePersistente(Class classePersistente) {
		this.classePersistente = classePersistente;
	}

	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	protected final Session buscaSession() {
		return (Session) manager.getDelegate();
	}

	protected final Criteria criaCriteria() {
		return buscaSession().createCriteria(getClassePersistente().getName());
	}

	public T salvar(T objeto) {
		objeto = manager.merge(objeto);
		return objeto;
	}

	public void excluir(T objeto) {
		System.out.println("excluindo: " + objeto);
		manager.remove(manager.merge(objeto));
	}

}
