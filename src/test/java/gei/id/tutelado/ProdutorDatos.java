package gei.id.tutelado;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.*;

public class ProdutorDatos {
    
    // Crea un conxunto de obxectos para utilizar nos casos de proba
	
	private EntityManagerFactory emf=null;
	
	public Mesa m1, m2;
	public List<Mesa> listaxeM;

    public Camarero cam0, cam1;
	public List<Camarero> listaxeCam;

	public Cocinero coc0, coc1;
	public List<Cocinero> listaxeC;

	public Plato p1, p2;
	public List<Plato> listaxeP;
	
    public void Setup (Configuracion config) {
		this.emf=(EntityManagerFactory) config.get("EMF");
	}

    public void creaMesasSoltas() {

		// Crea dous mesas EN MEMORIA: m0, m1
		// SEN camarero
		
		this.m1 = new Mesa();
        this.m1.setCodigo("T000");
        this.m1.setTipo("Terraza");

        this.m2 = new Mesa();
        this.m2.setCodigo("C000");
        this.m2.setTipo("Comedor");

        this.listaxeM = new ArrayList<Mesa> ();
        this.listaxeM.add(0,m1);
        this.listaxeM.add(1,m2);        

	}

    public void creaCamarerosSoltos() {

		// Crea dous camareros EN MEMORIA: cam0, cam1
		// SEN mesas
		
		this.cam0 = new Camarero();
        this.cam0.setNif("0230X");
        this.cam0.setNombre("Pedro");
        this.cam0.setApellido1("Perez");
        this.cam0.setApellido2("Maroto");
        this.cam0.setTelefono("687009201");

        this.cam1 = new Camarero();
        this.cam1.setNif("9880C");
        this.cam1.setNombre("Leticia");
        this.cam1.setApellido1("Sabater");
        this.cam1.setApellido2("Molina");
        this.cam1.setTelefono("722419300");

        this.listaxeCam = new ArrayList<Camarero> ();
        this.listaxeCam.add(0,cam0);
        this.listaxeCam.add(1,cam1);        

	}

    public void creaCamarerosConMesas () {

		this.creaCamarerosSoltos();
		this.creaMesasSoltas();
		
        this.cam1.engadirMesa(this.m1);
        this.cam1.engadirMesa(this.m2);

	}

    public void gravaCamareros() {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Iterator<Camarero> itCam = this.listaxeCam.iterator();
			while (itCam.hasNext()) {
				Camarero c = itCam.next();
				em.persist(c);
				// DESCOMENTAR SE A PROPAGACION DO PERSIST NON ESTA ACTIVADA
				
				Iterator<Mesa> itM = c.getMesas().iterator();
				while (itM.hasNext()) {
					em.persist(itM.next());
				}
				
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

	public void creaCocinerosSoltos() {

		// Crea dous cocineros EN MEMORIA: c0, c1
		// SEN platos



		this.coc0 = new Cocinero();
		this.coc0.setNif("000A");
		this.coc0.setNombre("Cocinero cero");
		this.coc0.setApellido1("Cero");
		this.coc0.setApellido2("Cero");
		this.coc0.setTelefono("000000");


		this.coc1 = new Cocinero();
		this.coc1.setNif("111A");
		this.coc1.setNombre("Cocinero uno");
		this.coc1.setApellido1("Uno");
		this.coc1.setApellido2("Uno");
		this.coc1.setTelefono("111111");

		this.listaxeC = new ArrayList<Cocinero> ();
		this.listaxeC.add(0,coc0);
		this.listaxeC.add(1,coc1);

	}

	public void creaPlatosSoltos () {

		// Crea dous platos EN MEMORIA: p1, p2
		// Sen empleado asignado (momentaneamente)
		
		this.p1=new Plato();
		this.p1.setNombre("Macarrones con queso");
		List<String> ingredientes1 = new ArrayList<>();
		ingredientes1.add(0, "Macarrones");
		ingredientes1.add(1, "Tomate frito");
		ingredientes1.add(2, "Queso en polvo");
		this.p1.setIngredientes(ingredientes1);

		this.p2=new Plato();
		this.p2.setNombre("Huevos rotos con jamon");
		List<String> ingredientes2 = new ArrayList<>();
		ingredientes1.add(1, "Huevos");
		ingredientes1.add(2, "Patatas");
		ingredientes1.add(3, "Jamon");
		this.p2.setIngredientes(ingredientes2);

        this.listaxeP = new ArrayList<Plato> ();
        this.listaxeP.add(0,this.p1);
        this.listaxeP.add(1,this.p2);

	}

	public void creaCocinerosConPlatos () {

		this.creaCocinerosSoltos();
		this.creaPlatosSoltos();
		
        this.coc1.engadirPlato(p1);
		this.coc1.engadirPlato(p2);

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
			
			Iterator <Mesa> itM = em.createNamedQuery("Mesa.recuperaTodas", Mesa.class).getResultList().iterator();
			while (itM.hasNext()) em.remove(itM.next());
			Iterator <Empleado> itE = em.createNamedQuery("Empleado.recuperaTodos", Empleado.class).getResultList().iterator();
			while (itE.hasNext()) em.remove(itE.next());
			Iterator <Plato> itP = em.createNamedQuery("Plato.recuperaTodos", Plato.class).getResultList().iterator();
			while (itP.hasNext()) em.remove(itP.next());		

			
			em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idEmpleado'" ).executeUpdate();
			em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idMesa'" ).executeUpdate();
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
