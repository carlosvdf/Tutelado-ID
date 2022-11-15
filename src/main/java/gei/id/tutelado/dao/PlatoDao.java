package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Plato;

import java.util.List;


public interface PlatoDao {
    
    void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
	Plato almacena (Plato user);
	Plato modifica (Plato user);
	void elimina (Plato user);

	List<Plato> recuperaTodos();
	Plato recuperaPorNombre (String nombre);

	// QUERIES ADICIONAIS

	Plato recuperaMediaIngredientes();
}
