package mx.edu.um.portlets.sgcampus.dao;

import com.liferay.portal.model.User;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.portlets.sgcampus.utils.Constantes;
import mx.edu.um.portlets.sgcampus.model.Alumno;
import mx.edu.um.portlets.sgcampus.model.AlumnoCurso;
import mx.edu.um.portlets.sgcampus.model.Curso;
import mx.edu.um.portlets.sgcampus.model.Etiqueta;
import mx.edu.um.portlets.sgcampus.model.Sesion;
import mx.edu.um.portlets.sgcampus.model.XCurso;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jdmr
 */
@Repository
@Transactional
public class CursoDao {

    private static final Logger log = LoggerFactory.getLogger(CursoDao.class);
    private HibernateTemplate hibernateTemplate;

    public CursoDao() {
        log.info("Nueva instancia del dao de cursos");
    }

    @Autowired
    protected void setSessionFactory(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public Curso crea(Curso curso, Long creadorId) {
        log.info("Creando el curso {}", curso);
        if (curso.getInicia() != null && curso.getTermina() != null && curso.getInicia().after(curso.getTermina())) {
            throw new RuntimeException("La fecha inicial debe ser antes de la que termina");
        }
        Long id = (Long) hibernateTemplate.save(curso);
        XCurso xcurso = new XCurso();
        BeanUtils.copyProperties(curso, xcurso);
        xcurso.setCursoId(id);
        xcurso.setAccion(Constantes.CREAR);
        xcurso.setCreadorId(creadorId);
        curso.setId(id);
        curso.setVersion(0);
        hibernateTemplate.save(xcurso);
        return curso;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> busca(Map<String, Object> params) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Curso.class);
        DetachedCriteria countCriteria = DetachedCriteria.forClass(Curso.class);
        List<Curso> cursos;
        List<Long> cantidades;
        if (params != null) {
            if (params.containsKey("filtro") && ((String) params.get("filtro")).trim().length() > 0) {
                String filtro = "%" + ((String) params.get("filtro")).trim() + "%";
                log.debug("Buscando cursos por {}", filtro);
                Disjunction propiedades = Restrictions.disjunction();
                propiedades.add(Restrictions.ilike("codigo", filtro));
                propiedades.add(Restrictions.ilike("nombre", filtro));
                propiedades.add(Restrictions.ilike("comunidadNombre", filtro));
                propiedades.add(Restrictions.ilike("maestroNombre", filtro));
                criteria.add(propiedades);
                countCriteria.add(propiedades);
            }
            
            if (!params.containsKey("estatus")) {
                criteria.add(Restrictions.disjunction()
                        .add(Restrictions.eq("estatus", "ACTIVO"))
                        .add(Restrictions.eq("estatus", "PENDIENTE")));
                countCriteria.add(Restrictions.disjunction()
                        .add(Restrictions.eq("estatus", "ACTIVO"))
                        .add(Restrictions.eq("estatus", "PENDIENTE")));
            } else {
                criteria.add(Restrictions.eq("estatus", (String)params.get("estatus")));
                countCriteria.add(Restrictions.eq("estatus", (String)params.get("estatus")));
            }

            if (params.containsKey("comunidades")) {
                criteria.add(Restrictions.in("comunidadId", (Set<Long>) params.get("comunidades")));
                countCriteria.add(Restrictions.in("comunidadId", (Set<Long>) params.get("comunidades")));
            }

            Integer max = 0;
            if (params.containsKey("max")) {
                max = (Integer) params.get("max");
            }
            Integer offset = 0;
            if (params.containsKey("offset")) {
                offset = (Integer) params.get("offset");
            }
            if (params.containsKey("order")) {
                if (params.containsKey("sort")) {
                    if (params.get("sort").equals(Constantes.ASC)) {
                        criteria.addOrder(Order.asc((String) params.get("order")));
                    } else {
                        criteria.addOrder(Order.desc((String) params.get("order")));
                    }
                } else {
                    criteria.addOrder(Order.asc((String) params.get("order")));
                }
            } else {
                criteria.addOrder(Order.desc("inicia"));
            }

            cursos = hibernateTemplate.findByCriteria(criteria, offset, max);
        } else {
            criteria.addOrder(Order.desc("inicia"));
            cursos = hibernateTemplate.findByCriteria(criteria);
        }

        countCriteria.setProjection(Projections.rowCount());
        cantidades = hibernateTemplate.findByCriteria(countCriteria);

        Map<String, Object> resultados = new HashMap<String, Object>();
        resultados.put("cursos", cursos);
        resultados.put("cantidad", cantidades.get(0));
        return resultados;
    }

