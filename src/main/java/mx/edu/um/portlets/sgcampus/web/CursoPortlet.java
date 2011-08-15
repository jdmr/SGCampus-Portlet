package mx.edu.um.portlets.sgcampus.web;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import mx.edu.um.portlets.sgcampus.dao.CursoDao;
import mx.edu.um.portlets.sgcampus.model.Curso;
import mx.edu.um.portlets.sgcampus.utils.ComunidadUtil;
import mx.edu.um.portlets.sgcampus.utils.CursoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
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
    private static List<String> tipos;

    public CursoPortlet() {
        log.info("Nueva instancia de Curso Portlet ha sido creada");
    }

    @InitBinder
    public void inicializar(PortletRequestDataBinder binder) {
        if (binder.getTarget() instanceof Curso) {
            binder.setValidator(cursoValidator);
            binder.registerCustomEditor(Date.class, null, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), false));
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

        return "curso/lista";
    }

    @RequestMapping(params = "action=nuevo")
    public String nuevo(RenderRequest request, Model model) throws SystemException, PortalException {
        log.debug("Nuevo curso");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        curso = new Curso();
        model.addAttribute("curso", curso);
        model.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        model.addAttribute("tipos", this.getTipos(themeDisplay));
        return "curso/nuevo";
    }

    @RequestMapping(params = "action=nuevoError")
    public String nuevoError(RenderRequest request, Model model) throws SystemException, PortalException {
        log.debug("Hubo algun error y regresamos a editar el nuevo curso");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        model.addAttribute("comunidades", ComunidadUtil.obtieneComunidades(request));
        model.addAttribute("tipos", this.getTipos(themeDisplay));
        return "curso/nuevo";
    }

    @RequestMapping(params = "action=crea")
    public void crea(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model, SessionStatus sessionStatus) throws PortalException, SystemException {
        log.debug("Creando el curso");
        curso.setComunidadNombre(GroupLocalServiceUtil.getGroup(curso.getComunidadId()).getDescriptiveName());
        cursoValidator.validate(curso, result);
        if (!result.hasErrors()) {
            curso = cursoDao.crea(curso, curso.getComunidadId());
            response.setRenderParameter("action", "ver");
            response.setRenderParameter("cursoId", curso.getId().toString());
            sessionStatus.setComplete();
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
    
    public List<String> getTipos(ThemeDisplay themeDisplay) {
        if (tipos == null) {
            tipos = new ArrayList<String>();
            tipos.add(messageSource.getMessage("PAGADO", null, themeDisplay.getLocale()));
            tipos.add(messageSource.getMessage("PATROCINADO", null, themeDisplay.getLocale()));
        }
        return tipos;
    }
}
