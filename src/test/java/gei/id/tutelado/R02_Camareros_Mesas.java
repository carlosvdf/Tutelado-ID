package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.MesaDao;
import gei.id.tutelado.dao.MesaDaoJPA;
import gei.id.tutelado.dao.CamareroDao;
import gei.id.tutelado.dao.CamareroDaoJPA;
import gei.id.tutelado.model.Mesa;
import gei.id.tutelado.model.Camarero;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.lang.Exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R02_Camareros_Mesas {
    
    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatos produtorDatos = new ProdutorDatos();
    
    private static Configuracion cfg;
    private static CamareroDao camDao;
    private static MesaDao mesaDao;

    @Rule
    public TestRule watcher = new TestWatcher() {
       protected void starting(Description description) {
    	   log.info("");
    	   log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    	   log.info("Iniciando test: " + description.getMethodName());
    	   log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
       }
       protected void finished(Description description) {
    	   log.info("");
    	   log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
    	   log.info("Finalizado test: " + description.getMethodName());
    	   log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
       }
    };

    @BeforeClass
    public static void init() throws Exception {
    	cfg = new ConfiguracionJPA();
    	cfg.start();

    	camDao = new CamareroDaoJPA();
    	mesaDao = new MesaDaoJPA();
    	camDao.setup(cfg);
    	mesaDao.setup(cfg);
    	
    	produtorDatos = new ProdutorDatos();
    	produtorDatos.Setup(cfg);
    }
    
    @AfterClass
    public static void endclose() throws Exception {
    	cfg.endUp();    	
    }

    @Before
	public void setUp() throws Exception {		
		log.info("");	
		log.info("Limpando BD -----------------------------------------------------------------------------------------------------");
		produtorDatos.limpaBD();
	}

    @After
	public void tearDown() throws Exception {
	}

    @Test 
    public void test01_Recuperacion() {
   	
    	Mesa m;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosConMesas();
    	produtorDatos.gravaCamareros();


		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación (por codigo) de mesas soltas\n"   
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación por codigo existente\n"
		+ "\t\t\t\t b) Recuperacion por codigo inexistente\n");     	

    	// Situación de partida:
    	// cam1, m1, m2 desligados
    	
		log.info("Probando recuperacion por codigo EXISTENTE --------------------------------------------------");

        m = mesaDao.recuperaPorCodigo(produtorDatos.m1.getCodigo());

    	Assert.assertEquals (produtorDatos.m1.getCodigo(),     m.getCodigo());
    	Assert.assertEquals (produtorDatos.m1.getTipo(), m.getTipo());

    	log.info("");	
		log.info("Probando recuperacion por codigo INEXISTENTE --------------------------------------------------");
    	
    	m = mesaDao.recuperaPorCodigo("iwbvyhuebvuwebvi");
    	Assert.assertNull (m);

    }
    
    @Test
    public void test02_Alta() {


    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosSoltos();
    	produtorDatos.gravaCamareros();
    	produtorDatos.creaMesasSoltas();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da gravación de entradas de log soltas\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Primeira entrada de log vinculada a un usuario\n"
    			+ "\t\t\t\t b) Nova entrada de log para un usuario con entradas previas\n");     	

    	// Situación de partida:
    	// cam1 desligado    	
    	// m1, m2 transitorios

    	produtorDatos.cam1.engadirMesa(produtorDatos.m1);
		
    	log.info("");	
		log.info("Gravando primeira mesa dun camarero --------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.m1.getId());
    	mesaDao.almacena(produtorDatos.m1);
    	Assert.assertNotNull(produtorDatos.m1.getId());

    	produtorDatos.cam1.engadirMesa(produtorDatos.m2);

    	log.info("");	
		log.info("Gravando segunda mesa dun camarero ---------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.m2.getId());
    	mesaDao.almacena(produtorDatos.m2);
    	Assert.assertNotNull(produtorDatos.m2.getId());

    }
     
    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

    	produtorDatos.creaCamarerosConMesas();
    	produtorDatos.gravaCamareros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación de mesa solta (asignada a camarero)\n");
    	
    	// Situación de partida:
    	// m0 desligado

		Assert.assertNotNull(mesaDao.recuperaPorCodigo(produtorDatos.m1.getCodigo()));
    	mesaDao.elimina(produtorDatos.m1);    	
		Assert.assertNull(mesaDao.recuperaPorCodigo(produtorDatos.m1.getCodigo()));

    }
     
    @Test 
    public void test04_Modificacion() {

    	Mesa m1, m2;
    	String novoTipo;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
  
		produtorDatos.creaCamarerosConMesas();
    	produtorDatos.gravaCamareros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificación da información dunha mesa solta\n");
 
    	
    	// Situación de partida:
    	// e1A desligado
    	
		novoTipo = new String ("Cafeteria");

		m1 = mesaDao.recuperaPorCodigo(produtorDatos.m1.getCodigo());

		Assert.assertNotEquals(novoTipo, m1.getTipo());
    	m1.setTipo(novoTipo);

    	mesaDao.modifica(m1);    	
    	
		m2 = mesaDao.recuperaPorCodigo(produtorDatos.m1.getCodigo());
		Assert.assertEquals (novoTipo, m2.getTipo());

    	// NOTA: Non probamos modificación de usuario da entrada porque non ten sentido no dominio considerado
        // Hay que probar la modificación de camarero 
    }
    @Test 
    public void test07_EAGER() {
    	
    	Mesa m;
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosConMesas();
    	produtorDatos.gravaCamareros();

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación de propiedades EAGER\n");   

    	// Situación de partida:
    	// cam1, m1, m2 desligados
    	
		log.info("Probando (que non hai excepcion tras) acceso inicial a propiedade EAGER fora de sesion ----------------------------------------");
    	
    	m = mesaDao.recuperaPorCodigo(produtorDatos.m1.getCodigo());  
		log.info("Acceso a camarero de mesa");
    	try	{
        	Assert.assertEquals(produtorDatos.cam1, m.getCamarero());
        	excepcion=false;
    	} catch (LazyInitializationException ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	};    	
    	Assert.assertFalse(excepcion);    
    }
}
