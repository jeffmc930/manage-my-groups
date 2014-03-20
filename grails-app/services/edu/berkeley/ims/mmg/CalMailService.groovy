package edu.berkeley.ims.mmg

import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*


import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLContext
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import java.security.SecureRandom
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry


// need these for cert issues
import java.security.KeyStore


class CalMailService {

	/* we need info from the grailsApp config */
	def grailsApplication
	
    /**
     * Finds the CalMail account by the provided person and username.
     */
    def account(person, username) {
        def uid = person.getAttributeValueAsInteger('uid')
        def account = CalMail.findWhere(ownerUid: uid, localpart: username)
        return account
    }

	/*
	** Add a groupName alias to Calmail database using their api
	** 
	** @params groupName
	** @params uid (of owner)
	*/
	def addGroupName(groupName, person) {
	
		if (grails.util.Environment.current.name == "development"){
			if (!checkAwake()) {
				checkAwake()
			}
		}
	
		def http = new HTTPBuilder( grailsApplication.config.calMailAPI.domainName )
		
		//=== SSL UNSECURE CERTIFICATE ===
	   def sslContext = SSLContext.getInstance("SSL")              
	   sslContext.init(null, 
			[ new X509TrustManager() 
				{
					X509Certificate[]  getAcceptedIssuers() {null }
	   				public void checkClientTrusted(X509Certificate[] certs, String authType) { }
	   				public void checkServerTrusted(X509Certificate[] certs, String authType) { }
	   			} 
			] as TrustManager[], new SecureRandom())
	   def sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
	   def httpsScheme = new Scheme("https", sf, 443)
	   http.client.connectionManager.schemeRegistry.register( httpsScheme )
	   //================================
	    

		// perform a GET request, expecting JSON response data
		http.request( GET, JSON ) {
	  		uri.path = grailsApplication.config.calMailAPI.uriCreatePath
	  		uri.query = [ 
							'apikey': grailsApplication.config.calMailAPI.calGroups.key, 
							'localpart': groupName,
							'domain': grailsApplication.config.calMailAPI.domain,
							'uid': person.getAttributeValue('uid'),
							'type': 'indiv',  // or 'dept'
							'dept_code': "",
							'authorizer': ""							
			]

	  		headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
	
			print "headers: " + getHeaders()
			print "path: " + uri.path
			print "query: " + uri.query

	  		// response handler for a success response code:
			response.success = { resp, json ->
	    		log.info resp.statusLine
				log.info "The group name -${groupName}@${grailsApplication.config.calMailAPI.domain}- was successfully added to CalMail DB."

				// All went well, return true
				return true
	  		} // end response.success

	  		// handler for any failure status code:
	  		response.failure = { resp ->
	    		log.error "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
				log.error "The group name -${groupName}@${grailsApplication.config.calMailAPI.domain}- was NOT added to CalMail DB."
	
				// failure return false
				return false
			} // end response.failure
		} // end http.response
	
	} // end addGroupName
	
	
	/*
	** Delete a groupName alias from Calmail database using their api
	** 
	** @params groupName
	*/
	def deleteGroupName(groupName) {
		
		if (grails.util.Environment.current.name == "development"){
			if (!checkAwake()) {
				checkAwake()
			}
		}
		
		def http = new HTTPBuilder( grailsApplication.config.calMailAPI.domainName )
		
		//=== SSL UNSECURE CERTIFICATE ===
	   def sslContext = SSLContext.getInstance("SSL")              
	   sslContext.init(null, 
			[ new X509TrustManager() 
				{
					X509Certificate[]  getAcceptedIssuers() {null }
	   				public void checkClientTrusted(X509Certificate[] certs, String authType) { }
	   				public void checkServerTrusted(X509Certificate[] certs, String authType) { }
	   			} 
			] as TrustManager[], new SecureRandom())
	   def sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
	   def httpsScheme = new Scheme("https", sf, 443)
	   http.client.connectionManager.schemeRegistry.register( httpsScheme )
	   //================================
	    
		
		
		// perform a GET request, expecting JSON response data
		http.request( GET, JSON ) {
			
	  		uri.path = grailsApplication.config.calMailAPI.uriDeletePath
	  		uri.query = [ 
							'apikey': grailsApplication.config.calMailAPI.calGroups.key, 
							'localpart': groupName,
							'domain': grailsApplication.config.calMailAPI.domain,
			]

	  		headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
	
			print "headers: " + getHeaders()
			print "path: " + uri.path
			print "query: " + uri.query

	  		// response handler for a success response code:
			response.success = { resp, json ->
    			log.info resp.statusLine
				log.info "The group name -${groupName}@${grailsApplication.config.calMailAPI.domain}- was successfully deleted from CalMail DB."

				// All went well, return true
				return true
	  		} // end response.success

	  		// handler for any failure status code:
	  		response.failure = { resp ->
    			log.error "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
				log.error "The group name -${groupName}@${grailsApplication.config.calMailAPI.domain}- was NOT deleted from CalMail DB."

				// failure return false
				return false
			} // end response.failure
		} // end http.response
	
	} // end deleteGroupName
	
	
	/*
	** Check if account uid has account in domain in Calmail database using their api
	** This will only be used for dev to make sure the api is awake
	** 
	** @params none
	*/
	def checkAwake() {
		
		def http = new HTTPBuilder( grailsApplication.config.calMailAPI.domainName )
		
		//=== SSL UNSECURE CERTIFICATE ===
	   def sslContext = SSLContext.getInstance("SSL")              
	   sslContext.init(null, 
			[ new X509TrustManager() 
				{
					X509Certificate[]  getAcceptedIssuers() {null }
	   				public void checkClientTrusted(X509Certificate[] certs, String authType) { }
	   				public void checkServerTrusted(X509Certificate[] certs, String authType) { }
	   			} 
			] as TrustManager[], new SecureRandom())
	   def sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
	   def httpsScheme = new Scheme("https", sf, 443)
	   http.client.connectionManager.schemeRegistry.register( httpsScheme )
	   //================================
	    
		
		
		// perform a GET request, expecting JSON response data
		http.request( GET, JSON ) {
			
	  		uri.path = "/manage/api1/getAccounts"
	  		uri.query = [ 
							'apikey': grailsApplication.config.calMailAPI.calGroups.key, 
							'uid': 5964,  // this only used to dev 
							'domain': grailsApplication.config.calMailAPI.domain,
			]

	  		headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
	
			print "headers: " + getHeaders()
			print "path: " + uri.path
			print "query: " + uri.query

	  		// response handler for a success response code:
			response.success = { resp, json ->
    			log.info resp.statusLine
				log.info "The api is awake"

				// All went well, return true
				return true
	  		} // end response.success

	  		// handler for any failure status code:
	  		response.failure = { resp ->
    			log.error "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
				log.error "The api is asleep"

				// failure return false
				return false
			} // end response.failure
		} // end http.response
	
	} // end checkAwake
	
}



