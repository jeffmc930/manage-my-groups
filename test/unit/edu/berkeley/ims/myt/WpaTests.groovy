package edu.berkeley.ims.myt



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Wpa)
class WpaTests {

    /**
     * Test to make sure a blank username fails.
     */
    void testBlankUsername() {
        def wpa = new Wpa(username:'', password:'hi')
        wpa.validate()
        
        assertEquals wpa.errors.getFieldError().getField(), "username"
        assertTrue wpa.hasErrors()
    }
    
    /**
     * Test to make sure a blank password fails.
     */
    void testBlankPassword() {
        def wpa = new Wpa(username:'hi', password:'')
        wpa.validate()
        
        assertEquals wpa.errors.getFieldError().getField(), "password"
        assertTrue wpa.hasErrors()
    }
}
