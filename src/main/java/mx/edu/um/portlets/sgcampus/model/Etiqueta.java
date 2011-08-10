package mx.edu.um.portlets.sgcampus.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "sg_etiquetas", uniqueConstraints =
@UniqueConstraint(columnNames = {"nombre","comunidad_id"}))
public class Etiqueta implements Serializable {

    @Id
    private String nombre;
    @Column(name = "comunidad_id", nullable = false)
    private Long comunidadId;

    public Etiqueta() {
    }

    public Etiqueta(String nombre, Long comunidadId) {
        this.nombre = nombre;
        this.comunidadId = comunidadId;
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

    public Long getComunidadId() {
        return comunidadId;
    }

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
        final Etiqueta other = (Etiqueta) obj;
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Etiqueta{" + "nombre=" + nombre + '}';
    }
}
