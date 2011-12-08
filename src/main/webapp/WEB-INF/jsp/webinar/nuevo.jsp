<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <h1><liferay-ui:message key="webinar.nuevo.titulo" /></h1>
    <portlet:actionURL var="actionUrl">
        <portlet:param name="action" value="crea"/>
    </portlet:actionURL>

    <form:form name="webinarForm" commandName="webinar" method="post" action="${actionUrl}" >
        <input type="hidden" id="<portlet:namespace />maestroId" name="<portlet:namespace />maestro.id" value="${webinar.maestro.id}" />
        <div class="dialog">
            <table>
                <tbody>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="codigo"><liferay-ui:message key="webinar.codigo" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:input path="codigo" maxlength="32"/>
                            <form:errors cssClass="errors" path="codigo" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="nombre"><liferay-ui:message key="webinar.nombre" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:input path="nombre" maxlength="128"/>
                            <form:errors cssClass="errors" path="nombre" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="objetivo"><liferay-ui:message key="webinar.objetivo" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:textarea path="objetivo" />
                            <form:errors cssClass="errors" path="objetivo" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="descripcion"><liferay-ui:message key="webinar.descripcion" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:textarea path="descripcion" />
                            <form:errors cssClass="errors" path="descripcion" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="temario"><liferay-ui:message key="webinar.temario" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:textarea path="temario" />
                            <form:errors cssClass="errors" path="temario" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="requerimientos"><liferay-ui:message key="webinar.requerimientos" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:textarea path="requerimientos" />
                            <form:errors cssClass="errors" path="requerimientos" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />maestroNombre"><liferay-ui:message key="webinar.maestro" /></label>
                        </td>
                        <td valign="top" class="value">
                            <div id="<portlet:namespace />maestroDiv">
                                <c:if test="${webinar.maestro != null}">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th><liferay-ui:message key="nombre" /></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>${webinar.maestro.nombreCompleto}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </c:if>
                            </div>
                            <input type="text" name="maestroNombre" id="<portlet:namespace />maestroNombre" value="" />
                            <form:errors cssClass="errors" path="maestro" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />inicia"><liferay-ui:message key="webinar.inicia" /></label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="inicia" id="<portlet:namespace />inicia" value="${inicia}" />
                            <form:errors cssClass="errors" path="inicia" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />sesionesIds"><liferay-ui:message key="webinar.sesiones" /></label>
                        </td>
                        <td valign="top" class="value" style="text-align: left;">
                            <input type="radio" name="<portlet:namespace />sesionesIds" value="1" style="width:10px;" /> 18:00 a 20:00 hrs<br/>
                            <input type="radio" name="<portlet:namespace />sesionesIds" value="2" style="width:10px;" /> 20:00 a 22:00 hrs<br/>
                        </td>
                    </tr>
                    
                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />url"><liferay-ui:message key="webinar.url" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:input id="<portlet:namespace />url" path="url" maxlength="254"/>
                            <form:errors cssClass="errors" path="url" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td colspan="2" style="padding:0;">
                            <table style="border:0;">
                                <tbody>
                                    <tr class="prop">
                                        <td valign="top" class="name">
                                            <label for="tipo"><liferay-ui:message key="webinar.tipo" /></label>
                                        </td>
                                        <td valign="top" class="value">
                                            <form:select path="tipo" items="${tipos}" />
                                            <form:errors cssClass="errors" path="tipo" />
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <div id="tipoDiv"
                                 <c:if test="${curso.tipo == 'PATROCINADO'}">style="display:none;"</c:if>
                                 >
                                <table style="border:0;">
                                    <tbody>
                                        <tr class="prop">
                                            <td valign="top" class="name">
                                                <label for="precio"><liferay-ui:message key="webinar.precio" /></label>
                                            </td>
                                            <td valign="top" class="value">
                                                <form:input path="precio" />
                                                <form:errors cssClass="errors" path="precio" />
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="comunidadId"><liferay-ui:message key="webinar.comunidad" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:select path="comunidadId" items="${comunidades}" />
                            <form:errors cssClass="errors" path="comunidadId" />
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>
        <div class="dialog">
            <table>
                <tbody>
                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />telefono"><liferay-ui:message key="webinar.telefono" /></label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="telefono" id="<portlet:namespace />telefono" value="${webinar.telefono}" />
                            <form:errors cssClass="errors" path="telefono" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />estado"><liferay-ui:message key="webinar.estado" /></label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="estado" id="<portlet:namespace />estado" value="${webinar.estado}" />
                            <form:errors cssClass="errors" path="estado" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />pais"><liferay-ui:message key="webinar.pais" /></label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="pais" id="<portlet:namespace />pais" value="${webinar.pais}" />
                            <form:errors cssClass="errors" path="pais" cssStyle="color:red;" />
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>

        <div class="nav">
            <span class="menuButton"><input type="submit" name="<portlet:namespace />_crea" class="save" value="<liferay-ui:message key='webinar.crea' />"/></span>
            <span class="menuButton"><a class="cancel" href="<portlet:renderURL portletMode="view"/>"><liferay-ui:message key="webinar.cancela" /></a></span>
        </div>
    </form:form>
    <script type="text/javascript">
        $(document).ready(function() {
            $("input#<portlet:namespace />maestroNombre")
            .autocomplete({
                source: "<portlet:resourceURL id='buscaMaestro'/>",
                select: function(event, ui) {
                    $("input#<portlet:namespace />maestroId").val(ui.item.id);
                    $("#<portlet:namespace />maestroDiv").load('<portlet:resourceURL id="asignaMaestro" />',{id:ui.item.id},function() {
                        $("input#<portlet:namespace />maestroNombre").val(ui.item.nombre);
                        $("input#<portlet:namespace />inicia").focus();
                    });
                    return false;
                }
            });
            $("input#<portlet:namespace />inicia").datepicker({dateFormat: 'dd/mm/yy'});
            $("input#<portlet:namespace />termina").datepicker({dateFormat: 'dd/mm/yy'});
            $("select#tipo").change(function() {
                if($(this).val() == 'PATROCINADO') {
                    $("div#tipoDiv").slideUp();
                } else {
                    $("div#tipoDiv").slideDown();
                }
            });
            
            $("input#codigo").focus();
        });
    </script>
</div>
