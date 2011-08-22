<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <div class="dialog">
        <div>${curso.descripcion}</div>
    </div>
    <div class="list">
        <table id="<portlet:namespace />sesiones">
            <thead>
                <tr>

                    <th><liferay-ui:message key="sesion.dia" /></th>

                    <th><liferay-ui:message key="sesion.horaInicial" /></th>

                    <th><liferay-ui:message key="sesion.duracion" /></th>

                    <c:if test="${puedeEditar}">
                        <th><liferay-ui:message key="acciones" /></th>
                    </c:if>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${sesiones}" var="sesion" varStatus="status">
                    <tr class="${(status.count % 2) == 0 ? 'odd' : 'even'}">

                        <td><liferay-ui:message key="dia${sesion.dia}" /></td>

                        <td>${sesion.horaInicialLocal}</td>

                        <td>${sesion.duracion}</td>

                        <c:if test="${puedeEditar}">
                            <portlet:actionURL var="eliminaSesion" >
                                <portlet:param name="action" value="eliminaSesion" />
                                <portlet:param name="sesionId" value="${sesion.id}" />
                                <portlet:param name="cursoId" value="${curso.id}" />
                            </portlet:actionURL>
                            <td><a href="${eliminaSesion}"><liferay-ui:message key="sesion.elimina" /></a></td>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <c:if test="${estatus != null}">
        <div class="dialog">
            <h1 style="margin: 5px 0;padding:0;"><%= LanguageUtil.format(pageContext, "curso.estatus.mensaje",request.getAttribute("estatus"),false) %></h1>
        </div>
    </c:if>
    <div class="nav">
        <span class="menuButton"><a class="list" href="<portlet:renderURL portletMode="view"/>"><liferay-ui:message key="curso.regresar" /></a></span>
        <c:if test="${puedeEditar}">
            <portlet:renderURL var="editaUrl" >
                <portlet:param name="action" value="edita" />
                <portlet:param name="cursoId" value="${curso.id}" />
            </portlet:renderURL>
            <portlet:renderURL var="nuevaSesionUrl" >
                <portlet:param name="action" value="nuevaSesion" />
                <portlet:param name="cursoId" value="${curso.id}" />
            </portlet:renderURL>
            <portlet:renderURL var="alumnosUrl" >
                <portlet:param name="action" value="alumnosPorCurso" />
                <portlet:param name="cursoId" value="${curso.id}" />
            </portlet:renderURL>

            <span class="menuButton"><a class="edit" href="${editaUrl}"><liferay-ui:message key="curso.editar" /></a></span>
            <span class="menuButton"><a class="create" href="${nuevaSesionUrl}"><liferay-ui:message key="curso.nuevaSesion" /></a></span>
            <span class="menuButton"><a class="list" href="${alumnosUrl}"><liferay-ui:message key="curso.alumnos" /></a></span>
        </c:if>
        <c:if test="${puedeInscribirse}">
            <portlet:renderURL var="inscribirseUrl" >
                <portlet:param name="action" value="inscribirse" />
                <portlet:param name="cursoId" value="${curso.id}" />
            </portlet:renderURL>
            <span class="menuButton"><a class="edit" href="${inscribirseUrl}"><liferay-ui:message key="curso.inscribirse" /></a></span>
        </c:if>
        <c:if test="${existeSesionActiva}">
            <portlet:actionURL var="entrarUrl" >
                <portlet:param name="action" value="entrar" />
                <portlet:param name="cursoId" value="${curso.id}" />
                <portlet:param name="alumnoCursoId" value="${alumnoCurso.id}" />
            </portlet:actionURL>
            <span class="menuButton"><a class="create" href="${entrarUrl}" target="_blank"><liferay-ui:message key="curso.entrar" /></a></span>
        </c:if>
    </div>
</div>
