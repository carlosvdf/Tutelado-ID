package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cocinero;
import gei.id.tutelado.model.Empleado;
import gei.id.tutelado.model.Plato;
import gei.id.tutelado.model.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class CocineroDaoJPA implements CocineroDao{

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

            Usuario empleadoTmp = em.find (Usuario.class, empleado.getId());
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
    public List<Plato> recuperaPlatos(Cocinero cocinero) {
        List<Plato> platos=null;

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            platos = em.createNamedQuery("Cocinero.recuperaPlatos", Plato.class).setParameter("c", cocinero).getResultList();

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

        return (platos);
    }

    @Override
    public List<String> findByIngrediente(String ingrediente) {
        List<String> ingredientes=null;

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            ingredientes = em.createNamedQuery("Cocinero.findByIngrediente", String.class).setParameter("i", ingrediente).getResultList();

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

        return (ingredientes);
    }

}
