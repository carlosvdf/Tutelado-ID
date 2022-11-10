package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Empleado;

public interface EmpleadoDao {
    
    void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
	Empleado almacena (Empleado user);
	Empleado modifica (Empleado user);
	void elimina (Empleado user);	
	Empleado recuperaPorNif (String nif);
}
