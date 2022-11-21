package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cocinero;
import gei.id.tutelado.model.Plato;

import java.util.List;

public interface CocineroDao {

    void setup (Configuracion config);

    Cocinero restauraPlatos(Cocinero cocinero);

    List<Plato> recuperaPlatos(Cocinero cocinero);

    List<Cocinero> findByIngrediente (String ingrediente);
}
