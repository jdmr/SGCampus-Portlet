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
    private String codigo;
    @Column(length = 128, nullable = false)
    private String nombre;
    @Column(name = "comunidad_id", nullable = false)
    private Long comunidadId;
    @Column(name = "comunidad_nombre", length = 128)
    private String comunidadNombre;
    @Column(name = "maestro_id", nullable = false)
    private Long maestroId;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inicia;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date termina;
    @Column(length = 254)
    private String url;
    @Column(scale = 2, precision = 8)
    private BigDecimal evaluacion;
    @Column(scale = 2, precision = 8)
    private BigDecimal calificacion;
    @Column(length = 64)
    private String tipo;
    @Column(nullable=false, length=32)
    private String estatus;
    @Column(length=32)
    private String telefono;
    @Column(length=32)
    private String estado;
    @Column(length=32)
    private String pais;
    @Column(length = 32, nullable = false)
    private String accion;
    @Column(name = "creador_id", nullable = false)
    private Long creadorId;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creado = new Date();

    public XCurso() {
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
     * @return the comunidadNombre
     */
    public String getComunidadNombre() {
        return comunidadNombre;
    }

    /**
     * @param comunidadNombre the comunidadNombre to set
     */
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
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
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
