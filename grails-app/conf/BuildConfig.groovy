grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.resource.dir = "resources"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.server.port.http = 8095
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.war.file = "target/${appName}.war"

// set for reloading
grails.reload.enabled = true

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()

        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
        mavenRepo "http://mvnrepository.com/artifact/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime 'mysql:mysql-connector-java:5.1.16'
        runtime 'postgresql:postgresql:9.1-901.jdbc4'
        runtime 'com.unboundid:unboundid-ldapsdk:2.3.1'
        //runtime 'org.jasig.cas:cas-client:3.1.10'
        runtime 'com.google:gdata-appsforyourdomain:1.0'
        //runtime 'com.google:gdata.client:1.0'
        runtime 'com.google.gdata:core:1.47.1'
        runtime 'com.google.oauth-client:google-oauth-client:1.17.0-rc'
        runtime 'com.google.http-client:google-http-client:1.17.0-rc'
		runtime 'com.google.apis:google-api-services-admin:directory_v1-rev1-1.15.0-rc'
		runtime 'com.google.api-client:google-api-client:1.15.0-rc'
		runtime 'com.google.http-client:google-http-client-jackson2:1.17.0-rc'
        runtime 'com.google.guava:guava:r09'
        runtime 'com.google.code.findbugs:jsr305:2.0.3'
		runtime 'com.googlecode.json-simple:json-simple:1.1.1'
		
		

        
        compile('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1') {
            excludes "commons-logging", "xml-apis", "groovy"
        }
        
    }


      plugins {
        compile ":mail:1.0", {
            excludes 'spring-test'
        }
        
        runtime ":hibernate:3.6.10.6"
        runtime ":jquery:1.7.1"
        runtime ":resources:1.1.6"

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.4"

        build ":tomcat:7.0.47"
    }

}
