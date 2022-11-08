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
}
