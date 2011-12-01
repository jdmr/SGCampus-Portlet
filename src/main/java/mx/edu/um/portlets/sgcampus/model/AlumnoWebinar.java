package mx.edu.um.portlets.sgcampus.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import mx.edu.um.portlets.sgcampus.utils.Constantes;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "sg_alumno_webinar", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"alumno_id", "webinar_id"})})
public class AlumnoWebinar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne
    private Alumno alumno;
    @ManyToOne
    private Webinar webinar;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;
    @Column(name = "creador_id", nullable = false)
    private Long creadorId;
    @Column(name = "creador_nombre", nullable = false, length = 200)
    private String creadorNombre;
    private BigDecimal evaluacion;
    @Column(name = "cantidad_evaluaciones")
    private Integer cantidadEvaluaciones;
    private BigDecimal calificacion;
    @Column(nullable = false, length = 32)
    private String estatus = Constantes.PENDIENTE;

    public AlumnoWebinar() {
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
     * @return the alumno
     */
    public Alumno getAlumno() {
        return alumno;
    }

    /**
     * @param alumno the alumno to set
     */
    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    /**
     * @return the webinar
     */
    public Webinar getWebinar() {
        return webinar;
    }

    /**
     * @param webinar the webinar to set
     */
    public void setWebinar(Webinar webinar) {
        this.webinar = webinar;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the creadorId
     */
    public Long getCreadorId() {
        return creadorId;
    }

    /**
     * @param creadorId the creadorId to set
     */
    public void setCreadorId(Long creadorId) {
        this.creadorId = creadorId;
    }

    /**
     * @return the creadorNombre
     */
    public String getCreadorNombre() {
        return creadorNombre;
    }

    /**
     * @param creadorNombre the creadorNombre to set
     */
    public void setCreadorNombre(String creadorNombre) {
        this.creadorNombre = creadorNombre;
    }

    /**
     * @return the evaluacion
     */
    public BigDecimal getEvaluacion() {
        return evaluacion;
    }

    /**
     * @param evaluacion the evaluacion to set
     */
    public void setEvaluacion(BigDecimal evaluacion) {
        this.evaluacion = evaluacion;
    }

    /**
     * @return the cantidadEvaluaciones
     */
    public Integer getCantidadEvaluaciones() {
        return cantidadEvaluaciones;
    }

    /**
     * @param cantidadEvaluaciones the cantidadEvaluaciones to set
     */
    public void setCantidadEvaluaciones(Integer cantidadEvaluaciones) {
        this.cantidadEvaluaciones = cantidadEvaluaciones;
    }

    /**
     * @return the calificacion
     */
    public BigDecimal getCalificacion() {
        return calificacion;
    }

    /**
     * @param calificacion the calificacion to set
     */
    public void setCalificacion(BigDecimal calificacion) {
        this.calificacion = calificacion;
    }

    /**
     * @return the estatus
     */
    public String getEstatus() {
        return estatus;
    }

    /**
     * @param estatus the estatus to set
     */
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AlumnoWebinar other = (AlumnoWebinar) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.version != other.version && (this.version == null || !this.version.equals(other.version))) {
            return false;
        }
        if (this.alumno != other.alumno && (this.alumno == null || !this.alumno.equals(other.alumno))) {
            return false;
        }
        if (this.webinar != other.webinar && (this.webinar == null || !this.webinar.equals(other.webinar))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.version != null ? this.version.hashCode() : 0);
        hash = 97 * hash + (this.alumno != null ? this.alumno.hashCode() : 0);
        hash = 97 * hash + (this.webinar != null ? this.webinar.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "AlumnoWebinar{" + "id=" + id + ", alumno=" + alumno + ", webinar=" + webinar + ", fecha=" + fecha + '}';
    }

}
