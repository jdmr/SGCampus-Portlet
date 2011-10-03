package mx.edu.um.portlets.sgcampus.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
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
    @Column(name = "descripcion_id", nullable = false)
    private Long descripcionId;
    @Transient
    private String descripcion = "";
    @Column(name = "comunidad_id", nullable = false)
    private Long comunidadId;
    @Column(name = "comunidad_nombre", length = 128)
    private String comunidadNombre;
    @ManyToOne
    private Maestro maestro;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inicia;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date termina;
    @Column(length = 254)
    private String url;
    @Column(scale = 2, precision = 8)
    private BigDecimal evaluacion;
    @Column(name = "cantidad_evaluaciones")
    private Integer cantidadEvaluaciones = 0;
    @Column(scale = 2, precision = 8)
    private BigDecimal calificacion;
    @Column(length = 64)
    private String tipo;
    @Column(nullable=false, length=32)
    private String estatus = "ACTIVO";
    @Column(length=32)
    private String telefono;
    @Column(length=32)
    private String estado;
    @Column(length=32)
    private String pais;
    @OneToMany(mappedBy = "curso")
    private Set<Sesion> sesiones;
    @ManyToMany
    @JoinTable(name="sg_curso_etiqueta")
    private Set<Etiqueta> etiquetas;
    @Transient
    private String tags;
    @Transient
    private String verCurso;

    public Curso() {
    }

    public Curso(String codigo, String nombre, Long descripcionId, String descripcion, Long comunidadId, String comunidadNombre, Maestro maestro, Date inicia, Date termina, String url, String tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcionId = descripcionId;
        this.descripcion = descripcion;
        this.comunidadId = comunidadId;
        this.comunidadNombre = comunidadNombre;
        this.maestro = maestro;
        this.inicia = inicia;
        this.termina = termina;
        this.url = url;
        this.tipo = tipo;
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

    /**
     * @return the descripcionId
     */
    public Long getDescripcionId() {
        return descripcionId;
    }

    /**
     * @param descripcionId the descripcionId to set
     */
    public void setDescripcionId(Long descripcionId) {
        this.descripcionId = descripcionId;
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
     * @return the maestro
     */
    public Maestro getMaestro() {
        return maestro;
    }

    /**
     * @param maestro the maestro to set
     */
    public void setMaestro(Maestro maestro) {
        this.maestro = maestro;
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
     * @return the sesiones
     */
    public Set<Sesion> getSesiones() {
        return sesiones;
    }

    /**
     * @param sesiones the sesiones to set
     */
    public void setSesiones(Set<Sesion> sesiones) {
        this.sesiones = sesiones;
    }

    /**
     * @return the etiquetas
     */
    public Set<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    /**
     * @param etiquetas the etiquetas to set
     */
    public void setEtiquetas(Set<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * Asigna URL para llevar a página que contiene portlet para ver el curso
     * 
     * @return the verCurso
     */
    public String getVerCurso() {
        return verCurso;
    }

    /**
     * Obtiene URL para llevar a página que contiene portlet para ver el curso
     * 
     * @param verCurso the verCurso to set
     */
    public void setVerCurso(String verCurso) {
        this.verCurso = verCurso;
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
