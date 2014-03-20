package edu.berkeley.ims.myt

import grails.test.mixin.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class GoogleAppsServiceTests extends GroovyTestCase {

    def googleAppsService
    def ldapService 
    
    public void setUp() {
        googleAppsService = new GoogleAppsService()
        googleAppsService.grailsApplication = [config: ConfigurationHolder.config]
        
        ldapService = new LdapService()
        ldapService.grailsApplication = [config: ConfigurationHolder.config]
        ldapService.setConfig()
        
    }
    
    void testAccountExistsFor() {
        def account = googleAppsService.googleAppsAccountFor("calnet-test1")
        assertNotNull account
        
        account = googleAppsService.googleAppsAccountFor("calnet-test1zzzzzzzzzz")
        assertNull account
    }
    
    void testGoogleAppsAccounts() {
        setUp()
        def person = ldapService.findByEppn("lr@lucasrockwell.com")
        
        def accounts = googleAppsService.googleAppsAccounts(person)
        
        assertEquals accounts.size(), 6
    }
}
