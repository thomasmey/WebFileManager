package de.m3y3r.WebFileManager.beans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

public class UserBean {

	private String userId;
	private String password;
	private boolean authenticated;

	private Properties userPassword;

	public UserBean() {
		initUserPassword();
	}

	private void initUserPassword() {
		userPassword = new Properties();
		InputStream inputStream = this.getClass().getResourceAsStream("passwords.xml");
		if(inputStream != null) {
			try {
				userPassword.loadFromXML(inputStream);
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		this.password = null;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		authenticate();
	}

	private void authenticate() {
		authenticated = false;

		if(userPassword.containsKey(userId)) {
			String password = (String) userPassword.get(userId);
			if(password.equals(this.password))
				authenticated = true;
		}
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public Map<String, File> getPrivateFiles() {
		File baseDir = ApplicationContext.INSTANCE.getBaseDir();
		File[] privateFiles = new File(baseDir, userId).listFiles();
		return getFiles(privateFiles);
	}

	public Map<String, File> getPublicFiles() {
		File baseDir = ApplicationContext.INSTANCE.getBaseDir();
		String publicFolderName = ApplicationContext.INSTANCE.getPublicDirectoryName();
		File[] publicFiles = new File(baseDir, publicFolderName).listFiles();
		return getFiles(publicFiles);
	}

	private Map<String, File> getFiles(File[] publicFiles) {
		Map<String, File> files = new HashMap<String, File>();
		int i = 0;
		for(File file: publicFiles) {
			files.put(String.valueOf(i++), file);
		}
		return files;
	}

	public String getPublicDirectoryName() {
		return ApplicationContext.INSTANCE.getPublicDirectoryName();
	}
}
