<meta name="layout" content="ucb" />

<h1><g:message code="wpa.setPage.heading" /></h1>

<g:include controller="alert" action="formErrors" params="['item':wpa]" />
<g:include controller="alert" action="flashError" />

<div id="form">
    <g:render template="form" />
</div>
