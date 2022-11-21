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
        // Devolve o obxecto user coa coleccion de platos cargada (se non o estaba xa)

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            try {
                cocinero.getPlatos().size();
            } catch (Exception ex2) {
                if (ex2 instanceof LazyInitializationException)

                {
                    /* OPCION DE IMPLEMENTACION 1 (comentada): Cargar a propiedade "manualmente" cunha consulta,
                     *  e actualizar tamen "manualmente" o valor da propiedade  */
                    //List<EntradaLog> entradas = (List<EntradaLog>) entityManager.createQuery("From EntradaLog l where l.usuario=:usuario order by dataHora").setParameter("usuario",user).getResultList();
                    //user.setEntradasLog (entradas);

                    /* OPCION DE IMPLEMENTACIÓN 2: Volver a ligar o obxecto usuario a un novo CP,
                     * e acceder á propiedade nese momento, para que Hibernate a cargue.*/
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
