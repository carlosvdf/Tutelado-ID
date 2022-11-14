package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.CocineroDao;
import gei.id.tutelado.dao.CocineroDaoJPA;
import gei.id.tutelado.dao.UsuarioDao;
import gei.id.tutelado.dao.UsuarioDaoJPA;
import gei.id.tutelado.model.Cocinero;
import gei.id.tutelado.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R01_Empleados {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosProba produtorDatos = new ProdutorDatosProba();
    
    private static Configuracion cfg;
    private static CocineroDao cociDao;
    
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

    	cociDao = new CocineroDaoJPA();
    	cociDao.setup(cfg);
    	
    	produtorDatos = new ProdutorDatosProba();
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
    	
    	Cocinero c;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosSoltos();
    	produtorDatos.gravaCocineros();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de recuperación desde a BD de cocineros (sen entradas asociadas) por nif\n"
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperación por nif existente\n"
    			+ "\t\t\t\t b) Recuperacion por nif inexistente\n");

    	// Situación de partida:
    	// c0 desligado

    	log.info("Probando recuperacion por nif EXISTENTE --------------------------------------------------");

    	c = (Cocinero) cociDao.recuperaPorNif(produtorDatos.c0.getNif());
    	Assert.assertEquals(produtorDatos.c0.getNif(),      c.getNif());
    	Assert.assertEquals(produtorDatos.c0.getNombre(),     c.getNombre());
    	Assert.assertEquals(produtorDatos.c0.getApellido1(), c.getApellido1());
		Assert.assertEquals(produtorDatos.c0.getApellido2(), c.getApellido2());
		Assert.assertEquals(produtorDatos.c0.getTelefono(), c.getTelefono());



		log.info("");
		log.info("Probando recuperacion por nif INEXISTENTE -----------------------------------------------");
    	
    	c = (Cocinero) cociDao.recuperaPorNif("iwbvyhuebvuwebvi");
    	Assert.assertNull (c);

    } 	
/*
    @Test 
    public void test02_Alta() {

    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
  
		produtorDatos.creaUsuariosSoltos();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de gravación na BD de novo usuario (sen entradas de log asociadas)\n");
    	
    	// Situación de partida:
    	// u0 transitorio    	
    	
    	Assert.assertNull(produtorDatos.u0.getId());
    	usuDao.almacena(produtorDatos.u0);    	
    	Assert.assertNotNull(produtorDatos.u0.getId());
    }
    
    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosSoltos();
    	produtorDatos.gravaUsuarios();

    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación da BD de usuario sen entradas asociadas\n");   
 
    	// Situación de partida:
    	// u0 desligado  

    	Assert.assertNotNull(usuDao.recuperaPorNif(produtorDatos.u0.getNif()));
    	usuDao.elimina(produtorDatos.u0);    	
    	Assert.assertNull(usuDao.recuperaPorNif(produtorDatos.u0.getNif()));
    } 	

    @Test 
    public void test04_Modificacion() {
    	
    	Usuario u1, u2;
    	String novoNome;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosSoltos();
    	produtorDatos.gravaUsuarios();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificación da información básica dun usuario sen entradas de log\n");

    	// Situación de partida:
    	// u0 desligado  

		novoNome = new String ("Nome novo");

		u1 = usuDao.recuperaPorNif(produtorDatos.u0.getNif());
		Assert.assertNotEquals(novoNome, u1.getNome());
    	u1.setNome(novoNome);

    	usuDao.modifica(u1);    	
    	
		u2 = usuDao.recuperaPorNif(produtorDatos.u0.getNif());
		Assert.assertEquals (novoNome, u2.getNome());

    } 	
    
    @Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosSoltos();
    	usuDao.almacena(produtorDatos.u0);
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violación de restricións not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Gravación de usuario con nif duplicado\n"
    			+ "\t\t\t\t b) Gravación de usuario con nif nulo\n");

    	// Situación de partida:
    	// u0 desligado, u1 transitorio
    	
		log.info("Probando gravacion de usuario con Nif duplicado -----------------------------------------------");
    	produtorDatos.u1.setNif(produtorDatos.u0.getNif());
    	try {
        	usuDao.almacena(produtorDatos.u1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    	
    	// Nif nulo
    	log.info("");	
		log.info("Probando gravacion de usuario con Nif nulo ----------------------------------------------------");
    	produtorDatos.u1.setNif(null);
    	try {
        	usuDao.almacena(produtorDatos.u1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    } 	
    */
}