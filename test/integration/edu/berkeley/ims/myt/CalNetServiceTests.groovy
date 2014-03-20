package edu.berkeley.ims.myt

import grails.test.mixin.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CalNetService)
class CalNetServiceTests extends GroovyTestCase {

    def calNetService
    def ldapService

    public void setUp() {
        calNetService = new CalNetService()
        calNetService.grailsApplication = [config: ConfigurationHolder.config]
        
        ldapService = new LdapService()
        ldapService.grailsApplication = [config: ConfigurationHolder.config]
        ldapService.setConfig()
        
    }
    
    /**
     * Test good authentication for user test-212383
     */
    void testGoodAuthentication() {
        setUp()
        def person = ldapService.find("uid", "212383")
        def pwd = ConfigurationHolder.config.myt.calNetTestIdPassphrase
        assertTrue calNetService.testAuthenticationFor(pwd, person)
    }
    
    /**
     * Test bad authentication for user test-212383.
     */
    void testBadAuthentication() {
        setUp()
        def person = ldapService.find("uid", "212383")
        assertFalse calNetService.testAuthenticationFor('hi there', person)
    }
    
    /**
     * Test good passphrases for user test-212383.
     *
     * As of 2012-06-27, this record in ldap-test.berkeley.edu contains:
     *
     *  uid: 212383
     *  displayName: STU-NEW TEST
     *  berkeleyEduKerberosPrincipalString: test-212383
     *
     * And user 'lr' contains:
     *
     *  uid: 125236
     *  displayName: Lucas Rockwell
     *  berkeleyEduKerberosPrincipalString: lr
     */
    void testGoodPassphrases() {
        setUp()
        def person = ldapService.find("uid", "212383")
        assertTrue calNetService.validatePassphraseComplexityFor('Hi there!', person)
        assertTrue calNetService.validatePassphraseComplexityFor('aaabbbCC2', person)
        assertTrue calNetService.validatePassphraseComplexityFor('Password1', person) // Yes, this is a valid CalNet passphrase!
        assertTrue calNetService.validatePassphraseComplexityFor('a      B1', person)
        
        // User with a CalNet ID with only two characters: 'lr'
        person = ldapService.find("uid", "125236")
        assertTrue calNetService.validatePassphraseComplexityFor('Hi there lr!', person)
        assertTrue calNetService.validatePassphraseComplexityFor('Hi there luc!', person)
    }
    
    /**
     * Test bad passphrases for user test-212383.
     *
     * As of 2012-06-27, this record in ldap-test.berkeley.edu contains:
     *
     *  uid: 212383
     *  displayName: STU-NEW TEST
     *  berkeleyEduKerberosPrincipalString: test-212383
     *
     */
    void testBadPassphrases() {
        setUp()
        def person = ldapService.find("uid", "212383")
        
        assertFalse calNetService.validatePassphraseComplexityFor('Hi', person)
        assertFalse calNetService.validatePassphraseComplexityFor('Hi1!', person)
        assertFalse calNetService.validatePassphraseComplexityFor('What___?', person)
        assertFalse calNetService.validatePassphraseComplexityFor('what about this', person)
        assertFalse calNetService.validatePassphraseComplexityFor('abcefghijklmn1', person)
        assertFalse calNetService.validatePassphraseComplexityFor('A      B1', person)
        assertFalse calNetService.validatePassphraseComplexityFor('a     B1', person)
        assertFalse calNetService.validatePassphraseComplexityFor('Hi there test-21238!', person) // displayName contains the word 'test'
        assertFalse calNetService.validatePassphraseComplexityFor('This is still not good enough one: test-212383!', person)
        assertFalse calNetService.validatePassphraseComplexityFor('This is still not good enough one: TEST-212383!', person)
        assertFalse calNetService.validatePassphraseComplexityFor('This is looking good! Oh, wait..................................................................................................................................................................................................................................', person)

    }
    
}
