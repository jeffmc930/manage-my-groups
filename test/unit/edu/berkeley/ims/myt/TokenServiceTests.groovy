package edu.berkeley.ims.myt



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(TokenService)
class TokenServiceTests {

    void testTokenLength() {
        def tokenService = new TokenService()
        def t1 = tokenService.token(8)
        assertEquals 8, t1.length()
        
        def t2 = tokenService.token(6)
        assertEquals 6, t2.length()
        
    }
}
