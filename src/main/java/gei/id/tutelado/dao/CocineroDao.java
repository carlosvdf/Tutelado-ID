package gei.id.tutelado.dao;

import gei.id.tutelado.model.Cocinero;
import gei.id.tutelado.model.Plato;

import java.util.List;

public interface CocineroDao extends EmpleadoDao {

    List<Plato> recuperaPlatos(Cocinero cocinero);
}
