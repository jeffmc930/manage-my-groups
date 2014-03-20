// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

//grails.config.locations = [ "classpath:myt-config.properties",
//                            "classpath:${appName}-config.groovy",
//                            "file:${userHome}/.grails/${appName}-config.properties",
//                            "file:${userHome}/.grails/${appName}-config.groovy"]

/*grails.config.locations = [
	"file:./ManageMyTokensConfig.groovy",
	"classpath:ManageMyTokensConfig.groovy",
	"file:${userHome}/.grails/ManageMyTokensConfig.groovy"
]*/


grails.config.locations = [
    "file:///opt/idc/conf/ManageMyGroupsConfig.groovy"
]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']


// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password', 'token', 'definedToken', 'definedTokenConfirmation']

// enable query caching by default
grails.hibernate.cache.queries = true

/*
 * Passwords, so they are all in one place so we can easily
 * remove them before committing. I would rather changes these
 * often in one place, than remember to check all the envs
 * before committing and building.
 */

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.logging.jul.usebridge = true
        // Now maintained in external config at /opt/idc/conf/ManageMyTokensConfig.groovy
        
    }
    test {
        // Now maintained in external config at /opt/idc/conf/ManageMyTokensConfig.groovy
    }
    production {
        // Now maintained in external config at /opt/idc/conf/ManageMyTokensConfig.groovy
    }
    
}

// log4j configuration is external, too.

/* This property is only in dev, and the AuthController makes sure the
   env is 'development', so there is no worry about it being enabled in
   production. */
//myt.autoLoginUserAs = 'jeffreym'


// Uncomment and edit the following lines to start using Grails encoding & escaping improvements

/* remove this line 
// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside null
                scriptlet = 'none' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}
remove this line */
