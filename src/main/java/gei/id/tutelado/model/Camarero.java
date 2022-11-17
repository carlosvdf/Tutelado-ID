package gei.id.tutelado.model;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@NamedQueries({
	@NamedQuery(name="Camarero.recuperaMesasJOIN",
				query="SELECT DISTINCT c FROM Camarero c LEFT OUTER JOIN c.mesas m")
})

@Entity
public class Camarero extends Empleado{
    @OneToMany (mappedBy="camarero", fetch=FetchType.EAGER, cascade={} )
    private List<Mesa> mesas = new ArrayList<Mesa>();

    public List<Mesa> getMesas() {
		return mesas;
	}

    public void setMesas(List<Mesa> mesas) {
		this.mesas = mesas;
	}

    // Metodo de conveniencia para asegurarnos de que actualizamos os dous extremos da asociación ao mesmo tempo
	public void engadirMesa(Mesa mesa) {
		if (mesa.getCamarero() != null) throw new RuntimeException ("");
		mesa.setCamarero(this);
		// É un sorted set, engadimos sempre por orde de nif (ascendente)
		this.mesas.add(mesa);
	}

    @Override
	public String toString() {
		return "Camarero [id=" + getId() + ", nif=" + getNif() + ", nombre=" + getNombre() + ", apellido1=" + getApellido1() + ", apellido2=" + getApellido2() +", telefono=" + getTelefono() + ", mesas=" + getMesas() + "]";
	}
}
