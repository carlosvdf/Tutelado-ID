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
	
	public Mesa m0, m1;
	public List<Mesa> listaxeM;

    public Camarero c0, c1;
	public List<Camarero> listaxeCam;
	
    public void Setup (Configuracion config) {
		this.emf=(EntityManagerFactory) config.get("EMF");
	}

    public void creaMesasSueltas() {

		// Crea dous mesas EN MEMORIA: m0, m1
		// SEN camarero
		
		this.m0 = new Mesa();
        this.m0.setCodigo("T000");
        this.m0.setTipo("Terraza");

        this.m1 = new Mesa();
        this.m1.setCodigo("C000");
        this.m1.setTipo("Comedor");

        this.listaxeM = new ArrayList<Mesa> ();
        this.listaxeM.add(0,m0);
        this.listaxeM.add(1,m1);        

	}

    public void creaCamarerosSueltos() {

		// Crea dous camareros EN MEMORIA: c0, c1
		// SEN mesas
		
		this.c0 = new Camarero();
        this.c0.setNif("0230X");
        this.c0.setNombre("Pedro");
        this.c0.setApellido1("Perez");
        this.c0.setApellido2("Maroto");
        this.c0.setTelefono("687009201");

        this.c1 = new Camarero();
        this.c1.setNif("9880C");
        this.c0.setNombre("Leticia");
        this.c0.setApellido1("Sabater");
        this.c0.setApellido2("Molina");
        this.c0.setTelefono("722419300");

        this.listaxeCam = new ArrayList<Camarero> ();
        this.listaxeCam.add(0,c0);
        this.listaxeCam.add(1,c1);        

	}

    public void creaCamarerosConMesas () {

		this.creaCamarerosSueltos();
		this.creaMesasSueltas();
		
        this.c1.engadirMesa(this.m0);
        this.c1.engadirMesa(this.m1);

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

    public void limpaBD () {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			
			Iterator <Empleado> itE = em.createNamedQuery("Empleado.recuperaTodos", Empleado.class).getResultList().iterator();
			while (itE.hasNext()) em.remove(itE.next());
			Iterator <Mesa> itM = em.createNamedQuery("Mesa.recuperaTodas", Mesa.class).getResultList().iterator();
			while (itM.hasNext()) em.remove(itM.next());		

			
			em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idEmpleado'" ).executeUpdate();
			em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idMesa'" ).executeUpdate();

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
