package gei.id.tutelado.dao;

import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Camarero;

public interface CamareroDao {
    
    void setup (Configuracion config);

    List<Camarero> recuperaMesasJOIN();

}
