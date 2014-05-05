package edu.berkeley.ims.mmg

import grails.transaction.Transactional

// imports from google apis
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
//May not need these two javanet imports
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport.Builder
//
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponse
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.Preconditions
import com.google.common.io.Files

import java.io.File
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter
import java.nio.charset.Charset
import java.util.Collections
import java.util.Collection
import java.util.ArrayList
import java.util.List
import java.util.Set


import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

// JSON Simple
import org.json.simple.JSONObject 
import org.json.simple.JSONArray
import org.json.simple.JSONValue


@Transactional
class GoogleGroupsService {

	/* Need this for config info */
	def grailsApplication
	
	/* Define things used by all the methods */
	// Setup 
	HttpTransport 		HTTP_TRANSPORT
	GoogleCredential 	CREDENTIAL
	
	/* Do the setup all the methods need */
	def doSetup() {
		try {
	
			// Set the SCOPE
		    def SCOPES = new ArrayList<String>()
		    SCOPES.add(grailsApplication.config.gGroups.directory.api)
		    SCOPES.add(grailsApplication.config.gGroups.groupSettings.api)


		    /** Global instance of the JSON factory. */
		    JsonFactory JSON_FACTORY = new JacksonFactory()

			/** Get info for HTTP_TRANSPORT */
			HTTP_TRANSPORT = new NetHttpTransport()
			
		    // check for valid setup
		    String p12Content = Files.readFirstLine(new File(grailsApplication.config.gGroups.keyFile), Charset.defaultCharset())

    		// Build service account credential.
		    CREDENTIAL = new GoogleCredential.Builder()
				.setTransport(HTTP_TRANSPORT)
		        .setJsonFactory(JSON_FACTORY)
		        .setServiceAccountId(grailsApplication.config.gGroups.serviceAccountEmail)
		        .setServiceAccountUser(grailsApplication.config.gGroups.serviceAccountUser)
		        .setServiceAccountScopes(SCOPES)
		        .setServiceAccountPrivateKeyFromP12File(new File(grailsApplication.config.gGroups.keyFile))
		        .build()

	  	} catch (IOException | InterruptedException e) {
        	log.error(e.getMessage())
       	}
    	
	}

	/* create group 
	** @param groupName
	*/
    def createGroup(groupName) {
	    
		try {
			doSetup()
			
			def groupNameEmail = groupName + "@" + grailsApplication.config.gGroups.domain;
		    
		   	// Create the Group
	       	String URI = grailsApplication.config.gGroups.directory.group.URI;
	       	HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(CREDENTIAL)
	       	GenericUrl url = new GenericUrl(URI)

	   		// Setup the JSON info
		   	JSONObject jsonObj = new JSONObject()
		   	jsonObj.put("email", groupNameEmail)
		   	jsonObj.put("name", JSONObject.escape(groupName))
	
		   	// Create the Post Request
	       	HttpRequest request = requestFactory.buildPostRequest(url, 
							ByteArrayContent.fromString("application/json", jsonObj.toJSONString()))
	       	request.getHeaders().setContentType("application/json")
	       	HttpResponse response = request.execute()
	       	String content = response.parseAsString()
			log.info (content)
			return response.isSuccessStatusCode()
		

      	} catch (IOException | InterruptedException e) {
        	log.error(e.getMessage())
        	return false
       	}
        
		
    }

	/* delete group
	** @param groupName
	*/
	def deleteGroup(groupName) {
	
		try {
		
			doSetup()
			
      		String URI = grailsApplication.config.gGroups.directory.group.URI + 
							"/${groupName}@${grailsApplication.config.gGroups.domain}"
			print "The URI is: " + URI
     		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(CREDENTIAL)
       		GenericUrl url = new GenericUrl(URI)
      		HttpRequest request = requestFactory.buildDeleteRequest(url)
   			HttpResponse response = request.execute()
      		String content = response.parseAsString()
			log.info("The group ${groupName} was successfully deleted.")
			return response.isSuccessStatusCode()

      } catch (IOException e) {
        log.error(e.getMessage())
		return false
      }
	}

