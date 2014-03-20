Manage My Keys
===========

Manage My Keys (MMK) is an application which allows users to manage an application-specific key (which is a password/passphrase) for a variety of services. At the present time, MMK is integrated with the WPA Wireless (AirBears2) system, and Google Apps for the berkeley.edu domain (bConnected).

## Requirements



## Configuration
Currently, the config for the MMK app is located under `/opt/idc/conf/ManageMyTokensConfig.groovy`. This file is readable only by the Tomcat user. To configure the app for the given environment, copy the file:

<pre>
grails-app/conf/ManageMyTokensConfig.groovy.dist
</pre>

to:

<pre>
/opt/idc/conf/ManageMyTokensConfig.groovy
</pre>

