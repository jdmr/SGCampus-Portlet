<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <h1><%= LanguageUtil.format(pageContext, "webinar.alumnos.titulo",request.getAttribute("webinarNombre"),false) %></h1>

    <c:if test="${alumnos != null}">
        <div class="list">
            <table id="<portlet:namespace />alumnos">
                <thead>
                    <tr>

                        <th><liferay-ui:message key="alumno.nombre" /></th>

                        <th><liferay-ui:message key="alumno.correo" /></th>

                        <th><liferay-ui:message key="alumno.estatus" /></th>

                        <th><liferay-ui:message key="acciones" /></th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${alumnos}" var="alumnoWebinar" varStatus="status">
                        <portlet:actionURL var="inscribeUrl" >
                            <portlet:param name="action" value="inscribe" />
                            <portlet:param name="alumnoWebinarId" value="${alumnoWebinar.id}" />
                            <portlet:param name="webinarId" value="${webinar.id}" />
                        </portlet:actionURL>
                        <portlet:actionURL var="pendienteUrl" >
                            <portlet:param name="action" value="pendiente" />
                            <portlet:param name="alumnoWebinarId" value="${alumnoWebinar.id}" />
                            <portlet:param name="webinarId" value="${webinar.id}" />
                        </portlet:actionURL>
                        <portlet:actionURL var="rechazaUrl" >
                            <portlet:param name="action" value="rechaza" />
                            <portlet:param name="alumnoWebinarId" value="${alumnoWebinar.id}" />
                            <portlet:param name="webinarId" value="${webinar.id}" />
                        </portlet:actionURL>

                        <tr class="${(status.count % 2) == 0 ? 'odd' : 'even'}">

                            <td>${alumnoWebinar.alumno.nombreCompleto}</td>

                            <td>${alumnoWebinar.alumno.correo}</td>

                            <td><liferay-ui:message key="${alumnoWebinar.estatus}" /></td>
                    
                            <td>
                                <a href="${inscribeUrl}"><liferay-ui:message key="webinar.inscribe" /></a>
                                <a href="${pendienteUrl}"><liferay-ui:message key="webinar.pendiente" /></a>
                                <a href="${rechazaUrl}"><liferay-ui:message key="webinar.rechaza" /></a>
                            </td>
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
        <portlet:renderURL var="verWebinar" >
            <portlet:param name="action" value="ver" />
            <portlet:param name="webinarId" value="${webinar.id}" />
        </portlet:renderURL>
        <span class="menuButton"><a class="list" href="${verWebinar}"><liferay-ui:message key="webinar.regresar" /></a></span>
    </div>
    <script type="text/javascript">
        <c:if test="${webinars != null}">
            highlightTableRows("<portlet:namespace />alumnos")
        </c:if>
    </script>
</div>
