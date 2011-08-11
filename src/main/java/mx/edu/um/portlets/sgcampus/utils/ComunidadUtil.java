/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.portlets.sgcampus.utils;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author jdmr
 */
@Service
public class ComunidadUtil {

    private static final Logger log = LoggerFactory.getLogger(ComunidadUtil.class);

    public ComunidadUtil() {
        log.info("Nueva instancia de comunidad util");
    }

    public static Map<Long, String> obtieneComunidades(RenderRequest request) throws SystemException, PortalException {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        return obtieneComunidades(themeDisplay);
    }
    
    public static Map<Long, String> obtieneComunidades(ActionRequest request) throws SystemException, PortalException {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        return obtieneComunidades(themeDisplay);
    }
    
    public static Map<Long, String> obtieneComunidades(ResourceRequest request) throws SystemException, PortalException {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        return obtieneComunidades(themeDisplay);
    }
    
    public static Map<Long, String> obtieneComunidades(ThemeDisplay themeDisplay) throws SystemException, PortalException {
        List<Integer> types = new ArrayList<Integer>();

        types.add(new Integer(GroupConstants.TYPE_COMMUNITY_OPEN));
        types.add(new Integer(GroupConstants.TYPE_COMMUNITY_RESTRICTED));
        types.add(new Integer(GroupConstants.TYPE_COMMUNITY_PRIVATE));

        LinkedHashMap<String, Object> groupParams = new LinkedHashMap<String, Object>();
        groupParams.put("types", types);
        groupParams.put("active", Boolean.TRUE);

        List<Group> comunidadesList = GroupLocalServiceUtil.search(themeDisplay.getCompanyId(), null, null, groupParams, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
        Map<Long, String> comunidades = new LinkedHashMap<Long, String>();
        for (Group group : comunidadesList) {
            log.debug("Group |{}|{}|{}|", new Object[]{group.getName(), group.getDescriptiveName(), group.getDescription()});
            comunidades.put(group.getGroupId(), group.getDescriptiveName());
        }
        return comunidades;
    }
    
    
}
