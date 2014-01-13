package com.ote.engine.kernel.CRUD;

import java.util.Date;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;

import com.ote.engine.tools.Utils;

public abstract class AbstractCRUD {

	public static String createURI(String type, String name) {
		return type + "_" + Utils.getStringForURI(name) + "_" + (new Date().getTime());
	}

	public static URI generateUri(ValueFactory valueFactory, String typeUri, String name){
		URI currentUri = valueFactory.createURI(createURI(typeUri, name));
		return currentUri;
	}
	
}
