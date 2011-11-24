package mx.edu.um.portlets.sgcampus.web;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.*;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.service.AssetEntryServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.portlet.*;
import mx.edu.um.portlets.sgcampus.dao.CursoDao;
import mx.edu.um.portlets.sgcampus.model.*;
import mx.edu.um.portlets.sgcampus.utils.ComunidadUtil;
import mx.edu.um.portlets.sgcampus.utils.Constantes;
import mx.edu.um.portlets.sgcampus.utils.CursoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Administrador de cursos
 *
 * @author jdmr
 */
@Controller
@RequestMapping("VIEW")
public class CursoPortlet {

    private static final Logger log = LoggerFactory.getLogger(CursoPortlet.class);
    @Autowired
    private CursoDao cursoDao;
    @Autowired
    private CursoValidator cursoValidator;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    private Curso curso;
    private Sesion sesion;
    private Contenido contenido;

    public CursoPortlet() {
        log.info("Nueva instancia de Curso Portlet ha sido creada");
    }

    @InitBinder
    public void inicializar(PortletRequestDataBinder binder) {
        if (binder.getTarget() instanceof Curso) {
            binder.setValidator(cursoValidator);
            binder.registerCustomEditor(Date.class, null, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), false));
//            binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        }
    }

    @RequestMapping
    public String lista(RenderRequest request,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "max", required = false) Integer max,
            @RequestParam(value = "direccion", required = false) String direccion,
            Model modelo) throws PortalException, SystemException {

        curso = null;


        if (max == null) {
            max = new Integer(5);
        }
        if (offset == null) {
            offset = new Integer(0);
        } else if (direccion.equals("siguiente")) {
            offset = offset + max;
        } else if (direccion.equals("anterior") && offset > 0) {
            offset = offset - max;
        }

        Map<Long, String> comunidades = ComunidadUtil.obtieneComunidades(request);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("max", max);
        params.put("offset", offset);
        params.put("comunidades", comunidades.keySet());

        params = cursoDao.busca(params);
        modelo.addAttribute("cursos", params.get("cursos"));
        modelo.addAttribute("cantidad", params.get("cantidad"));
        modelo.addAttribute("max", max);
        modelo.addAttribute("offset", offset);
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            modelo.addAttribute("puedeAutorizar", true);
        }

        return "curso/lista";
    }

    @RequestMapping(params = "action=pendientes")
    public String pendientes(RenderRequest request,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "max", required = false) Integer max,
            @RequestParam(value = "direccion", required = false) String direccion,
            Model modelo) throws PortalException, SystemException {

        log.debug("Filtrando por pendientes");

        curso = null;


        if (max == null) {
            max = new Integer(5);
        }
        if (offset == null) {
            offset = new Integer(0);
        } else if (direccion.equals("siguiente")) {
            offset = offset + max;
        } else if (direccion.equals("anterior") && offset > 0) {
            offset = offset - max;
        }

        Map<Long, String> comunidades = ComunidadUtil.obtieneComunidades(request);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("max", max);
        params.put("offset", offset);
        params.put("comunidades", comunidades.keySet());
        params.put("estatus", Constantes.PENDIENTE);

        params = cursoDao.busca(params);
        modelo.addAttribute("cursos", params.get("cursos"));
        modelo.addAttribute("cantidad", params.get("cantidad"));
        modelo.addAttribute("max", max);
        modelo.addAttribute("offset", offset);
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            modelo.addAttribute("puedeAutorizar", true);
        }

        return "curso/lista";
    }

    @RequestMapping(params = "action=rechazados")
    public String rechazados(RenderRequest request,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "max", required = false) Integer max,
            @RequestParam(value = "direccion", required = false) String direccion,
            Model modelo) throws PortalException, SystemException {

        log.debug("Filtrando por rechazados");

        curso = null;


        if (max == null) {
            max = new Integer(5);
        }
        if (offset == null) {
            offset = new Integer(0);
        } else if (direccion.equals("siguiente")) {
            offset = offset + max;
        } else if (direccion.equals("anterior") && offset > 0) {
            offset = offset - max;
        }

        Map<Long, String> comunidades = ComunidadUtil.obtieneComunidades(request);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("max", max);
        params.put("offset", offset);
        params.put("comunidades", comunidades.keySet());
        params.put("estatus", Constantes.RECHAZADO);

        params = cursoDao.busca(params);
        modelo.addAttribute("cursos", params.get("cursos"));
        modelo.addAttribute("cantidad", params.get("cantidad"));
        modelo.addAttribute("max", max);
        modelo.addAttribute("offset", offset);
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            modelo.addAttribute("puedeAutorizar", true);
        }

        return "curso/lista";
    }

    @RequestMapping(params = "action=nuevo")
    public String nuevo(RenderRequest request, Model model) throws SystemException, PortalException {
        log.debug("Nuevo curso");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        curso = new Curso();
        model.addAttribute("curso", curso);
        model.addAttribute("contenido", UnicodeFormatter.toString(curso.getDescripcion()));
        model.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        model.addAttribute("tipos", this.getTipos(themeDisplay));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (curso.getInicia() != null) {
            model.addAttribute("inicia", sdf.format(curso.getInicia()));
        }
        if (curso.getTermina() != null) {
            model.addAttribute("termina", sdf.format(curso.getTermina()));
        }

        User user = PortalUtil.getUser(request);
        if (user != null) {
            if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
                return "curso/nuevo";
            } else {
                return "curso/nuevoUsuario";
            }
        } else {
            return "curso/sinPrivilegios";
        }
    }

    @RequestMapping(params = "action=nuevoError")
    public String nuevoError(RenderRequest request, Model model) throws SystemException, PortalException {
        log.debug("Hubo algun error y regresamos a editar el nuevo curso");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        model.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        model.addAttribute("tipos", this.getTipos(themeDisplay));
        model.addAttribute("curso", curso);
        model.addAttribute("contenido", UnicodeFormatter.toString(curso.getDescripcion()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (curso.getInicia() != null) {
            String inicia = sdf.format(curso.getInicia());
            model.addAttribute("inicia", inicia);
        }
        if (curso.getTermina() != null) {
            model.addAttribute("termina", sdf.format(curso.getTermina()));
        }

        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            return "curso/nuevo";
        } else {
            return "curso/nuevoUsuario";
        }
    }

    @Transactional
    @RequestMapping(params = "action=crea")
    public void crea(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model, SessionStatus sessionStatus) throws PortalException, SystemException {
        log.debug("Creando el curso");
        this.curso = curso;
        String[] tags;
        log.debug("getTags():{}", curso.getTags());
        if (curso.getTags() != null && !",".equals(curso.getTags().trim())) {
            tags = StringUtils.delimitedListToStringArray(curso.getTags() + curso.getCodigo(), ",");
            log.debug("Resultado de delimited: {}", tags);
        } else {
            tags = new String[]{curso.getCodigo()};
            log.debug("Resultado de crearlo directo: {}", tags);
        }

        curso.setComunidadNombre(GroupLocalServiceUtil.getGroup(curso.getComunidadId()).getDescriptiveName());
        cursoValidator.validate(curso, result);
        if (!result.hasErrors()) {
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
            try {
                User creador = PortalUtil.getUser(request);
                Maestro maestro = cursoDao.obtieneMaestro(curso.getMaestro().getId());
                if (maestro == null) {
                    User maestroLiferay = UserLocalServiceUtil.getUser(curso.getMaestro().getId());
                    maestro = new Maestro(maestroLiferay);
                    maestro = cursoDao.registraMaestro(maestro);
                }
                curso.setMaestro(maestro);

                // Creando contenido dentro de liferay
                ServiceContext serviceContext = ServiceContextFactory.getInstance(JournalArticle.class.getName(), request);
                log.debug("TAGS: {}", tags);
                serviceContext.setAssetTagNames(tags);

                Calendar displayDate;
                if (themeDisplay != null) {
                    displayDate = CalendarFactoryUtil.getCalendar(themeDisplay.getTimeZone(), themeDisplay.getLocale());
                } else {
                    displayDate = CalendarFactoryUtil.getCalendar();
                }

                //String descripcion = HtmlUtil.fromInputSafe(curso.getDescripcion());
                //String descripcion = curso.getDescripcion();
                String descripcion = ParamUtil.getString(request, "descripcion");

                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version='1.0' encoding='UTF-8'?><root><static-content><![CDATA[");
                sb.append(descripcion);
                sb.append("]]></static-content></root>");
                descripcion = sb.toString();
                                
                curso.setDescripcion(descripcion);
                log.debug("Descripcion: {}", descripcion);
                
                JournalArticle article = JournalArticleLocalServiceUtil.addArticle(
                        creador.getUserId(), // UserId
                        curso.getComunidadId(), // GroupId
                        "", // ArticleId
                        true, // AutoArticleId
                        JournalArticleConstants.DEFAULT_VERSION, // Version
                        curso.getNombre(), // Titulo
                        null, // Descripcion
                        descripcion, // Contenido
                        "general", // Tipo
                        "", // Estructura
                        "", // Template
                        displayDate.get(Calendar.MONTH), // displayDateMonth,
                        displayDate.get(Calendar.DAY_OF_MONTH), // displayDateDay,
                        displayDate.get(Calendar.YEAR), // displayDateYear,
                        displayDate.get(Calendar.HOUR_OF_DAY), // displayDateHour,
                        displayDate.get(Calendar.MINUTE), // displayDateMinute,
                        0, // expirationDateMonth, 
                        0, // expirationDateDay, 
                        0, // expirationDateYear, 
                        0, // expirationDateHour, 
                        0, // expirationDateMinute, 
                        true, // neverExpire
                        0, // reviewDateMonth, 
                        0, // reviewDateDay, 
                        0, // reviewDateYear, 
                        0, // reviewDateHour, 
                        0, // reviewDateMinute, 
                        true, // neverReview
                        true, // indexable
                        false, // SmallImage
                        "", // SmallImageUrl
                        null, // SmallFile
                        null, // Images
                        "", // articleURL 
                        serviceContext // serviceContext
                        );

                curso.setDescripcionId(article.getPrimaryKey());

                curso = cursoDao.crea(curso, creador.getUserId());

                response.setRenderParameter("action", "ver");
                response.setRenderParameter("cursoId", curso.getId().toString());
                sessionStatus.setComplete();
            } catch (DataIntegrityViolationException e) {
                log.error("No se pudo crear el curso", e);
                StringBuilder sb = new StringBuilder(curso.getDescripcion());
                sb.insert(0, messageSource.getMessage("curso.descripcion.demasiado.grande", null, themeDisplay.getLocale()));
                curso.setDescripcion(sb.toString());
                response.setRenderParameter("action", "nuevoError");
            }
        } else {
            log.error("No se pudo crear el curso");
            response.setRenderParameter("action", "nuevoError");
        }
    }

    @Transactional
    @RequestMapping(params = "action=creaUsuario")
    public void creaUsuario(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model, SessionStatus sessionStatus) throws PortalException, SystemException {
        log.debug("Creando curso por el usuario");
        this.curso = curso;
        String[] tags;
        if (curso.getTags() != null && !",".equals(curso.getTags().trim())) {
            tags = StringUtils.delimitedListToStringArray(curso.getTags() + curso.getCodigo(), ",");
            log.debug("Resultado de delimited: {}", tags);
        } else if (curso.getCodigo() != null) {
            tags = new String[]{curso.getCodigo()};
            log.debug("Resultado de crearlo directo: {}", tags);
        } else {
            tags = new String[0];
        }
        curso.setComunidadNombre(GroupLocalServiceUtil.getGroup(curso.getComunidadId()).getDescriptiveName());

        User user = PortalUtil.getUser(request);
        Maestro maestro = cursoDao.obtieneMaestro(user.getUserId());
        if (maestro == null) {
            maestro = new Maestro(user);
            maestro = cursoDao.registraMaestro(maestro);
        }
        curso.setMaestro(maestro);
        curso.setTipo("PATROCINADO");
        curso.setUrl("URL-INVALIDA");
        curso.setEstatus("PENDIENTE");

        cursoValidator.validate(curso, result);
        if (!result.hasErrors()) {
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
            try {
                // Creando contenido dentro de liferay
                ServiceContext serviceContext = ServiceContextFactory.getInstance(JournalArticle.class.getName(), request);
                serviceContext.setAssetTagNames(tags);

                Calendar displayDate;
                if (themeDisplay != null) {
                    displayDate = CalendarFactoryUtil.getCalendar(themeDisplay.getTimeZone(), themeDisplay.getLocale());
                } else {
                    displayDate = CalendarFactoryUtil.getCalendar();
                }

                //String descripcion = HtmlUtil.fromInputSafe(curso.getDescripcion());
                String descripcion = ParamUtil.getString(request, "descripcion");

                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version='1.0' encoding='UTF-8'?><root><static-content><![CDATA[");
                sb.append(descripcion);
                sb.append("]]></static-content></root>");
                descripcion = sb.toString();
                                
                curso.setDescripcion(descripcion);
                log.debug("Contenido: {}",descripcion);
                JournalArticle article = JournalArticleLocalServiceUtil.addArticle(
                        user.getUserId(), // UserId
                        curso.getComunidadId(), // GroupId
                        "", // ArticleId
                        true, // AutoArticleId
                        JournalArticleConstants.DEFAULT_VERSION, // Version
                        curso.getNombre(), // Titulo
                        null, // Descripcion
                        descripcion, // Contenido
                        "general", // Tipo
                        "", // Estructura
                        "", // Template
                        displayDate.get(Calendar.MONTH), // displayDateMonth,
                        displayDate.get(Calendar.DAY_OF_MONTH), // displayDateDay,
                        displayDate.get(Calendar.YEAR), // displayDateYear,
                        displayDate.get(Calendar.HOUR_OF_DAY), // displayDateHour,
                        displayDate.get(Calendar.MINUTE), // displayDateMinute,
                        0, // expirationDateMonth, 
                        0, // expirationDateDay, 
                        0, // expirationDateYear, 
                        0, // expirationDateHour, 
                        0, // expirationDateMinute, 
                        true, // neverExpire
                        0, // reviewDateMonth, 
                        0, // reviewDateDay, 
                        0, // reviewDateYear, 
                        0, // reviewDateHour, 
                        0, // reviewDateMinute, 
                        true, // neverReview
                        true, // indexable
                        false, // SmallImage
                        "", // SmallImageUrl
                        null, // SmallFile
                        null, // Images
                        "", // articleURL 
                        serviceContext // serviceContext
                        );

                curso.setDescripcionId(article.getPrimaryKey());

                curso = cursoDao.crea(curso, user.getUserId());
                response.setRenderParameter("action", "ver");
                response.setRenderParameter("cursoId", curso.getId().toString());
                sessionStatus.setComplete();
            } catch (DataIntegrityViolationException e) {
                log.error("No se pudo crear el curso", e);
                StringBuilder sb = new StringBuilder(curso.getDescripcion());
                sb.insert(0, messageSource.getMessage("curso.descripcion.demasiado.grande", null, themeDisplay.getLocale()));
                curso.setDescripcion(sb.toString());
                response.setRenderParameter("action", "nuevoError");
            }
        } else {
            log.error("No se pudo crear el curso");
            response.setRenderParameter("action", "nuevoError");
        }
    }

    @ResourceMapping(value = "buscaMaestro")
    public void buscaMaestro(@RequestParam("term") String maestroNombre, ResourceRequest request, ResourceResponse response) throws IOException, SystemException {
        log.debug("Buscando maestros que contengan {}", maestroNombre);
        JSONArray results = JSONFactoryUtil.createJSONArray();

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        List<User> usuarios = UserLocalServiceUtil.search(themeDisplay.getCompanyId(), maestroNombre, true, null, QueryUtil.ALL_POS, QueryUtil.ALL_POS, (OrderByComparator) null);
        for (User usuario : usuarios) {
            JSONObject listEntry = JSONFactoryUtil.createJSONObject();

            listEntry.put("id", usuario.getPrimaryKey());
            listEntry.put("value", usuario.getFullName() + " | " + usuario.getScreenName() + " | " + usuario.getEmailAddress());
            listEntry.put("nombre", usuario.getFullName());

            results.put(listEntry);
        }

        PrintWriter writer = response.getWriter();
        writer.println(results.toString());
    }

    @ResourceMapping(value = "asignaMaestro")
    public void asignaMaestro(@RequestParam("id") Long maestroId, ResourceRequest request, ResourceResponse response) throws IOException, SystemException, PortalException {
        log.debug("Asignando maestro {}", maestroId);
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        StringBuilder sb = new StringBuilder();
        User user = UserLocalServiceUtil.getUser(maestroId);
        sb.append("<table><thead><tr>");
        sb.append("<th>");
        sb.append(messageSource.getMessage("nombre", null, themeDisplay.getLocale()));
        sb.append("</th><th>").append(messageSource.getMessage("usuario", null, themeDisplay.getLocale())).append("</th>");
        sb.append("</th><th>").append(messageSource.getMessage("correo", null, themeDisplay.getLocale())).append("</th>");
        sb.append("</tr></thead>");
        sb.append("<tbody><tr><td>");
        sb.append(user.getFullName());
        sb.append("</td><td>");
        sb.append(user.getScreenName());
        sb.append("</td><td>");
        sb.append(user.getEmailAddress());
        sb.append("</td></tbody></table>");

        PrintWriter writer = response.getWriter();
        writer.println(sb.toString());
    }

    @RequestMapping(params = "action=ver")
    public String ver(RenderRequest request,
            @RequestParam(value = "cursoId") Long cursoId,
            Model modelo) throws PortalException, SystemException {

        AlumnoCurso alumnoCurso = null;
        curso = cursoDao.obtiene(cursoId);

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        JournalArticle ja = JournalArticleLocalServiceUtil.getArticle(curso.getDescripcionId());
        AssetEntryServiceUtil.incrementViewCounter(JournalArticle.class.getName(), ja.getResourcePrimKey());
        String contenido2 = JournalArticleLocalServiceUtil.getArticleContent(ja.getGroupId(), ja.getArticleId(), "view", "" + themeDisplay.getLocale(), themeDisplay);
        log.debug("Contenido: {}",contenido2);
        curso.setDescripcion(contenido2);

        modelo.addAttribute("curso", curso);
        User creador = PortalUtil.getUser(request);
        boolean puedeEditar = false;
        if (request.isUserInRole("Administrator")
                || request.isUserInRole("cursos-admin")
                || (creador != null && creador.getUserId() == curso.getMaestro().getId())) {
            puedeEditar = true;
            modelo.addAttribute("puedeEditar", puedeEditar);
        }

        // Si no es el administrador / creador del curso y este no esta
        // activo no mostrar detalle.
        if (!puedeEditar && !curso.getEstatus().equals(Constantes.ACTIVO)) {
            return lista(request, null, null, null, modelo);
        }
        
        if (creador != null) {
            // Si no es el maestro
            if (creador.getUserId() != curso.getMaestro().getId()) { 
                log.debug("Buscando alumno por {}", creador.getUserId());
                Alumno alumno = cursoDao.obtieneAlumno(creador);
                if (alumno == null) {
                    log.debug("Creando alumno");
                    alumno = new Alumno(creador);
                    log.debug("con el id {}", alumno.getId());
                    alumno = cursoDao.creaAlumno(alumno);
                }
                alumnoCurso = cursoDao.obtieneAlumno(alumno, curso);
                if (alumnoCurso != null) {
                    modelo.addAttribute("alumnoCurso", alumnoCurso);

                    modelo.addAttribute("estatus", messageSource.getMessage(alumnoCurso.getEstatus(), null, themeDisplay.getLocale()));
                    // Validar su estatus
                    if (alumnoCurso.getEstatus().equals(Constantes.INSCRITO)) {
                        // Validar si puede entrar
                        Calendar cal = Calendar.getInstance(themeDisplay.getTimeZone());
                        boolean existeSesionActiva = cursoDao.existeSesionActiva(cursoId, cal);
                        if (existeSesionActiva) {
                            modelo.addAttribute("existeSesionActiva", true);
                        }
                    }
                } else {
                    modelo.addAttribute("puedeInscribirse", true);
                }
            }
        } else {
            modelo.addAttribute("noPuedeInscribirse", true);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm z");
        sdf.setTimeZone(themeDisplay.getTimeZone());
        List<Sesion> sesiones = cursoDao.obtieneSesiones(curso);
        for (Sesion x : sesiones) {
            x.setSdf(sdf);
        }
        modelo.addAttribute("sesiones", sesiones);

        return "curso/ver";
    }

    @RequestMapping(params = "action=edita")
    public String edita(RenderRequest request,
            @RequestParam(value = "cursoId") Long cursoId,
            Model modelo) throws PortalException, SystemException {

        curso = cursoDao.obtiene(cursoId);

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        JournalArticle ja = JournalArticleLocalServiceUtil.getArticle(curso.getDescripcionId());
        AssetEntryServiceUtil.incrementViewCounter(JournalArticle.class.getName(), ja.getResourcePrimKey());
        String contenido2 = JournalArticleLocalServiceUtil.getArticleContent(ja.getGroupId(), ja.getArticleId(), "view", "" + themeDisplay.getLocale(), themeDisplay);
        curso.setDescripcion(contenido2);

        modelo.addAttribute("curso", curso);
        modelo.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        modelo.addAttribute("tipos", this.getTipos(themeDisplay));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (curso.getInicia() != null) {
            String inicia = sdf.format(curso.getInicia());
            modelo.addAttribute("inicia", inicia);
        }
        if (curso.getTermina() != null) {
            modelo.addAttribute("termina", sdf.format(curso.getTermina()));
        }
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            Map<String, String> tiposDeEstatus = new LinkedHashMap<String, String>();
            tiposDeEstatus.put("ACTIVO", messageSource.getMessage("ACTIVO", null, themeDisplay.getLocale()));
            tiposDeEstatus.put("PENDIENTE", messageSource.getMessage("PENDIENTE", null, themeDisplay.getLocale()));
            tiposDeEstatus.put("RECHAZADO", messageSource.getMessage("RECHAZADO", null, themeDisplay.getLocale()));
            modelo.addAttribute("tiposDeEstatus", tiposDeEstatus);
            return "curso/edita";
        } else {
            return "curso/editaUsuario";
        }
    }

    @RequestMapping(params = "action=editaError")
    public String editaError(RenderRequest request,
            @RequestParam(value = "cursoId") Long cursoId,
            Model modelo) throws PortalException, SystemException {

        modelo.addAttribute("curso", curso);
        modelo.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        modelo.addAttribute("tipos", this.getTipos(themeDisplay));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (curso.getInicia() != null) {
            String inicia = sdf.format(curso.getInicia());
            modelo.addAttribute("inicia", inicia);
        }
        if (curso.getTermina() != null) {
            modelo.addAttribute("termina", sdf.format(curso.getTermina()));
        }
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            Map<String, String> tiposDeEstatus = new LinkedHashMap<String, String>();
            tiposDeEstatus.put("ACTIVO", messageSource.getMessage("ACTIVO", null, themeDisplay.getLocale()));
            tiposDeEstatus.put("PENDIENTE", messageSource.getMessage("PENDIENTE", null, themeDisplay.getLocale()));
            tiposDeEstatus.put("RECHAZADO", messageSource.getMessage("RECHAZADO", null, themeDisplay.getLocale()));
            modelo.addAttribute("tiposDeEstatus", tiposDeEstatus);
            return "curso/edita";
        } else {
            return "curso/editaUsuario";
        }
    }

    @Transactional
    @RequestMapping(params = "action=actualiza")
    public void actualiza(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Guardando el curso");
        curso.setMaestro(cursoDao.refreshMaestro(curso.getMaestro()));
        this.curso = curso;
        cursoValidator.validate(curso, result);
        if (!result.hasErrors()) {
            try {
                User creador = PortalUtil.getUser(request);

                String texto = curso.getDescripcion();

                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version='1.0' encoding='UTF-8'?><root><static-content><![CDATA[");
                sb.append(texto);
                sb.append("]]></static-content></root>");
                texto = sb.toString();
                                
                JournalArticle ja = JournalArticleLocalServiceUtil.getArticle(curso.getDescripcionId());
                JournalArticleLocalServiceUtil.updateContent(ja.getGroupId(), ja.getArticleId(), ja.getVersion(), texto);

                cursoDao.actualiza(curso, creador.getUserId());
                response.setRenderParameter("action", "ver");
                response.setRenderParameter("cursoId", curso.getId().toString());
                sessionStatus.setComplete();
            } catch (Exception e) {
                log.error("No se pudo actualizar el curso", e);
                response.setRenderParameter("action", "editaError");
                response.setRenderParameter("cursoId", curso.getId().toString());
            }
        } else {
            log.error("No se pudo actualizar el curso");
            response.setRenderParameter("action", "editaError");
            response.setRenderParameter("cursoId", curso.getId().toString());
        }
    }

    @Transactional
    @RequestMapping(params = "action=actualizaUsuario")
    public void actualizaUsuario(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Guardando el curso");
        this.curso = curso;
        Curso viejo = cursoDao.obtiene(curso.getId());
        curso.setCodigo(viejo.getCodigo());
        curso.setMaestro(viejo.getMaestro());
        curso.setTipo(viejo.getTipo());
        curso.setUrl(viejo.getUrl());
        curso.setEstatus(viejo.getEstatus());
        cursoValidator.validate(curso, result);
        if (!result.hasErrors()) {
            try {
                User creador = PortalUtil.getUser(request);

                String texto = curso.getDescripcion();

                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version='1.0' encoding='UTF-8'?><root><static-content><![CDATA[");
                sb.append(texto);
                sb.append("]]></static-content></root>");
                texto = sb.toString();
                                
                JournalArticle ja = JournalArticleLocalServiceUtil.getArticle(curso.getDescripcionId());
                JournalArticleLocalServiceUtil.updateContent(ja.getGroupId(), ja.getArticleId(), ja.getVersion(), texto);

                cursoDao.actualiza(curso, creador.getUserId());
                response.setRenderParameter("action", "ver");
                response.setRenderParameter("cursoId", curso.getId().toString());
                sessionStatus.setComplete();
            } catch (Exception e) {
                log.error("No se pudo actualizar el curso", e);
                response.setRenderParameter("action", "editaError");
                response.setRenderParameter("cursoId", curso.getId().toString());
            }
        } else {
            log.error("No se pudo actualizar el curso");
            response.setRenderParameter("action", "editaError");
            response.setRenderParameter("cursoId", curso.getId().toString());
        }
    }

    @RequestMapping(params = "action=elimina")
    public void elimina(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model, SessionStatus sessionStatus, @RequestParam("cursoId") Long id) {
        log.debug("Eliminando el curso {}", id);
        try {
            curso = cursoDao.obtiene(id);
            this.curso = curso;
            User creador = PortalUtil.getUser(request);
            if (request.isUserInRole("Administrator")
                    || request.isUserInRole("cursos-admin")
                    || (creador != null && creador.getUserId() == curso.getMaestro().getId())) {

                cursoDao.elimina(id, creador.getUserId());
                // TODO Elimina contenido
                this.curso = null;
                sessionStatus.setComplete();
            } else {
                throw new RuntimeException("No tiene permisos para eliminar este curso");
            }
        } catch (Exception e) {
            log.error("No se pudo eliminar el curso " + id, e);
            response.setRenderParameter("action", "ver");
            response.setRenderParameter("cursoId", id.toString());
        }
    }

    @RequestMapping(params = "action=nuevaSesion")
    public String nuevaSesion(RenderRequest request, @RequestParam("cursoId") Long id, Model model) {
        log.debug("Nueva Sesion");
        curso = cursoDao.obtiene(id);

        sesion = new Sesion();
        sesion.setCurso(curso);

        model.addAttribute("sesion", sesion);
        model.addAttribute("dias", getDias(request));

        return "curso/nuevaSesion";
    }

    @RequestMapping(params = "action=creaSesion")
    public void creaSesion(ActionRequest request, ActionResponse response,
            @RequestParam String horaInicial,
            @ModelAttribute("sesion") Sesion sesion,
            BindingResult result,
            Model model, SessionStatus sessionStatus) throws PortalException, SystemException {
        log.debug("Creando la sesion");

        log.debug("Sesion {} {} {}", new Object[]{sesion.getDia(), sesion.getCurso().getId(), horaInicial});

        if (sesion.getCurso().getId() != null && sesion.getCurso().getId() > 0) {
            sesion.setCurso(cursoDao.obtiene(sesion.getCurso().getId()));
        }

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(themeDisplay.getTimeZone());
        try {
            sesion.setHoraInicial(sdf.parse(horaInicial));
        } catch (ParseException ex) {
            log.error("No se pudo leer la hora inicial", ex);
            response.setRenderParameter("action", "nuevaSesion");
        }

        try {
            cursoDao.creaSesion(sesion);
            sessionStatus.setComplete();
            response.setRenderParameter("action", "ver");
            response.setRenderParameter("cursoId", curso.getId().toString());
        } catch (Exception e) {
            log.error("No se pudo crear la sesion", e);
            response.setRenderParameter("action", "ver");
            response.setRenderParameter("cursoId", curso.getId().toString());
        }

    }

    @RequestMapping(params = "action=eliminaSesion")
    public void eliminaSesion(ActionRequest request, ActionResponse response,
            @RequestParam Long sesionId,
            @RequestParam Long cursoId,
            @ModelAttribute("sesion") Sesion sesion,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Eliminando sesion {}", sesionId);

        try {
            curso = cursoDao.obtiene(cursoId);
            User creador = PortalUtil.getUser(request);
            if (request.isUserInRole("Administrator")
                    || request.isUserInRole("cursos-admin")
                    || (creador != null && creador.getUserId() == curso.getMaestro().getId())) {

                cursoDao.eliminaSesion(sesionId);
                sessionStatus.setComplete();
            } else {
                throw new RuntimeException("No tiene permisos para eliminar esta sesion");
            }
        } catch (Exception e) {
            log.error("No se pudo eliminar la sesion " + sesionId, e);
        }
        response.setRenderParameter("action", "ver");
        response.setRenderParameter("cursoId", cursoId.toString());
    }

    @RequestMapping(params = "action=inscribirse")
    public String inscribirse(RenderRequest request, @RequestParam Long cursoId, Model model) {
        log.debug("Solicitar inscripcion a curso {}", cursoId);
        try {
            User usuario = PortalUtil.getUser(request);
            if (usuario != null) {
                curso = cursoDao.obtiene(cursoId);
                if (curso.getTipo().equals(Constantes.PATROCINADO)) {
                    Alumno alumno = cursoDao.obtieneAlumno(usuario);
                    AlumnoCurso alumnoCurso = new AlumnoCurso();
                    alumnoCurso.setAlumno(alumno);
                    alumnoCurso.setCurso(curso);
                    alumnoCurso.setCreadorId(usuario.getUserId());
                    alumnoCurso.setCreadorNombre(usuario.getFullName());
                    cursoDao.preInscribeAlumno(alumnoCurso);
                    return "curso/preinscripcion";
                } else {
                    return ver(request, cursoId, model);
                }
            } else {
                return "curso/noInscribir";
            }
        } catch (Exception e) {
            log.error("No se pudo solicitar la inscripcion al curso " + cursoId, e);
        }
        return "curso/nopreincripcion";
    }

    @RequestMapping(params = "action=alumnosPorCurso")
    public String alumnosPorCurso(RenderRequest request, @RequestParam("cursoId") Long id, Model model) {
        log.debug("Alumnos por Curso {}", id);
        curso = cursoDao.obtiene(id);
        List<AlumnoCurso> alumnos = cursoDao.obtieneAlumnos(curso);

        model.addAttribute("curso", curso);
        model.addAttribute("alumnos", alumnos);

        return "curso/alumnos";
    }

    @RequestMapping(params = "action=inscribe")
    public void inscribe(ActionRequest request, ActionResponse response,
            @RequestParam Long alumnoCursoId,
            @RequestParam Long cursoId,
            @ModelAttribute("sesion") Sesion sesion,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Inscribiendo a {}", alumnoCursoId);

        AlumnoCurso alumnoCurso = null;
        try {
            alumnoCurso = cursoDao.obtieneAlumnoCurso(alumnoCursoId);
            User creador = PortalUtil.getUser(request);
            if (request.isUserInRole("Administrator")
                    || request.isUserInRole("cursos-admin")
                    || (creador != null && creador.getUserId() == curso.getMaestro().getId())) {

                cursoDao.inscribeAlumno(alumnoCurso);
                sessionStatus.setComplete();
            } else {
                throw new RuntimeException("No tiene permisos para inscribir este alumno");
            }
        } catch (Exception e) {
            log.error("No se pudo inscribir al alumno " + alumnoCursoId, e);
        }
        response.setRenderParameter("action", "alumnosPorCurso");
        response.setRenderParameter("cursoId", alumnoCurso.getCurso().getId().toString());
    }

    @RequestMapping(params = "action=pendiente")
    public void pendiente(ActionRequest request, ActionResponse response,
            @RequestParam Long alumnoCursoId,
            @RequestParam Long cursoId,
            @ModelAttribute("sesion") Sesion sesion,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Poniendo en pendiente a {}", alumnoCursoId);

        AlumnoCurso alumnoCurso = null;
        try {
            alumnoCurso = cursoDao.obtieneAlumnoCurso(alumnoCursoId);
            User creador = PortalUtil.getUser(request);
            if (request.isUserInRole("Administrator")
                    || request.isUserInRole("cursos-admin")
                    || (creador != null && creador.getUserId() == curso.getMaestro().getId())) {

                cursoDao.preInscribeAlumno(alumnoCurso);
                sessionStatus.setComplete();
            } else {
                throw new RuntimeException("No tiene permisos para pre-inscribir a este alumno");
            }
        } catch (Exception e) {
            log.error("No se pudo pre-inscribir al alumno " + alumnoCursoId, e);
        }
        response.setRenderParameter("action", "alumnosPorCurso");
        response.setRenderParameter("cursoId", alumnoCurso.getCurso().getId().toString());
    }

    @RequestMapping(params = "action=rechaza")
    public void rechaza(ActionRequest request, ActionResponse response,
            @RequestParam Long alumnoCursoId,
            @RequestParam Long cursoId,
            @ModelAttribute("sesion") Sesion sesion,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Rechazando a {}", alumnoCursoId);

        AlumnoCurso alumnoCurso = null;
        try {
            alumnoCurso = cursoDao.obtieneAlumnoCurso(alumnoCursoId);
            User creador = PortalUtil.getUser(request);
            if (request.isUserInRole("Administrator")
                    || request.isUserInRole("cursos-admin")
                    || (creador != null && creador.getUserId() == curso.getMaestro().getId())) {

                cursoDao.rechazaAlumno(alumnoCurso);
                sessionStatus.setComplete();
            } else {
                throw new RuntimeException("No tiene permisos para rechazar a este alumno");
            }
        } catch (Exception e) {
            log.error("No se pudo rechazar al alumno " + alumnoCursoId, e);
        }
        response.setRenderParameter("action", "alumnosPorCurso");
        response.setRenderParameter("cursoId", alumnoCurso.getCurso().getId().toString());
    }

    @RequestMapping(params = "action=entrar")
    public void entrar(ActionRequest request, ActionResponse response,
            @RequestParam Long alumnoCursoId,
            @RequestParam Long cursoId,
            @ModelAttribute("sesion") Sesion sesion,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Entrar a curso {}", cursoId);
        try {
            User usuario = PortalUtil.getUser(request);
            if (usuario != null) {
                cursoDao.guardaAsistencia(alumnoCursoId);
                response.sendRedirect(curso.getUrl());
            } else {
                log.error("No pudo entrar el alumno al curso {}", cursoId);
            }
        } catch (Exception e) {
            log.error("No pudo entrar el alumno al curso " + cursoId, e);
        }
    }

    @RequestMapping(params = "action=pagado")
    public void pagado(ActionRequest request, ActionResponse response,
            @RequestParam Long cursoId,
            @ModelAttribute("sesion") Sesion sesion,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("El alumno pago el curso {}", cursoId);
        try {
            User usuario = PortalUtil.getUser(request);
            if (usuario != null) {
                curso = cursoDao.obtiene(cursoId);
                log.debug("Curso {}", curso);
                Alumno alumno = cursoDao.obtieneAlumno(usuario);
                log.debug("Alumno {}", alumno);
                AlumnoCurso alumnoCurso = new AlumnoCurso();
                alumnoCurso.setAlumno(alumno);
                alumnoCurso.setCurso(curso);
                alumnoCurso.setCreadorId(usuario.getUserId());
                alumnoCurso.setCreadorNombre(usuario.getFullName());
                alumnoCurso = cursoDao.preInscribeAlumno(alumnoCurso);
                cursoDao.inscribeAlumno(alumnoCurso);

                response.setRenderParameter("action", "ver");
                response.setRenderParameter("cursoId", curso.getId().toString());
            } else {
                log.error("No pudo entrar el alumno al curso {}", cursoId);
            }
        } catch (Exception e) {
            log.error("No pudo entrar el alumno al curso " + cursoId, e);
        }
    }

    @RequestMapping(params = "action=noPagado")
    public void noPagado(ActionRequest request, ActionResponse response,
            @RequestParam Long cursoId,
            @ModelAttribute("sesion") Sesion sesion,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("El alumno no pago el curso {}", cursoId);
        response.setRenderParameter("action", "noPago");
    }

    @RequestMapping(params = "action=noPago")
    public String noPago(RenderRequest request, Model model) {
        log.debug("Mostrando pagina de NO PAGO");

        return "curso/noPago";
    }

    @RequestMapping(params = "action=contenidoLista")
    public String contenidoLista(RenderRequest request, @RequestParam Long cursoId, Model model) {
        log.debug("Lista de contenidos del curso {}", cursoId);
        List<Contenido> contenidos = cursoDao.obtieneContenidos(cursoId);
        model.addAttribute("contenidos", contenidos);
        model.addAttribute("cantidad", contenidos.size());
        model.addAttribute("cursoId", cursoId);

        return "curso/listaContenidos";
    }

    @RequestMapping(params = "action=nuevoContenido")
    public String nuevoContenido(RenderRequest request, @RequestParam Long cursoId, Model model) {
        log.debug("Nuevo contenido del curso {}", cursoId);

        curso = cursoDao.obtiene(cursoId);
        contenido = new Contenido();
        contenido.setTipo(Constantes.TEXTO);
        contenido.setCurso(curso);
        model.addAttribute("contenido", contenido);

        return "curso/nuevoContenido";
    }

    @Transactional
    @RequestMapping(params = "action=creaContenido")
    public void creaContenido(ActionRequest request, ActionResponse response,
            @ModelAttribute("contenido") Contenido contenido,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Creando contenido para el curso {}", contenido.getCurso().getId());
        try {
            curso = cursoDao.refresh(contenido.getCurso());
            contenido.setCurso(curso);
            // Creando contenido dentro de liferay
            ServiceContext serviceContext = ServiceContextFactory.getInstance(JournalArticle.class.getName(), request);
            //serviceContext.setAssetTagNames(tags);

            User user = PortalUtil.getUser(request);
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
            Calendar displayDate;
            if (themeDisplay != null) {
                displayDate = CalendarFactoryUtil.getCalendar(themeDisplay.getTimeZone(), themeDisplay.getLocale());
            } else {
                displayDate = CalendarFactoryUtil.getCalendar();
            }

            String texto = contenido.getTexto();

            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version='1.0' encoding='UTF-8'?><root><static-content><![CDATA[");
            sb.append(texto);
            sb.append("]]></static-content></root>");
            texto = sb.toString();

            log.debug("UsuarioId: {}", user.getUserId());
            log.debug("GroupId: {}", curso.getComunidadId());
            log.debug("Nombre: {}", contenido.getNombre());
            log.debug("Descripcion: {}", contenido.getDescripcion());
            log.debug("Contenido: {}", contenido.getTexto());
            JournalArticle article = JournalArticleLocalServiceUtil.addArticle(
                    user.getUserId(), // UserId
                    curso.getComunidadId(), // GroupId
                    "", // ArticleId
                    true, // AutoArticleId
                    JournalArticleConstants.DEFAULT_VERSION, // Version
                    contenido.getNombre(), // Titulo
                    contenido.getDescripcion(), // Descripcion
                    texto, // Contenido
                    "general", // Tipo
                    "", // Estructura
                    "", // Template
                    displayDate.get(Calendar.MONTH), // displayDateMonth,
                    displayDate.get(Calendar.DAY_OF_MONTH), // displayDateDay,
                    displayDate.get(Calendar.YEAR), // displayDateYear,
                    displayDate.get(Calendar.HOUR_OF_DAY), // displayDateHour,
                    displayDate.get(Calendar.MINUTE), // displayDateMinute,
                    0, // expirationDateMonth, 
                    0, // expirationDateDay, 
                    0, // expirationDateYear, 
                    0, // expirationDateHour, 
                    0, // expirationDateMinute, 
                    true, // neverExpire
                    0, // reviewDateMonth, 
                    0, // reviewDateDay, 
                    0, // reviewDateYear, 
                    0, // reviewDateHour, 
                    0, // reviewDateMinute, 
                    true, // neverReview
                    true, // indexable
                    false, // SmallImage
                    "", // SmallImageUrl
                    null, // SmallFile
                    null, // Images
                    "", // articleURL 
                    serviceContext // serviceContext
                    );
            log.debug("Articulo creado creando contenido");
            contenido.setContenidoId(article.getId());
            cursoDao.creaContenido(contenido);
            log.debug("Contenido creado regresando a lista de contenidos");
            response.setRenderParameter("action", "contenidoLista");
            response.setRenderParameter("cursoId", contenido.getCurso().getId().toString());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el contenido", e);
        }
    }

    @RequestMapping(params = "action=verContenido")
    public String verContenido(RenderRequest request, @RequestParam Long contenidoId, Model model) {
        log.debug("Viendo el contenido {}", contenidoId);

        try {
            contenido = cursoDao.obtieneContenido(contenidoId);

            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
            JournalArticle ja = JournalArticleLocalServiceUtil.getArticle(contenido.getContenidoId());
            AssetEntryServiceUtil.incrementViewCounter(JournalArticle.class.getName(), ja.getResourcePrimKey());
            String texto = JournalArticleLocalServiceUtil.getArticleContent(ja.getGroupId(), ja.getArticleId(), "view", "" + themeDisplay.getLocale(), themeDisplay);
            contenido.setTexto(texto);
            model.addAttribute("contenido", contenido);

            return "curso/verContenido";
        } catch (Exception e) {
            throw new RuntimeException("No se pudo ver el contenido", e);
        }

    }

    @RequestMapping(params = "action=nuevoVideo")
    public String nuevoVideo(RenderRequest request, @RequestParam Long cursoId, Model model) {
        log.debug("Nuevo video del curso {}", cursoId);
        curso = cursoDao.obtiene(cursoId);
        contenido = new Contenido();
        contenido.setTipo(Constantes.VIDEO);
        contenido.setCurso(curso);
        model.addAttribute("contenido", contenido);

        return "curso/nuevoVideo";
    }

    @Transactional
    @RequestMapping(params = "action=creaVideo")
    public void creaVideo(ActionRequest request, ActionResponse response,
            @ModelAttribute("contenido") Contenido contenido,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Creando video para el curso {}", contenido.getCurso().getId());
        MultipartFile video = contenido.getArchivo();
        if (video == null) {
            log.error("El video esta vacio");
        } else {
            log.debug("Subio un archivo tipo {}",video.getContentType());
        }
        try {
            curso = cursoDao.refresh(contenido.getCurso());
            contenido.setCurso(curso);
            // Creando contenido dentro de liferay
            ServiceContext serviceContext = ServiceContextFactory.getInstance(JournalArticle.class.getName(), request);
            //serviceContext.setAssetTagNames(tags);

            User user = PortalUtil.getUser(request);
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
            Calendar displayDate;
            if (themeDisplay != null) {
                displayDate = CalendarFactoryUtil.getCalendar(themeDisplay.getTimeZone(), themeDisplay.getLocale());
            } else {
                displayDate = CalendarFactoryUtil.getCalendar();
            }

            JournalArticle article = JournalArticleLocalServiceUtil.addArticle(
                    user.getUserId(), // UserId
                    curso.getComunidadId(), // GroupId
                    "", // ArticleId
                    true, // AutoArticleId
                    JournalArticleConstants.DEFAULT_VERSION, // Version
                    contenido.getNombre(), // Titulo
                    contenido.getDescripcion(), // Descripcion
                    contenido.getTexto(), // Contenido
                    "general", // Tipo
                    "", // Estructura
                    "", // Template
                    displayDate.get(Calendar.MONTH), // displayDateMonth,
                    displayDate.get(Calendar.DAY_OF_MONTH), // displayDateDay,
                    displayDate.get(Calendar.YEAR), // displayDateYear,
                    displayDate.get(Calendar.HOUR_OF_DAY), // displayDateHour,
                    displayDate.get(Calendar.MINUTE), // displayDateMinute,
                    0, // expirationDateMonth, 
                    0, // expirationDateDay, 
                    0, // expirationDateYear, 
                    0, // expirationDateHour, 
                    0, // expirationDateMinute, 
                    true, // neverExpire
                    0, // reviewDateMonth, 
                    0, // reviewDateDay, 
                    0, // reviewDateYear, 
                    0, // reviewDateHour, 
                    0, // reviewDateMinute, 
                    true, // neverReview
                    true, // indexable
                    false, // SmallImage
                    "", // SmallImageUrl
                    null, // SmallFile
                    null, // Images
                    "", // articleURL 
                    serviceContext // serviceContext
                    );
            log.debug("Articulo creado creando contenido");
            contenido.setContenidoId(article.getId());
            cursoDao.creaContenido(contenido);
            

            // Crear video
            
            
            log.debug("Contenido creado regresando a lista de contenidos");
            response.setRenderParameter("action", "contenidoLista");
            response.setRenderParameter("cursoId", contenido.getCurso().getId().toString());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el contenido", e);
        }
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public CursoDao getCursoDao() {
        return cursoDao;
    }

    public void setCursoDao(CursoDao cursoDao) {
        this.cursoDao = cursoDao;
    }

    public Map<String, String> getTipos(ThemeDisplay themeDisplay) {
        Map<String, String> tipos = new LinkedHashMap<String, String>();
        tipos.put("PAGADO", messageSource.getMessage("PAGADO", null, themeDisplay.getLocale()));
        tipos.put("PATROCINADO", messageSource.getMessage("PATROCINADO", null, themeDisplay.getLocale()));
        return tipos;
    }

    public Map<Integer, String> getDias(RenderRequest request) {
        Map<Integer, String> dias;
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        dias = new LinkedHashMap<Integer, String>();
        for (int i = 1; i <= 7; i++) {
            dias.put(i, messageSource.getMessage("dia" + i, null, themeDisplay.getLocale()));
        }
        return dias;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }
}
