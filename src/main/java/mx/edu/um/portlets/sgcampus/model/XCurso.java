package mx.edu.um.portlets.sgcampus.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "sg_xcursos")
public class XCurso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "curso_id", nullable = false)
    private Long cursoId;
    @Column(length = 32, nullable = false)
    private String codigo;
    @Column(length = 128, nullable = false)
    private String nombre;
    @Column(length = 500, nullable = false)
    private String descripcion;
    @Column(name = "comunidad_id", nullable = false)
    private Long comunidadId;
    @Column(name = "maestro_id", nullable = false)
    private Long maestroId;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inicia;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date termina;
    @Column(length = 254)
    private String url;
    @Column(length = 32, nullable = false)
    private String accion;
    @Column(name = "creador_id", nullable = false)
    private Long creadorId;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creado = new Date();

    public XCurso() {
    }

    public XCurso(Long cursoId, String codigo, String nombre, String descripcion, Long comunidadId, Long maestroId, Date inicia, Date termina, String url, String accion, Long creadorId) {
        this.cursoId = cursoId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.comunidadId = comunidadId;
        this.maestroId = maestroId;
        this.inicia = inicia;
        this.termina = termina;
        this.url = url;
        this.accion = accion;
        this.creadorId = creadorId;
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

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
     * @return the creado
     */
    public Date getCreado() {
        return creado;
    }

    /**
     * @param creado the creado to set
     */
    public void setCreado(Date creado) {
        this.creado = creado;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XCurso other = (XCurso) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "XCurso{" + "id=" + getId() + ", cursoId=" + getCursoId() + ", codigo=" + getCodigo() + '}';
    }
}
