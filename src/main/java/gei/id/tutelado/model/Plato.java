package gei.id.tutelado.model;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@TableGenerator(name="xeradorIdsPlatos", table="taboa_ids",
pkColumnName="nome_id", pkColumnValue="idPlato",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
		@NamedQuery (name="Plato.recuperaPorNombre",
				 query="SELECT p FROM Plato p where p.nombre=:nombre"),
		@NamedQuery (name="Plato.recuperaMediaIngredientes",
				 query="SELECT AVG(size(p.ingredientes)) FROM Plato p"),
		@NamedQuery (name="Plato.recuperaTodos",
	 			 query="SELECT p FROM Plato p"),
		@NamedQuery (name="Plato.recuperaTodosTipo",
				 query="SELECT p FROM Plato p where p.tipo=:tipo"),
		@NamedQuery (name="Plato.recuperaCocineros",
				query="SELECT c FROM Cocinero c JOIN Plato p where c.platos =:plato")
				 //Añadir por ingrediente?
})

@Entity
public class Plato implements Comparable<Plato> {
    @Id
    @GeneratedValue(generator="xeradorIdsPlatos")
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(unique=false, nullable = false)
    private String tipo;

    @Column(unique=false, nullable = true)
	@ElementCollection(fetch=FetchType.EAGER)
    private List<String> ingredientes = new ArrayList<String>();

    public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}

    public List<String> getIngredientes() {
        return this.ingredientes;
    }

    public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Plato)) return false;
		Plato plato = (Plato) o;
		return id.equals(plato.id) && nombre.equals(plato.nombre) && tipo.equals(plato.tipo) && Objects.equals(ingredientes, plato.ingredientes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre, tipo, ingredientes);
	}

	@Override
	public String toString() {
		return "Plato{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				", tipo='" + tipo + '\'' +
				", ingredientes=" + ingredientes +
				'}';
	}

	@Override
	public int compareTo(Plato other) {
		if(this.nombre.compareToIgnoreCase(other.getNombre()) == 1){
			return 1;
		}
		else{
			return -1;
		}
	}
}
