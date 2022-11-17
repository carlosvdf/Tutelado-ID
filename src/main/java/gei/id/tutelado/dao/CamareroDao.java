package gei.id.tutelado.dao;

import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Camarero;
import gei.id.tutelado.model.Mesa;

public interface CamareroDao extends EmpleadoDao{
    
    void setup (Configuracion config);

    List<Mesa> recuperaMesasCamarero(Camarero camarero);

}
