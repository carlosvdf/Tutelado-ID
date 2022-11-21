package gei.id.tutelado;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.*;

public class ProdutorDatosCocinero {


	// Crea un conxunto de obxectos para utilizar nos casos de proba
	
	private EntityManagerFactory emf=null;
	
	public Empleado e0, e1;
	public List<Empleado> listaxeE;

	public Cocinero c0, c1;
	public List<Cocinero> listaxeC;
	public Plato p1, p2;
	public List<Plato> listaxeP;
	
	
	
	public void Setup (Configuracion config) {
		this.emf=(EntityManagerFactory) config.get("EMF");
	}


	public void creaCocinerosSoltos() {

		// Crea dous cocineros EN MEMORIA: c0, c1
		// SEN platos



		this.c0 = new Cocinero();
		this.c0.setNif("000A");
		this.c0.setNombre("Cocinero cero");
		this.c0.setApellido1("Cero");
		this.c0.setApellido2("Cero");
		this.c0.setTelefono("000000");


		this.c1 = new Cocinero();
		this.c1.setNif("111A");
		this.c1.setNombre("Cocinero uno");
		this.c1.setApellido1("Uno");
		this.c1.setApellido2("Uno");
		this.c1.setTelefono("111111");

		this.listaxeC = new ArrayList<Cocinero> ();
		this.listaxeC.add(0,c0);
		this.listaxeC.add(1,c1);

	}
	
	public void creaPlatosSoltos () {

		// Crea dous platos EN MEMORIA: p1, p2
		// Sen empleado asignado (momentaneamente)
		
		this.p1=new Plato();
		this.p1.setNombre("Macarrones con queso");
		this.p1.setTipo("Entrante");
		List<String> ingredientes1 = new ArrayList<>();
		ingredientes1.add(0, "Macarrones");
		ingredientes1.add(1, "Tomate frito");
		ingredientes1.add(2, "Queso en polvo");
		this.p1.setIngredientes(ingredientes1);

		this.p2=new Plato();
		this.p2.setNombre("Huevos rotos con jamon");
		this.p2.setTipo("Entrante");
		List<String> ingredientes2 = new ArrayList<>();
		ingredientes2.add(0, "Huevos");
		ingredientes2.add(1, "Patatas");
		ingredientes2.add(2, "Jamon");
		this.p2.setIngredientes(ingredientes2);

        this.listaxeP = new ArrayList<Plato> ();
        this.listaxeP.add(0,this.p1);
        this.listaxeP.add(1,this.p2);

	}

	public void gravaPlatos() {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Iterator<Plato> itP = this.listaxeP.iterator();
			while (itP.hasNext()) {
				Plato p = itP.next();
				em.persist(p);
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (e);
			}
		}
	}
	
	public void creaCocinerosConPlatos () {

		this.creaCocinerosSoltos();
		this.creaPlatosSoltos();
		
        this.c1.engadirPlato(p1);
		this.c1.engadirPlato(p2);

	}


	public void gravaCocineros() {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Iterator<Cocinero> itC = this.listaxeC.iterator();
			while (itC.hasNext()) {
				Cocinero c = itC.next();
				em.persist(c);
				// DESCOMENTAR SE A PROPAGACION DO PERSIST NON ESTA ACTIVADA
				/*
				Iterator<Plato> it<p = c.getPlatos().iterator();
				while (itP.hasNext()) {
					em.persist(itP.next());
				}
				*/
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (e);
			}
		}	
	}
	
	public void limpaBD () {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			
			Iterator <Cocinero> itC = em.createNamedQuery("Cocinero.recuperaTodos", Cocinero.class).getResultList().iterator();
			while (itC.hasNext()) em.remove(itC.next());
			Iterator <Plato> itP = em.createNamedQuery("Plato.recuperaTodos", Plato.class).getResultList().iterator();
			while (itP.hasNext()) em.remove(itP.next());

			
			em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idEmpleado'" ).executeUpdate();
			em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idPlato'" ).executeUpdate();

			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (e);
			}
		}
	}
	
	
}
