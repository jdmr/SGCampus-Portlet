<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <h1><liferay-ui:message key="javax.portlet.title" /> (<%= LanguageUtil.format(pageContext, "webinar.cantidad",request.getAttribute("cantidad"),false) %>)</h1>
    <portlet:renderURL var="actionUrl" >
        <portlet:param name="action" value="busca" />
    </portlet:renderURL>

    <form name="<portlet:namespace />buscaWebinar" method="post" action="${actionUrl}" >
        <div class="search">
            <table>
                <tbody>
                    <tr class="prop">
                        <td>
                            <input type="text" name="<portlet:namespace />filtro" id="<portlet:namespace />filtro" value="" />
                            <input type="submit" name="<portlet:namespace />_busca" value='<liferay-ui:message key="webinar.buscar" />'/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </form>
    <c:if test="${webinars != null}">
        <div class="list">
            <table id="<portlet:namespace />webinars">
                <thead>
                    <tr>

                        <th><liferay-ui:message key="webinar.nombre" /></th>

                        <th><liferay-ui:message key="webinar.inicia" /></th>

                        <th><liferay-ui:message key="webinar.maestro" /></th>

                        <th><liferay-ui:message key="webinar.tipo" /></th>

                        <th><liferay-ui:message key="webinar.estatus" /></th>
    
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${webinars}" var="webinar" varStatus="status">
                        <portlet:renderURL var="verWebinar" >
                            <portlet:param name="action" value="ver" />
                            <portlet:param name="webinarId" value="${webinar.id}" />
                        </portlet:renderURL>
                        <tr class="${(status.count % 2) == 0 ? 'odd' : 'even'}">

                            <td><a href="${verWebinar}">${webinar.nombre}</a></td>

                            <td>${webinar.inicia}</td>

                            <td>${webinar.maestro.nombreCompleto}</td>

                            <td><liferay-ui:message key="${webinar.tipo}" /></td>

                            <td><liferay-ui:message key="${webinar.estatus}" /></td>
                    
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <portlet:renderURL var="anterior" >
            <portlet:param name="max" value="${max}" />
            <portlet:param name="offset" value="${offset}" />
            <portlet:param name="direccion" value="anterior" />
        </portlet:renderURL>

        <portlet:renderURL var="siguiente" >
            <portlet:param name="max" value="${max}" />
            <portlet:param name="offset" value="${offset}" />
            <portlet:param name="direccion" value="siguiente" />
        </portlet:renderURL>

        <div class="paginateButtons">
            <c:if test="${offset > 0}">
                <a href="${anterior}" class="prevLink"><liferay-ui:message key="webinar.anterior" /></a>
            </c:if>
            <c:if test="${cantidad > 5}">
                <a href="${siguiente}" class="nextLink"><liferay-ui:message key="webinar.siguiente" /></a>
            </c:if>
        </div>

    </c:if>
    <div class="nav">
        <span class="menuButton"><a class="create" href='<portlet:renderURL windowState="<%= WindowState.MAXIMIZED.toString() %>" ><portlet:param name="action" value="nuevo"/></portlet:renderURL>'><liferay-ui:message key="webinar.nuevo" /></a></span>
        <c:if test="${puedeAutorizar}">
            <span class="menuButton"><a class="list" href='<portlet:renderURL><portlet:param name="action" value="pendientes"/></portlet:renderURL>'><liferay-ui:message key="webinar.pendientes" /></a></span>
            <span class="menuButton"><a class="list" href='<portlet:renderURL><portlet:param name="action" value="rechazados"/></portlet:renderURL>'><liferay-ui:message key="webinar.rechazados" /></a></span>
        </c:if>
    </div>
    <script type="text/javascript">
        <c:if test="${webinars != null}">
            highlightTableRows("<portlet:namespace />webinars")
        </c:if>
    </script>
</div>
