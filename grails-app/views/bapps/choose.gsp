<meta name="layout" content="ucb" />

<h1><g:message code="bapps.choose.heading" /></h1>

<g:include controller="alert" action="flashSuccess" />

<h2>Choose Account</h2>

<ul class="options-list buttons">
    <g:each in="${accounts}">
    <li>
        <g:form controller="bapps" action="choose" id="${it.getLogin().getUserName()}" method="get" name="choose-${it.getLogin().getUserName()}">
            <button type="submit" class="btn" title="Choose account ${it.getLogin().getUserName()}.">
                <i class="icon-cog"></i> ${it.getLogin().getUserName()}
            </button>
        </g:form>
    </li>
</g:each>
</ul>

