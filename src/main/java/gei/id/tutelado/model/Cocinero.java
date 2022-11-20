package gei.id.tutelado.model;

import javax.persistence.*;
import java.util.SortedSet;
import java.util.TreeSet;


@NamedQueries ({
        @NamedQuery (name="Cocinero.recuperaTodos",
                query="SELECT c FROM Cocinero c ORDER BY c.nif"),
		@NamedQuery (name="Cocinero.recuperaPlatos",
				query="SELECT p FROM Cocinero c JOIN c.platos p WHERE c=:cocinero"),
        @NamedQuery (name="Cocinero.findByIngrediente",
                query="SELECT c FROM Cocinero c JOIN c.platos p WHERE p IN (SELECT p2 FROM Plato p2 WHERE :ingrediente MEMBER OF p2.ingredientes)")
})
@Entity
public class Cocinero extends Empleado{
    @ManyToMany (cascade={CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.LAZY)
    @JoinColumn (nullable=false, unique=false)
    @OrderBy("nombre ASC")
    private SortedSet<Plato> platos = new TreeSet<Plato>();

    public SortedSet<Plato> getPlatos() {
        return platos;
    }

    public void engadirPlato(Plato plato) {
        this.platos.add(plato);
        // É un sorted set, engadimos sempre por orde de nome (ascendente)
    }

    @Override
	public String toString() {
		return "Cocinero [id=" + getId() + ", nif=" + getNif() + ", nombre=" + getNombre() + ", apellido1=" + getApellido1() + ", apellido2=" + getApellido2() +", telefono=" + getTelefono() + "]";
	}
}
