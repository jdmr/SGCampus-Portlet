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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(name = "alumno_id", nullable = false, unique = true)
    private Long alumnoId;
    @Column(length = 32, nullable = false)
    private String usuario;
    @Column(length = 128, nullable = false)
    private String correo;
    @Column(name = "nombre_completo", length = 200, nullable = false)
    private String nombreCompleto;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;

    public Alumno() {
    }

    public Alumno(User alumno) {
        alumnoId = alumno.getUserId();
        usuario = alumno.getScreenName();
        correo = alumno.getEmailAddress();
        nombreCompleto = alumno.getFullName();
        fecha = new Date();
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
     * @return the alumnoId
     */
    public Long getAlumnoId() {
        return alumnoId;
    }

    /**
     * @param alumnoId the alumnoId to set
     */
    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
