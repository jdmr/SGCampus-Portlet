package mx.edu.um.portlets.sgcampus.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

/**
 *
 * @author jdmr
 */
@Entity
@Table(
        name = "sg_folios",
        uniqueConstraints =
            @UniqueConstraint(columnNames = {"comunidad_id", "nombre"})
)
public class Folio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(length = 32, nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Long valor;
    @Column(name = "comunidad_id", nullable = false)
    private Long comunidadId;

    public Folio() {
    }

    public Folio(String nombre, Long valor, Long comunidadId) {
        this.nombre = nombre;
        this.valor = valor;
        this.comunidadId = comunidadId;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the valor
     */
    public Long getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Long valor) {
        this.valor = valor;
    }

    /**
     * @return the comunidadId
     */
    public Long getComunidadId() {
        return comunidadId;
    }

    /**
     * @param comunidadId the comunidadId to set
     */
    public void setComunidadId(Long comunidadId) {
        this.comunidadId = comunidadId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Folio other = (Folio) obj;
        if (this.id != other.id && ( this.id == null || !this.id.equals(other.id) )) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + ( this.id != null ? this.id.hashCode() : 0 );
        return hash;
    }

    @Override
    public String toString() {
        return "Folio{" + "id=" + id + ", nombre=" + nombre + ", valor=" + valor + ", comunidadId=" + comunidadId + '}';
    }
}
