<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>${flash.title}<g:if test="${flash.title}"> | </g:if><g:message code="${controllerName}.${actionName}.title" /> | Manage My Groups</title>
    <meta name="description" content="UCB Create Goup app">
    <meta name="author" content="Jeff McCullough">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <style type="text/css">
          body {
            padding-top: 60px;
            padding-bottom: 40px;
          }
          .sidebar-nav {
            padding: 9px 0;
          }
        </style>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-ucb.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'ucb.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'styles.css')}" type="text/css">
    
    <script src="${resource(dir:'js',file:'jquery-1.7.1.min.js')}"></script>
    <script src="${resource(dir:'js',file:'bootstrap.js')}"></script>
    
    <g:layoutHead/>
    <r:layoutResources />
    
  </head>

  <body>
    <g:unless test="${controllerName == 'auth' && actionName == 'index'}">
        <a class="skip-content" href="#content">Skip to main content</a>
    </g:unless>
    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
            <g:render template="/layouts/navigation" />
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container ">
        <div class="row-fluid">
            <div class="span12">
                <div class="app-body">
                    <a name="content"></a>
                    <g:layoutBody/>
                    <g:javascript library="application"/>
                    <r:layoutResources />

                </div>
            </div>
        </div>
    </div><!-- End container -->

  </body>
</html>
