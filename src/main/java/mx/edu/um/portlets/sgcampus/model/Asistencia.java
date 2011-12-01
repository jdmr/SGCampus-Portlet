package mx.edu.um.portlets.sgcampus.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "sg_asistencias")
public class Asistencia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne
    private AlumnoCurso alumnoCurso;
    @ManyToOne
    private AlumnoWebinar alumnoWebinar;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;

    public Asistencia() {
    }

    public Asistencia(AlumnoCurso alumnoCurso, Date fecha) {
        this.alumnoCurso = alumnoCurso;
        this.fecha = fecha;
    }

    public Asistencia(AlumnoWebinar alumnoWebinar, Date fecha) {
        this.alumnoWebinar = alumnoWebinar;
        this.fecha = fecha;
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
     * @return the alumnoCurso
     */
    public AlumnoCurso getAlumnoCurso() {
        return alumnoCurso;
    }

    /**
     * @param alumnoCurso the alumnoCurso to set
     */
    public void setAlumnoCurso(AlumnoCurso alumnoCurso) {
        this.alumnoCurso = alumnoCurso;
    }

    /**
     * @return the alumnoWebinar
     */
    public AlumnoWebinar getAlumnoWebinar() {
        return alumnoWebinar;
    }

    /**
     * @param alumnoWebinar the alumnoWebinar to set
     */
    public void setAlumnoWebinar(AlumnoWebinar alumnoWebinar) {
        this.alumnoWebinar = alumnoWebinar;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Asistencia other = (Asistencia) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Asistencia{" + "id=" + id + ", alumnoCurso=" + alumnoCurso + ", alumnoWebinar=" + alumnoWebinar + ", fecha=" + fecha + '}';
    }

}
