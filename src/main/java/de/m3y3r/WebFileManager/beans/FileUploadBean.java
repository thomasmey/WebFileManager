package de.m3y3r.WebFileManager.beans;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.message.SimpleContentHandler;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.MimeConfig;

public class FileUploadBean {

	private PageContext context;
	private boolean publicUpload;

	public void setPageContext(PageContext context) {
		this.context = context;
		parseInputStream();
	}

	private void parseInputStream() {

		ServletRequest request = context.getRequest();
		MimeConfig mimeConfig = new MimeConfig();
		mimeConfig.setHeadlessParsing(request.getContentType());
		MimeStreamParser mimeParser = new MimeStreamParser(mimeConfig);
		mimeParser.setContentDecoding(true);

		ContentHandler handler = new SimpleContentHandler() {

			private String contentDisposition;
			private String contentType;

			@Override
			public void headers(Header header) {
				Field f1 = header.getField("content-disposition");
				if(f1 != null)
					contentDisposition = f1.getBody();
				f1 = header.getField("content-type");
				if(f1 != null)
					contentType = f1.getBody();
			}

			@Override
			public void body(BodyDescriptor bd, InputStream is)
					throws MimeException, IOException {
				if(contentDisposition != null && contentDisposition.startsWith("form-data")) {
					
					if(contentDisposition.contains("name=\"upload\"")) {
						processUpload(is, contentDisposition);
					} else if(contentDisposition.contains("name=\"publicUpload\"")) {
						processPublicUpload(is, contentDisposition);
					}
				}
			}

		};

		mimeParser.setContentHandler(handler);

		try {
			ServletInputStream inputStream = request.getInputStream();
			mimeParser.parse(inputStream);
		} catch (MimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void copyFile(InputStream is, OutputStream os) throws IOException {
		int b = is.read();
		while(b >= 0) {
			os.write(b);
			b = is.read();
		}
	}

	private void processPublicUpload(InputStream inputStream, String contentDisposition) throws IOException {
		OutputStream os = new ByteArrayOutputStream();
		copyFile(inputStream, os);
		os.close();
		if(os.toString().contains("on"))
			publicUpload = true;
		else
			publicUpload = false;
	}

	private void processUpload(InputStream inputStream, String contentDisposition) throws IOException {

		String filename = null;

		String[] parts = contentDisposition.split("; ");
		for(String part: parts) {
			String[] ps = part.split("=");
			if("filename".equals(ps[0])) {
				filename = ps[1].substring(1, ps[1].length() - 1);
			}
		}
		File baseDir = ApplicationContext.INSTANCE.getBaseDir();

		HttpSession session = context.getSession();
		UserBean userBean = (UserBean) session.getAttribute("userBean");
		String userDir;
		if(isPublicUpload())
			userDir = ApplicationContext.INSTANCE.getPublicDirectoryName();
		else
			userDir = userBean.getUserId();

		File outFile = new File(baseDir,userDir  + File.separatorChar + filename);
		if(outFile.exists())
			return;
		OutputStream os = new BufferedOutputStream(new FileOutputStream(outFile));
		copyFile(inputStream, os);
		os.close();
	}

	private boolean isPublicUpload() {
		return publicUpload;
	}
}