package mx.edu.um.portlets.sgcampus.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.portlets.sgcampus.model.Curso;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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

    public Curso crea(Curso curso) {
        log.info("Creando el curso {}", curso);
        Long id = (Long) hibernateTemplate.save(curso);
        curso.setId(id);
        curso.setVersion(0);
        return curso;
    }

    @Transactional(readOnly=true)
    public Map<String, Object> busca(Map<String, Object> params) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Curso.class);
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
            }
            
            if (params.containsKey("comunidades")) {
                criteria.add(Restrictions.in("comunidadId", (Set<Long>) params.get("comunidades")));
            }
            
            Integer max = 0;
            if (params.get("max") != null) {
                max = (Integer) params.get("max");
            }
            Integer offset = 0;
            if (params.get("offset") != null) {
                offset = (Integer) params.get("offset");
            }
            
            cursos = hibernateTemplate.findByCriteria(criteria, offset, max);
        } else {
            cursos = hibernateTemplate.findByCriteria(criteria);
        }
        criteria.setProjection(Projections.rowCount());
        cantidades = hibernateTemplate.findByCriteria(criteria);
        Map<String, Object> resultados = new HashMap<String, Object>();
        resultados.put("cursos", cursos);
        resultados.put("cantidad", cantidades.get(0));
        return resultados;
    }

    public Curso actualiza(Curso curso) {
        hibernateTemplate.update(curso);
        return curso;
    }
    
    @Transactional(readOnly=true)
    public Curso obtiene(Long id) {
        Curso curso = hibernateTemplate.get(Curso.class, id);
        return curso;
    }
    
    public void elimina(Long id) {
        Curso curso = hibernateTemplate.load(Curso.class, id);
        hibernateTemplate.delete(curso);
    }
}
