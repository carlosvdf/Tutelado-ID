package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.CocineroDao;
import gei.id.tutelado.dao.CocineroDaoJPA;
import gei.id.tutelado.dao.EmpleadoDao;
import gei.id.tutelado.dao.EmpleadoDaoJPA;
import gei.id.tutelado.model.Cocinero;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R01_Cocineros {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosCocinero produtorDatos = new ProdutorDatosCocinero();
    
    private static Configuracion cfg;
    private static CocineroDao cociDao;
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

    	cociDao = new CocineroDaoJPA();
    	cociDao.setup(cfg);

		empDao = new EmpleadoDaoJPA();
		empDao.setup(cfg);
    	
    	produtorDatos = new ProdutorDatosCocinero();
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
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosSoltos();
    	produtorDatos.gravaCocineros();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de recuperaci??n desde a BD de cocineros (sen platos asociados) por nif\n"
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperaci??n por nif existente\n"
    			+ "\t\t\t\t b) Recuperacion por nif inexistente\n");

    	// Situaci??n de partida:
    	// c0 desligado

    	log.info("Probando recuperacion por nif EXISTENTE --------------------------------------------------");

    	c = (Cocinero) empDao.recuperaPorNif(produtorDatos.c0.getNif());
    	Assert.assertEquals(produtorDatos.c0.getNif(),      c.getNif());
    	Assert.assertEquals(produtorDatos.c0.getNombre(),     c.getNombre());
    	Assert.assertEquals(produtorDatos.c0.getApellido1(), c.getApellido1());
		Assert.assertEquals(produtorDatos.c0.getApellido2(), c.getApellido2());
		Assert.assertEquals(produtorDatos.c0.getTelefono(), c.getTelefono());



		log.info("");
		log.info("Probando recuperacion por nif INEXISTENTE -----------------------------------------------");
    	
    	c = (Cocinero) empDao.recuperaPorNif("iwbvyhuebvuwebvi");
    	Assert.assertNull (c);

    } 	

    @Test 
    public void test02_Alta() {

    	log.info("");	
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");
  
		produtorDatos.creaCocinerosSoltos();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de gravaci??n na BD dun novo coci??eiro (sen platos asociados)\n");
    	
    	// Situaci??n de partida:
    	// c0 transitorio
    	
    	Assert.assertNull(produtorDatos.c0.getId());
    	empDao.almacena(produtorDatos.c0);
    	Assert.assertNotNull(produtorDatos.c0.getId());
    }


    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosSoltos();
    	produtorDatos.gravaCocineros();

    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminaci??n da BD do coci??eiro sen platos asociados\n");
 
    	// Situaci??n de partida:
    	// c0 desligado

    	Assert.assertNotNull(empDao.recuperaPorNif(produtorDatos.c0.getNif()));
    	empDao.elimina(produtorDatos.c0);
    	Assert.assertNull(empDao.recuperaPorNif(produtorDatos.c0.getNif()));
    } 	


    @Test 
    public void test04_Modificacion() {
    	
    	Cocinero c1, c2;
    	String novoNome;
    	
    	log.info("");	
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosSoltos();
    	produtorDatos.gravaCocineros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificaci??n da informaci??n b??sica dun coci??eiro sen platos\n");

    	// Situaci??n de partida:
    	// c0 desligado

		novoNome = new String ("Nome novo");

		c1 = (Cocinero) empDao.recuperaPorNif(produtorDatos.c0.getNif());
		Assert.assertNotEquals(novoNome, c1.getNombre());
    	c1.setNombre(novoNome);

    	empDao.modifica(c1);
    	
		c2 = (Cocinero) empDao.recuperaPorNif(produtorDatos.c0.getNif());
		Assert.assertEquals (novoNome, c2.getNombre());

    } 	

	@Test 
    public void test06_CocineroIngrediente() {

    	List<Cocinero> listaC;   
		String ingrediente = "Huevos"; 	

    	log.info("");	
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosConPlatos();
    	produtorDatos.gravaCocineros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da consulta Cocinero.findByIngrediente\n");   


		listaC = cociDao.findByIngrediente(ingrediente);
		
		System.out.println(listaC);
		Assert.assertEquals(1, listaC.size());

    }

    @Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situaci??n de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosSoltos();
    	empDao.almacena(produtorDatos.c0);
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violaci??n de restrici??ns not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Gravaci??n de coci??eiro con nif duplicado\n"
    			+ "\t\t\t\t b) Gravaci??n de coci??eiro con nif nulo\n");

    	// Situaci??n de partida:
    	// c0 desligado, c1 transitorio
    	
		log.info("Probando gravacion de cocinero con Nif duplicado -----------------------------------------------");
    	produtorDatos.c1.setNif(produtorDatos.c0.getNif());
    	try {
        	empDao.almacena(produtorDatos.c1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    	
    	// Nif nulo
    	log.info("");	
		log.info("Probando gravacion de coci??eiro con Nif nulo ----------------------------------------------------");
    	produtorDatos.c1.setNif(null);
    	try {
        	empDao.almacena(produtorDatos.c1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    } 	

}