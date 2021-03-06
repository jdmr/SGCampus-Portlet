package mx.edu.um.portlets.sgcampus.dao;

import com.liferay.portal.model.User;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import mx.edu.um.portlets.sgcampus.model.*;
import mx.edu.um.portlets.sgcampus.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
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

        // La fecha de inicio debe ser antes de la de termino
        if (curso.getInicia() != null && curso.getTermina() != null && curso.getInicia().after(curso.getTermina())) {
            throw new RuntimeException("La fecha inicial debe ser antes de la que termina");
        }

        // Asignando codigo si no tiene uno
        if (curso.getCodigo() == null) {
            log.debug("Asignando codigo");
            List<Folio> folios = hibernateTemplate.findByNamedParam("select folio from Folio folio where folio.nombre = :nombre and folio.comunidadId = :comunidadId", new String[]{"nombre", "comunidadId"}, new Object[]{Constantes.CURSO, curso.getComunidadId()});
            Folio folio = null;
            if (folios != null && folios.size() > 0) {
                folio = folios.get(0);
            } else {
                folio = new Folio(Constantes.CURSO, 0L, curso.getComunidadId());
            }
            folio.setValor(folio.getValor() + 1);
            NumberFormat nf = DecimalFormat.getInstance();
            nf.setGroupingUsed(false);
            nf.setMinimumIntegerDigits(7);
            curso.setCodigo("U" + nf.format(folio.getValor()));
        }

        // Creando curso
        Long id = (Long) hibernateTemplate.save(curso);
        curso.setId(id);
        curso.setVersion(0);

        // Historial
        XCurso xcurso = new XCurso();
        BeanUtils.copyProperties(curso, xcurso);
        xcurso.setCursoId(id);
        xcurso.setAccion(Constantes.CREAR);
        xcurso.setCreadorId(creadorId);
        xcurso.setMaestroId(curso.getMaestro().getId());
        hibernateTemplate.save(xcurso);


        return curso;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> busca(Map<String, Object> params) {
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Curso.class);
        Criteria countCriteria = session.createCriteria(Curso.class);
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
                criteria.add(propiedades);
                countCriteria.add(propiedades);
            }
            
            if (!params.containsKey("estatus")) {
                criteria.add(Restrictions.disjunction().add(Restrictions.eq("estatus", "ACTIVO")).add(Restrictions.eq("estatus", "PENDIENTE")));
                countCriteria.add(Restrictions.disjunction().add(Restrictions.eq("estatus", "ACTIVO")).add(Restrictions.eq("estatus", "PENDIENTE")));
            } else {
                criteria.add(Restrictions.eq("estatus", (String) params.get("estatus")));
                countCriteria.add(Restrictions.eq("estatus", (String) params.get("estatus")));
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
            
//            if (params.containsKey("maestroId") || params.containsKey("alumnoId")) {
//                if (params.containsKey("maestroId") && params.containsKey("alumnoId")) {
//                    criteria.createAlias("maestro", "m");
//                    criteria.createAlias("alumnos", "a");
//                    countCriteria.createAlias("maestro","m");
//                    countCriteria.createAlias("alumnos", "a");
//                    Disjunction x = Restrictions.disjunction();
//                    x.add(Restrictions.eq("m.id", params.get("maestroId")));
//                    x.add(Restrictions.eq("a.alumno.id", params.get("alumnoId")));
//                    criteria.add(x);
//                    countCriteria.add(x);
//                } else if(params.containsKey("maestroId")) {
//                    criteria.createCriteria("maestro").add(Restrictions.idEq(params.get("maestroId")));
//                    countCriteria.createCriteria("maestro").add(Restrictions.idEq(params.get("maestroId")));
//                } else {
//                    criteria.createCriteria("alumnos").add(Restrictions.eq("alumno.id", params.get("alumnoId")));
//                    countCriteria.createCriteria("alumnos").add(Restrictions.eq("alumno.id", params.get("alumnoId")));
//                }
//            }
            
            criteria.setFirstResult(offset);
            criteria.setMaxResults(max);
            cursos = criteria.list();
        } else {
            criteria.addOrder(Order.desc("inicia"));
            cursos = criteria.list();
        }

        countCriteria.setProjection(Projections.rowCount());
        cantidades = countCriteria.list();

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
        xcurso.setMaestroId(curso.getMaestro().getId());
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
        xcurso.setMaestroId(curso.getMaestro().getId());
        hibernateTemplate.save(xcurso);
        hibernateTemplate.delete(curso);
    }

    public Sesion creaSesion(Sesion sesion) {
        if (sesion.getDuracion() > 0) {
            Long id = (Long) hibernateTemplate.save(sesion);
            sesion.setId(id);
            return sesion;
        } else {
            log.debug("Hora inicial: {} | Duracion: {}", sesion.getHoraInicial(), sesion.getDuracion());
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
        log.debug("Creando alumno con id {}", alumno.getId());
        hibernateTemplate.save(alumno);
        alumno.setVersion(0);
        return alumno;
    }

    public AlumnoCurso preInscribeAlumno(AlumnoCurso alumnoCurso) {
        alumnoCurso.setEstatus(Constantes.PENDIENTE);
        alumnoCurso.setFecha(new Date());
        if (alumnoCurso.getId() != null && alumnoCurso.getVersion() != null) {
            hibernateTemplate.update(alumnoCurso);
        } else {
            Long id = (Long) hibernateTemplate.save(alumnoCurso);
            alumnoCurso.setId(id);
            alumnoCurso.setVersion(0);
        }
        // HISTORIAL
        XAlumnoCurso xalumnoCurso = new XAlumnoCurso();
        BeanUtils.copyProperties(alumnoCurso, xalumnoCurso);
        xalumnoCurso.setAlumnoCursoId(alumnoCurso.getId());
        xalumnoCurso.setAlumnoId(alumnoCurso.getAlumno().getId());
        xalumnoCurso.setCursoId(alumnoCurso.getCurso().getId());
        xalumnoCurso.setAccion(Constantes.CREAR);
        hibernateTemplate.save(xalumnoCurso);

        return alumnoCurso;
    }

    public AlumnoCurso inscribeAlumno(AlumnoCurso alumnoCurso) {
        alumnoCurso.setEstatus(Constantes.INSCRITO);
        hibernateTemplate.update(alumnoCurso);

        // HISTORIAL
        XAlumnoCurso xalumnoCurso = new XAlumnoCurso();
        BeanUtils.copyProperties(alumnoCurso, xalumnoCurso);
        xalumnoCurso.setAlumnoCursoId(alumnoCurso.getId());
        xalumnoCurso.setAlumnoId(alumnoCurso.getAlumno().getId());
        xalumnoCurso.setCursoId(alumnoCurso.getCurso().getId());
        xalumnoCurso.setAccion(Constantes.ACTUALIZAR);
        hibernateTemplate.save(xalumnoCurso);

        return alumnoCurso;
    }

    public AlumnoCurso rechazaAlumno(AlumnoCurso alumnoCurso) {
        alumnoCurso.setEstatus(Constantes.RECHAZADO);
        hibernateTemplate.update(alumnoCurso);

        // HISTORIAL
        XAlumnoCurso xalumnoCurso = new XAlumnoCurso();
        BeanUtils.copyProperties(alumnoCurso, xalumnoCurso);
        xalumnoCurso.setAlumnoCursoId(alumnoCurso.getId());
        xalumnoCurso.setAlumnoId(alumnoCurso.getAlumno().getId());
        xalumnoCurso.setCursoId(alumnoCurso.getCurso().getId());
        xalumnoCurso.setAccion(Constantes.ACTUALIZAR);
        hibernateTemplate.save(xalumnoCurso);

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

        // HISTORIAL
        XAlumnoCurso xalumnoCurso = new XAlumnoCurso();
        BeanUtils.copyProperties(alumnoCurso, xalumnoCurso);
        xalumnoCurso.setAlumnoCursoId(alumnoCurso.getId());
        xalumnoCurso.setAlumnoId(alumnoCurso.getAlumno().getId());
        xalumnoCurso.setCursoId(alumnoCurso.getCurso().getId());
        xalumnoCurso.setAccion(Constantes.ACTUALIZAR);
        hibernateTemplate.save(xalumnoCurso);

        Curso curso = alumnoCurso.getCurso();
        hibernateTemplate.refresh(curso);
        DetachedCriteria criteria = DetachedCriteria.forClass(AlumnoCurso.class);
        criteria.add(Restrictions.eq("curso", curso));
        criteria.add(Restrictions.isNotNull("evaluacion"));
        List<AlumnoCurso> evaluaciones = hibernateTemplate.findByCriteria(criteria);
        BigDecimal total = new BigDecimal("0");
        for (AlumnoCurso x : evaluaciones) {
            total = total.add(x.getEvaluacion());
        }
        BigDecimal evaluacion = total.divide(new BigDecimal(evaluaciones.size()));
        curso.setEvaluacion(evaluacion);
        curso.setCantidadEvaluaciones(evaluaciones.size());
        hibernateTemplate.update(curso);

        XCurso xcurso = new XCurso();
        BeanUtils.copyProperties(curso, xcurso);
        xcurso.setCursoId(curso.getId());
        xcurso.setAccion(Constantes.ACTUALIZAR);
        xcurso.setCreadorId(alumnoCurso.getAlumno().getId());
        xcurso.setMaestroId(curso.getMaestro().getId());
        hibernateTemplate.save(xcurso);

        alumnoCurso.setCurso(curso);
        return alumnoCurso;
    }

    public AlumnoCurso califica(AlumnoCurso alumnoCurso) {
        hibernateTemplate.update(alumnoCurso);

        // HISTORIAL
        XAlumnoCurso xalumnoCurso = new XAlumnoCurso();
        BeanUtils.copyProperties(alumnoCurso, xalumnoCurso);
        xalumnoCurso.setAlumnoCursoId(alumnoCurso.getId());
        xalumnoCurso.setAlumnoId(alumnoCurso.getAlumno().getId());
        xalumnoCurso.setCursoId(alumnoCurso.getCurso().getId());
        xalumnoCurso.setAccion(Constantes.ACTUALIZAR);
        hibernateTemplate.save(xalumnoCurso);

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


        XCurso xcurso = new XCurso();
        BeanUtils.copyProperties(curso, xcurso);
        xcurso.setCursoId(curso.getId());
        xcurso.setAccion(Constantes.ACTUALIZAR);
        xcurso.setCreadorId(alumnoCurso.getCurso().getMaestro().getId());
        xcurso.setMaestroId(curso.getMaestro().getId());
        hibernateTemplate.save(xcurso);


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
        List<Alumno> alumnos = hibernateTemplate.findByNamedParam("select a from Alumno a where a.id = :alumnoId", "alumnoId", usuario.getUserId());
        if (alumnos != null && alumnos.size() > 0) {
            alumno = alumnos.get(0);
        } else {
            alumno = new Alumno(usuario);
            hibernateTemplate.save(alumno);
            alumno.setVersion(0);
        }
        return alumno;
    }

    public Boolean existeSesionActiva(Long cursoId, Calendar calendario) {
        Integer dia = calendario.get(Calendar.DAY_OF_WEEK);
        Date hoy = calendario.getTime();
        log.debug("existeSesionActiva: {} {} {}", new Object[]{cursoId, dia, hoy});
        boolean resultado = false;
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createQuery("select sesion from Sesion sesion where sesion.curso.id = :cursoId and sesion.dia = :dia and :hoy between sesion.curso.inicia and sesion.curso.termina");
        query.setParameter("cursoId", cursoId);
        query.setParameter("dia", dia);
        query.setParameter("hoy", hoy);
        Sesion sesion = (Sesion) query.uniqueResult();
        if (sesion != null) {
            Calendar cal = Calendar.getInstance(calendario.getTimeZone());
            cal.setTime(sesion.getHoraInicial());
            calendario.set(Calendar.HOUR, cal.get(Calendar.HOUR));
            calendario.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            Date inicia = calendario.getTime();
            log.debug("Encontre una sesion activa que inicia a las {}", inicia);
            long diff = (hoy.getTime() - inicia.getTime()) / (60 * 1000);
            log.debug("TEST {} vs {}", new Date(hoy.getTime()), new Date(inicia.getTime()));
            log.debug("Con una diferencia de {} es menor que {}", diff, sesion.getDuracion());
            if (diff < sesion.getDuracion() && diff > -30) {
                resultado = true;
            }
        } else {
            log.debug("No encontre una sesion activa");
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

    public AlumnoCurso obtieneAlumnoCurso(Long alumnoCursoId) {
        AlumnoCurso alumnoCurso = hibernateTemplate.get(AlumnoCurso.class, alumnoCursoId);
        return alumnoCurso;
    }

    public AlumnoCurso obtieneAlumnoCurso(User alumno, Curso curso) {
        AlumnoCurso alumnoCurso = null;
        List<AlumnoCurso> alumnos = hibernateTemplate.findByNamedParam("from AlumnoCurso where alumno.id = :alumnoId and curso = :curso", new String[]{"alumnoId", "curso"}, new Object[]{alumno.getUserId(), curso});
        if (alumnos != null && alumnos.size() > 0) {
            alumnoCurso = alumnos.get(0);
        }
        return alumnoCurso;
    }

    public void guardaAsistencia(Long alumnoCursoId) {
        log.debug("Buscando alumnoCurso {}", alumnoCursoId);
        AlumnoCurso alumnoCurso = hibernateTemplate.get(AlumnoCurso.class, alumnoCursoId);
        log.debug("Guardando asistencia de alumno {} al curso {}", alumnoCurso.getAlumno().getId(), alumnoCurso.getCurso().getId());
        if (alumnoCurso != null) {
            Asistencia asistencia = new Asistencia(alumnoCurso, new Date());
            log.debug("Guardando la asistencia");
            Long id = (Long) hibernateTemplate.save(asistencia);
            log.debug("Se ha guardado la asistencia {}", id);
        } else {
            throw new RuntimeException("No se pudo guardar la asistencia de " + alumnoCurso.getAlumno().getId() + " al curso " + alumnoCurso.getCurso().getId());
        }
    }

    public Maestro obtieneMaestro(Long maestroId) {
        log.debug("Buscando maestro {}", maestroId);
        return hibernateTemplate.get(Maestro.class, maestroId);
    }

    public Maestro registraMaestro(Maestro maestro) {
        hibernateTemplate.save(maestro);
        return maestro;
    }

    public Maestro refreshMaestro(Maestro maestro) {
        hibernateTemplate.refresh(maestro);
        return maestro;
    }

    public List<Contenido> obtieneContenidos(Long cursoId) {
        log.debug("Buscando los contenidos del curso {}", cursoId);
        List<Contenido> contenidos = hibernateTemplate.findByNamedParam("select contenido from Contenido contenido where contenido.curso.id = :cursoId order by orden", "cursoId", cursoId);
        return contenidos;
    }

    public void creaContenido(Contenido contenido) {
        log.debug("Creando contenido para el curso {}", contenido.getCurso().getId());
        hibernateTemplate.save(contenido);
    }

    public Contenido obtieneContenido(Long contenidoId) {
        log.debug("Buscando el contenido {}", contenidoId);
        Contenido contenido = hibernateTemplate.get(Contenido.class, contenidoId);
        return contenido;
    }
    
    public Map<String, Object> obtieneMisCursos(Map<String, Object> params) {
        StringBuilder query = new StringBuilder();
        query.append("select distinct new Curso(curso.id, curso.nombre, curso.inicia, maestro.id, maestro.nombreCompleto, curso.tipo, curso.estatus) ");
        query.append("from Curso curso inner join curso.maestro as maestro left outer join curso.alumnos as alumnos ");
        query.append("where curso.comunidadId in (:comunidades) ");
        query.append("and (curso.maestro.id = :maestroId or alumnos.alumno.id = :maestroId) ");
        query.append("order by curso.inicia desc");
        List<Curso> cursos = hibernateTemplate.findByNamedParam(query.toString(), 
                new String[]{"comunidades","maestroId"}, 
                new Object[]{params.get("comunidades"),params.get("maestroId")});
        
        log.debug("Cursos: {}", cursos);

        Map<String, Object> resultados = new HashMap<String, Object>();
        resultados.put("cursos", cursos);
        resultados.put("cantidad", new Long(cursos.size()));
        return resultados;
    }
}
