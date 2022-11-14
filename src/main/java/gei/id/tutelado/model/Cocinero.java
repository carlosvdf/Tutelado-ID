package gei.id.tutelado.model;

import javax.persistence.*;
import java.util.SortedSet;
import java.util.TreeSet;


@NamedQueries ({
		@NamedQuery (name="Cocinero.recuperaPlatos",
				query="SELECT p FROM Cocinero c JOIN c.platos p WHERE c=:cocinero"),

})
@Entity
public class Cocinero extends Empleado{
    @ManyToMany (cascade={CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.LAZY)
    @OrderBy("nombre ASC")
    @JoinColumn (nullable=false, unique=false)
    private SortedSet<Plato> platos = new TreeSet<Plato>();



    @Override
	public String toString() {
		return "Cocinero [id=" + getId() + ", nif=" + getNif() + ", nombre=" + getNombre() + ", apellido1=" + getApellido1() + ", apellido2=" + getApellido2() +", telefono=" + getTelefono() + "]";
	}
}
