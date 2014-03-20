<meta name="layout" content="ucb" />

<h1><g:message code="wpa.index.heading" /></h1>

<g:include controller="alert" action="flashSuccess" />

<p><g:message code="wpa.index.generalMessage" /></p>

<g:if test="${hasToken}">
<h2><g:message code="wpa.index.optionsHeading" /></h2>
</g:if>

<ul class="options-list buttons">
    <li>
        <g:form controller="wpa" action="set" method="get" name="set">
            <button type="submit" class="btn btn-success" title="Set AirBears2 key...">
                <i class="icon-pencil icon-white"></i> <g:message code="wpa.index.optionsSetLink" />
            </button>
        </g:form>
    </li>
    <g:if test="${hasToken}">
    <li>
        <g:form controller="wpa" action="view" method="get" name="view">
            <button type="submit" class="btn btn-warning" title="View AirBears2 key...">
                <i class="icon-eye-open icon-white"></i> <g:message code="wpa.index.optionsViewLink" />
            </button>
        </g:form>
    </li>
    <li>
        <g:form controller="wpa" action="delete" method="get" name="delete">
            <button type="submit" class="btn btn-danger" title="Delete AirBears2 key...">
                <i class="icon-eye-open icon-white"></i> <g:message code="wpa.index.optionsDeleteLink" />
            </button>
        </g:form>
    </li>
</g:if>
</ul>
