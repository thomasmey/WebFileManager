package de.m3y3r.WebFileManager.beans;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public enum ApplicationContext implements ServletContextListener {

	INSTANCE;

	private final String PUBLIC_FILES = "PUBLIC";
	private final File BASE_DIR = new File("/var/lib/webfilemanager");
	private ServletContext servletContext;

	public File getBaseDir() {
		return BASE_DIR;
	}

	public String getPublicDirectoryName() {
		return PUBLIC_FILES;
	}

	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		servletContext = null;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
}
