package gei.id.tutelado.model;

import javax.persistence.*;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class Camarero extends Empleado{
    @OneToMany (mappedBy="camarero", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE} )
    @OrderBy("nif ASC")
    private SortedSet<Mesa> mesas = new TreeSet<Mesa>();

    public SortedSet<Mesa> getMesas() {
		return mesas;
	}

    public void setMesas(SortedSet<Mesa> mesas) {
		this.mesas = mesas;
	}

    // Metodo de conveniencia para asegurarnos de que actualizamos os dous extremos da asociación ao mesmo tempo
	public void engadirMesa(Mesa mesa) {
		if (mesa.getCamarero() != null) throw new RuntimeException ("");
		mesa.setCamarero(this);
		// É un sorted set, engadimos sempre por orde de data (ascendente)
		this.mesas.add(mesa);
	}
}
