package gei.id.tutelado.dao;

import gei.id.tutelado.model.Cocinero;
import gei.id.tutelado.model.Plato;

import java.util.List;

public interface CocineroDao extends EmpleadoDao {

    Cocinero restauraPlatos(Cocinero cocinero);

    List<Plato> recuperaPlatos(Cocinero cocinero);

    List<Cocinero> findByIngrediente (String ingrediente);
}
