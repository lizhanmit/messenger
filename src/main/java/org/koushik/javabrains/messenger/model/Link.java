package org.koushik.javabrains.messenger.model;


// do not need to annotate @XmlRootElement here, as this class is not a root element, it is going to be a member attribute of the Message class
public class Link {

	private String link; 
	private String rel;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	} 
}
