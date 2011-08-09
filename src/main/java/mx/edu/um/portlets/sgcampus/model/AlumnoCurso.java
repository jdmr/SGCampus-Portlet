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
import mx.edu.um.portlets.sgcampus.Constantes;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "sg_alumno_curso", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"alumno_id", "curso_id"})})
public class AlumnoCurso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne
    private Alumno alumno;
    @ManyToOne
    private Curso curso;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date alta;
    @Column(name = "usuario_alta", nullable = false)
    private Long usuarioAlta;
    @Column(name = "usuario_alta_nombre", nullable = false, length = 200)
    private String usuarioAltaNombre;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date baja;
    private Long usuarioBaja;
    @Column(name = "usuario_baja_nombre", length = 200)
    private String usuarioBajaNombre;
    private Integer evaluacion;
    private BigDecimal calificacion;
    @Column(nullable = false, length = 32)
    private String estatus = Constantes.PENDIENTE;

    public AlumnoCurso() {
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
     * @return the curso
     */
    public Curso getCurso() {
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    /**
     * @return the alta
     */
    public Date getAlta() {
        return alta;
    }

    /**
     * @param alta the alta to set
     */
    public void setAlta(Date alta) {
        this.alta = alta;
    }

    /**
     * @return the usuarioAlta
     */
    public Long getUsuarioAlta() {
        return usuarioAlta;
    }

    /**
     * @param usuarioAlta the usuarioAlta to set
     */
    public void setUsuarioAlta(Long usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    /**
     * @return the usuarioAltaNombre
     */
    public String getUsuarioAltaNombre() {
        return usuarioAltaNombre;
    }

    /**
     * @param usuarioAltaNombre the usuarioAltaNombre to set
     */
    public void setUsuarioAltaNombre(String usuarioAltaNombre) {
        this.usuarioAltaNombre = usuarioAltaNombre;
    }

    /**
     * @return the baja
     */
    public Date getBaja() {
        return baja;
    }

    /**
     * @param baja the baja to set
     */
    public void setBaja(Date baja) {
        this.baja = baja;
    }

    /**
     * @return the usuarioBaja
     */
    public Long getUsuarioBaja() {
        return usuarioBaja;
    }

    /**
     * @param usuarioBaja the usuarioBaja to set
     */
    public void setUsuarioBaja(Long usuarioBaja) {
        this.usuarioBaja = usuarioBaja;
    }

    /**
     * @return the usuarioBajaNombre
     */
    public String getUsuarioBajaNombre() {
        return usuarioBajaNombre;
    }

    /**
     * @param usuarioBajaNombre the usuarioBajaNombre to set
     */
    public void setUsuarioBajaNombre(String usuarioBajaNombre) {
        this.usuarioBajaNombre = usuarioBajaNombre;
    }

    /**
     * @return the evaluacion
     */
    public Integer getEvaluacion() {
        return evaluacion;
    }

    /**
     * @param evaluacion the evaluacion to set
     */
    public void setEvaluacion(Integer evaluacion) {
        this.evaluacion = evaluacion;
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
        final AlumnoCurso other = (AlumnoCurso) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "AlumnoCurso{" + "id=" + getId() + ", alumno=" + getAlumno() + ", curso=" + getCurso() + ", estatus=" + getEstatus() + '}';
    }
}
