package gei.id.tutelado.dao;

import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Mesa;
import gei.id.tutelado.model.Camarero;

public interface MesaDao {

    void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
	Mesa almacena (Mesa log);
	Mesa modifica (Mesa log);
	void elimina (Mesa log);
	Mesa recuperaPorCodigo (String codigo);
	
	//QUERIES ADICIONAIS
    
}
