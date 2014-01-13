package com.ote.engine.kernel.space;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.CRUD.AbstractCRUD;
import com.ote.engine.kernel.atom.Atom;
import com.ote.engine.kernel.exceptions.OTEEngineException;
import com.ote.engine.repositories.rdf.query.CreateQueryBuilder;
import com.ote.engine.repositories.rdf.query.SelectQueryBuilder;
import com.ote.engine.repositories.rdf.query.WriteQueryBuilder;

public class SpacesCRUD extends AbstractCRUD {

	public static Space loadSpaceByUri(OTEEngine engine, String uri) throws OTEEngineException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		// prepare the query
		String[] statements = { "?" + SelectQueryBuilder.URI_VARIABLE + " rdf:type <" + Space.TYPE_URI + ">." };
		SelectQueryBuilder query = new SelectQueryBuilder(engine, Space.TYPE_URI, null, statements);

		// Execute the query
		List<HashMap<String, String>> result = query.execute();
		if (!result.isEmpty()) {
			HashMap<String, String> current = result.get(0);
			// Atom properties
			Atom atom = query.extractAtomPropertiesFromResult(current);

			Space space = new Space(atom);
			return space;
		} else {
			return null;
		}
	}

	public static List<Space> loadSpaces(OTEEngine engine) throws OTEEngineException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		// prepare the query
		String[] statements = { "?" + SelectQueryBuilder.URI_VARIABLE + " rdf:type <" + Space.TYPE_URI + ">." };
		SelectQueryBuilder query = new SelectQueryBuilder(engine, Space.TYPE_URI, null, statements);

		List<Space> result = new ArrayList<Space>();
		// Execute the query
		List<HashMap<String, String>> values = query.execute();
		if (!values.isEmpty()) {
			for (HashMap<String, String> current : values) {
				// Atom properties
				Atom atom = query.extractAtomPropertiesFromResult(current);

				Space space = new Space(atom);
				result.add(space);
			}
		}

		return result;
	}

	public static Space createSpace(OTEEngine engine, String user, String name, String comment) throws OTEEngineException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		engine.logOperation(user, "createSpace", "name=" + name + "&comment=" + comment);

		if (name == null)
			throw new OTEEngineException("The name is mandatory");
		if (name.isEmpty())
			throw new OTEEngineException("The name can't be empty");

		CreateQueryBuilder query = new CreateQueryBuilder(engine);

		// Add new data
		List<Statement> statementsToAdd = new ArrayList<Statement>();
		URI currentUri = generateUri(query.getValueFactory(), Space.TYPE_URI, name);
		// Atoms properties
		statementsToAdd.addAll(query.getAtomPropertiesForCreation(query.getValueFactory(), currentUri, Space.TYPE_URI, user, name, comment));

		// execute
		query.execute(statementsToAdd);

		return loadSpaceByUri(engine, currentUri.toString());
	}

	public static Space updateSpace(OTEEngine engine, String user, String uri, String name, String comment) throws OTEEngineException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		engine.logOperation(user, "updateSpace", "uri=" + uri + "name=" + name + "&comment=" + comment);

		if (name == null)
			throw new OTEEngineException("The name is mandatory");
		if (name.isEmpty())
			throw new OTEEngineException("The name can't be empty");

		Space current = loadSpaceByUri(engine, uri);
		if (current == null)
			throw new OTEEngineException("The space does not exist : " + uri);

		WriteQueryBuilder query = new WriteQueryBuilder(engine);

		URI currentUri = query.getValueFactory().createURI(current.getUri());

		// Remove existing data
		List<Statement> statementsToDelete = new ArrayList<Statement>();
		// Atoms properties
		statementsToDelete.addAll(query.getAtomPropertiesToDeleteBeforeUpdate(query.getValueFactory(), currentUri));

		// Add new data
		List<Statement> statementsToAdd = new ArrayList<Statement>();
		// Atoms properties
		statementsToAdd.addAll(query.getAtomPropertiesToAddForUpdate(query.getValueFactory(), currentUri, name, comment));

		// execute
		query.execute(statementsToDelete, statementsToAdd);

		// load the space
		return loadSpaceByUri(engine, uri);
	}

	public static void deleteSpace(OTEEngine engine, String user, String uri) throws OTEEngineException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		engine.logOperation(user, "deleteSpace", "uri=" + uri);

		Space current = loadSpaceByUri(engine, uri);
		if (current == null)
			throw new OTEEngineException("The space does not exist : " + uri);

		WriteQueryBuilder query = new WriteQueryBuilder(engine);

		URI currentUri = query.getValueFactory().createURI(current.getUri());

		// Remove existing data
		List<Statement> statementsToDelete = new ArrayList<Statement>();
		// Atoms properties
		statementsToDelete.addAll(query.getAllPropertiesToDelete(currentUri));

	
		// execute
		query.execute(statementsToDelete, null);
	
	}
}
