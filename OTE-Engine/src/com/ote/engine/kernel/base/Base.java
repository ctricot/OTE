package com.ote.engine.kernel.base;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.atom.Atom;
import com.ote.engine.kernel.contants.OTEConstants;
import com.ote.engine.kernel.exceptions.OTEEngineException;
import com.ote.engine.kernel.space.Space;
import com.ote.engine.kernel.space.SpacesCRUD;


public class Base extends Atom{
	private static final long serialVersionUID = 1L;
	
	public static final String TYPE_URI = OTEConstants.KERNEL_NAMESPACE+"base";
	public static final String 	NAMESPACE_URI =  OTEConstants.KERNEL_NAMESPACE+"namespace";

	
	private String namespace;
	
	public Base() {
		this(null,null,null,null,null,null,null);
	}

	public Base(String uri, String author, String name, String namespace,String comment, String created, String modified) {
		super(uri,author, name,created,modified,comment);
		this.namespace=namespace;
	}
	
	public Base(Atom atom, String namespace) {
		super(atom);
		this.namespace=namespace;
	}

	@Override
	public String getTypeUri() {
		return Base.TYPE_URI;
	}
	
	@Override
	public String toString() {
		return super.toString()+" (namespace="+this.getNamespace()+")";
	}
	
	public String getNamespace() {
		return namespace;
	}

	//Json
	public JsonObject generateJson(OTEEngine engine) throws QueryEvaluationException, RepositoryException, MalformedQueryException, OTEEngineException {
		JsonObject baseOut = super.generateJson(engine);
		
		baseOut.addProperty("namespace", this.getNamespace());
		
		JsonArray spaces = new JsonArray();
		baseOut.add("spaces", spaces);
		for (Space space : SpacesCRUD.loadSpaces(engine)) {
			spaces.add(space.generateJson(engine));
		}
		
		return baseOut;
	}

	

}