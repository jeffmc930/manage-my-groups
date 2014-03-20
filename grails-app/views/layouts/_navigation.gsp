<g:link controller="main" action="index" class="brand"><g:message code="general.appName" /></g:link></a>
<div class="nav-collapse">
<g:if test="${session.person}">
<ul class="nav">
    <li><g:link controller="bapps" action="index"><g:message code="navigation.manage.bapps" /></g:link></li>
</ul>
</g:if>
<g:if test="${session.person}">
<ul class="nav pull-right">
   <li class="divider-vertical"></li>
   <li class="dropdown">
       <a href="#" class="dropdown-toggle" data-toggle="dropdown"><g:message code="navigation.other" /> <b class="caret"></b></a>
       <ul class="dropdown-menu">
           <li><a href="https://net-auth.calnet.berkeley.edu/cgi-bin/krbcpw">Change CalNet Passphrase</a></li>
           <li><a href="https://net-auth.calnet.berkeley.edu/cgi-bin/krbsync">Synchronize CalNet Passphrase</a></li>
           <li><a href="https://calnet.berkeley.edu/myi/id/change">Change CalNet ID</a></li>
           <li><a href="https://calnet.berkeley.edu/myi/ids/list">List Your IDs</a></li>
           <li><a href="https://calnet.berkeley.edu/myi/key/set">Set Your CalNetKey</a></li>
           <li><a href="https://calnet.berkeley.edu/myi/questions/set">Set Your Security Questions</a></li>
           <li><a href="https://auth.berkeley.edu/cas/login?renew=true&service=https://auth.berkeley.edu/">Test CalNet ID</a></li>
           <li><a href="https://auth-key.berkeley.edu/">Test CalNetKey</a></li>
           <li><a href="https://calnet.berkeley.edu/myi/passphrase/reset_preference">Passphrase Reset Preference</a></li>
           <li><a href="https://calnet.berkeley.edu/directory/update/">Update Directory Listing</a></li>
       </ul>
   </li>
   <li class="divider-vertical"></li>
   <li class="dropdown">
     <a href="#" class="dropdown-toggle" data-toggle="dropdown"><ldap:attr entry="${session.person}" attr="displayName" /> <b class="caret"></b></a>
     <ul class="dropdown-menu">
       <li><g:link controller="auth" action="logout">Log Out</g:link></li>
     </ul>
   </li>
</ul>
</g:if>
</div><!--/.nav-collapse -->