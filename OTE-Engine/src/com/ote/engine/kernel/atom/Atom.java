package com.ote.engine.kernel.atom;

import java.io.Serializable;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import com.google.gson.JsonObject;
import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.contants.OTEConstants;
import com.ote.engine.kernel.exceptions.OTEEngineException;

public class Atom implements Serializable, Comparable<Atom> {
	private static final long serialVersionUID = 1L;
	public static final String TYPE_URI = OTEConstants.KERNEL_NAMESPACE+"atom";

	public static final String AUTHOR_URI = OTEConstants.KERNEL_NAMESPACE+"author";
	public final static String CREATED_URI= OTEConstants.KERNEL_NAMESPACE+"created";
	public final static String MODIFIED_URI= OTEConstants.KERNEL_NAMESPACE+"modified";


	
	private String uri;
	private String name;
	private String created;
	private String modified;
	private String comment;
	private String author;
	
	public Atom(String uri,String author, String name,String created,String modified,String comment) {
		this.uri=uri;
		this.name=name;
		this.created=created;
		this.modified=modified;
		this.comment=comment;
		this.author=author;
	}
	
	public Atom() {
		this(null, null, null, null, null, null);
	}
	
	public Atom(Atom atom) {
		this(atom.getUri(), atom.getAuthor(), atom.getName(), atom.getCreated(), atom.getModified(), atom.getComment());
	}

	@Override
	public String toString() {
		return getClass()+" \""+this.getName()+"\" "+(this.getComment()!=null?" : "+this.getComment():"")+"["+this.getUri()+"]";

	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Atom) {
			Atom element = (Atom) obj;
			return this.getUri().equals(element.getUri());
		}return super.equals(obj);
	}
	
	@Override
	public int compareTo(Atom o) {
		return this.getName().toLowerCase().compareTo((o!=null?o.getName().toLowerCase():""));
	}

	public String getUri() {return uri;}
	public void setUri(String uri) {this.uri = uri;	}
	public String getName() {return name;}
	public void setName(String label) {this.name = label;	}
	public String getCreated() {return created;}
	public void setCreated(String created) {this.created = created;}
	public String getModified() {return modified;}
	public void setModified(String modified) {this.modified = modified;}
	public String getComment() {return comment;	}
	public void setComment(String comment) {this.comment = comment;}
	
	/**
	 * @return the author login (not the uri)
	 */
	public String getAuthor() {	return author;}
	public void setAuthor(String author) {	this.author = author;}

	//Json
	public JsonObject generateJson(OTEEngine engine)  throws QueryEvaluationException, RepositoryException, MalformedQueryException, OTEEngineException{
		JsonObject data = new JsonObject();
		data.addProperty("type",this.getTypeUri());
		data.addProperty("uri",this.getUri());
		data.addProperty("name",this.getName());
		data.addProperty("created",this.getCreated());
		data.addProperty("modified",this.getModified());
		data.addProperty("comment",this.getComment());
		data.addProperty("author",this.getAuthor());
		
		return data;
	}

	public String getTypeUri(){
		return TYPE_URI;
	};
}
