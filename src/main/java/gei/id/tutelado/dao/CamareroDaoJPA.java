package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Camarero;
import gei.id.tutelado.model.Empleado;

public class CamareroDaoJPA implements CamareroDao{
    
    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void setup(Configuracion config) {
        this.emf = (EntityManagerFactory) config.get("EMF");
    }

    @Override
    public Empleado almacena(Empleado empleado) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            em.persist(empleado);

            em.getTransaction().commit();
            em.close();

        } catch (Exception ex ) {
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return empleado;
    }

    @Override
    public Empleado modifica(Empleado empleado) {

        try {

            em = emf.createEntityManager();
            em.getTransaction().begin();

            empleado= em.merge (empleado);

            em.getTransaction().commit();
            em.close();

        } catch (Exception ex ) {
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return (empleado);
    }

    @Override
    public void elimina(Empleado empleado) {
        try {

            em = emf.createEntityManager();
            em.getTransaction().begin();

            Empleado empleadoTmp = em.find (Empleado.class, empleado.getId());
            em.remove (empleadoTmp);

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
    public List<Empleado> recuperaTodos() {
        return null;
    }

    @Override
    public Empleado recuperaPorNif(String nif) {
        List<Empleado> empleados=null;

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            empleados = em.createNamedQuery("Empleado.recuperaPorNif", Empleado.class).setParameter("nif", nif).getResultList();

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

        return (empleados.size()!=0?empleados.get(0):null);
    }

    @Override
	public List<Camarero> recuperaMesasJOIN() {
		List <Camarero> camareros=null;

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			camareros = em.createNamedQuery("Camarero.recuperaMesasJOIN", Camarero.class).getResultList(); 

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

		return camareros;
	}
}
