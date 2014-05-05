package edu.berkeley.ims.mmg

import grails.transaction.Transactional
import java.util.regex.Pattern

@Transactional
class NameSpaceService {

def grailsApplication

def ldapService

	// we check the name in the LDAP NameSpace
	// if it exists we need to confirm it is owned by the current user
	// otherwise we return false. If it doesn't exist, they 
	// we return true, and the user is good to go.
	// @param {@code name, person} - the name to check, the current user
    // @return boolean 
    
    def validateName(name, person) {
		// look for the name
		def entry = ldapService.findEntryForName(name)
		if (entry) {
			if (entry.getAttributeValue(grailsApplication.config.ldap.personIdAttr) == 		
					person.getAttributeValue(grailsApplication.config.ldap.personIdAttr)){
				return true
			} else {
				return false
			}
    	} else {
			return true
		}
	}
	
	
	// Delete a name from the namespace
	// or delete the service name only
	// It uses several methods in LDAP service to do the work.
	// @param {@code name, person} - the name to add, the current user
    // @return boolean 
	def deleteNameOrService(name){
		def result = false
		//Determine if we delete the entry or modify an entry
		name = name + "@" + grailsApplication.config.gGroups.domain
		def entry = ldapService.findEntryForName(name)
		if (entry) {
			if ((entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr).size() == 1) &&
				(entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr)
									.hasValue(grailsApplication.config.ldap.cGbCServiceName))){
					result = ldapService.deleteNameSpaceEntry(name)
			} else if ((entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr).size() > 1) && 
				(entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr)
									.hasValue(grailsApplication.config.ldap.cGbCServiceName))){
					result = ldapService.deleteGroupServiceFromNameSpaceEntry(name)
			} else {
				result = false
			}
		}
		return result
	}
	
	
	
	// Add name to the namespace
	// It uses a method in LDAP service to do the work.
	// @param {@code name, person} - the name to add, the current user
    // @return boolean 
	def addName(name, person, reuseNames){
		def addNameResult = false
		def addFQNResult = false
		//Determine if we add the entry or modify an entry
		def entry = ldapService.findEntryForName(name)
		if (entry) {
			if (entry.getAttributeValue(grailsApplication.config.ldap.personIdAttr) == 		
					person.getAttributeValue(grailsApplication.config.ldap.personIdAttr)){
				if (!entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr)
									.hasValue(grailsApplication.config.ldap.cGbCServiceName)) {
					addNameResult = ldapService.addGroupServiceToNameSpaceEntry(name)
					if (addNameResult) {log.info("Added group service to cn=" + name + " for uid=" + 
						person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))}
					else {log.error("Error adding group service to cn=" + name + " for uid=" + 
						person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))}
				} else {
					addNameResult = true
					log.info("Group Service entry exists:  cn=" + name + " for uid=" + 
						person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))
				}
			}
		} else {
				addNameResult = ldapService.addNameSpaceEntry(name, person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))
				if (addNameResult){log.info("Added entry for " + name + "with uid=" + 
					person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))}
				else {log.error("Error adding entry for " + name + "with uid=" + 
						person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))}
		}
		
		// The name entry has been added, now we add the fully qualified name
		def fullyQualifiedName = name + "@" + grailsApplication.config.gGroups.domain
		entry = ldapService.findEntryForName(fullyQualifiedName)
		if (entry) {
			if (entry.getAttributeValue(grailsApplication.config.ldap.personIdAttr) == 		
					person.getAttributeValue(grailsApplication.config.ldap.personIdAttr)){
				if (!entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr)
									.hasValue(grailsApplication.config.ldap.cGbCServiceName)) {
					addFQNResult = ldapService.addGroupServiceToNameSpaceEntry(fullyQualifiedName)
					if (addFQNResult) {log.info("Added group service to cn=" + fullyQualifiedName + " for uid=" + 
						person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))}
					else {log.error("Error adding group service to cn=" + fullyQualifiedName + " for uid=" + 
						person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))}
				} else {
					addFQNResult = true
					log.info("Group Service entry exists:  cn=" + fullyQualifiedName + " for uid=" + 
								person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))
				}
			}
		} else {
				addFQNResult = ldapService.addNameSpaceEntry(fullyQualifiedName, 
							person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))
				if (addFQNResult){log.info("Added entry for " + fullyQualifiedName + "with uid=" + 
					person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))}
				else {log.error("Error adding entry for " + fullyQualifiedName + "with uid=" + 
						person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))}
		}
		
		if (addNameResult & addFQNResult) {
			return true
		} else {
			return false
		}
	}
	
	
	// Get Reuse Names available to this account
	// Only names that can be reused are returned. Names with and "@" or existing group names are not included.
	// @param {@code person} - the current user
    // @return possible reuse names
	def getReuseNames(person){
		def reuseNames = []
		def existingGroupNames = []
		// Use LDAP service to get the names
		def entries = ldapService.findNamesById(person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))
		for (entry in entries){
			if ( 
				entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr)
								.hasValue(grailsApplication.config.ldap.mailingListServiceName) ||
				(entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr)
								.hasValue(grailsApplication.config.ldap.cGbCServiceName) &&
				!entry.getAttributeValue(grailsApplication.config.ldap.nameSpaceNameAttr).contains('@'))
				){
					reuseNames.add(entry.getAttributeValue(grailsApplication.config.ldap.nameSpaceNameAttr))
					log.info ("Using name: " + entry.getAttributeValue(grailsApplication.config.ldap.nameSpaceNameAttr))
			} else if (
				entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr)
						.hasValue(grailsApplication.config.ldap.cGbCServiceName) &&
				entry.getAttributeValue(grailsApplication.config.ldap.nameSpaceNameAttr).contains('@')
				){
					def gName = entry.getAttributeValue(grailsApplication.config.ldap.nameSpaceNameAttr)
					existingGroupNames.add(gName.substring(0, gName.indexOf("@")))
					log.info ("Existing group name: ${gName}")
			} else {
				log.info ("Not using: " + entry.getAttributeValue(grailsApplication.config.ldap.nameSpaceNameAttr))
			}
		}
		return reuseNames.minus(existingGroupNames)
	
	}
	
	
	// Get Existing Group Names available to this account
	// Only names that can be reused are returned. Names that have cGGroupNameService as a service are selected.
	// @param {@code person} - the current user
    // @return possible reuse names	
	def getExistingGroupNames(person) {
		def existingGroupNames = []
		// Use LDAP service to get the names
		def entries = ldapService.findNamesById(person.getAttributeValue(grailsApplication.config.ldap.personIdAttr))
		for (entry in entries){
			if ((entry.getAttribute(grailsApplication.config.ldap.nameSpaceServiceAttr)
								.hasValue(grailsApplication.config.ldap.cGbCServiceName)) &&
				entry.getAttributeValue(grailsApplication.config.ldap.nameSpaceNameAttr)
										.contains(grailsApplication.config.gGroups.domain)){
				def gName = entry.getAttributeValue(grailsApplication.config.ldap.nameSpaceNameAttr)
				existingGroupNames.add(gName.substring(0, gName.indexOf("@")))
				log.info(gName)
			}
		}
		return existingGroupNames	
		
	}
	
}
