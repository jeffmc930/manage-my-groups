package edu.berkeley.ims.mmg

import java.security.SecureRandom;

import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder

class TokenService {

    char[] charset = [
        '2', '3', '4', '5', '6', '7', '8', '9',
        'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N',
        'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z',
        'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n',
        'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z',
        '&', '!', '@', '#', '(', ')', '^', '$', '+', '%', '*', '{', '}', ',', '.'
    ]

    public String token(int length) {
        SecureRandom sr = new SecureRandom()
        StringBuilder sb = new StringBuilder(length)
        for(int i = 0; i < length; i++) {
            // Stick a random character in our result.
            sb.append(charset[sr.nextInt(charset.length)])
        }
        return(sb.toString())
    }
    
    public boolean verifyCurrentToken(currentToken, token) {
        if (currentToken == token) {
            return true
        }
        else {
            return false
        }
    }
}
