package mx.edu.um.portlets.sgcampus.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "sg_cursos")
public class Curso implements Serializable {

    private static final long serialVersionUID = -7693918750910761286L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(length = 32, nullable = false)
    private String codigo;
    @Column(length = 128, nullable = false)
    private String nombre;
    @Column(length = 500, nullable = false)
    private String descripcion;
    @Column(name = "comunidad_id", nullable = false)
    private Long comunidadId;
    @Column(name = "comunidad_nombre", length = 128)
    private String comunidadNombre;
    @Column(name = "maestro_id", nullable = false)
    private Long maestroId;
    @Column(name = "maestro_nombre", length = 128)
    private String maestroNombre;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inicia;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date termina;
    @Column(length = 254)
    private String url;
    @OneToMany(mappedBy="curso")
    private Set<Sesion> sesiones;

    public Curso() {
    }

    public Curso(String codigo, String nombre, String descripcion, Long comunidadId, String comunidadNombre, Long maestroId, String maestroNombre, Date inicia, Date termina, String url) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.comunidadId = comunidadId;
        this.comunidadNombre = comunidadNombre;
        this.maestroId = maestroId;
        this.maestroNombre = maestroNombre;
        this.inicia = inicia;
        this.termina = termina;
        this.url = url;
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
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getComunidadId() {
        return comunidadId;
    }

    public void setComunidadId(Long comunidadId) {
        this.comunidadId = comunidadId;
    }

    public String getComunidadNombre() {
        return comunidadNombre;
    }

    public void setComunidadNombre(String comunidadNombre) {
        this.comunidadNombre = comunidadNombre;
    }

    /**
     * @return the maestroId
     */
    public Long getMaestroId() {
        return maestroId;
    }

    /**
     * @param maestroId the maestroId to set
     */
    public void setMaestroId(Long maestroId) {
        this.maestroId = maestroId;
    }

    /**
     * @return the maestroNombre
     */
    public String getMaestroNombre() {
        return maestroNombre;
    }

    /**
     * @param maestroNombre the maestroNombre to set
     */
    public void setMaestroNombre(String maestroNombre) {
        this.maestroNombre = maestroNombre;
    }

    /**
     * @return the inicia
     */
    public Date getInicia() {
        return inicia;
    }

    /**
     * @param inicia the inicia to set
     */
    public void setInicia(Date inicia) {
        this.inicia = inicia;
    }

    /**
     * @return the termina
     */
    public Date getTermina() {
        return termina;
    }

    /**
     * @param termina the termina to set
     */
    public void setTermina(Date termina) {
        this.termina = termina;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(Set<Sesion> sesiones) {
        this.sesiones = sesiones;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Curso other = (Curso) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo();
    }

}
