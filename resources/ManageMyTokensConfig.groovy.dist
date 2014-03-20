/*
 * DO NOT put passwords into this file! This file gets committed with the
 * project and should not have password in it. To use this file in a
 * deployment situation, copy it to:
 *
 * /opt/idc/conf/ManageMyTokensConfig.groovy
 *
 * Or any location defined in the grails.config.locations section of
 * Config.groovy
 *
 * This file contains some sensible defaults, but double-check to make sure
 * the settings are correct for the given deployment environment.
 */

// MailService Plugin
grails.mail.disabled=false

grails.serverURL = 'https://idc-d1.calnet.berkeley.edu/mmk' // development
//grails.serverURL = 'https://idc-test.berkeley.edu/mmk' // test
//grails.serverURL = 'https://idc.berkeley.edu/mmk' // production

/* Length of the WPA token. */
myt.wpaTokenLength = 8

/* Length of generated Google Apps token. 
   This is just for reference, because we can't set this here because
   of the way we do object validation. See the BappsController for more
   info. */
myt.bAppsTokenLength = 12

/* Email Subjects */
myt.wpaEmailSetSubject = 'AirBears2 Login Key Set'
myt.wpaEmailDeleteSubject = 'AirBears2 Login Key Deleted'
myt.bAppsEmailSetSubject = 'bConnected Login Key Set'
myt.bAppsEmailDeleteSubject = 'bConnected Login Key Deleted'

// For local, non-server development only
//grails.mail.host = 'smtp.gmail.com'
//grails.mail.from = 'ucidentity@gmail.com'
//grails.mail.replyTo = 'ucidentity@gmail.com'
//grails.mail.port = 465
//grails.mail.username = 'ucidentity@gmail.com'
//grails.mail.password = ''
//grails.mail.props = [
//    "mail.smtp.auth":"true",
//    "mail.smtp.socketFactory.port":"465",
//    "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
//    "mail.smtp.socketFactory.fallback":"false"
//]

// For server deployement
grails.mail.host = 'localhost'
grails.mail.from = 'donotreply@berkeley.edu'
grails.mail.replyTo = 'donotreply@berkeley.edu'
grails.mail.port = 25

/* Whether or not to allow CalNet test IDs to use the app. */
myt.allowTestIds = true

/* CalNet Test ID passphrase. */
myt.calNetTestIdPassphrase = ''

/* This property is only in dev, and the AuthController makes sure the
   env is 'development', so there is no worry about it being enabled in
   production. */
myt.autoLoginUserAs = 'jeffreym'

/* Where the application should send the user after logout. This will
   probably be the CAS logout URL, or the local Shib SP logout URL.
   http://nose10-2.local/Shibboleth.sso/Logout for dev and test
   https://auth-test.berkeley.edu/cas/logout for UCB dev and test 
   https://auth.berkeley.edu/cas/logout for UCB prod */
myt.logoutURL = 'https://auth-test.berkeley.edu/cas/logout' // development/test
//myt.logoutURL = 'https://auth.berkeley.edu/cas/logout' // production

/* Kerberos Service app ID. */
myt.krbAppId = ''

/* Krb Service password. */
myt.krbAuthKey = ''

/* Kerberos Service URL. */
myt.krbURL = 'https://net-auth.berkeley.edu/cgi-bin/krbservice?'

/* Google Apps API URL for getting and update a user entry */
myt.gAppsUrl = 'https://apps-apis.google.com/a/feeds/testg.berkeley.edu/user/2.0' // development/test
//myt.gAppsUrl = 'https://apps-apis.google.com/a/feeds/berkeley.edu/user/2.0' // production

/* Username for the Google Apps API */
myt.gAppsUsername = '<username>@testg.berkeley.edu' // development/test
//myt.gAppsUsername = '<username>@berkeley.edu' // production

/* Google Apps password. */
myt.gAppsPassword = ''

/* domain_id in the CalMail accounts table. */
myt.gAppsDomainId = 110 // development/test
//myt.gAppsDomainId = 1 // production

/* CalNet username attribute 
   eduPersonPrincipalName for dev and test on localhost
   berkeleyEduKerberosPrincipalString for server deployment on UCB servers */
myt.calNetUsername = 'berkeleyEduKerberosPrincipalString'

/* Attribute in ldap that will be used for the username in the WPA token
   database.
   eduPersonPrincipalName for dev and test on localhost
   berkeleyEduKerberosPrincipalString for server deployment on UCB servers */
myt.wpaTokenLdapUsername = 'berkeleyEduKerberosPrincipalString'

/* Mail attribute for sending mail once someone sets/deletes their WPA token.
   mail for dev and test on localhost
   berkeleyEduOfficialEmail for server deployment on UCB servers */
