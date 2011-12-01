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
public class WebinarDao {

    private static final Logger log = LoggerFactory.getLogger(WebinarDao.class);
    private HibernateTemplate hibernateTemplate;

    public WebinarDao() {
        log.info("Nueva instancia del dao de webinars");
    }

    @Autowired
    protected void setSessionFactory(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public Webinar crea(Webinar webinar, Long creadorId) {
        log.info("Creando el webinar {}", webinar);

        // La fecha de inicio debe ser antes de la de termino
        if (webinar.getInicia() != null && webinar.getTermina() != null && webinar.getInicia().after(webinar.getTermina())) {
            throw new RuntimeException("La fecha inicial debe ser antes de la que termina");
        }

        // Asignando codigo si no tiene uno
        if (webinar.getCodigo() == null) {
            log.debug("Asignando codigo");
            List<Folio> folios = hibernateTemplate.findByNamedParam("select folio from Folio folio where folio.nombre = :nombre and folio.comunidadId = :comunidadId", new String[]{"nombre", "comunidadId"}, new Object[]{Constantes.WEBINAR, webinar.getComunidadId()});
            Folio folio = null;
            if (folios != null && folios.size() > 0) {
                folio = folios.get(0);
            } else {
                folio = new Folio(Constantes.WEBINAR, 0L, webinar.getComunidadId());
            }
            folio.setValor(folio.getValor() + 1);
            NumberFormat nf = DecimalFormat.getInstance();
            nf.setGroupingUsed(false);
            nf.setMinimumIntegerDigits(7);
            webinar.setCodigo("U" + nf.format(folio.getValor()));
        }

        // Creando webinar
        Long id = (Long) hibernateTemplate.save(webinar);
        webinar.setId(id);
        webinar.setVersion(0);

        return webinar;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> busca(Map<String, Object> params) {
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Webinar.class);
        Criteria countCriteria = session.createCriteria(Webinar.class);
        List<Curso> webinars;
        List<Long> cantidades;
        if (params != null) {
            if (params.containsKey("filtro") && ((String) params.get("filtro")).trim().length() > 0) {
                String filtro = "%" + ((String) params.get("filtro")).trim() + "%";
                log.debug("Buscando webinars por {}", filtro);
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
                        
            criteria.setFirstResult(offset);
            criteria.setMaxResults(max);
            webinars = criteria.list();
        } else {
            criteria.addOrder(Order.desc("inicia"));
            webinars = criteria.list();
        }

        countCriteria.setProjection(Projections.rowCount());
        cantidades = countCriteria.list();

        Map<String, Object> resultados = new HashMap<String, Object>();
        resultados.put("webinars", webinars);
        resultados.put("cantidad", cantidades.get(0));
        return resultados;
    }

    public Webinar actualiza(Webinar webinar, Long creadorId) {
        hibernateTemplate.update(webinar);
        return webinar;
    }

    @Transactional(readOnly = true)
    public Webinar obtiene(Long id) {
        Webinar webinar = hibernateTemplate.get(Webinar.class, id);
        return webinar;
    }

    @Transactional(readOnly = true)
    public Webinar refresh(Webinar webinar) {
        hibernateTemplate.refresh(webinar);
        return webinar;
    }

    public void elimina(Long id, Long creadorId) {
        Webinar webinar = hibernateTemplate.load(Webinar.class, id);
        hibernateTemplate.delete(webinar);
    }

    public Alumno creaAlumno(Alumno alumno) {
        log.debug("Creando alumno con id {}", alumno.getId());
        hibernateTemplate.save(alumno);
        alumno.setVersion(0);
        return alumno;
    }

    public AlumnoWebinar preInscribeAlumno(AlumnoWebinar alumnoWebinar) {
        alumnoWebinar.setEstatus(Constantes.PENDIENTE);
        alumnoWebinar.setFecha(new Date());
        if (alumnoWebinar.getId() != null && alumnoWebinar.getVersion() != null) {
            hibernateTemplate.update(alumnoWebinar);
        } else {
            Long id = (Long) hibernateTemplate.save(alumnoWebinar);
            alumnoWebinar.setId(id);
            alumnoWebinar.setVersion(0);
        }

        return alumnoWebinar;
    }

    public AlumnoWebinar inscribeAlumno(AlumnoWebinar alumnoWebinar) {
        alumnoWebinar.setEstatus(Constantes.INSCRITO);
        hibernateTemplate.update(alumnoWebinar);

        return alumnoWebinar;
    }

    public AlumnoWebinar rechazaAlumno(AlumnoWebinar alumnoWebinar) {
        alumnoWebinar.setEstatus(Constantes.RECHAZADO);
        hibernateTemplate.update(alumnoWebinar);

        return alumnoWebinar;
    }

    @Transactional(readOnly = true)
    public AlumnoWebinar refreshAlumnoWebinar(AlumnoWebinar alumnoWebinar) {
        hibernateTemplate.refresh(alumnoWebinar);
        return alumnoWebinar;
    }

    public List<AlumnoWebinar> obtieneAlumnos(Webinar webinar) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AlumnoWebinar.class);
        criteria.add(Restrictions.eq("webinar", webinar));
        return hibernateTemplate.findByCriteria(criteria);
    }

