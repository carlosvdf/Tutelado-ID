package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Plato;

public class PlatoDaoJPA implements PlatoDao{
    
    private EntityManagerFactory emf; 
	private EntityManager em;
    
	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}

    @Override
	public Plato almacena(Plato plato) {
		try {
				
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.persist(plato);

			em.getTransaction().commit();
			em.close();
		
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return plato;
	}

    @Override
	public Plato modifica(Plato plato) {
		try {

			em = emf.createEntityManager();		
			em.getTransaction().begin();

			plato = em.merge (plato);

			em.getTransaction().commit();
			em.close();
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return plato;
	}

    @Override
	public void elimina(Plato plato) {
		try {

			em = emf.createEntityManager();
			em.getTransaction().begin();

			Plato platoTmp = em.find (Plato.class, plato.getId());
			em.remove (platoTmp);

			em.getTransaction().commit();
			em.close();
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
	}

    @Override
	public Plato recuperaPorNombre(String nombre) {

		List<Plato> platos=null;
		
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			platos = em.createNamedQuery("Plato.recuperaPorNombre", Plato.class)
					.setParameter("nombre", nombre).getResultList(); 

			em.getTransaction().commit();
			em.close();
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (platos.size()==0?null:platos.get(0));
	}
}
