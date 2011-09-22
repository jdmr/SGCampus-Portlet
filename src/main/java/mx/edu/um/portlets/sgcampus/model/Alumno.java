package mx.edu.um.portlets.sgcampus.model;

import com.liferay.portal.model.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "sg_alumnos")
public class Alumno implements Serializable {

    @Id
    private Long id;
    @Version
    private Integer version;
    @Column(length = 32, nullable = false)
    private String usuario;
    @Column(length = 128, nullable = false)
    private String correo;
    @Column(name = "nombre_completo", length = 200, nullable = false)
    private String nombreCompleto;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="fecha_ingreso", nullable = false)
    private Date fechaIngreso;
    @Column(length=32)
    private String telefono;
    @Column(length=32)
    private String estado;
    @Column(length=32)
    private String pais;

    public Alumno() {
    }

    public Alumno(User alumno) {
        id = alumno.getUserId();
        usuario = alumno.getScreenName();
        correo = alumno.getEmailAddress();
        nombreCompleto = alumno.getFullName();
        fechaIngreso = new Date();
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
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return the nombreCompleto
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * @param nombreCompleto the nombreCompleto to set
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * @return the fechaIngreso
     */
    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    /**
     * @param fechaIngreso the fechaIngreso to set
     */
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the pais
     */
    public String getPais() {
        return pais;
    }

    /**
     * @param pais the pais to set
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Alumno other = (Alumno) obj;
        if (this.id != other.id && ( this.id == null || !this.id.equals(other.id) )) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + ( this.id != null ? this.id.hashCode() : 0 );
        return hash;
    }

    @Override
    public String toString() {
        return "Alumno{" + "id=" + id + ", usuario=" + usuario + ", correo=" + correo + ", nombreCompleto=" + nombreCompleto + '}';
    }
}
