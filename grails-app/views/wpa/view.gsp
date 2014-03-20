<meta name="layout" content="ucb" />

<h1><g:message code="wpa.viewPage.heading" /></h1>

<g:include controller="alert" action="flashSuccess" />

<p>
    <g:message code="wpa.viewPage.generalMessage" />
</p>

<h2><g:message code="general.viewPage.yourKeyIs" /></h2>
<p class="token-value">
    ${token}
</p>

<p><g:link controller="wpa" action="index"><g:message code="wpa.view.returnToIndexLink" /></g:link>