myt.tokenLdapEmailAddress = 'berkeleyEduOfficialEmail'

/* LDAP hostname(s). If more than one, separate them with a comma. Do not
   leave a space. */
ldap.addresses = 'nds-test.berkeley.edu,nds-test.berkeley.edu' // development/test
//ldap.addresses = 'nds.berkeley.edu,nds.berkeley.edu' // production

/* LDAP port(s). If more than one, separate them with a comma. Do not leave a
   space, and they number must match the number of hosts, above. */
ldap.ports = '636,636'

/* Whether or not the LDAP service should auto reconnect. */
ldap.autoReconnect = true

/* Whether or not the LDAP service should use SSL. */
ldap.useSsl = true

/* Whether or not the LDAP service should implicitly trust the SSL cert. */
ldap.alwaysTrustSslCert = true

/* DN of the privileged LDAP bind. */
ldap.bindDn = ''

/* LDAP password. */
ldap.bindPassword = ''

/* DN of the LDAP server. */
ldap.baseDn = 'dc=berkeley,dc=edu'

/* DN for the branch where people are found in the LDAP server. */
ldap.peopleDn = 'ou=people,dc=berkeley,dc=edu'

/* RDN attribute for people, e.g., uid, cn. */
ldap.peopleRdnAttr = 'uid'

/* Person ID attribute. Right now this only works if you have one ID number
   for everyone. Not being used by this app, but the LdapService wants to
   to know about it */
ldap.personIdAttr = 'uid'

/* Person username attribute. This should be the name of the attribute which
   will map to the remoteUserHeader from CAS or Shib.
   eduPersonPrincipalName for Shib
   uid for CAS */
ldap.personUsernameAttr = 'eduPersonPrincipalName'

/* Person affiliation attribute.
   ucsfEduStatus for dev and test
   berkeleyEduAffiliations for production */
ldap.personAffiliationAttr = 'berkeleyEduAffiliations'

/* Test ID attribute name. Using employeeNumber for now in dev. Will change
   this once we are using the DirectoryService plugin. */
ldap.testIdAttr = 'employeeNumber'

/* Database configurations */

/* WPA dataSource */
dataSource_wpa {
    //
    dbCreate = "validate"
    url = "jdbc:postgresql://cascades.ist.berkeley.edu:5300/airbears2?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory" // development
    // url = "jdbc:postgresql://alps.ist.berkeley.edu:5300/airbears2?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory" // production
    driverClassName = "org.postgresql.Driver"
    dialect = 'org.hibernate.dialect.PostgreSQLDialect'
    username = 'token_app'
    password = ''
    properties {
       maxActive = -1
       minEvictableIdleTimeMillis=1800000
       timeBetweenEvictionRunsMillis=1800000
       numTestsPerEvictionRun=3
       testOnBorrow=true
       testWhileIdle=true
       testOnReturn=true
       validationQuery="SELECT 1"
    }
}

/* CalMail dataSource */
dataSource_calmail {
    dbCreate = "validate"
    url = "jdbc:mysql://cmdev01ws.ist.berkeley.edu:3306/calmail2?useSSL=true&verifyServerCertificate=false" // development
    //url = "jdbc:mysql://cm02adm.ist.berkeley.edu:3306/calmail?useSSL=true&verifyServerCertificate=false" // production
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = 'org.hibernate.dialect.MySQL5InnoDBDialect'
    username = 'calnetoken'
    password = ''
    properties {
       maxActive = -1
       minEvictableIdleTimeMillis=1800000
       timeBetweenEvictionRunsMillis=1800000
       numTestsPerEvictionRun=3
       testOnBorrow=true
       testWhileIdle=true
       testOnReturn=true
       validationQuery="SELECT 1"
    }
}

/* Logging config */
log4j = {
    appenders {
        rollingFile name:'file', maxFileSize:10485760,
            file:'/var/log/tomcat6/mmk.log'
        rollingFile name:'stacktrace', maxFileSize:10485760,
            file:'/var/log/tomcat6/mmk-stacktrace.log'
    }
    root {
        info 'stdout', 'file'
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
           
    info 'grails.app.edu.berkeley.ims.myt',
         'grails.app.services.edu.berkeley.ims.myt.WpaService',
         'grails.app.services.edu.berkeley.ims.myt.KerberosService'
         'grails.app.services.edu.berkeley.ims.myt.GoogleAppsService'
    
    debug 'grails.app.edu.berkeley.ims.myt',
          'grails.app.services.edu.berkeley.ims.myt.WpaService',
          'grails.app.services.edu.berkeley.ims.myt.KerberosService',
          'grails.app.services.edu.berkeley.ims.myt.GoogleAppsService'
}
