package Facade;

import br.com.Core.CoreUtils;
import br.com.Model.Usuario;
import br.com.DAO.JPAConect;
import br.com.DAO.JPAConect;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class UsuarioFacade {

    EntityManager em = new JPAConect().getEntityManager();

    public boolean existe(Usuario usuario) {

        TypedQuery<Usuario> query = em.createQuery("select u from Usuario u where u.login = :pLogin and u.senha = :pSenha", Usuario.class);
        query.setParameter("pLogin", usuario.getLogin());
        query.setParameter("pSenha", usuario.getSenha());
        try {
            Usuario resultado = (Usuario) query.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        em.close();
        return true;
    }

    public Usuario logar(String login, String senha) {
        Query query = em.createQuery("select u from Usuario u where u.login = :login and u.senha = :senha");
        query.setParameter("login", login);
        query.setParameter("senha", senha);
        Usuario usuario = (Usuario) query.getSingleResult();
        return usuario;
    }
    
//    public Usuario logar(String login) {
//        Query q = em.createQuery("select u from Usuario u where u.login = :login");
//        q.setParameter("login", login);
//        return (Usuario) q.getSingleResult();
//    }

    public Usuario buscaEmail(String email) {
        Query query = em.createQuery("select u from Usuario u where u.email = :email");
        query.setParameter("email", email);
        Usuario usuario = (Usuario) query.getSingleResult();
        return usuario;
    }

    public Usuario buscaLogin(String login) {
        Query q = em.createQuery("select u from Usuario u where u.login = :login");
        q.setParameter("login", login);
        return (Usuario) q.getSingleResult();
    }

//    public Usuario logar(Usuario usuario) {
//		usuario.setId(this.porLogin(usuario).getId());
//		if (usuario.getId() == null) {
//			return new Usuario();
//		}
//		Criteria c = criaCriteria();
//		c.add(Restrictions.eq("usuario", usuario.getUsuario()));
//		c.add(Restrictions.eq("senha", CoreUtils.convertStringToMd5(usuario.getId().toString()+usuario.getSenha())));
//		Usuario usuarioLogado = (Usuario) c.uniqueResult();
//		return usuarioLogado == null ? new Usuario() : usuarioLogado;
//	}
//	
//	public Usuario porLogin(Usuario usuario) {
//		Criteria c = criaCriteria();
//		c.add(Restrictions.eq("usuario", usuario.getUsuario()));
//		Usuario usuarioLogado = (Usuario) c.uniqueResult();
//		return usuarioLogado == null ? new Usuario() : usuarioLogado;
//	}
//    
}
