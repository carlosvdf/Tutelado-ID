package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Empleado;

public interface EmpleadoDao {
    
    void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
	Empleado almacena (Empleado empleado);
	Empleado modifica (Empleado empleado);
	void elimina (Empleado empleado);
	Empleado recuperaPorNif (String nif);
}
