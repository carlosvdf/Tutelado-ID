package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.*;
import gei.id.tutelado.model.Cocinero;
import gei.id.tutelado.model.Plato;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R03_Cocineros_Platos {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosCocinero produtorDatos = new ProdutorDatosCocinero();
    
    private static Configuracion cfg;
    private static CocineroDao cociDao;
	private static EmpleadoDao empDao;
    private static PlatoDao platoDao;
    
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
    	platoDao = new PlatoDaoJPA();
    	cociDao.setup(cfg);
    	platoDao.setup(cfg);

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
		log.info("Limpando BD -----------------------------------------------------------------------------------------------------");
		produtorDatos.limpaBD();
	}

	@After
	public void tearDown() throws Exception {
	}	

    @Test 
    public void test01_Recuperacion() {
   	
    	Plato p;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosConPlatos();
    	produtorDatos.gravaCocineros();


		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación (por nome) de platos soltos\n"
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación por nome existente\n"
		+ "\t\t\t\t b) Recuperacion por nome inexistente\n");

    	// Situación de partida:
    	// c1, p1, p2 desligados
    	
		log.info("Probando recuperacion por nome EXISTENTE --------------------------------------------------");

    	p = platoDao.recuperaPorNombre(produtorDatos.p1.getNombre());

    	Assert.assertEquals (produtorDatos.p1.getNombre(),     p.getNombre());
    	Assert.assertEquals (produtorDatos.p1.getTipo(), p.getTipo());

		for(int i= 0; i<p.getIngredientes().size();i++){
			Assert.assertEquals (produtorDatos.p1.getIngredientes().get(i),   p.getIngredientes().get(i));
		}

    	log.info("");	
		log.info("Probando recuperacion por nome INEXISTENTE --------------------------------------------------");
    	
    	p = platoDao.recuperaPorNombre("iwbvyhuebvuwebvi");
    	Assert.assertNull (p);

    }

    @Test
	public void test02_Alta() {


    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosSoltos();
    	produtorDatos.gravaCocineros();
    	produtorDatos.creaPlatosSoltos();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da gravación de platos soltos\n"
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Primeiro plato vinculado a un cociñeiro\n"
				+ "\t\t\t\t b) Novo plato para un cociñeiro con platos previos\n");

    	// Situación de partida:
    	// c1 desligado
    	// p1, p2 transitorios

    	produtorDatos.c1.engadirPlato(produtorDatos.p1);
		
    	log.info("");	
		log.info("Gravando primeiro plato dun cociñeiro --------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.p1.getId());
    	platoDao.almacena(produtorDatos.p1);
    	Assert.assertNotNull(produtorDatos.p1.getId());

    	produtorDatos.c1.engadirPlato(produtorDatos.p2);

    	log.info("");	
		log.info("Gravando segundo plato dun cociñeiro ---------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.p2.getId());
    	platoDao.almacena(produtorDatos.p2);
    	Assert.assertNotNull(produtorDatos.p2.getId());

    }

    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaPlatosSoltos();
		produtorDatos.gravaPlatos();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación dun plato solto\n");
    	


		Assert.assertNotNull(platoDao.recuperaPorNombre(produtorDatos.p2.getNombre()));
    	platoDao.elimina(produtorDatos.p2);
		Assert.assertNull(platoDao.recuperaPorNombre(produtorDatos.p2.getNombre()));

    } 	

    @Test 
    public void test04_Modificacion() {

    	Plato p1, p2;
    	String novoTipo;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
  
		produtorDatos.creaCocinerosConPlatos();
    	produtorDatos.gravaCocineros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificación da información dun plato solto\n");
 
    	
    	// Situación de partida:
    	// p1 desligado
    	
		novoTipo = new String ("Segundo");

		p1 = platoDao.recuperaPorNombre(produtorDatos.p1.getNombre());

		Assert.assertNotEquals(novoTipo, p1.getTipo());
    	p1.setTipo(novoTipo);

    	platoDao.modifica(p1);
    	
		p2 = platoDao.recuperaPorNombre(produtorDatos.p1.getNombre());
		Assert.assertEquals (novoTipo, p2.getTipo());


    } 	

    @Test 
    public void test05_Propagacion_Persist() {

   	    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosSoltos();
    	produtorDatos.creaPlatosSoltos();
    	produtorDatos.c1.engadirPlato(produtorDatos.p1);
    	produtorDatos.c1.engadirPlato(produtorDatos.p2);


    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da gravación de novo cocinero con platos (novos) asociados\n");

    	// Situación de partida:
    	// c1, p1, p2 transitorios

    	Assert.assertNull(produtorDatos.c1.getId());
    	Assert.assertNull(produtorDatos.p1.getId());
    	Assert.assertNull(produtorDatos.p2.getId());
    	
		log.info("Gravando na BD o cociñeiro con platos asociados ----------------------------------------------------------------------");

    	// Aqui o persist sobre c1 debe propagarse a p1 e p2
		empDao.almacena(produtorDatos.c1);

		Assert.assertNotNull(produtorDatos.c1.getId());
    	Assert.assertNotNull(produtorDatos.p1.getId());
    	Assert.assertNotNull(produtorDatos.p2.getId());
    }

    @Test 
    public void test05_Propagacion_Remove() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
   	
    	produtorDatos.creaCocinerosConPlatos();
    	produtorDatos.gravaCocineros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación de cociñeiro con platos asociados\n");

    	// Situación de partida:
    	// c1, p1, p2 desligados

    	Assert.assertNotNull(empDao.recuperaPorNif(produtorDatos.c1.getNif()));
		Assert.assertNotNull(platoDao.recuperaPorNombre(produtorDatos.p1.getNombre()));
		Assert.assertNotNull(platoDao.recuperaPorNombre(produtorDatos.p2.getNombre()));
		
		// Aqui o remove sobre c1 debe propagarse a p1 e p2
		empDao.elimina(produtorDatos.c1);
		
		Assert.assertNull(empDao.recuperaPorNif(produtorDatos.c1.getNif()));
		Assert.assertNull(platoDao.recuperaPorNombre(produtorDatos.p1.getNombre()));
		Assert.assertNull(platoDao.recuperaPorNombre(produtorDatos.p2.getNombre()));

    }

	@Test 
    public void test06_mediaIngredientesPlato() {

    	Double media;    	

    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosConPlatos();
    	produtorDatos.gravaCocineros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da consulta Plato.recuperaMediaIngredientes\n");

		media = platoDao.recuperaMediaIngredientes();
		
		Assert.assertEquals((Double)3.0, media);
    }

	@Test 
    public void test07_platosCocinero() {

    	List<Plato> listaP;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosConPlatos();
    	produtorDatos.gravaCocineros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da consulta Cocinero.recuperaPlatos\n");   


		listaP = cociDao.recuperaPlatos(produtorDatos.c0);
		Assert.assertEquals(0, listaP.size());
		
		listaP = cociDao.recuperaPlatos(produtorDatos.c1);
		Assert.assertEquals(2, listaP.size());
		Assert.assertEquals(produtorDatos.p1.getNombre(), listaP.get(1).getNombre());
		Assert.assertEquals(produtorDatos.p2.getNombre(), listaP.get(0).getNombre());

    }

    @Test 
    public void test08_LAZY() {
    	
    	Cocinero c;
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosConPlatos();
    	produtorDatos.gravaCocineros();

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación de propiedades LAZY\n"   
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación de cociñeiro con colección (LAZY) de platos \n"
		+ "\t\t\t\t b) Carga forzada de colección LAZY da dita coleccion\n");

    	// Situación de partida:
    	// c1, p1, p2 desligados
    	
		log.info("Probando (excepcion tras) recuperacion LAZY ---------------------------------------------------------------------");
    	
    	c = (Cocinero) empDao.recuperaPorNif(produtorDatos.c1.getNif());
		log.info("Acceso a platos");
    	try	{
        	Assert.assertEquals(2, c.getPlatos().size());
        	Assert.assertEquals(produtorDatos.p1, c.getPlatos().first());
        	Assert.assertEquals(produtorDatos.p2, c.getPlatos().last());
        	excepcion=false;
    	} catch (LazyInitializationException ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	};    	
    	Assert.assertTrue(excepcion);
    
    	log.info("");
    	log.info("Probando carga forzada de coleccion LAZY ------------------------------------------------------------------------");
    	
    	c = (Cocinero) empDao.recuperaPorNif(produtorDatos.c1.getNif());   // Cociñeiro c con proxy sen inicializar
    	c = cociDao.restauraPlatos(c);						// Cociñeiro c con proxy xa inicializado
    	
    	Assert.assertEquals(2, c.getPlatos().size());
		Assert.assertEquals(produtorDatos.p1.getNombre(), c.getPlatos().first().getNombre());
		Assert.assertEquals(produtorDatos.p1.getTipo(), c.getPlatos().first().getTipo());
		Assert.assertEquals(produtorDatos.p2.getNombre(), c.getPlatos().last().getNombre());
		Assert.assertEquals(produtorDatos.p2.getTipo(), c.getPlatos().last().getTipo());

		//Comparamos obxecto a obxecto da lista
		for(int i= 0; i<c.getPlatos().first().getIngredientes().size();i++){
			Assert.assertEquals (produtorDatos.p1.getIngredientes().get(i),   c.getPlatos().first().getIngredientes().get(i));
		}
		for(int i= 0; i<c.getPlatos().last().getIngredientes().size();i++){
			Assert.assertEquals (produtorDatos.p2.getIngredientes().get(i),   c.getPlatos().last().getIngredientes().get(i));
		}


    } 	

    @Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaCocinerosSoltos();
		produtorDatos.gravaCocineros();
		produtorDatos.creaPlatosSoltos();
		produtorDatos.c1.engadirPlato(produtorDatos.p1);
		platoDao.almacena(produtorDatos.p1);
		
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violacion de restricions not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t b) Gravación de plato con nome nulo\n"
    			+ "\t\t\t\t c) Gravación de plato con nome duplicado\n");


    	// Ligar o plato a cociñeiro para poder probar outros erros
		produtorDatos.c1.engadirPlato(produtorDatos.p2);
    	    	
    	log.info("");	
		log.info("Probando gravacion de plato con nome nulo -------------------------------------------------------------------");
		produtorDatos.p2.setNombre(null);
    	try {
        	platoDao.almacena(produtorDatos.p2);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    	log.info("");	
		log.info("Probando gravacion de plato con nome duplicado --------------------------------------------------------------");
		produtorDatos.p2.setNombre(produtorDatos.p1.getNombre());
    	try {
        	platoDao.almacena(produtorDatos.p2);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    }
}