	/* update group All (includes manager and group settings)
	** @param groupName
	** @param settings
	*/
	def updateGroupAll(groupName, settings) {
		doSetup()
		
		def resultsManager = addGroupManager(groupName, settings.manager)
		def resultsSettings = updateGroupSettings(groupName, settings)
		def resultsAlias = addGroupAlias(groupName)
		if (resultsManager && resultsSettings && resultsAlias) {
			return true
		} else {
			return false
		}
	} // end updateGroupAll
	
	
	/* update group
	** @param groupName
	** @param settings
	*/
	def updateGroup(groupName, settings) {
	
		try {
		
  			String URI = grailsApplication.config.gGroups.directory.group.URI + 
						"/${groupName}@${grailsApplication.config.gGroups.domain}"
						
		   	// Update the Group
	       	HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(CREDENTIAL)
	       	GenericUrl url = new GenericUrl(URI)

	   		// Setup the JSON info
		   	JSONObject jsonObj = new JSONObject()
		   	jsonObj.put("description", JSONObject.escape(settings.description))
	
		   	// Create the Post Request
	       	HttpRequest request = requestFactory.buildPutRequest(url, 
					ByteArrayContent.fromString("application/json", jsonObj.toJSONString()))
	       	request.getHeaders().setContentType("application/json")
	       	HttpResponse response = request.execute()
	       	String content = response.parseAsString()
			log.info (content)
			return response.isSuccessStatusCode()		

      	} catch (IOException | InterruptedException e) {
        	log.error(e.getMessage())
        	return false
       	}
	} // end updateGroup
	

	/* update group settings
	** @param groupName
	** @param settings
	*/
	def updateGroupSettings(groupName, settings) {
	
		try {
		   // Get Group Settings
	    	String URI = "${grailsApplication.config.gGroups.groupSettings.URI}/" + 
				"${groupName}@${grailsApplication.config.gGroups.domain}"
			print "URI is: ${URI}"
	       	HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(CREDENTIAL)
	       	GenericUrl url = new GenericUrl(URI)
	            // Setup JSON object to put in the request body
		   		JSONObject jsonObj = new JSONObject()
		   		jsonObj.put("allowExternalMembers", "true")
		   		jsonObj.put("isArchived", "true")
		   		jsonObj.put("includeInGlobalAddressList", "false")
		   		jsonObj.put("whoCanJoin", "INVITED_CAN_JOIN")
		   		jsonObj.put("whoCanPostMessage", "ALL_MEMBERS_CAN_POST")
		   		jsonObj.put("whoCanViewGroup", "ALL_MEMBERS_CAN_VIEW")
		   		jsonObj.put("whoCanViewMembership", "ALL_MEMBERS_CAN_VIEW")
				jsonObj.put("description", settings.description)
	            // build the request
	    		def request = requestFactory.buildPostRequest(url, 
							ByteArrayContent.fromString("application/json", jsonObj.toJSONString()))
	    		request.getHeaders().setContentType("application/json")
	    		request.getHeaders().set("X-HTTP-Method-Override", "PATCH")
	    		def response = request.execute()
	    		def content = response.parseAsString()
				log.info (content)
				return response.isSuccessStatusCode()

		} catch (IOException | InterruptedException e) {
      		log.error(e.getMessage())
      		return false
     	}	
	} // end updateGroupSettings
	

