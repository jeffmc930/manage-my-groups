<meta name="layout" content="ucb" />

<h1><g:message code="overview.welcome" /></h1>
<p><g:message code="overview.welcomeMessage" /></p>

<h2><g:message code="overview.optionsHeading" /></h2>
    
    <ul class="options-list buttons">
        <li>
            <g:form controller="bapps" action="index" method="get" name="bapps">
                <button type="submit" class="btn">
                    <i class="icon-calendar"></i> <g:message code="overview.bAppsLink" />
                </button>
            </g:form>
        </li>
        <li>
            <g:form controller="gencollab" action="index" method="get" name="gencollab">
                <button type="submit" class="btn">
                    <i class="icon-calendar"></i> Multi-purpose Groups 
                </button>
            </g:form>
        </li>

        <li>
            <g:form controller="gencollab" action="index" method="get" name="gencollab">
                <button type="submit" class="btn">
                    <i class="icon-calendar"></i> Dynamic Data Driven Groups 
                </button>
            </g:form>
        </li>

    </ul>
        