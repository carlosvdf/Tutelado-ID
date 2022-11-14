package gei.id.tutelado.model;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@TableGenerator(name="xeradorIdsPlatos", table="taboa_ids",
pkColumnName="nome_id", pkColumnValue="idPlato",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
		@NamedQuery (name="Plato.recuperaPorNombre",
				 query="SELECT p FROM Plato p where p.nombre=:nombre"),
		@NamedQuery (name="Plato.recuperaTodos",
	 			 query="SELECT p FROM Plato p"),
		@NamedQuery (name="Plato.recuperaTodosTipo",
				 query="SELECT p FROM Plato p where p.tipo=:tipo")
				 //Añadir por ingrediente?
})

@Entity
public class Plato {
    @Id
    @GeneratedValue(generator="xeradorIdsPlatos")
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(unique=false, nullable = false)
    private String tipo;

    @Column(unique=false, nullable = false)
	@ElementCollection
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
        return ingredientes;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Plato other = (Plato) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Plato [id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + "]";
	}    
}