	/* add group manager
	** @param groupName
	** @param manager
	*/
	def addGroupManager(groupName, manager) {
	
		try {
		
  			String URI = grailsApplication.config.gGroups.directory.group.URI + 
						"/${groupName}@${grailsApplication.config.gGroups.domain}/members"
			log.info ("URI is: ${URI}")
		   	// Update the Group
	       	HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(CREDENTIAL)
	       	GenericUrl url = new GenericUrl(URI)

	   		// Setup the JSON info
		   	JSONObject jsonObj = new JSONObject()
		   	jsonObj.put("email", manager)
		   	jsonObj.put("role", "MANAGER")
		
		   	// Create the Post Request
	       	HttpRequest request = requestFactory.buildPostRequest(url, 
							ByteArrayContent.fromString("application/json", jsonObj.toJSONString()))
	       	request.getHeaders().setContentType("application/json")
	       	HttpResponse response = request.execute()
	       	String content = response.parseAsString()
			log.info (content)
			return response.isSuccessStatusCode()
		

      	} catch (IOException | InterruptedException e) {
        	log.error(e.getMessage())
        	return false
       	}
	} // end addGroupManager
	
	/* add group alias
	** @param groupName
	*/
	def addGroupAlias(groupName) {
	
		try {
		
  			String URI = grailsApplication.config.gGroups.directory.group.URI + 
						"/${groupName}@${grailsApplication.config.gGroups.domain}/aliases"
			String alias = "${groupName}@${grailsApplication.config.gGroups.emailalias.domain}"
			
			log.info ("URI is: ${URI}")
			
		   	// Update the Group
	       	HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(CREDENTIAL)
	       	GenericUrl url = new GenericUrl(URI)
			
	   		// Setup the JSON info
		   	JSONObject jsonObj = new JSONObject()
		   	jsonObj.put("alias", alias)
	
		   	// Create the Post Request
	       	HttpRequest request = requestFactory.buildPostRequest(url, 
							ByteArrayContent.fromString("application/json", jsonObj.toJSONString()))
	       	request.getHeaders().setContentType("application/json")
	       	HttpResponse response = request.execute()
	       	String content = response.parseAsString()
			log.info (content)
			return response.isSuccessStatusCode()

      	} catch (IOException | InterruptedException e) {
        	log.error(e.getMessage())
        	return false
       	}
	} // end addGroupAlias
	
	
	/* find groups for manage_id
	** @param manager_id
	*/
	def findGroupsForManager(currentAccount, existingGroupNames) {
	
		try {
		
			doSetup()
			
			def groupsIsManager = []
			def String manager = currentAccount.getLogin().getUserName() + "@${grailsApplication.config.mmg.gAppsUserDomain}"
			
			existingGroupNames.each {
				def fullGroupName = it + "@${grailsApplication.config.gGroups.domain}"
				isGroupManager(manager, fullGroupName) ? groupsIsManager.add(it) : log.info("${it} is not included.")
				}
			return groupsIsManager
		

      	} catch (IOException | InterruptedException e) {
        	log.error(e.getMessage())
        	return groupsIsManager
       	}
	} // end findGroupsForManager
	
	
	/* Check if manager is group manager for group
	** @param manager_id
	** @param groupName
	*/
	def isGroupManager(manager, groupName) {
	
		try {
					
  			String URI = grailsApplication.config.gGroups.directory.group.URI + 
						"/${groupName}/members/${manager}"
			log.info ("URI is: ${URI}")
		   	// Update the Group
	       	HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(CREDENTIAL)
	       	GenericUrl url = new GenericUrl(URI)
		
		   	// Create the Get Request
	       	HttpRequest request = requestFactory.buildGetRequest(url)
	       	request.getHeaders().setContentType("application/json")
	       	HttpResponse response = request.execute()
	       	String content = response.parseAsString()
			log.info (content)
			Object obj=JSONValue.parse(content)
			print obj.get("role")
			return obj.get("role").equals("MANAGER")
			
		

      	} catch (IOException | InterruptedException e) {
        	log.error(e.getMessage())
        	return false
       	}
	} // end isGroupManager
	
	
	
	/* add group member
	** @param groupName
	** @param member
	** @param memberType
	*/
	def addGroupMember(groupName, member, memberType) {
	
	}
	
	/* delete group member
	** @param groupName
	** @param member
	*/
	def deleteGroupMember(groupName, member) {
	
	}
	
} // end googleGroupsService
