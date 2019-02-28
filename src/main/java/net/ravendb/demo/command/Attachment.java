package net.ravendb.demo.command;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.flow.server.StreamResource;

// todo: Attachment is too general class and has a misleading name, it should probably be just a "Picture" class, or even to be entirely removed and replaced by byte[] array
public class Attachment {

	String name;
	String mimeType;
	byte[] bytes; 

	// todo: name, mimeType are properties that is known in the design time, and probably whould not be part of the class
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public byte[] getBytes(){
		return bytes;
	}
	
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public InputStream getInputStream(){
		return new ByteArrayInputStream(bytes);
	}
	public StreamResource getStreamResource(){
	  ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	  return new StreamResource(name, () -> bis);
	}
	
}
