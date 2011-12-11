package mx.edu.um.portlets.sgcampus.web;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.*;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.portlet.*;
import mx.edu.um.portlets.sgcampus.dao.WebinarDao;
import mx.edu.um.portlets.sgcampus.model.*;
import mx.edu.um.portlets.sgcampus.utils.ComunidadUtil;
import mx.edu.um.portlets.sgcampus.utils.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Administrador de webinars
 *
 * @author jdmr
 */
@Controller
@RequestMapping("VIEW")
public class WebinarPortlet {

    private static final Logger log = LoggerFactory.getLogger(WebinarPortlet.class);
    @Autowired
    private WebinarDao webinarDao;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    private Webinar webinar;

    public WebinarPortlet() {
        log.info("Nueva instancia de Webinar Portlet ha sido creada");
    }

    @InitBinder
    public void inicializar(PortletRequestDataBinder binder) {
        if (binder.getTarget() instanceof Webinar) {
            binder.registerCustomEditor(Date.class, null, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), false));
        }
    }

    @RequestMapping
    public String lista(RenderRequest request,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "max", required = false) Integer max,
            @RequestParam(value = "direccion", required = false) String direccion,
            Model modelo) throws PortalException, SystemException {

        webinar = null;


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

        params = webinarDao.busca(params);
        modelo.addAttribute("webinars", params.get("webinars"));
        modelo.addAttribute("cantidad", params.get("cantidad"));
        modelo.addAttribute("max", max);
        modelo.addAttribute("offset", offset);
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            modelo.addAttribute("puedeAutorizar", true);
        }

        return "webinar/lista";
    }

    @RequestMapping(params = "action=pendientes")
    public String pendientes(RenderRequest request,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "max", required = false) Integer max,
            @RequestParam(value = "direccion", required = false) String direccion,
            Model modelo) throws PortalException, SystemException {

        log.debug("Filtrando por pendientes");

        webinar = null;


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

        params = webinarDao.busca(params);
        modelo.addAttribute("webinars", params.get("webinars"));
        modelo.addAttribute("cantidad", params.get("cantidad"));
        modelo.addAttribute("max", max);
        modelo.addAttribute("offset", offset);
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            modelo.addAttribute("puedeAutorizar", true);
        }

        return "webinar/lista";
    }

    @RequestMapping(params = "action=rechazados")
    public String rechazados(RenderRequest request,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "max", required = false) Integer max,
            @RequestParam(value = "direccion", required = false) String direccion,
            Model modelo) throws PortalException, SystemException {

        log.debug("Filtrando por rechazados");

        webinar = null;


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

        params = webinarDao.busca(params);
        modelo.addAttribute("webinars", params.get("webinars"));
        modelo.addAttribute("cantidad", params.get("cantidad"));
        modelo.addAttribute("max", max);
        modelo.addAttribute("offset", offset);
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            modelo.addAttribute("puedeAutorizar", true);
        }

        return "webinar/lista";
    }

    @RequestMapping(params = "action=nuevo")
    public String nuevo(RenderRequest request, Model model) throws SystemException, PortalException {
        log.debug("Nuevo webinar");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        webinar = new Webinar();
        model.addAttribute("webinar", webinar);
        model.addAttribute("contenido", UnicodeFormatter.toString(webinar.getDescripcion()));
        model.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        model.addAttribute("tipos", this.getTipos(themeDisplay));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (webinar.getInicia() != null) {
            model.addAttribute("inicia", sdf.format(webinar.getInicia()));
        }

        User user = PortalUtil.getUser(request);
        if (user != null) {
            if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
                return "webinar/nuevo";
            } else {
                return "webinar/nuevoUsuario";
            }
        } else {
            return "webinar/sinPrivilegios";
        }
    }

    @RequestMapping(params = "action=nuevoError")
    public String nuevoError(RenderRequest request, Model model) throws SystemException, PortalException {
        log.debug("Hubo algun error y regresamos a editar el nuevo webinar");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        model.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        model.addAttribute("tipos", this.getTipos(themeDisplay));
        model.addAttribute("webinar", webinar);
        model.addAttribute("objetivo", UnicodeFormatter.toString(webinar.getObjetivo()));
        model.addAttribute("descripcion", UnicodeFormatter.toString(webinar.getDescripcion()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (webinar.getInicia() != null) {
            String inicia = sdf.format(webinar.getInicia());
            model.addAttribute("inicia", inicia);
        }
        
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            return "webinar/nuevo";
        } else {
            return "webinar/nuevoUsuario";
        }
    }

    @Transactional
    @RequestMapping(params = "action=crea")
    public void crea(ActionRequest request, ActionResponse response,
            @ModelAttribute("webinar") Webinar webinar, BindingResult result,
            Model model, SessionStatus sessionStatus) throws PortalException, SystemException {
        log.debug("Creando el webinar");
        this.webinar = webinar;

        webinar.setComunidadNombre(GroupLocalServiceUtil.getGroup(webinar.getComunidadId()).getDescriptiveName());
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        try {
            User creador = PortalUtil.getUser(request);
            Maestro maestro = webinarDao.obtieneMaestro(webinar.getMaestro().getId());
            if (maestro == null) {
                User maestroLiferay = UserLocalServiceUtil.getUser(webinar.getMaestro().getId());
                maestro = new Maestro(maestroLiferay);
                maestro = webinarDao.registraMaestro(maestro);
            }
            webinar.setMaestro(maestro);

            log.debug("Sesiones: {}", ParamUtil.getIntegerValues(request, "sesionesIds"));
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm Z");
            for (int i : ParamUtil.getIntegerValues(request, "sesionesIds")) {
                switch (i) {
                    case 1:
                        webinar.setHora(sdf1.parse("18:00 CST"));
                        webinar.setDuracion(120);
                        break;
                    case 2:
                        webinar.setHora(sdf1.parse("20:00 CST"));
                        webinar.setDuracion(120);
                        break;
                }
            }

            webinar = webinarDao.crea(webinar, creador.getUserId());

            response.setRenderParameter("action", "ver");
            response.setRenderParameter("webinarId", webinar.getId().toString());
            sessionStatus.setComplete();
        } catch (DataIntegrityViolationException e) {
            log.error("No se pudo crear el webinar", e);
            StringBuilder sb = new StringBuilder(webinar.getDescripcion());
            sb.insert(0, messageSource.getMessage("webinar.descripcion.demasiado.grande", null, themeDisplay.getLocale()));
            webinar.setDescripcion(sb.toString());
            response.setRenderParameter("action", "nuevoError");
        } catch (Exception e) {
            log.error("No se pudo crear el webinar", e);
            response.setRenderParameter("action", "nuevoError");
        }
    }

    @Transactional
    @RequestMapping(params = "action=creaUsuario")
    public void creaUsuario(ActionRequest request, ActionResponse response,
            @ModelAttribute("webinar") Webinar webinar, BindingResult result,
            Model model, SessionStatus sessionStatus) throws PortalException, SystemException {
        log.debug("Creando webinar por el usuario");
        this.webinar = webinar;
        webinar.setComunidadNombre(GroupLocalServiceUtil.getGroup(webinar.getComunidadId()).getDescriptiveName());

        User user = PortalUtil.getUser(request);
        Maestro maestro = webinarDao.obtieneMaestro(user.getUserId());
        if (maestro == null) {
            maestro = new Maestro(user);
            maestro = webinarDao.registraMaestro(maestro);
        }
        webinar.setMaestro(maestro);
        webinar.setTipo("PATROCINADO");
        webinar.setUrl("URL-INVALIDA");
        webinar.setEstatus("PENDIENTE");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        try {
            log.debug("Sesiones: {}", ParamUtil.getIntegerValues(request, "sesionesIds"));
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm Z");
            for (int i : ParamUtil.getIntegerValues(request, "sesionesIds")) {
                switch (i) {
                    case 1:
                        webinar.setHora(sdf1.parse("18:00 CST"));
                        webinar.setDuracion(120);
                        break;
                    case 2:
                        webinar.setHora(sdf1.parse("20:00 CST"));
                        webinar.setDuracion(120);
                        break;
                }
            }

            webinar = webinarDao.crea(webinar, user.getUserId());

            response.setRenderParameter("action", "ver");
            response.setRenderParameter("webinarId", webinar.getId().toString());
            sessionStatus.setComplete();
        } catch (DataIntegrityViolationException e) {
            log.error("No se pudo crear el webinar", e);
            StringBuilder sb = new StringBuilder(webinar.getDescripcion());
            sb.insert(0, messageSource.getMessage("webinar.descripcion.demasiado.grande", null, themeDisplay.getLocale()));
            webinar.setDescripcion(sb.toString());
            response.setRenderParameter("action", "nuevoError");
        } catch (Exception e) {
            log.error("No se pudo crear el webinar", e);
            response.setRenderParameter("action", "nuevoError");
        }
    }

    public Map<String, String> getTipos(ThemeDisplay themeDisplay) {
        Map<String, String> tipos = new LinkedHashMap<String, String>();
        tipos.put("PAGADO", messageSource.getMessage("PAGADO", null, themeDisplay.getLocale()));
        tipos.put("PATROCINADO", messageSource.getMessage("PATROCINADO", null, themeDisplay.getLocale()));
        return tipos;
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
            @RequestParam Long webinarId,
            Model modelo) throws PortalException, SystemException {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        AlumnoWebinar alumnoWebinar = null;
        webinar = webinarDao.obtiene(webinarId);
        
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm Z");
        sdf2.setTimeZone(themeDisplay.getTimeZone());
        sb.append(sdf1.format(webinar.getInicia()));
        sb.append(" ");
        sb.append(sdf2.format(webinar.getHora()));
        modelo.addAttribute("fechaCompleta",sb.toString());

        modelo.addAttribute("webinar", webinar);
        User creador = PortalUtil.getUser(request);
        boolean puedeEditar = false;
        if (request.isUserInRole("Administrator")
                || request.isUserInRole("cursos-admin")
                || (creador != null && creador.getUserId() == webinar.getMaestro().getId())) {
            puedeEditar = true;
            modelo.addAttribute("puedeEditar", puedeEditar);
        }

        // Si no es el administrador / creador del webinar y este no esta
        // activo no mostrar detalle.
        if (!puedeEditar && !webinar.getEstatus().equals(Constantes.ACTIVO)) {
            return lista(request, null, null, null, modelo);
        }

        if (creador != null) {
            // Si no es el maestro
            if (creador.getUserId() != webinar.getMaestro().getId()) {
                log.debug("Buscando alumno por {}", creador.getUserId());
                Alumno alumno = webinarDao.obtieneAlumno(creador);
                if (alumno == null) {
                    log.debug("Creando alumno");
                    alumno = new Alumno(creador);
                    log.debug("con el id {}", alumno.getId());
                    alumno = webinarDao.creaAlumno(alumno);
                }
                alumnoWebinar = webinarDao.obtieneAlumno(alumno, webinar);
                if (alumnoWebinar != null) {
                    modelo.addAttribute("alumnoWebinar", alumnoWebinar);

                    modelo.addAttribute("estatus", messageSource.getMessage(alumnoWebinar.getEstatus(), null, themeDisplay.getLocale()));
                    // Validar su estatus
                    if (alumnoWebinar.getEstatus().equals(Constantes.INSCRITO)) {
                        // Validar si puede entrar
                        Calendar cal = Calendar.getInstance(themeDisplay.getTimeZone());
                        boolean existeSesionActiva = webinarDao.existeSesionActiva(webinarId, cal);
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

        return "webinar/ver";
    }

    @RequestMapping(params = "action=edita")
    public String edita(RenderRequest request,
            @RequestParam Long webinarId,
            Model modelo) throws PortalException, SystemException {

        webinar = webinarDao.obtiene(webinarId);

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        modelo.addAttribute("webinar", webinar);
        modelo.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        modelo.addAttribute("tipos", this.getTipos(themeDisplay));
        
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        cal.setTime(webinar.getHora());
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        log.debug("HORA: {}",hora);
        modelo.addAttribute("hora",hora);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (webinar.getInicia() != null) {
            String inicia = sdf.format(webinar.getInicia());
            modelo.addAttribute("inicia", inicia);
        }
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            Map<String, String> tiposDeEstatus = new LinkedHashMap<String, String>();
            tiposDeEstatus.put("ACTIVO", messageSource.getMessage("ACTIVO", null, themeDisplay.getLocale()));
            tiposDeEstatus.put("PENDIENTE", messageSource.getMessage("PENDIENTE", null, themeDisplay.getLocale()));
            tiposDeEstatus.put("RECHAZADO", messageSource.getMessage("RECHAZADO", null, themeDisplay.getLocale()));
            modelo.addAttribute("tiposDeEstatus", tiposDeEstatus);
            return "webinar/edita";
        } else {
            return "webinar/editaUsuario";
        }
    }

    @RequestMapping(params = "action=editaError")
    public String editaError(RenderRequest request,
            @RequestParam Long webinarId,
            Model modelo) throws PortalException, SystemException {

        modelo.addAttribute("webinar", webinar);
        modelo.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        modelo.addAttribute("tipos", this.getTipos(themeDisplay));
        
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        cal.setTime(webinar.getHora());
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        log.debug("HORA: {}",hora);
        modelo.addAttribute("hora",hora);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (webinar.getInicia() != null) {
            String inicia = sdf.format(webinar.getInicia());
            modelo.addAttribute("inicia", inicia);
        }
        if (request.isUserInRole("Administrator") || request.isUserInRole("cursos-admin")) {
            Map<String, String> tiposDeEstatus = new LinkedHashMap<String, String>();
            tiposDeEstatus.put("ACTIVO", messageSource.getMessage("ACTIVO", null, themeDisplay.getLocale()));
            tiposDeEstatus.put("PENDIENTE", messageSource.getMessage("PENDIENTE", null, themeDisplay.getLocale()));
            tiposDeEstatus.put("RECHAZADO", messageSource.getMessage("RECHAZADO", null, themeDisplay.getLocale()));
            modelo.addAttribute("tiposDeEstatus", tiposDeEstatus);
            return "webinar/edita";
        } else {
            return "webinar/editaUsuario";
        }
    }

    @RequestMapping(params = "action=actualiza")
    public void actualiza(ActionRequest request, ActionResponse response,
            @ModelAttribute("webinar") Webinar webinar, BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Guardando el webinar");
        webinar.setMaestro(webinarDao.refreshMaestro(webinar.getMaestro()));
        this.webinar = webinar;
        try {
            User creador = PortalUtil.getUser(request);

            log.debug("Sesiones: {}", ParamUtil.getIntegerValues(request, "sesionesIds"));
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm Z");
            for (int i : ParamUtil.getIntegerValues(request, "sesionesIds")) {
                switch (i) {
                    case 1:
                        webinar.setHora(sdf1.parse("18:00 CST"));
                        webinar.setDuracion(120);
                        break;
                    case 2:
                        webinar.setHora(sdf1.parse("20:00 CST"));
                        webinar.setDuracion(120);
                        break;
                }
            }
            
            webinarDao.actualiza(webinar, creador.getUserId());
            response.setRenderParameter("action", "ver");
            response.setRenderParameter("webinarId", webinar.getId().toString());
            sessionStatus.setComplete();
        } catch (Exception e) {
            log.error("No se pudo actualizar el webinar", e);
            response.setRenderParameter("action", "editaError");
            response.setRenderParameter("webinarId", webinar.getId().toString());
        }
    }

    @RequestMapping(params = "action=actualizaUsuario")
    public void actualizaUsuario(ActionRequest request, ActionResponse response,
            @ModelAttribute("webinar") Webinar webinar, BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Guardando el webinar");
        this.webinar = webinar;
        Webinar viejo = webinarDao.obtiene(webinar.getId());
        webinar.setCodigo(viejo.getCodigo());
        webinar.setMaestro(viejo.getMaestro());
        webinar.setTipo(viejo.getTipo());
        webinar.setUrl(viejo.getUrl());
        webinar.setEstatus(viejo.getEstatus());
        try {
            User creador = PortalUtil.getUser(request);

            log.debug("Sesiones: {}", ParamUtil.getIntegerValues(request, "sesionesIds"));
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm Z");
            for (int i : ParamUtil.getIntegerValues(request, "sesionesIds")) {
                switch (i) {
                    case 1:
                        webinar.setHora(sdf1.parse("18:00 CST"));
                        webinar.setDuracion(120);
                        break;
                    case 2:
                        webinar.setHora(sdf1.parse("20:00 CST"));
                        webinar.setDuracion(120);
                        break;
                }
            }
            
            webinarDao.actualiza(webinar, creador.getUserId());
            response.setRenderParameter("action", "ver");
            response.setRenderParameter("webinarId", webinar.getId().toString());
            sessionStatus.setComplete();
        } catch (Exception e) {
            log.error("No se pudo actualizar el webinar", e);
            response.setRenderParameter("action", "editaError");
            response.setRenderParameter("webinarId", webinar.getId().toString());
        }
    }

    @RequestMapping(params = "action=elimina")
    public void elimina(ActionRequest request, ActionResponse response,
            @ModelAttribute("webinar") Webinar webinar, BindingResult result,
            Model model, SessionStatus sessionStatus, @RequestParam Long webinarId) {
        log.debug("Eliminando el webinar {}", webinarId);
        try {
            webinar = webinarDao.obtiene(webinarId);
            this.webinar = webinar;
            User creador = PortalUtil.getUser(request);
            if (request.isUserInRole("Administrator")
                    || request.isUserInRole("cursos-admin")
                    || (creador != null && creador.getUserId() == webinar.getMaestro().getId())) {

                webinarDao.elimina(webinarId, creador.getUserId());
                // TODO Elimina contenido
                this.webinar = null;
                sessionStatus.setComplete();
            } else {
                throw new RuntimeException("No tiene permisos para eliminar este curso");
            }
        } catch (Exception e) {
            log.error("No se pudo eliminar el webinar " + webinarId, e);
            response.setRenderParameter("action", "ver");
            response.setRenderParameter("webinarId", webinarId.toString());
        }
    }

    @RequestMapping(params = "action=inscribirse")
    public String inscribirse(RenderRequest request, @RequestParam Long webinarId, Model model) {
        log.debug("Solicitar inscripcion a webinar {}", webinarId);
        try {
            User usuario = PortalUtil.getUser(request);
            if (usuario != null) {
                webinar = webinarDao.obtiene(webinarId);
                if (webinar.getTipo().equals(Constantes.PATROCINADO)) {
                    Alumno alumno = webinarDao.obtieneAlumno(usuario);
                    AlumnoWebinar alumnoWebinar = new AlumnoWebinar();
                    alumnoWebinar.setAlumno(alumno);
                    alumnoWebinar.setWebinar(webinar);
                    alumnoWebinar.setCreadorId(usuario.getUserId());
                    alumnoWebinar.setCreadorNombre(usuario.getFullName());
                    webinarDao.preInscribeAlumno(alumnoWebinar);
                    return "webinar/preinscripcion";
                } else {
                    return ver(request, webinarId, model);
                }
            } else {
                return "webinar/noInscribir";
            }
        } catch (Exception e) {
            log.error("No se pudo solicitar la inscripcion al webinar " + webinarId, e);
        }
        return "webinar/nopreincripcion";
    }

    @RequestMapping(params = "action=alumnosPorWebinar")
    public String alumnosPorWebinar(RenderRequest request, @RequestParam Long webinarId, Model model) {
        log.debug("Alumnos por Webinar {}", webinarId);
        webinar = webinarDao.obtiene(webinarId);
        List<AlumnoWebinar> alumnos = webinarDao.obtieneAlumnos(webinar);

        model.addAttribute("webinar", webinar);
        model.addAttribute("webinarNombre", webinar.getNombre());
        model.addAttribute("alumnos", alumnos);

        return "webinar/alumnos";
    }

    @RequestMapping(params = "action=inscribe")
    public void inscribe(ActionRequest request, ActionResponse response,
            @RequestParam Long alumnoWebinarId,
            @ModelAttribute("webinar") Webinar webinar,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Inscribiendo a {}", alumnoWebinarId);

        AlumnoWebinar alumnoWebinar = null;
        try {
            alumnoWebinar = webinarDao.obtieneAlumnoWebinar(alumnoWebinarId);
            User creador = PortalUtil.getUser(request);
            if (request.isUserInRole("Administrator")
                    || request.isUserInRole("cursos-admin")
                    || (creador != null && creador.getUserId() == webinar.getMaestro().getId())) {

                webinarDao.inscribeAlumno(alumnoWebinar);
                sessionStatus.setComplete();
            } else {
                throw new RuntimeException("No tiene permisos para inscribir este alumno");
            }
        } catch (Exception e) {
            log.error("No se pudo inscribir al alumno " + alumnoWebinarId, e);
        }
        response.setRenderParameter("action", "alumnosPorWebinar");
        response.setRenderParameter("webinarId", alumnoWebinar.getWebinar().getId().toString());
    }

    @RequestMapping(params = "action=pendiente")
    public void pendiente(ActionRequest request, ActionResponse response,
            @RequestParam Long alumnoWebinarId,
            @ModelAttribute("webinar") Webinar webinar,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Poniendo en pendiente a {}", alumnoWebinarId);

        AlumnoWebinar alumnoWebinar = null;
        try {
            alumnoWebinar = webinarDao.obtieneAlumnoWebinar(alumnoWebinarId);
            User creador = PortalUtil.getUser(request);
            if (request.isUserInRole("Administrator")
                    || request.isUserInRole("cursos-admin")
                    || (creador != null && creador.getUserId() == webinar.getMaestro().getId())) {

                webinarDao.preInscribeAlumno(alumnoWebinar);
                sessionStatus.setComplete();
            } else {
                throw new RuntimeException("No tiene permisos para pre-inscribir a este alumno");
            }
        } catch (Exception e) {
            log.error("No se pudo pre-inscribir al alumno " + alumnoWebinarId, e);
        }
        response.setRenderParameter("action", "alumnosPorWebinar");
        response.setRenderParameter("webinarId", alumnoWebinar.getWebinar().getId().toString());
    }

    @RequestMapping(params = "action=rechaza")
    public void rechaza(ActionRequest request, ActionResponse response,
            @RequestParam Long alumnoWebinarId,
            @ModelAttribute("webinar") Webinar webinar,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Rechazando a {}", alumnoWebinarId);

        AlumnoWebinar alumnoWebinar = null;
        try {
            alumnoWebinar = webinarDao.obtieneAlumnoWebinar(alumnoWebinarId);
            User creador = PortalUtil.getUser(request);
            if (request.isUserInRole("Administrator")
                    || request.isUserInRole("cursos-admin")
                    || (creador != null && creador.getUserId() == webinar.getMaestro().getId())) {

                webinarDao.rechazaAlumno(alumnoWebinar);
                sessionStatus.setComplete();
            } else {
                throw new RuntimeException("No tiene permisos para rechazar a este alumno");
            }
        } catch (Exception e) {
            log.error("No se pudo rechazar al alumno " + alumnoWebinarId, e);
        }
        response.setRenderParameter("action", "alumnosPorWebinar");
        response.setRenderParameter("webinarId", alumnoWebinar.getWebinar().getId().toString());
    }

    @RequestMapping(params = "action=entrar")
    public void entrar(ActionRequest request, ActionResponse response,
            @RequestParam Long alumnoWebinarId,
            @ModelAttribute("webinar") Webinar webinar,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("Entrar a webinar {}", webinar);
        try {
            User usuario = PortalUtil.getUser(request);
            if (usuario != null) {
                webinar = webinarDao.guardaAsistencia(alumnoWebinarId);
                response.sendRedirect(webinar.getUrl());
            } else {
                log.error("No pudo entrar el alumno al webinar {}", webinar);
            }
        } catch (Exception e) {
            log.error("No pudo entrar el alumno al webinar {}", webinar, e);
        }
    }

    @RequestMapping(params = "action=pagado")
    public void pagado(ActionRequest request, ActionResponse response,
            @RequestParam Long webinarId,
            @ModelAttribute("webinar") Webinar webinar,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("El alumno pago el webinar {}", webinarId);
        try {
            User usuario = PortalUtil.getUser(request);
            if (usuario != null) {
                webinar = webinarDao.obtiene(webinarId);
                this.webinar = webinar;
                log.debug("Webinar {}", webinar);
                Alumno alumno = webinarDao.obtieneAlumno(usuario);
                log.debug("Alumno {}", alumno);
                AlumnoWebinar alumnoWebinar = new AlumnoWebinar();
                alumnoWebinar.setAlumno(alumno);
                alumnoWebinar.setWebinar(webinar);
                alumnoWebinar.setCreadorId(usuario.getUserId());
                alumnoWebinar.setCreadorNombre(usuario.getFullName());
                alumnoWebinar = webinarDao.preInscribeAlumno(alumnoWebinar);
                webinarDao.inscribeAlumno(alumnoWebinar);

                response.setRenderParameter("action", "ver");
                response.setRenderParameter("webinarId", webinar.getId().toString());
            } else {
                log.error("No pudo entrar el alumno al webinar {}", webinarId);
            }
        } catch (Exception e) {
            log.error("No pudo entrar el alumno al webinar {} ", webinarId, e);
        }
    }

    @RequestMapping(params = "action=noPagado")
    public void noPagado(ActionRequest request, ActionResponse response,
            @ModelAttribute("webinar") Webinar webinar,
            BindingResult result,
            Model model, SessionStatus sessionStatus) {
        log.debug("El alumno no pago el webinar {}", webinar);
        response.setRenderParameter("action", "noPago");
    }

    @RequestMapping(params = "action=noPago")
    public String noPago(RenderRequest request, Model model) {
        log.debug("Mostrando pagina de NO PAGO");

        return "webinar/noPago";
    }

//    @RequestMapping(params = "action=contenidoLista")
//    public String contenidoLista(RenderRequest request, @RequestParam Long cursoId, Model model) {
//        log.debug("Lista de contenidos del curso {}", cursoId);
//        List<Contenido> contenidos = cursoDao.obtieneContenidos(cursoId);
//        model.addAttribute("contenidos", contenidos);
//        model.addAttribute("cantidad", contenidos.size());
//        model.addAttribute("cursoId", cursoId);
//
//        return "curso/listaContenidos";
//    }
//
//    @RequestMapping(params = "action=nuevoContenido")
//    public String nuevoContenido(RenderRequest request, @RequestParam Long cursoId, Model model) {
//        log.debug("Nuevo contenido del curso {}", cursoId);
//
//        curso = cursoDao.obtiene(cursoId);
//        contenido = new Contenido();
//        contenido.setTipo(Constantes.TEXTO);
//        contenido.setCurso(curso);
//        model.addAttribute("contenido", contenido);
//
//        return "curso/nuevoContenido";
//    }
//
//    @Transactional
//    @RequestMapping(params = "action=creaContenido")
//    public void creaContenido(ActionRequest request, ActionResponse response,
//            @ModelAttribute("contenido") Contenido contenido,
//            BindingResult result,
//            Model model, SessionStatus sessionStatus) {
//        log.debug("Creando contenido para el curso {}", contenido.getCurso().getId());
//        try {
//            curso = cursoDao.refresh(contenido.getCurso());
//            contenido.setCurso(curso);
//            // Creando contenido dentro de liferay
//            ServiceContext serviceContext = ServiceContextFactory.getInstance(JournalArticle.class.getName(), request);
//            //serviceContext.setAssetTagNames(tags);
//
//            User user = PortalUtil.getUser(request);
//            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
//            Calendar displayDate;
//            if (themeDisplay != null) {
//                displayDate = CalendarFactoryUtil.getCalendar(themeDisplay.getTimeZone(), themeDisplay.getLocale());
//            } else {
//                displayDate = CalendarFactoryUtil.getCalendar();
//            }
//
//            String texto = contenido.getTexto();
//
//            StringBuilder sb = new StringBuilder();
//            sb.append("<?xml version='1.0' encoding='UTF-8'?><root><static-content><![CDATA[");
//            sb.append(texto);
//            sb.append("]]></static-content></root>");
//            texto = sb.toString();
//
//            log.debug("UsuarioId: {}", user.getUserId());
//            log.debug("GroupId: {}", curso.getComunidadId());
//            log.debug("Nombre: {}", contenido.getNombre());
//            log.debug("Descripcion: {}", contenido.getDescripcion());
//            log.debug("Contenido: {}", contenido.getTexto());
//            JournalArticle article = JournalArticleLocalServiceUtil.addArticle(
//                    user.getUserId(), // UserId
//                    curso.getComunidadId(), // GroupId
//                    "", // ArticleId
//                    true, // AutoArticleId
//                    JournalArticleConstants.DEFAULT_VERSION, // Version
//                    contenido.getNombre(), // Titulo
//                    contenido.getDescripcion(), // Descripcion
//                    texto, // Contenido
//                    "general", // Tipo
//                    "", // Estructura
//                    "", // Template
//                    displayDate.get(Calendar.MONTH), // displayDateMonth,
//                    displayDate.get(Calendar.DAY_OF_MONTH), // displayDateDay,
//                    displayDate.get(Calendar.YEAR), // displayDateYear,
//                    displayDate.get(Calendar.HOUR_OF_DAY), // displayDateHour,
//                    displayDate.get(Calendar.MINUTE), // displayDateMinute,
//                    0, // expirationDateMonth, 
//                    0, // expirationDateDay, 
//                    0, // expirationDateYear, 
//                    0, // expirationDateHour, 
//                    0, // expirationDateMinute, 
//                    true, // neverExpire
//                    0, // reviewDateMonth, 
//                    0, // reviewDateDay, 
//                    0, // reviewDateYear, 
//                    0, // reviewDateHour, 
//                    0, // reviewDateMinute, 
//                    true, // neverReview
//                    true, // indexable
//                    false, // SmallImage
//                    "", // SmallImageUrl
//                    null, // SmallFile
//                    null, // Images
//                    "", // articleURL 
//                    serviceContext // serviceContext
//                    );
//            log.debug("Articulo creado creando contenido");
//            contenido.setContenidoId(article.getId());
//            cursoDao.creaContenido(contenido);
//            log.debug("Contenido creado regresando a lista de contenidos");
//            response.setRenderParameter("action", "contenidoLista");
//            response.setRenderParameter("cursoId", contenido.getCurso().getId().toString());
//        } catch (Exception e) {
//            throw new RuntimeException("No se pudo crear el contenido", e);
//        }
//    }
//
//    @RequestMapping(params = "action=verContenido")
//    public String verContenido(RenderRequest request, @RequestParam Long contenidoId, Model model) {
//        log.debug("Viendo el contenido {}", contenidoId);
//
//        try {
//            contenido = cursoDao.obtieneContenido(contenidoId);
//
//            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
//            JournalArticle ja = JournalArticleLocalServiceUtil.getArticle(contenido.getContenidoId());
//            AssetEntryServiceUtil.incrementViewCounter(JournalArticle.class.getName(), ja.getResourcePrimKey());
//            String texto = JournalArticleLocalServiceUtil.getArticleContent(ja.getGroupId(), ja.getArticleId(), "view", "" + themeDisplay.getLocale(), themeDisplay);
//            contenido.setTexto(texto);
//            model.addAttribute("contenido", contenido);
//
//            return "curso/verContenido";
//        } catch (Exception e) {
//            throw new RuntimeException("No se pudo ver el contenido", e);
//        }
//
//    }
//
//    @RequestMapping(params = "action=nuevoVideo")
//    public String nuevoVideo(RenderRequest request, @RequestParam Long cursoId, Model model) {
//        log.debug("Nuevo video del curso {}", cursoId);
//        curso = cursoDao.obtiene(cursoId);
//        contenido = new Contenido();
//        contenido.setTipo(Constantes.VIDEO);
//        contenido.setCurso(curso);
//        model.addAttribute("contenido", contenido);
//
//        return "curso/nuevoVideo";
//    }
//
//    @Transactional
//    @RequestMapping(params = "action=creaVideo")
//    public void creaVideo(ActionRequest request, ActionResponse response,
//            @ModelAttribute("contenido") Contenido contenido,
//            BindingResult result,
//            Model model, SessionStatus sessionStatus) {
//        log.debug("Creando video para el curso {}", contenido.getCurso().getId());
//        MultipartFile video = contenido.getArchivo();
//        if (video == null) {
//            log.error("El video esta vacio");
//        } else {
//            log.debug("Subio un archivo tipo {}", video.getContentType());
//        }
//        try {
//            curso = cursoDao.refresh(contenido.getCurso());
//            contenido.setCurso(curso);
//            // Creando contenido dentro de liferay
//            ServiceContext serviceContext = ServiceContextFactory.getInstance(JournalArticle.class.getName(), request);
//            //serviceContext.setAssetTagNames(tags);
//
//            User user = PortalUtil.getUser(request);
//            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
//            Calendar displayDate;
//            if (themeDisplay != null) {
//                displayDate = CalendarFactoryUtil.getCalendar(themeDisplay.getTimeZone(), themeDisplay.getLocale());
//            } else {
//                displayDate = CalendarFactoryUtil.getCalendar();
//            }
//
//            JournalArticle article = JournalArticleLocalServiceUtil.addArticle(
//                    user.getUserId(), // UserId
//                    curso.getComunidadId(), // GroupId
//                    "", // ArticleId
//                    true, // AutoArticleId
//                    JournalArticleConstants.DEFAULT_VERSION, // Version
//                    contenido.getNombre(), // Titulo
//                    contenido.getDescripcion(), // Descripcion
//                    contenido.getTexto(), // Contenido
//                    "general", // Tipo
//                    "", // Estructura
//                    "", // Template
//                    displayDate.get(Calendar.MONTH), // displayDateMonth,
//                    displayDate.get(Calendar.DAY_OF_MONTH), // displayDateDay,
//                    displayDate.get(Calendar.YEAR), // displayDateYear,
//                    displayDate.get(Calendar.HOUR_OF_DAY), // displayDateHour,
//                    displayDate.get(Calendar.MINUTE), // displayDateMinute,
//                    0, // expirationDateMonth, 
//                    0, // expirationDateDay, 
//                    0, // expirationDateYear, 
//                    0, // expirationDateHour, 
//                    0, // expirationDateMinute, 
//                    true, // neverExpire
//                    0, // reviewDateMonth, 
//                    0, // reviewDateDay, 
//                    0, // reviewDateYear, 
//                    0, // reviewDateHour, 
//                    0, // reviewDateMinute, 
//                    true, // neverReview
//                    true, // indexable
//                    false, // SmallImage
//                    "", // SmallImageUrl
//                    null, // SmallFile
//                    null, // Images
//                    "", // articleURL 
//                    serviceContext // serviceContext
//                    );
//            log.debug("Articulo creado creando contenido");
//            contenido.setContenidoId(article.getId());
//            cursoDao.creaContenido(contenido);
//
//
//            // Crear video
//
//
//            log.debug("Contenido creado regresando a lista de contenidos");
//            response.setRenderParameter("action", "contenidoLista");
//            response.setRenderParameter("cursoId", contenido.getCurso().getId().toString());
//        } catch (Exception e) {
//            throw new RuntimeException("No se pudo crear el contenido", e);
//        }
//    }
//
//    public Curso getCurso() {
//        return curso;
//    }
//
//    public void setCurso(Curso curso) {
//        this.curso = curso;
//    }
//
//    public CursoDao getCursoDao() {
//        return cursoDao;
//    }
//
//    public void setCursoDao(CursoDao cursoDao) {
//        this.cursoDao = cursoDao;
//    }
//
//    public Map<Integer, String> getDias(RenderRequest request) {
//        Map<Integer, String> dias;
//        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
//
//        dias = new LinkedHashMap<Integer, String>();
//        for (int i = 1; i <= 7; i++) {
//            dias.put(i, messageSource.getMessage("dia" + i, null, themeDisplay.getLocale()));
//        }
//        return dias;
//    }
//
//    public Sesion getSesion() {
//        return sesion;
//    }
//
//    public void setSesion(Sesion sesion) {
//        this.sesion = sesion;
//    }
//
//    public Contenido getContenido() {
//        return contenido;
//    }
//
//    public void setContenido(Contenido contenido) {
//        this.contenido = contenido;
//    }
//
//    private void creaSesion(Sesion sesion, Integer dia, String hora) {
//        sesion.setDia(dia);
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm Z");
//            sesion.setHoraInicial(sdf.parse(hora));
//        } catch (ParseException ex) {
//            log.error("No se pudo leer la hora inicial", ex);
//        }
//        sesion.setDuracion(120);
//        cursoDao.creaSesion(sesion);
//    }
}
