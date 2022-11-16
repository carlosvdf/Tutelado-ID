package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.*;
import gei.id.tutelado.model.Plato;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R03_Cocineros_Platos {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosProba produtorDatos = new ProdutorDatosProba();
    
    private static Configuracion cfg;
    private static CocineroDao cociDao;
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
    	
		log.info("Probando recuperacion por codigo EXISTENTE --------------------------------------------------");

    	p = platoDao.recuperaPorNombre(produtorDatos.p1.getNombre());

    	Assert.assertEquals (produtorDatos.p1.getNombre(),     p.getNombre());
    	Assert.assertEquals (produtorDatos.p1.getTipo(), p.getTipo());

		for(int i= 0; i<p.getIngredientes().size();i++){
			Assert.assertEquals (produtorDatos.p1.getIngredientes().get(i),   p.getIngredientes().get(i));
		}

    	log.info("");	
		log.info("Probando recuperacion por codigo INEXISTENTE --------------------------------------------------");
    	
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
    			+ "\t\t\t\t a) Primeiro plato vinculado a un cocinero\n"
    			+ "\t\t\t\t b) Novo plato para un cocinero con platos previos\n");

    	// Situación de partida:
    	// c1 desligado
    	// p1, p2 transitorios

    	produtorDatos.c1.engadirPlato(produtorDatos.p1);
		
    	log.info("");	
		log.info("Gravando primeira entrada de log dun usuario --------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.p1.getId());
    	platoDao.almacena(produtorDatos.p1);
    	Assert.assertNotNull(produtorDatos.p1.getId());

    	produtorDatos.c1.engadirPlato(produtorDatos.p2);

    	log.info("");	
		log.info("Gravando segunda entrada de log dun usuario ---------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.p2.getId());
    	platoDao.almacena(produtorDatos.p2);
    	Assert.assertNotNull(produtorDatos.p2.getId());

    }
/*
    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

    	produtorDatos.creaCocinerosConPlatos();
    	produtorDatos.gravaCocineros();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación de plato solto (asignado a cocinero)\n");
    	
    	// Situación de partida:
    	// p1 desligado

		Assert.assertNotNull(platoDao.recuperaPorNombre(produtorDatos.p1.getNombre()));
    	platoDao.elimina(produtorDatos.p1);
		Assert.assertNull(platoDao.recuperaPorNombre(produtorDatos.p1.getNombre()));

    } 	

    @Test 
    public void test04_Modificacion() {

    	EntradaLog e1, e2;
    	String novaDescricion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
  
		produtorDatos.creaUsuariosConEntradasLog();
    	produtorDatos.gravaUsuarios();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificación da información dunha entrada de log solta\n");
 
    	
    	// Situación de partida:
    	// e1A desligado
    	
		novaDescricion = new String ("Descricion nova");

		e1 = logDao.recuperaPorCodigo(produtorDatos.e1A.getCodigo());

		Assert.assertNotEquals(novaDescricion, e1.getDescricion());
    	e1.setDescricion(novaDescricion);

    	logDao.modifica(e1);    	
    	
		e2 = logDao.recuperaPorCodigo(produtorDatos.e1A.getCodigo());
		Assert.assertEquals (novaDescricion, e2.getDescricion());

    	// NOTA: Non probamos modificación de usuario da entrada porque non ten sentido no dominio considerado

    } 	
    
    @Test 
    public void test05_Propagacion_Persist() {

   	    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosSoltos();
    	produtorDatos.creaEntradasLogSoltas();
    	produtorDatos.u1.engadirEntradaLog(produtorDatos.e1A);
    	produtorDatos.u1.engadirEntradaLog(produtorDatos.e1B);


    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da gravación de novo usuario con entradas (novas) de log asociadas\n");   

    	// Situación de partida:
    	// u1, e1A, e1B transitorios

    	Assert.assertNull(produtorDatos.u1.getId());
    	Assert.assertNull(produtorDatos.e1A.getId());
    	Assert.assertNull(produtorDatos.e1B.getId());
    	
		log.info("Gravando na BD usuario con entradas de log ----------------------------------------------------------------------");

    	// Aqui o persist sobre u1 debe propagarse a e1A e e1B
		usuDao.almacena(produtorDatos.u1);

		Assert.assertNotNull(produtorDatos.u1.getId());
    	Assert.assertNotNull(produtorDatos.e1A.getId());
    	Assert.assertNotNull(produtorDatos.e1B.getId());    	
    }

    @Test 
    public void test05_Propagacion_Remove() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
   	
    	produtorDatos.creaUsuariosConEntradasLog();
    	produtorDatos.gravaUsuarios();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación de de usuario con entradas de log asociadas\n");

    	// Situación de partida:
    	// u1, e1A, e1B desligados

    	Assert.assertNotNull(usuDao.recuperaPorNif(produtorDatos.u1.getNif()));
		Assert.assertNotNull(logDao.recuperaPorCodigo(produtorDatos.e1A.getCodigo()));
		Assert.assertNotNull(logDao.recuperaPorCodigo(produtorDatos.e1B.getCodigo()));
		
		// Aqui o remove sobre u1 debe propagarse a e1A e e1B
		usuDao.elimina(produtorDatos.u1);    	
		
		Assert.assertNull(usuDao.recuperaPorNif(produtorDatos.u1.getNif()));
		Assert.assertNull(logDao.recuperaPorCodigo(produtorDatos.e1A.getCodigo()));
		Assert.assertNull(logDao.recuperaPorCodigo(produtorDatos.e1B.getCodigo()));

    } 	

    @Test 
    public void test07_EAGER() {
    	
    	Usuario u;
    	EntradaLog e;
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosConEntradasLog();
    	produtorDatos.gravaUsuarios();

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación de propiedades EAGER\n");   

    	// Situación de partida:
    	// u1, e1A, e1B desligados
    	
		log.info("Probando (que non hai excepcion tras) acceso inicial a propiedade EAGER fora de sesion ----------------------------------------");
    	
    	e = logDao.recuperaPorCodigo(produtorDatos.e1A.getCodigo());  
		log.info("Acceso a usuario de entrada de log");
    	try	{
        	Assert.assertEquals(produtorDatos.u1, e.getUsuario());
        	excepcion=false;
    	} catch (LazyInitializationException ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	};    	
    	Assert.assertFalse(excepcion);    
    } 	

    @Test 
    public void test08_LAZY() {
    	
    	Usuario u;
    	EntradaLog e;
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosConEntradasLog();
    	produtorDatos.gravaUsuarios();

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación de propiedades LAZY\n"   
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación de usuario con colección (LAZY) de entradas de log \n"
		+ "\t\t\t\t b) Carga forzada de colección LAZY da dita coleccion\n"     	
		+ "\t\t\t\t c) Recuperacion de entrada de log solta con referencia (EAGER) a usuario\n");     	

    	// Situación de partida:
    	// u1, e1A, e1B desligados
    	
		log.info("Probando (excepcion tras) recuperacion LAZY ---------------------------------------------------------------------");
    	
    	u = usuDao.recuperaPorNif(produtorDatos.u1.getNif());
		log.info("Acceso a entradas de log de usuario");
    	try	{
        	Assert.assertEquals(2, u.getEntradasLog().size());
        	Assert.assertEquals(produtorDatos.e1A, u.getEntradasLog().first());
        	Assert.assertEquals(produtorDatos.e1B, u.getEntradasLog().last());	
        	excepcion=false;
    	} catch (LazyInitializationException ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	};    	
    	Assert.assertTrue(excepcion);
    
    	log.info("");
    	log.info("Probando carga forzada de coleccion LAZY ------------------------------------------------------------------------");
    	
    	u = usuDao.recuperaPorNif(produtorDatos.u1.getNif());   // Usuario u con proxy sen inicializar
    	u = usuDao.restauraEntradasLog(u);						// Usuario u con proxy xa inicializado
    	
    	Assert.assertEquals(2, u.getEntradasLog().size());
    	Assert.assertEquals(produtorDatos.e1A, u.getEntradasLog().first());
    	Assert.assertEquals(produtorDatos.e1B, u.getEntradasLog().last());

    	log.info("");
    	log.info("Probando acceso a referencia EAGER ------------------------------------------------------------------------------");
    
    	e = logDao.recuperaPorCodigo(produtorDatos.e1A.getCodigo());
    	Assert.assertEquals(produtorDatos.u1, e.getUsuario());
    } 	

    @Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosSoltos();
		produtorDatos.gravaUsuarios();
		produtorDatos.creaEntradasLogSoltas();		
		produtorDatos.u1.engadirEntradaLog(produtorDatos.e1A);		
		logDao.almacena(produtorDatos.e1A);
		
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violacion de restricions not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Gravación de entrada con usuario nulo\n"
    			+ "\t\t\t\t b) Gravación de entrada con codigo nulo\n"
    			+ "\t\t\t\t c) Gravación de entrada con codigo duplicado\n");

    	// Situación de partida:
    	// u0, u1 desligados
    	// e1A desligado, e1B transitorio (e sen usuario asociado)
    	
		log.info("Probando gravacion de entrada con usuario nulo ------------------------------------------------------------------");
    	try {
    		logDao.almacena(produtorDatos.e1B);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    	// Ligar entrada a usuario para poder probar outros erros
		produtorDatos.u1.engadirEntradaLog(produtorDatos.e1B);
    	    	
    	log.info("");	
		log.info("Probando gravacion de entrada con codigo nulo -------------------------------------------------------------------");
		produtorDatos.e1B.setCodigo(null);
    	try {
        	logDao.almacena(produtorDatos.e1B);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    	log.info("");	
		log.info("Probando gravacion de entrada con codigo duplicado --------------------------------------------------------------");
		produtorDatos.e1B.setCodigo(produtorDatos.e1A.getCodigo());
    	try {
        	logDao.almacena(produtorDatos.e1B);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    } 	
*/
}