package gei.id.tutelado.model;

import javax.persistence.*;

@TableGenerator(name="xeradorIdsMesas", table="taboa_ids",
pkColumnName="nome_id", pkColumnValue="idMesa",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
	@NamedQuery (name="Mesa.recuperaPorCodigo",
				 query="SELECT m FROM Mesa m where m.codigo=:codigo"),
	@NamedQuery (name="Mesa.recuperaTodasCamarero",
	 			 query="SELECT m FROM Mesa m JOIN m.camarero c WHERE c=:c"),
	@NamedQuery (name="Mesa.recuperaTodas",
	 			 query="SELECT m FROM Mesa m")
})

@Entity
public class Mesa {
    @Id
    @GeneratedValue(generator="xeradorIdsMesas")
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(unique=false, nullable = false)
    private String tipo;

    @ManyToOne (cascade={}, fetch=FetchType.EAGER)
    @JoinColumn (nullable=false, unique=false)
    private Camarero camarero;

    public Long getId() {
		return id;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getTipo() {
		return tipo;
	}

    public Camarero getCamarero() {
		return camarero;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

    public void setCamarero(Camarero camarero) {
		this.camarero = camarero;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Mesa other = (Mesa) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Mesa [id=" + id + ", codigo=" + codigo + ", tipo=" + tipo + "]";
	}
}
