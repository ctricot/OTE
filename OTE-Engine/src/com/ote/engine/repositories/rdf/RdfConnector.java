package com.ote.engine.repositories.rdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.openrdf.model.Resource;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

import com.ote.engine.kernel.contants.OTEConstants;
import com.ote.engine.kernel.exceptions.OTEEngineException;

public class RdfConnector {
	private Repository repository;

	public RdfConnector(String rdfServerURL, String repositoryID) throws RepositoryException {
		this.repository = new HTTPRepository(rdfServerURL, repositoryID);
		this.repository.initialize();
	}

	// Accessors
	public Repository getRepository() {
		return repository;
	}

	public RepositoryConnection getConnection() throws OTEEngineException {
		try {
			return this.getRepository().getConnection();
		} catch (RepositoryException e) {
			throw new OTEEngineException(e);
		}
	}

	public void closeConnection() throws OTEEngineException {
		try {
			this.getConnection().close();
		} catch (RepositoryException e) {
			throw new OTEEngineException(e);
		}
	}

	
	/**
	 * Empty the RDF repository
	 * @throws OTEEngineException
	 */
	public void clear() throws OTEEngineException {
		try {
			try {
				this.getConnection().clear(new Resource[0]);
			} finally {
				this.getConnection().close();
			}
		} catch (RepositoryException e) {
			throw new OTEEngineException(e);
		}
	}

	/**
	 * Load RDF data in the repository
	 * 
	 * @param clearBefore
	 *            True if you want to delete the current repository data before
	 *            loading data
	 * @param filename
	 *            path to source data
	 * @throws OTEEngineException
	 */
	public void loadRdfData(boolean clearBefore, String filename) throws OTEEngineException {
		try {
			if (clearBefore)
				this.clear();

			RepositoryConnection con = this.getRepository().getConnection();
			File data = new File(filename);
			if (!data.exists())
				throw new RepositoryException("The file does not exist :" + filename);
			try {
				con.add(data, OTEConstants.BASE_URI, RDFFormat.RDFXML);
				con.commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			throw new OTEEngineException(e);
		}
	}

	/**
	 * Load RDF data in the repository
	 * 
	 * @param clearBefore
	 *            True if you want to delete the current repository data before
	 *            loading data
	 * @param is
	 *            InputStream to source data
	 * @throws OTEEngineException
	 */
	public void loadRdfData(boolean clearBefore, InputStream is) throws OTEEngineException {
		try {
			RepositoryConnection con = this.getRepository().getConnection();
			if (clearBefore)
				con.clear(new Resource[0]);

			try {
				con.add(is, OTEConstants.BASE_URI, RDFFormat.RDFXML);
			} finally {
				con.close();
			}
		} catch (RDFParseException e) {
			throw new OTEEngineException(e);
		} catch (IOException e) {
			throw new OTEEngineException(e);
		} catch (RepositoryException e) {
			throw new OTEEngineException(e);
		}
	}

	public ValueFactory getValueFactory() {
		return this.getRepository().getValueFactory();
	}
}
