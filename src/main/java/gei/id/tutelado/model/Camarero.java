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
		// É un sorted set, engadimos sempre por orde de nif (ascendente)
		this.mesas.add(mesa);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNif() == null) ? 0 : getNif().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
        Camarero other = (Camarero) obj;
        if (getNif() == null) {
            if (other.getNif() != null)
                return false;
        } else if (!getNif().equals(other.getNif()))
            return false;
		return true;
	}

    @Override
	public String toString() {
		return "Camarero [id=" + getId() + ", nif=" + getNif() + ", nombre=" + getNombre() + ", apellido1=" + getApellido1() + ", apellido2=" + getApellido2() +", telefono=" + getTelefono() + "]";
	}
}
