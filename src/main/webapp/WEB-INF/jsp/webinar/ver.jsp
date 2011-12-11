<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <div class="dialog">
        <h1>${webinar.nombre}</h1>
        <h2>Descripción</h2>
        <p>${webinar.descripcion}</p>
        <h2>Objetivos</h2>
        <p>${webinar.objetivo}</p>
        <h2>Temario</h2>
        <p>${webinar.temario}</p>
        <h2>Requerimientos</h2>
        <p>${webinar.requerimientos}</p>
        <h2>Fecha</h2>
        <p>${fechaCompleta}</p>
    </div>
    <c:if test="${estatus != null}">
        <div class="dialog">
            <h1 style="margin: 5px 0;padding:0;"><%= LanguageUtil.format(pageContext, "webinar.estatus.mensaje",request.getAttribute("estatus"),false) %></h1>
        </div>
    </c:if>
    <div class="nav">
        <c:if test="${puedeEditar}">
            <portlet:renderURL var="editaUrl" >
                <portlet:param name="action" value="edita" />
                <portlet:param name="webinarId" value="${webinar.id}" />
            </portlet:renderURL>
            <portlet:renderURL var="nuevaSesionUrl" >
                <portlet:param name="action" value="nuevaSesion" />
                <portlet:param name="webinarId" value="${webinar.id}" />
            </portlet:renderURL>
            <portlet:renderURL var="alumnosUrl" >
                <portlet:param name="action" value="alumnosPorWebinar" />
                <portlet:param name="webinarId" value="${webinar.id}" />
            </portlet:renderURL>
            <portlet:renderURL var="contenidoUrl" >
                <portlet:param name="action" value="contenidoLista" />
                <portlet:param name="webinarId" value="${webinar.id}" />
            </portlet:renderURL>

            <span class="menuButton"><a class="edit" href="${editaUrl}"><liferay-ui:message key="webinar.editar" /></a></span>
            <span class="menuButton"><a class="list" href="${alumnosUrl}"><liferay-ui:message key="webinar.alumnos" /></a></span>
            <span class="menuButton"><a class="list" href="${contenidoUrl}"><liferay-ui:message key="webinar.contenido" /></a></span>
        </c:if>
        <c:if test="${puedeInscribirse}">
            <c:choose>
                <c:when test="${webinar.tipo == 'PAGADO'}">
                    <portlet:actionURL var="pagadoUrl" >
                        <portlet:param name="action" value="pagado" />
                        <portlet:param name="webinarId" value="${webinar.id}" />
                    </portlet:actionURL>
                    <portlet:actionURL var="noPagadoUrl" >
                        <portlet:param name="action" value="noPagado" />
                        <portlet:param name="webinarId" value="${webinar.id}" />
                    </portlet:actionURL>
                    <form action="https://mexico.dineromail.com/Shop/Shop_Ingreso.asp" method="post" style="display: inline-block;padding-right: 10px;"> 
                        <input type="hidden" name="NombreItem" value="${webinar.nombre}"/> 
                        <input type="hidden" name="TipoMoneda" value="1"/> 
                        <input type="hidden" name="PrecioItem" value="${webinar.precio}"/> 
                        <input type="hidden" name="E_Comercio" value="547336"/> 
                        <input type="hidden" name="NroItem" value="-"/> 
                        <input type="hidden" name="DireccionExito" value="${pagadoUrl}"/> 
                        <input type="hidden" name="DireccionFracaso" value="${noPagadoUrl}"/> 
                        <input type="hidden" name="DireccionEnvio" value="0"/> 
                        <input type="hidden" name="Mensaje" value="1"/>
                        <span class="menuButton"><input type="submit" name="enviaPago" class="edit" value='<liferay-ui:message key="webinar.inscribirse" />' /></span>
                    </form>
                </c:when>
                <c:otherwise>
                    <portlet:renderURL var="inscribirseUrl" >
                        <portlet:param name="action" value="inscribirse" />
                        <portlet:param name="webinarId" value="${webinar.id}" />
                    </portlet:renderURL>
                    <span class="menuButton"><a class="edit" href="${inscribirseUrl}"><liferay-ui:message key="webinar.inscribirse" /></a></span>
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:if test="${noPuedeInscribirse}">
            <portlet:renderURL var="registrarseUrl" >
                <portlet:param name="action" value="inscribirse" />
                <portlet:param name="webinarId" value="${webinar.id}" />
            </portlet:renderURL>
            <span class="menuButton"><a class="edit" href="${registrarseUrl}"><liferay-ui:message key="webinar.inscribirse" /></a></span>
        </c:if>
        <c:if test="${existeSesionActiva}">
            <portlet:actionURL var="entrarUrl" >
                <portlet:param name="action" value="entrar" />
                <portlet:param name="webinarId" value="${webinar.id}" />
                <portlet:param name="alumnoWebinarId" value="${alumnoWebinar.id}" />
            </portlet:actionURL>
            <span class="menuButton"><a class="create" href="${entrarUrl}" target="_blank"><liferay-ui:message key="webinar.entrar" /></a></span>
        </c:if>
        
        <span class="menuButton"><a class="list" href="<portlet:renderURL portletMode="view"/>"><liferay-ui:message key="webinar.regresar" /></a></span>
    </div>
</div>
