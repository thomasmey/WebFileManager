package de.m3y3r.WebFileManager.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.m3y3r.WebFileManager.beans.ApplicationContext;
import de.m3y3r.WebFileManager.beans.UserBean;

public class FileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// check for auth
		UserBean userBean = checkAuth(req);
		if(userBean == null) {
			forwardLogin(req, resp);
		}

		// check userId
		String path = req.getPathInfo();
		if(path == null) {
			redirectError(req, resp);
			return;
		}

		String[] p = path.split("/");
		if(p.length < 2) {
			redirectError(req, resp);
			return;
		}

		if(p[1] == null) {
			redirectError(req, resp);
			return;
		}

		if(!p[1].equals(userBean.getUserId()) && !p[1].equals(ApplicationContext.INSTANCE.getPublicDirectoryName())) {
			redirectError(req, resp);
			return;
		}

		File outputFile = new File(ApplicationContext.INSTANCE.getBaseDir(), path);

		if(!outputFile.exists() || outputFile.isDirectory()) {
			redirectError(req, resp);
			return;
		}
		if(!outputFile.getCanonicalPath().equals(outputFile.getAbsolutePath())) {
			redirectError(req, resp);
			return;
		}

		writeFileToOutputStream(resp, outputFile);
	}


	private void forwardLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/login.jsp";
		RequestDispatcher dispatcher = req.getRequestDispatcher(path);
		req.setAttribute("origURL", req.getRequestURL());
		req.setAttribute("errorMsg", "Not logged in");
		dispatcher.forward(req, resp);
		return;
	}


	private void redirectError(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String path = req.getContextPath() + "/main.jsp";
		String url = resp.encodeRedirectURL(path);
		resp.sendRedirect(url);
	}

	private UserBean checkAuth(HttpServletRequest req) {
		UserBean userBean = (UserBean) req.getSession().getAttribute("userBean");
		if(userBean == null || userBean != null && !userBean.isAuthenticated()) {
			return null;
		}
		return userBean;
	}

	private void writeFileToOutputStream(HttpServletResponse response, File outputFile) {

//		response.resetBuffer();
		response.setContentLength((int)outputFile.length());

		InputStream is = null;
		try {
			String contentType = Files.probeContentType(outputFile.toPath());
			if(contentType == null)
				contentType = "application/octet-stream";
			response.setContentType(contentType);
			is = new BufferedInputStream(new FileInputStream(outputFile));
			OutputStream os = response.getOutputStream();

			int b = is.read();
			while(b >= 0) {
				os.write(b);
				b = is.read();
			}
//			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {}
		}
	}
}
