package edu.berkeley.ims.myt


import grails.test.mixin.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(BappsController)
class BappsControllerTests extends GroovyTestCase {

    def googleAppsService
    def ldapService

    public void setUp() {
        googleAppsService = new GoogleAppsService()
        googleAppsService.grailsApplication = [config: ConfigurationHolder.config]
        
        ldapService = new LdapService()
        ldapService.grailsApplication = [config: ConfigurationHolder.config]
        ldapService.setConfig()
        
    }

    void testOneAccount() {
        //def bApps = new BappsController()
        //assert bApps.response.redirectedUrl == '/auth/index'
    }
    
    void testMoreThanOneAccount() {
        setUp()
        def person = ldapService.findByEppn("lr@lucasrockwell.com")
        
        def accounts = googleAppsService.googleAppsAccounts(person)
        session.googleAppsAccounts = accounts
        println controller.accountFromId()
        //assert response.redirectedUrl == '/auth/notEligibleBApps'
    }
}