    public Curso actualiza(Curso curso, Long creadorId) {
        hibernateTemplate.update(curso);
        XCurso xcurso = new XCurso();
        BeanUtils.copyProperties(curso, xcurso);
        xcurso.setCursoId(curso.getId());
        xcurso.setAccion(Constantes.ACTUALIZAR);
        xcurso.setCreadorId(creadorId);
        hibernateTemplate.save(xcurso);
        return curso;
    }

    @Transactional(readOnly = true)
    public Curso obtiene(Long id) {
        Curso curso = hibernateTemplate.get(Curso.class, id);
        return curso;
    }

    @Transactional(readOnly = true)
    public Curso refresh(Curso curso) {
        hibernateTemplate.refresh(curso);
        return curso;
    }

    public void elimina(Long id, Long creadorId) {
        Curso curso = hibernateTemplate.load(Curso.class, id);
        XCurso xcurso = new XCurso();
        BeanUtils.copyProperties(curso, xcurso);
        xcurso.setCursoId(curso.getId());
        xcurso.setAccion(Constantes.ELIMINAR);
        xcurso.setCreadorId(creadorId);
        hibernateTemplate.save(xcurso);
        hibernateTemplate.delete(curso);
    }

    public Sesion creaSesion(Sesion sesion) {
        if (sesion.getHoraInicial().before(sesion.getHoraFinal())) {
            Long id = (Long) hibernateTemplate.save(sesion);
            sesion.setId(id);
            return sesion;
        } else {
            log.debug("Hora inicial: {} | Hora final: {}", sesion.getHoraInicial(), sesion.getHoraFinal());
            throw new RuntimeException("La hora final debe ser despues de la hora inicial");
        }
    }

    @Transactional(readOnly = true)
    public Sesion obtieneSesion(Long id) {
        return (Sesion) hibernateTemplate.get(Sesion.class, id);
    }

    public void eliminaSesion(Long id) {
        Sesion sesion = hibernateTemplate.load(Sesion.class, id);
        hibernateTemplate.delete(sesion);
    }

    public Alumno creaAlumno(Alumno alumno) {
        Long id = (Long) hibernateTemplate.save(alumno);
        alumno.setId(id);
        alumno.setVersion(0);
        return alumno;
    }

    public AlumnoCurso preInscribeAlumno(AlumnoCurso alumnoCurso) {
        alumnoCurso.setEstatus(Constantes.PENDIENTE);
        alumnoCurso.setAlta(new Date());
        Long id = (Long) hibernateTemplate.save(alumnoCurso);
        alumnoCurso.setId(id);
        alumnoCurso.setVersion(0);
        return alumnoCurso;
    }

    public AlumnoCurso inscribeAlumno(AlumnoCurso alumnoCurso) {
        alumnoCurso.setEstatus(Constantes.INSCRITO);
        hibernateTemplate.update(alumnoCurso);
        return alumnoCurso;
    }

    public AlumnoCurso rechazaAlumno(AlumnoCurso alumnoCurso) {
        alumnoCurso.setEstatus(Constantes.RECHAZADO);
        hibernateTemplate.update(alumnoCurso);
        return alumnoCurso;
    }

    @Transactional(readOnly = true)
    public AlumnoCurso refreshAlumnoCurso(AlumnoCurso alumnoCurso) {
        hibernateTemplate.refresh(alumnoCurso);
        return alumnoCurso;
    }
    
