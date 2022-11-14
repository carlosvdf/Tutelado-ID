package gei.id.tutelado.model;

import javax.persistence.*;

@TableGenerator(name="xeradorIdsEmpleados", table="taboa_ids",
pkColumnName="nome_id", pkColumnValue="idEmpleado",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
	@NamedQuery (name="Empleado.recuperaPorNif",
				 query="SELECT e FROM Empleado e where e.nif=:nif"),
	@NamedQuery (name="Empleado.recuperaTodos",
				 query="SELECT e FROM Empleado e ORDER BY e.nif")
})

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Empleado {
    @Id
    @GeneratedValue(generator="xeradorIdsEmpleados")
    private Long id;

    @Column(unique = true, nullable = false)
    private String nif;

    @Column(unique=false, nullable = false)
    private String nombre;

    @Column(unique=false, nullable = false)
    private String apellido1;

    @Column(unique=false, nullable = false)
    private String apellido2;

    @Column(unique=false, nullable = false)
    private String telefono;

    public Long getId() {
		return id;
	}

	public String getNif() {
		return nif;
	}

	public String getNombre() {
		return nombre;
	}

    public String getApellido1() {
		return apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public String getTelefono() {
		return telefono;
	}

    public void setId(Long id) {
		this.id = id;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

    public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
    
    public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

    public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nif == null) ? 0 : nif.hashCode());
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
        Empleado other = (Empleado) obj;
        if (nif == null) {
            if (other.nif != null)
                return false;
        } else if (!nif.equals(other.nif))
            return false;
		return true;
	}

    @Override
	public String toString() {
		return "Empleado [id=" + id + ", nif=" + nif + ", nombre=" + nombre + ", apellido1=" + apellido1 + ", apellido2=" + apellido2 +", telefono=" + telefono + "]";
	}
}
