package com.ote.engine.repositories.rdf.query;

import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.atom.Atom;
import com.ote.engine.kernel.exceptions.OTEEngineException;
import com.ote.engine.tools.Utils;

public class CreateQueryBuilder extends SparqlQueryBuilder {

	public CreateQueryBuilder(OTEEngine engine) throws OTEEngineException {
		super(engine);
	}

	public void execute(List<Statement> statementsToAdd) throws OTEEngineException {
		try {
			RepositoryConnection con = getEngine().getRdfConnector().getConnection();
			try {
				// Add new values
				for (Statement statement : statementsToAdd) {
					con.add(statement);
				}			
			} finally {
				con.close();
			}

		} catch (RepositoryException e) {
			throw new OTEEngineException(e);
		}
	}
	
	public List<Statement> getAtomPropertiesForCreation(ValueFactory valueFactory, URI currentUri, String typeUri, String authorLogin, String name, String comment) {
		List<Statement> result = new ArrayList<Statement>();

		// type
		result.add( new StatementImpl(currentUri, RDF.TYPE, valueFactory.createURI(typeUri)));

		// author
		result.add( new StatementImpl(currentUri, valueFactory.createURI(Atom.AUTHOR_URI), valueFactory.createLiteral(authorLogin)));

		// name
		result.add( new StatementImpl(currentUri, RDFS.LABEL, valueFactory.createLiteral(name)));

		// comment
		if (comment != null)
			if (!comment.isEmpty())
				result.add( new StatementImpl(currentUri, RDFS.COMMENT, valueFactory.createLiteral(comment)));
		
		// modified date
		result.add( new StatementImpl(currentUri, valueFactory.createURI(Atom.MODIFIED_URI), valueFactory.createLiteral(Utils.getCurrentDateInString())));

		// created date
		result.add( new StatementImpl(currentUri, valueFactory.createURI(Atom.CREATED_URI), valueFactory.createLiteral(Utils.getCurrentDateInString())));
		
		
		return result;
	}
	
}
