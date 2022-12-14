package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.CamareroDao;
import gei.id.tutelado.dao.CamareroDaoJPA;
import gei.id.tutelado.dao.EmpleadoDao;
import gei.id.tutelado.dao.EmpleadoDaoJPA;
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

    private static ProdutorDatosCamareros produtorDatos = new ProdutorDatosCamareros();
    
    private static Configuracion cfg;
    private static CamareroDao camDao;

	private static EmpleadoDao empDao;
    
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

		empDao = new EmpleadoDaoJPA();
		empDao.setup(cfg);

    	produtorDatos = new ProdutorDatosCamareros();
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
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosSoltos();
    	produtorDatos.gravaCamareros();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de recuperaci??n desde a BD de camareros (sen mesas asociadas) por nif\n"
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperaci??n por nif existente\n"
    			+ "\t\t\t\t b) Recuperacion por nif inexistente\n");

    	// Situaci??n de partida:
    	// cam0 desligado

    	log.info("Probando recuperacion por nif EXISTENTE --------------------------------------------------");

    	c = (Camarero) empDao.recuperaPorNif(produtorDatos.cam0.getNif());
    	Assert.assertEquals(produtorDatos.cam0.getNif(),      c.getNif());
    	Assert.assertEquals(produtorDatos.cam0.getNombre(),     c.getNombre());
    	Assert.assertEquals(produtorDatos.cam0.getApellido1(), c.getApellido1());
		Assert.assertEquals(produtorDatos.cam0.getApellido2(), c.getApellido2());
		Assert.assertEquals(produtorDatos.cam0.getTelefono(), c.getTelefono());



		log.info("");
		log.info("Probando recuperacion por nif INEXISTENTE -----------------------------------------------");
    	
    	c = (Camarero) empDao.recuperaPorNif("iwbvyhuebvuwebvi");
    	Assert.assertNull (c);

    }

    @Test 
    public void test02_Alta() {

    	log.info("");	
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");
  
		produtorDatos.creaCamarerosSoltos();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de gravaci??n na BD de novo camarero (sen mesas asociadas)\n");
    	
    	// Situaci??n de partida:
    	// cam0 transitorio
    	
    	Assert.assertNull(produtorDatos.cam0.getId());
    	empDao.almacena(produtorDatos.cam0);
    	Assert.assertNotNull(produtorDatos.cam0.getId());
    }

    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosSoltos();
    	produtorDatos.gravaCamareros();

    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminaci??n da BD de camarero sen mesas asociadas\n");
 
    	// Situaci??n de partida:
    	// cam0 desligado

    	Assert.assertNotNull(empDao.recuperaPorNif(produtorDatos.cam0.getNif()));
    	empDao.elimina(produtorDatos.cam0);
    	Assert.assertNull(empDao.recuperaPorNif(produtorDatos.cam0.getNif()));
    }

    @Test 
    public void test04_Modificacion() {
    	
    	Camarero c1, c2;
    	String novoNome;
    	
    	log.info("");	
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosSoltos();
    	produtorDatos.gravaCamareros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificaci??n da informaci??n b??sica dun camarero sen mesas\n");

    	// Situaci??n de partida:
    	// cam0 desligado

		novoNome = new String ("Nome novo");

		c1 = (Camarero) empDao.recuperaPorNif(produtorDatos.cam0.getNif());
		Assert.assertNotEquals(novoNome, c1.getNombre());
    	c1.setNombre(novoNome);

    	empDao.modifica(c1);
    	
		c2 = (Camarero) empDao.recuperaPorNif(produtorDatos.cam0.getNif());
		Assert.assertEquals (novoNome, c2.getNombre());

    }

    @Test 
    public void test06_todosCamMesas() {

    	List<Camarero> listaCam;    	

    	log.info("");	
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosConMesas();
    	produtorDatos.gravaCamareros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da consulta Camarero.recuperaMesasJOIN\n");   

    	// Situaci??n de partida:
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
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosConMesas();
    	produtorDatos.gravaCamareros();

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperaci??n de propiedades EAGER\n");   

    	// Situaci??n de partida:
    	// cam0, m1, m2 desligadas
    	
		log.info("Probando (que non hai excepcion tras) acceso inicial a propiedade EAGER fora de sesion ----------------------------------------");
    	
    	c = (Camarero)empDao.recuperaPorNif(produtorDatos.cam1.getNif());
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
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCamarerosSoltos();
    	empDao.almacena(produtorDatos.cam0);
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violaci??n de restrici??ns not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Gravaci??n de camarero con nif duplicado\n"
    			+ "\t\t\t\t b) Gravaci??n de camarero con nif nulo\n");

    	// Situaci??n de partida:
    	// cam0 desligado, cam1 transitorio
    	
		log.info("Probando gravacion de camarero con Nif duplicado -----------------------------------------------");
    	produtorDatos.cam1.setNif(produtorDatos.cam0.getNif());
    	try {
        	empDao.almacena(produtorDatos.cam1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    	
    	// Nif nulo
    	log.info("");	
		log.info("Probando gravacion de camarero con Nif nulo ----------------------------------------------------");
    	produtorDatos.cam1.setNif(null);
    	try {
        	empDao.almacena(produtorDatos.cam1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    }
}
