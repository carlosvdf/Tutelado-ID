package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Mesa;
import gei.id.tutelado.model.Camarero;

public class MesaDaoJPA implements MesaDao{

    private EntityManagerFactory emf; 
	private EntityManager em;
    
	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}

    @Override
	public Mesa almacena(Mesa mesa) {
		try {
				
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.persist(mesa);

			em.getTransaction().commit();
			em.close();
		
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return mesa;
	}

    @Override
	public Mesa modifica(Mesa mesa) {
		try {

			em = emf.createEntityManager();		
			em.getTransaction().begin();

			mesa = em.merge (mesa);

			em.getTransaction().commit();
			em.close();
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return mesa;
	}

    @Override
	public void elimina(Mesa mesa) {
		try {

			em = emf.createEntityManager();
			em.getTransaction().begin();

			Mesa mesaTmp = em.find (Mesa.class, mesa.getId());
			em.remove (mesaTmp);

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
	public Mesa recuperaPorCodigo(String codigo) {

		List<Mesa> mesas=null;
		
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			mesas = em.createNamedQuery("Mesa.recuperaPorCodigo", Mesa.class)
					.setParameter("codigo", codigo).getResultList(); 

			em.getTransaction().commit();
			em.close();
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (mesas.size()==0?null:mesas.get(0));
	}
    

}
