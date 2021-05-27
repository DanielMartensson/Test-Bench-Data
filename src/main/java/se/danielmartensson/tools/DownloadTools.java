package se.danielmartensson.tools;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.server.StreamResource;

public class DownloadTools {
	
	public static StreamResource createStreamResource(StringWriter stringWriter) {
		StreamResource streamResource;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");  
		LocalDateTime now = LocalDateTime.now();  
		String filename = dtf.format(now) + ".csv";  
		try {
			byte[] buffer = stringWriter.toString().getBytes("UTF-8");
			streamResource = new StreamResource(filename, () -> new ByteArrayInputStream(buffer));
		} catch (Exception e) {
			byte[] buffer = new byte[] { 0 };
			streamResource =  new StreamResource(filename, () -> new ByteArrayInputStream(buffer));
		}
		return streamResource;
	}
}
