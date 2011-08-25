package mx.edu.um.portlets.sgcampus.web;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.portlet.RenderRequest;
import mx.edu.um.portlets.sgcampus.dao.CursoDao;
import mx.edu.um.portlets.sgcampus.model.Curso;
import mx.edu.um.portlets.sgcampus.utils.ComunidadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Administrador de cursos en la pagina principal
 * 
 * @author jdmr
 */
@Controller
@RequestMapping("VIEW")
public class CursosMaestroPortlet {

    private static final Logger log = LoggerFactory.getLogger(CursosMaestroPortlet.class);
    @Autowired
    private CursoDao cursoDao;
    private Curso curso;

    public CursosMaestroPortlet() {
        log.info("Nueva instancia de Cursos x Maestro Portlet ha sido creada");
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
        log.debug("Para las comunidades {}",comunidades.keySet());
        
        log.debug("Buscando maestro");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        Group group = GroupLocalServiceUtil.getGroup(themeDisplay.getScopeGroupId());
        params.put("maestroId",group.getClassPK());
        //User perfil = UserLocalServiceUtil.getUserById(group.getClassPK());

        params = cursoDao.busca(params);
        List<Curso> cursos = (List<Curso>)params.get("cursos");
        for(Curso x : cursos) {
            StringBuilder sb = new StringBuilder();
            sb.append("/cursos?p_p_id=Cursos_WAR_sgcampusportlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_Cursos_WAR_sgcampusportlet_action=ver&_Cursos_WAR_sgcampusportlet_cursoId=");
            sb.append(x.getId());
            x.setVerCurso(sb.toString());
        }
        modelo.addAttribute("cursos", cursos);
        modelo.addAttribute("cantidad", params.get("cantidad"));
        modelo.addAttribute("max", max);
        modelo.addAttribute("offset", offset);

        return "cursosMaestro/lista";
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
}
