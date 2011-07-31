package mx.edu.um.portlets.sgcampus.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.Assert;
import mx.edu.um.portlets.sgcampus.model.Curso;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

/**
 *
 * @author jdmr
 */
@RunWith(SpringJUnit4ClassRunner.class)
// specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations = {"/context/applicationContext.xml"})
@Transactional
public class CursoDaoTest {

    private static final Logger log = LoggerFactory.getLogger(CursoDaoTest.class);
    @Autowired
    private CursoDao cursoDao;

    public CursoDaoTest() {
    }

    @Test
    public void debieraObtenerListaDeCursos() {
        // Inicializacion
        StringBuilder sb;
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        for (int i = 1; i <= 20; i++) {
            sb = new StringBuilder();
            sb.append("TEST");
            sb.append(i);
            String nombre = sb.toString();
            Curso curso = new Curso(nombre, nombre, 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
            cursoDao.crea(curso);
        }

        for (int i = 1; i <= 10; i++) {
            sb = new StringBuilder();
            sb.append("TEST");
            sb.append(i);
            String nombre = sb.toString();
            Curso curso = new Curso(nombre, nombre, 2L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
            cursoDao.crea(curso);
        }

        // Prueba
        Set<Long> comunidades = new HashSet<Long>();
        comunidades.add(1L);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("max", 10);
        params.put("comunidades", comunidades);
        Map<String, Object> resultado = cursoDao.busca(params);
        Assert.assertNotNull(resultado);
        List<Curso> cursos = (List<Curso>) resultado.get("cursos");
        Long cantidad = (Long) resultado.get("cantidad");
        Assert.assertNotNull(cursos);
        Assert.assertNotNull(cantidad);
        Assert.assertTrue(cantidad >= 20);
        Assert.assertTrue(cursos.size() == 10);
    }

    @Test
    public void debieraObtenerListaFiltradaDeCursos() {
        // Inicializacion
        StringBuilder sb;
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        for (int i = 1; i <= 20; i++) {
            sb = new StringBuilder();
            sb.append("TEST");
            sb.append(i);
            String nombre = sb.toString();
            Curso curso = new Curso(nombre, nombre, 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
            cursoDao.crea(curso);
        }

        for (int i = 1; i <= 10; i++) {
            sb = new StringBuilder();
            sb.append("TEST");
            sb.append(i);
            String nombre = sb.toString();
            Curso curso = new Curso(nombre, nombre, 2L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
            cursoDao.crea(curso);
        }

        // Prueba
        Set<Long> comunidades = new HashSet<Long>();
        comunidades.add(1L);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("filtro", "TEST1");
        params.put("comunidades",comunidades);
        Map<String, Object> resultado = cursoDao.busca(params);
        Assert.assertNotNull(resultado);
        List<Curso> cursos = (List<Curso>) resultado.get("cursos");
        Long cantidad = (Long) resultado.get("cantidad");
        log.debug("RESULTADOS: {}", resultado);
        Assert.assertNotNull(cursos);
        Assert.assertNotNull(cantidad);
        Assert.assertTrue(cantidad == 11);
        Assert.assertTrue(cursos.size() == 11);
    }

    @Test(expected=DataIntegrityViolationException.class)
    public void noDebieraCrearCursoDuplicado() {
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
        cursoDao.crea(curso);
        
        // prueba
        Curso curso2 = new Curso("TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
        cursoDao.crea(curso2);
        Assert.fail("Debio de lanzar excepcion de curso duplicado");
    }

    @Test
    public void debieraCrearCursoDuplicadoEnOtraComunidad() {
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
        cursoDao.crea(curso);
        
        // prueba
        Curso curso2 = new Curso("TEST-1", "TEST-1", 2L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
        curso = cursoDao.crea(curso2);
        Assert.assertNotNull(curso2);
        Assert.assertNotNull(curso2.getId());
    }
    
    @Test
    public void debieraModificarCurso() {
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
        curso = cursoDao.crea(curso);

        // prueba
        curso = cursoDao.obtiene(curso.getId());
        curso.setNombre("TEST-2");
        curso = cursoDao.actualiza(curso);
        Assert.assertNotNull(curso);
        Assert.assertEquals("TEST-2", curso.getNombre());
        
        curso = cursoDao.obtiene(curso.getId());
        Assert.assertNotNull(curso);
        Assert.assertEquals("TEST-2", curso.getNombre());
    }
    
    @Test
    public void debieraEliminarCurso() {
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com");
        curso = cursoDao.crea(curso);
        
        // prueba
        cursoDao.elimina(curso.getId());
        curso = cursoDao.obtiene(curso.getId());
        Assert.assertNull(curso);
    }
}
