package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Empleado;

import java.util.List;

public interface EmpleadoDao {
    
    void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
	Empleado almacena (Empleado empleado);
	Empleado modifica (Empleado empleado);
	void elimina (Empleado empleado);

	List<Empleado> recuperaTodos();
	Empleado recuperaPorNif (String nif);
}
