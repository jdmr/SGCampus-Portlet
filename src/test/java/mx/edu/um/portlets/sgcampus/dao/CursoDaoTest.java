package mx.edu.um.portlets.sgcampus.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.Assert;
import mx.edu.um.portlets.sgcampus.utils.Constantes;
import mx.edu.um.portlets.sgcampus.model.Alumno;
import mx.edu.um.portlets.sgcampus.model.AlumnoCurso;
import mx.edu.um.portlets.sgcampus.model.Curso;
import mx.edu.um.portlets.sgcampus.model.Etiqueta;
import mx.edu.um.portlets.sgcampus.model.Sesion;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Test;

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
            Curso curso = new Curso(nombre, nombre, nombre, 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
            cursoDao.crea(curso, 1L);
        }

        for (int i = 1; i <= 10; i++) {
            sb = new StringBuilder();
            sb.append("TEST");
            sb.append(i);
            String nombre = sb.toString();
            Curso curso = new Curso(nombre, nombre, nombre, 2L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
            cursoDao.crea(curso, 1L);
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
            Curso curso = new Curso(nombre, nombre, nombre, 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
            cursoDao.crea(curso, 1L);
        }

        for (int i = 1; i <= 10; i++) {
            sb = new StringBuilder();
            sb.append("TEST");
            sb.append(i);
            String nombre = sb.toString();
            Curso curso = new Curso(nombre, nombre, nombre, 2L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
            cursoDao.crea(curso, 1L);
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

    @Test
    public void debieraCrearCursoDuplicadoEnOtraComunidad() {
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        cursoDao.crea(curso, 1L);
        
        // prueba
        Curso curso2 = new Curso("TEST-1", "TEST-1", "TEST-1", 2L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        curso = cursoDao.crea(curso2, 1L);
        Assert.assertNotNull(curso2);
        Assert.assertNotNull(curso2.getId());
    }
    
    @Test
    public void debieraModificarCurso() {
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        curso = cursoDao.crea(curso, 1L);

        // prueba
        curso = cursoDao.obtiene(curso.getId());
        curso.setNombre("TEST-2");
        curso = cursoDao.actualiza(curso, 1L);
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
        Curso curso = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        curso = cursoDao.crea(curso, 1L);
        
        // prueba
        cursoDao.elimina(curso.getId(), 1L);
        curso = cursoDao.obtiene(curso.getId());
        Assert.assertNull(curso);
    }
    
    @Test
    public void debieraCrearUnaSesion() {
        log.debug("Debiera crear una sesion");
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        curso = cursoDao.crea(curso, 1L);
        log.debug("inicializado");

        // prueba
        DateTime date3 = date.plusHours(2);
        log.debug("{} | {}", date, date3);
        Sesion sesion = new Sesion(1,date.toDate(), 240);
        sesion.setCurso(curso);
        log.debug("Creando sesion");
        sesion = cursoDao.creaSesion(sesion);
        log.debug("Sesion {}", sesion);
        
        // validaciones
        Assert.assertNotNull(sesion);
        Assert.assertNotNull(sesion.getId());
        curso = cursoDao.refresh(curso);
        log.debug("Curso {}", curso);
        log.debug("Sesiones {}", curso.getSesiones());
        if (curso.getSesiones() != null && curso.getSesiones().size() == 1) {
            for(Sesion x : curso.getSesiones()) {
                Assert.assertEquals(sesion, x);
            }
        } else {
            Assert.fail("Debiera haber sesiones");
        }
    }
    
    @Test
    public void debieraEliminarUnaSesion() {
        log.debug("Debiera eliminar una sesion");
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        curso = cursoDao.crea(curso, 1L);
        log.debug("inicializado");

        // prueba
        DateTime date3 = date.plusHours(2);
        Sesion sesion = new Sesion(1,date.toDate(), 240);
        sesion.setCurso(curso);
        sesion = cursoDao.creaSesion(sesion);
        
        // validaciones
        Assert.assertNotNull(sesion);
        Assert.assertNotNull(sesion.getId());
        curso = cursoDao.refresh(curso);
        if (curso.getSesiones() != null && curso.getSesiones().size() == 1) {
            for(Sesion x : curso.getSesiones()) {
                Assert.assertEquals(sesion, x);
                
                Long sesionId = sesion.getId();
                cursoDao.eliminaSesion(sesionId);
                
                Sesion y = cursoDao.obtieneSesion(sesionId);
                Assert.assertNull("No deberia de encontrar la sesion",y);
            }
        } else {
            Assert.fail("Debiera haber sesiones");
        }
    }
    
    @Test
    public void debieraInscribirAlumno() {
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        curso = cursoDao.crea(curso, 1L);
        
        Alumno alumno = new Alumno();
        alumno.setAlumnoId(1L);
        alumno.setUsuario("admin");
        alumno.setNombreCompleto("Admin User");
        alumno.setCorreo("admin@test.com");
        alumno.setFecha(new Date());
        alumno = cursoDao.creaAlumno(alumno);
        
        log.debug("inicializado");
        AlumnoCurso alumnoCurso = new AlumnoCurso();
        alumnoCurso.setAlumno(alumno);
        alumnoCurso.setCurso(curso);
        alumnoCurso.setAlta(new Date());
        alumnoCurso.setUsuarioAlta(1L);
        alumnoCurso.setUsuarioAltaNombre("Admin User");
        alumnoCurso = cursoDao.preInscribeAlumno(alumnoCurso);

        Assert.assertNotNull(alumnoCurso);
        alumnoCurso = cursoDao.refreshAlumnoCurso(alumnoCurso);
        Assert.assertNotNull(alumnoCurso);
        Assert.assertNotNull(alumnoCurso.getId());
        Assert.assertEquals(Constantes.PENDIENTE, alumnoCurso.getEstatus());
        
        alumnoCurso = cursoDao.inscribeAlumno(alumnoCurso);
        
        Assert.assertNotNull(alumnoCurso);
        Assert.assertNotNull(alumnoCurso);
        Assert.assertNotNull(alumnoCurso.getId());
        Assert.assertEquals(Constantes.INSCRITO, alumnoCurso.getEstatus());
    }

    @Test
    public void debieraEvaluarCurso() {
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        curso = cursoDao.crea(curso, 1L);
        
        Alumno alumno = new Alumno();
        alumno.setAlumnoId(1L);
        alumno.setUsuario("admin");
        alumno.setNombreCompleto("Admin User");
        alumno.setCorreo("admin@test.com");
        alumno.setFecha(new Date());
        alumno = cursoDao.creaAlumno(alumno);
        
        Alumno alumno2 = new Alumno();
        alumno2.setAlumnoId(2L);
        alumno2.setUsuario("admin2");
        alumno2.setNombreCompleto("Admin2 User");
        alumno2.setCorreo("admin2@test.com");
        alumno2.setFecha(new Date());
        alumno2 = cursoDao.creaAlumno(alumno2);
        
        AlumnoCurso alumnoCurso = new AlumnoCurso();
        alumnoCurso.setAlumno(alumno);
        alumnoCurso.setCurso(curso);
        alumnoCurso.setAlta(new Date());
        alumnoCurso.setUsuarioAlta(1L);
        alumnoCurso.setUsuarioAltaNombre("Admin User");
        alumnoCurso = cursoDao.preInscribeAlumno(alumnoCurso);
        Assert.assertNotNull(alumnoCurso);
        alumnoCurso = cursoDao.refreshAlumnoCurso(alumnoCurso);
        Assert.assertNotNull(alumnoCurso);
        Assert.assertNotNull(alumnoCurso.getId());
        Assert.assertEquals(Constantes.PENDIENTE, alumnoCurso.getEstatus());

        AlumnoCurso alumnoCurso2 = new AlumnoCurso();
        alumnoCurso2.setAlumno(alumno2);
        alumnoCurso2.setCurso(curso);
        alumnoCurso2.setAlta(new Date());
        alumnoCurso2.setUsuarioAlta(2L);
        alumnoCurso2.setUsuarioAltaNombre("Admin2 User");
        alumnoCurso2 = cursoDao.preInscribeAlumno(alumnoCurso2);
        Assert.assertNotNull(alumnoCurso2);
        alumnoCurso2 = cursoDao.refreshAlumnoCurso(alumnoCurso2);
        Assert.assertNotNull(alumnoCurso2);
        Assert.assertNotNull(alumnoCurso2.getId());
        Assert.assertEquals(Constantes.PENDIENTE, alumnoCurso2.getEstatus());

        alumnoCurso = cursoDao.inscribeAlumno(alumnoCurso);
        alumnoCurso2 = cursoDao.inscribeAlumno(alumnoCurso2);
        
        log.debug("inicializado");
        alumnoCurso.setEvaluacion(5);
        alumnoCurso2.setEvaluacion(4);
        
        alumnoCurso = cursoDao.evaluacion(alumnoCurso);
        alumnoCurso2 = cursoDao.evaluacion(alumnoCurso2);
        
        curso = alumnoCurso2.getCurso();
        Assert.assertEquals(new BigDecimal("4.5"), curso.getEvaluacion());
    }
    
    @Test
    public void debieraEvaluarAAlumno() {
        // inicializacion
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        curso = cursoDao.crea(curso, 1L);
        
        Alumno alumno = new Alumno();
        alumno.setAlumnoId(1L);
        alumno.setUsuario("admin");
        alumno.setNombreCompleto("Admin User");
        alumno.setCorreo("admin@test.com");
        alumno.setFecha(new Date());
        alumno = cursoDao.creaAlumno(alumno);
        
        AlumnoCurso alumnoCurso = new AlumnoCurso();
        alumnoCurso.setAlumno(alumno);
        alumnoCurso.setCurso(curso);
        alumnoCurso.setAlta(new Date());
        alumnoCurso.setUsuarioAlta(1L);
        alumnoCurso.setUsuarioAltaNombre("Admin User");
        alumnoCurso = cursoDao.preInscribeAlumno(alumnoCurso);

        Assert.assertNotNull(alumnoCurso);
        alumnoCurso = cursoDao.refreshAlumnoCurso(alumnoCurso);
        Assert.assertNotNull(alumnoCurso);
        Assert.assertNotNull(alumnoCurso.getId());
        Assert.assertEquals(Constantes.PENDIENTE, alumnoCurso.getEstatus());
        
        alumnoCurso = cursoDao.inscribeAlumno(alumnoCurso);
        
        Assert.assertNotNull(alumnoCurso);
        Assert.assertNotNull(alumnoCurso.getId());
        Assert.assertEquals(Constantes.INSCRITO, alumnoCurso.getEstatus());
        
        log.debug("inicializado");
        alumnoCurso.setCalificacion(new BigDecimal(100));
        
        cursoDao.califica(alumnoCurso);
        
        alumnoCurso = cursoDao.refreshAlumnoCurso(alumnoCurso);
        Assert.assertNotNull(alumnoCurso);
        Assert.assertEquals(new BigDecimal("100").intValue(), alumnoCurso.getCalificacion().intValue());
        Assert.assertEquals(new BigDecimal("100").intValue(), curso.getCalificacion().intValue());
    }
    
    @Test
    public void debieraCrearCursoConEtiquetas() {
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        Set<Etiqueta> etiquetas = new HashSet<Etiqueta>();
        etiquetas.add(new Etiqueta("TEST1", 1L));
        etiquetas.add(new Etiqueta("TEST2", 1L));
        curso.setEtiquetas(etiquetas);
        curso = cursoDao.crea(curso, 1L);
        
        curso = cursoDao.refresh(curso);
        Assert.assertNotNull(curso.getEtiquetas());
        for(Etiqueta etiqueta : curso.getEtiquetas()) {
            Assert.assertTrue(etiqueta.getNombre().startsWith("TEST"));
        }
    }
    
    public void debieraEncontrarCusoConEtiqueta() {
        DateTime date = new DateTime();
        DateTime date2 = date.plusMonths(1);
        Curso curso  = new Curso("TEST-1", "TEST-1", "TEST-1", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        Curso curso2 = new Curso("TEST-2", "TEST-2", "TEST-2", 1L, "TEST", 1L, "MAESTRO1", date.toDate(), date2.toDate(), "http://www.yahoo.com", "PATROCINADO");
        Set<Etiqueta> etiquetas = new HashSet<Etiqueta>();
        etiquetas.add(new Etiqueta("TEST", 1L));
        etiquetas.add(new Etiqueta("PRUEBA", 1L));
        curso.setEtiquetas(etiquetas);
        curso  = cursoDao.crea(curso,  1L);
        curso2 = cursoDao.crea(curso2, 1L);

        List<Curso> cursos = cursoDao.buscaPorEtiqueta(new Etiqueta("TEST", 1L));
        Assert.assertNotNull(cursos);
        boolean encontrado = false;
        for(Curso x : cursos) {
            if (x.getNombre().equals("TEST-1")) {
                encontrado = true;
            }
            if (x.getNombre().equals("TEST-2")) {
                Assert.fail("Este curso no tiene esa etiqueta");
            }
        }
        Assert.assertTrue("Debio encontrar el curso", encontrado);
    }
    
}
