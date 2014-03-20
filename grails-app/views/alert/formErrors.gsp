<g:hasErrors bean="${item}">
<div class="alert alert-error">
<h3>Error</h3>
  <ul>
   <g:eachError bean="${item}">
       <li><g:message error="${it}"/></li>
   </g:eachError>
  </ul>
</div>
</g:hasErrors>