    public AlumnoWebinar evaluacion(AlumnoWebinar alumnoWebinar) {
        hibernateTemplate.update(alumnoWebinar);

        Webinar webinar = alumnoWebinar.getWebinar();
        hibernateTemplate.refresh(webinar);
        DetachedCriteria criteria = DetachedCriteria.forClass(AlumnoWebinar.class);
        criteria.add(Restrictions.eq("webinar", webinar));
        criteria.add(Restrictions.isNotNull("evaluacion"));
        List<AlumnoWebinar> evaluaciones = hibernateTemplate.findByCriteria(criteria);
        BigDecimal total = new BigDecimal("0");
        for (AlumnoWebinar x : evaluaciones) {
            total = total.add(x.getEvaluacion());
        }
        BigDecimal evaluacion = total.divide(new BigDecimal(evaluaciones.size()));
        webinar.setEvaluacion(evaluacion);
        webinar.setCantidadEvaluaciones(evaluaciones.size());
        hibernateTemplate.update(webinar);

        alumnoWebinar.setWebinar(webinar);
        return alumnoWebinar;
    }

    public AlumnoWebinar califica(AlumnoWebinar alumnoWebinar) {
        hibernateTemplate.update(alumnoWebinar);

        Webinar webinar = alumnoWebinar.getWebinar();
        hibernateTemplate.refresh(webinar);
        DetachedCriteria criteria = DetachedCriteria.forClass(AlumnoWebinar.class);
        criteria.add(Restrictions.eq("webinar", webinar));
        criteria.add(Restrictions.isNotNull("calificacion"));
        List<AlumnoWebinar> evaluaciones = hibernateTemplate.findByCriteria(criteria);
        BigDecimal total = new BigDecimal("0");
        for (AlumnoWebinar x : evaluaciones) {
            total = x.getCalificacion().add(total);
        }
        BigDecimal calificacionPromedio = total.divide(new BigDecimal(evaluaciones.size()));
        webinar.setCalificacion(calificacionPromedio);
        hibernateTemplate.update(webinar);
        alumnoWebinar.setWebinar(webinar);

        return alumnoWebinar;
    }

    public Alumno obtieneAlumno(User usuario) {
        Alumno alumno;
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

    public Boolean existeSesionActiva(Long webinarId, Calendar calendario) {
        Integer dia = calendario.get(Calendar.DAY_OF_WEEK);
        Date hoy = calendario.getTime();
        log.debug("existeSesionActiva: {} {} {}", new Object[]{webinarId, dia, hoy});
        boolean resultado = false;
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Query query = session.createQuery("select sesion from Sesion sesion where sesion.webinar.id = :webinarId and sesion.dia = :dia and :hoy between sesion.webinar.inicia and sesion.webinar.termina");
        query.setParameter("webinarId", webinarId);
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

    public AlumnoWebinar obtieneAlumno(Alumno alumno, Webinar webinar) {
        AlumnoWebinar alumnoWebinar = null;
        List<AlumnoWebinar> alumnos = hibernateTemplate.findByNamedParam("from AlumnoWebinar where alumno = :alumno and webinar = :webinar", new String[]{"alumno", "webinar"}, new Object[]{alumno, webinar});
        if (alumnos != null && alumnos.size() > 0) {
            alumnoWebinar = alumnos.get(0);
        }
        return alumnoWebinar;
    }

    public AlumnoWebinar obtieneAlumnoWebinar(Long alumnoWebinarId) {
        AlumnoWebinar alumnoWebinar = hibernateTemplate.get(AlumnoWebinar.class, alumnoWebinarId);
        return alumnoWebinar;
    }

    public AlumnoWebinar obtieneAlumnoWebinar(User alumno, Webinar webinar) {
        AlumnoWebinar alumnoWebinar = null;
        List<AlumnoWebinar> alumnos = hibernateTemplate.findByNamedParam("from AlumnoWebinar where alumno.id = :alumnoId and webinar = :webinar", new String[]{"alumnoId", "webinar"}, new Object[]{alumno.getUserId(), webinar});
        if (alumnos != null && alumnos.size() > 0) {
            alumnoWebinar = alumnos.get(0);
        }
        return alumnoWebinar;
    }

    public void guardaAsistencia(Long alumnoWebinarId) {
        log.debug("Buscando alumnoWebinar {}", alumnoWebinarId);
        AlumnoWebinar alumnoWebinar = hibernateTemplate.get(AlumnoWebinar.class, alumnoWebinarId);
        log.debug("Guardando asistencia de alumno {} al webinar {}", alumnoWebinar.getAlumno().getId(), alumnoWebinar.getWebinar().getId());
        if (alumnoWebinar != null) {
            Asistencia asistencia = new Asistencia(alumnoWebinar, new Date());
            log.debug("Guardando la asistencia");
            Long id = (Long) hibernateTemplate.save(asistencia);
            log.debug("Se ha guardado la asistencia {}", id);
        } else {
            throw new RuntimeException("No se pudo guardar la asistencia de " + alumnoWebinar.getAlumno().getId() + " al webinar " + alumnoWebinar.getWebinar().getId());
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

    public Map<String, Object> obtieneMisWebinars(Map<String, Object> params) {
        StringBuilder query = new StringBuilder();
        query.append("select distinct new Webinar(webinar.id, webinar.nombre, webinar.inicia, maestro.id, maestro.nombreCompleto, webinar.tipo, webinar.estatus) ");
        query.append("from Webinar webinar inner join webinar.maestro as maestro left outer join webinar.alumnos as alumnos ");
        query.append("where webinar.comunidadId in (:comunidades) ");
        query.append("and (webinar.maestro.id = :maestroId or alumnos.alumno.id = :maestroId) ");
        query.append("order by webinar.inicia desc");
        List<Webinar> webinars = hibernateTemplate.findByNamedParam(query.toString(), 
                new String[]{"comunidades","maestroId"}, 
                new Object[]{params.get("comunidades"),params.get("maestroId")});
        
        log.debug("Webinars: {}", webinars);

        Map<String, Object> resultados = new HashMap<String, Object>();
        resultados.put("webinars", webinars);
        resultados.put("cantidad", new Long(webinars.size()));
        return resultados;
    }
}
