package mx.edu.um.portlets.sgcampus.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import mx.edu.um.portlets.sgcampus.utils.Constantes;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "sg_xalumno_curso")
public class XAlumnoCurso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(name="alumno_curso_id")
    private Long alumnoCursoId;
    @Column(name="alumno_id", nullable = false)
    private Long alumnoId;
    @Column(name = "curso_id", nullable = false)
    private Long cursoId;
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
    @Column(nullable = false, length = 32)
    private String accion;

    public XAlumnoCurso() {
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
     * @return the alumnoCursoId
     */
    public Long getAlumnoCursoId() {
        return alumnoCursoId;
    }

    /**
     * @param alumnoCursoId the alumnoCursoId to set
     */
    public void setAlumnoCursoId(Long alumnoCursoId) {
        this.alumnoCursoId = alumnoCursoId;
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
     * @return the cursoId
     */
    public Long getCursoId() {
        return cursoId;
    }

    /**
     * @param cursoId the cursoId to set
     */
    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
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

    /**
     * @return the accion
     */
    public String getAccion() {
        return accion;
    }

    /**
     * @param accion the accion to set
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XAlumnoCurso other = (XAlumnoCurso) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.getVersion() != other.getVersion() && (this.getVersion() == null || !this.version.equals(other.version))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 89 * hash + (this.getVersion() != null ? this.getVersion().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "XAlumnoCurso{" + "id=" + getId() + ", version=" + getVersion() + ", alumnoId=" + getAlumnoId() + ", cursoId=" + getCursoId() + ", accion=" + getAccion() + '}';
    }

}
