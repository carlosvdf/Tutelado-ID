package gei.id.tutelado.model;

import javax.persistence.*;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class Cocinero extends Empleado{
    @ManyToMany (cascade={}, fetch=FetchType.EAGER)
    @OrderBy("nombre ASC")
    @JoinColumn (nullable=false, unique=false)
    private SortedSet<Plato> platos = new TreeSet<Plato>();

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
        Cocinero other = (Cocinero) obj;
        if (getNif() == null) {
            if (other.getNif() != null)
                return false;
        } else if (!getNif().equals(other.getNif()))
            return false;
		return true;
	}

    @Override
	public String toString() {
		return "Cocinero [id=" + getId() + ", nif=" + getNif() + ", nombre=" + getNombre() + ", apellido1=" + getApellido1() + ", apellido2=" + getApellido2() +", telefono=" + getTelefono() + "]";
	}
}
