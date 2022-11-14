package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Camarero;
import gei.id.tutelado.model.Mesa;

public class CamareroDaoJPA implements CamareroDao{
    
    private EntityManagerFactory emf; 
	private EntityManager em;
    
	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}

    @Override
	public List<Mesa> recuperaMesasCamarero(Camarero c) {
		List <Mesa> mesas=null;

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			mesas = em.createNamedQuery("Camarero.recuperaMesasJOIN", Mesa.class).setParameter("c", c).getResultList(); 

			em.getTransaction().commit();
			em.close();	

		}
		catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}

		return mesas;
	}
}