    public List<AlumnoCurso> obtieneAlumnos(Curso curso) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AlumnoCurso.class);
        criteria.add(Restrictions.eq("curso", curso));
        return hibernateTemplate.findByCriteria(criteria);
    }

    public AlumnoCurso evaluacion(AlumnoCurso alumnoCurso) {
        hibernateTemplate.update(alumnoCurso);
        Curso curso = alumnoCurso.getCurso();
        hibernateTemplate.refresh(curso);
        DetachedCriteria criteria = DetachedCriteria.forClass(AlumnoCurso.class);
        criteria.add(Restrictions.eq("curso", curso));
        criteria.add(Restrictions.isNotNull("evaluacion"));
        List<AlumnoCurso> evaluaciones = hibernateTemplate.findByCriteria(criteria);
        Integer total = 0;
        for (AlumnoCurso x : evaluaciones) {
            total += x.getEvaluacion();
        }
        BigDecimal evaluacion = new BigDecimal(total).divide(new BigDecimal(evaluaciones.size()));
        curso.setEvaluacion(evaluacion);
        hibernateTemplate.update(curso);
        alumnoCurso.setCurso(curso);
        return alumnoCurso;
    }

    public AlumnoCurso califica(AlumnoCurso alumnoCurso) {
        hibernateTemplate.update(alumnoCurso);
        Curso curso = alumnoCurso.getCurso();
        hibernateTemplate.refresh(curso);
        DetachedCriteria criteria = DetachedCriteria.forClass(AlumnoCurso.class);
        criteria.add(Restrictions.eq("curso", curso));
        criteria.add(Restrictions.isNotNull("calificacion"));
        List<AlumnoCurso> evaluaciones = hibernateTemplate.findByCriteria(criteria);
        BigDecimal total = new BigDecimal("0");
        for (AlumnoCurso x : evaluaciones) {
            total = x.getCalificacion().add(total);
        }
        BigDecimal calificacionPromedio = total.divide(new BigDecimal(evaluaciones.size()));
        curso.setCalificacion(calificacionPromedio);
        hibernateTemplate.update(curso);
        alumnoCurso.setCurso(curso);
        return alumnoCurso;
    }

    public List<Curso> buscaPorEtiqueta(Etiqueta etiqueta) {
        log.debug("Buscando curso por etiqueta {}", etiqueta);
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Curso.class);
        log.debug("Haciendo query");
        List<Curso> cursos = (List<Curso>) criteria.createCriteria("etiquetas").add(Restrictions.ilike("nombre", etiqueta.getNombre())).list();
        return cursos;
    }

    public List<Sesion> obtieneSesiones(Curso curso) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Sesion.class);
        criteria.add(Restrictions.eq("curso", curso));
        return hibernateTemplate.findByCriteria(criteria);
    }

    public Alumno obtieneAlumno(User usuario) {
        Alumno alumno = null;
        List<Alumno> alumnos = hibernateTemplate.findByNamedParam("select a from Alumno a where a.alumnoId = :alumnoId", "alumnoId", usuario.getUserId());
        if (alumnos != null && alumnos.size() > 0) {
            alumno = alumnos.get(0);
        }
        return alumno;
    }
    
    public Boolean existeSesionActiva(Long cursoId, Integer dia, Date hoy) {
        log.debug("existeSesionActiva: {} {} {}", new Object[] {cursoId, dia, hoy});
        boolean resultado = false;
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createQuery("select sesion from Sesion sesion where sesion.curso.id = :cursoId and sesion.dia = :dia and :hora between sesion.horaInicial and sesion.horaFinal and :hoy between sesion.curso.inicia and sesion.curso.termina");
        query.setParameter("cursoId", cursoId);
        query.setParameter("dia", dia);
        query.setParameter("hora", hoy);
        query.setParameter("hoy", hoy);
        Sesion sesion = (Sesion) query.uniqueResult();
        if (sesion != null) {
            resultado = true;
        }
        return resultado;
    }

    public AlumnoCurso obtieneAlumno(Alumno alumno, Curso curso) {
        AlumnoCurso alumnoCurso = null;
        List<AlumnoCurso> alumnos = hibernateTemplate.findByNamedParam("from AlumnoCurso where alumno = :alumno and curso = :curso", new String[]{"alumno", "curso"}, new Object[]{alumno, curso});
        if (alumnos != null && alumnos.size() > 0) {
            alumnoCurso = alumnos.get(0);
        }
        return alumnoCurso;
    }
}
