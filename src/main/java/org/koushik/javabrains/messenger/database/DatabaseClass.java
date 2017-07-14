package org.koushik.javabrains.messenger.database;

import java.util.HashMap;
import java.util.Map;

import org.koushik.javabrains.messenger.model.Message;
import org.koushik.javabrains.messenger.model.Profile;

public class DatabaseClass {

	private static Map<Long, Message> messages = new HashMap<>(); // use "static" in order to make only one instance of messages in the project
	private static Map<String, Profile> profiles = new HashMap<>(); // we use "/profiles/profileName" to access the profile, so we use String instead of Long here
	
	public static Map<Long, Message> getMessages() {
		return messages;
	}
	public static Map<String, Profile> getProfiles() {
		return profiles;
	}
}
