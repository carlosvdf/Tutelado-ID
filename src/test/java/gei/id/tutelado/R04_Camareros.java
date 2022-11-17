package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.CamareroDao;
import gei.id.tutelado.dao.CamareroDaoJPA;
import gei.id.tutelado.model.Camarero;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import org.hibernate.LazyInitializationException;
import java.lang.Exception;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R04_Camareros {
    
    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatos produtorDatos = new ProdutorDatos();
    
    private static Configuracion cfg;
    private static CamareroDao camDao;
    
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
    	camDao.setup(cfg);
    	
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
		log.info("Limpando BD --------------------------------------------------------------------------------------------");
		produtorDatos.limpaBD();
	}

	@After
	public void tearDown() throws Exception {
	}

    @Test
	public void test01_Recuperacion() {
    	
    	Camarero c;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosSoltos();
    	produtorDatos.gravaCamareros();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de recuperación desde a BD de camareros (sen mesas asociadas) por nif\n"
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperación por nif existente\n"
    			+ "\t\t\t\t b) Recuperacion por nif inexistente\n");

    	// Situación de partida:
    	// cam0 desligado

    	log.info("Probando recuperacion por nif EXISTENTE --------------------------------------------------");

    	c = (Camarero) camDao.recuperaPorNif(produtorDatos.cam0.getNif());
    	Assert.assertEquals(produtorDatos.cam0.getNif(),      c.getNif());
    	Assert.assertEquals(produtorDatos.cam0.getNombre(),     c.getNombre());
    	Assert.assertEquals(produtorDatos.cam0.getApellido1(), c.getApellido1());
		Assert.assertEquals(produtorDatos.cam0.getApellido2(), c.getApellido2());
		Assert.assertEquals(produtorDatos.cam0.getTelefono(), c.getTelefono());



		log.info("");
		log.info("Probando recuperacion por nif INEXISTENTE -----------------------------------------------");
    	
    	c = (Camarero) camDao.recuperaPorNif("iwbvyhuebvuwebvi");
    	Assert.assertNull (c);

    }

    @Test 
    public void test02_Alta() {

    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
  
		produtorDatos.creaCamarerosSoltos();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de gravación na BD de novo camarero (sen mesas asociadas)\n");
    	
    	// Situación de partida:
    	// cam0 transitorio
    	
    	Assert.assertNull(produtorDatos.cam0.getId());
    	camDao.almacena(produtorDatos.cam0);
    	Assert.assertNotNull(produtorDatos.cam0.getId());
    }

    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosSoltos();
    	produtorDatos.gravaCamareros();

    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación da BD de camarero sen mesas asociadas\n");
 
    	// Situación de partida:
    	// cam0 desligado

    	Assert.assertNotNull(camDao.recuperaPorNif(produtorDatos.cam0.getNif()));
    	camDao.elimina(produtorDatos.cam0);
    	Assert.assertNull(camDao.recuperaPorNif(produtorDatos.cam0.getNif()));
    }

    @Test 
    public void test04_Modificacion() {
    	
    	Camarero c1, c2;
    	String novoNome;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosSoltos();
    	produtorDatos.gravaCamareros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificación da información básica dun camarero sen mesas\n");

    	// Situación de partida:
    	// c0 desligado

		novoNome = new String ("Nome novo");

		c1 = (Camarero) camDao.recuperaPorNif(produtorDatos.cam0.getNif());
		Assert.assertNotEquals(novoNome, c1.getNombre());
    	c1.setNombre(novoNome);

    	camDao.modifica(c1);
    	
		c2 = (Camarero) camDao.recuperaPorNif(produtorDatos.cam0.getNif());
		Assert.assertEquals (novoNome, c2.getNombre());

    }

    @Test 
    public void test06_todosCamMesas() {

    	List<Camarero> listaCam;    	

    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosConMesas();
    	produtorDatos.gravaCamareros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da consulta Camarero.recuperaMesasJOIN\n");   

    	// Situación de partida:
    	// cam0, m1, m2 desligados

		listaCam = camDao.recuperaMesasJOIN();
		System.out.println(listaCam);
		Assert.assertEquals(2, listaCam.size());
		Assert.assertEquals(produtorDatos.cam0, listaCam.get(0));
		Assert.assertEquals(produtorDatos.cam1, listaCam.get(1));

    }

    @Test 
    public void test07_EAGER() {
    	
    	Camarero c;
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosConMesas();
    	produtorDatos.gravaCamareros();

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación de propiedades EAGER\n");   

    	// Situación de partida:
    	// cam0, m1, m2 desligadas
    	
		log.info("Probando (que non hai excepcion tras) acceso inicial a propiedade EAGER fora de sesion ----------------------------------------");
    	
    	c = (Camarero)camDao.recuperaPorNif(produtorDatos.cam1.getNif());  
		log.info("Acceso a mesas de camarero");
    	try	{
            for(int i = 0 ; i< produtorDatos.cam1.getMesas().size(); i++){
                Assert.assertEquals(produtorDatos.cam1.getMesas().get(i), c.getMesas().get(i));
            }
        	excepcion=false;
    	} catch (LazyInitializationException ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	};    	
    	Assert.assertFalse(excepcion);    
    }

    @Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosSoltos();
    	camDao.almacena(produtorDatos.cam0);
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violación de restricións not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Gravación de usuario con nif duplicado\n"
    			+ "\t\t\t\t b) Gravación de usuario con nif nulo\n");

    	// Situación de partida:
    	// cam0 desligado, cam1 transitorio
    	
		log.info("Probando gravacion de usuario con Nif duplicado -----------------------------------------------");
    	produtorDatos.cam1.setNif(produtorDatos.cam0.getNif());
    	try {
        	camDao.almacena(produtorDatos.cam1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    	
    	// Nif nulo
    	log.info("");	
		log.info("Probando gravacion de usuario con Nif nulo ----------------------------------------------------");
    	produtorDatos.cam1.setNif(null);
    	try {
        	camDao.almacena(produtorDatos.cam1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    }
}
