package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Plato;


public interface PlatoDao {
    
    void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
	Plato almacena (Plato user);
	Plato modifica (Plato user);
	void elimina (Plato user);	
	Plato recuperaPorNombre (String nombre);
	
	// QUERIES ADICIONAIS

	Plato recuperaMediaIngredientes();
}
