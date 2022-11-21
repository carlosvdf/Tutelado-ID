package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cocinero;
import gei.id.tutelado.model.Empleado;
import gei.id.tutelado.model.Plato;
import org.hibernate.LazyInitializationException;

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
    public Cocinero restauraPlatos(Cocinero cocinero) {
        // Devolve o obxecto cocinero coa coleccion de platos cargada (se non o estaba xa)

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            try {
                cocinero.getPlatos().size();
            } catch (Exception ex2) {
                if (ex2 instanceof LazyInitializationException)

                {
                    // Ligamos o obxecto cocinero a un novo CP
                    cocinero = em.merge(cocinero);
                    cocinero.getPlatos().size();

                } else {
                    throw ex2;
                }
            }
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

        return (cocinero);

    }
    @Override
    public List<Plato> recuperaPlatos(Cocinero cocinero) {
        //devolve a lista de platos asigada ao cociñeiro que lle pasamos
        List<Plato> platos=null;

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            platos = em.createNamedQuery("Cocinero.recuperaPlatos", Plato.class).setParameter("cocinero", cocinero).getResultList();

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
    public List<Cocinero> findByIngrediente(String ingrediente) {
        //Devolve a lista de cociñeiros que traballan co ingrediente que lle pasamos
        List<Cocinero> cocineros=null;

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            cocineros = em.createNamedQuery("Cocinero.findByIngrediente", Cocinero.class).setParameter("ingrediente", ingrediente).getResultList();

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

        return cocineros;
    }

}
