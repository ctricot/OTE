package com.ote.engine.repositories;

import java.io.File;

import org.openrdf.repository.RepositoryException;

import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.exceptions.OTEEngineException;
import com.ote.engine.repositories.rdf.RdfConnector;

public class OTERepositories {
	private OTEEngine engine;
	private File fileDirectory;
	private RdfConnector rdfConnector;

	public OTERepositories(OTEEngine engine, File fileDirectory, String rdfServerUrl, String repositoryID) throws OTEEngineException {
		this.engine = engine;

		// init the repositories
		initialize(fileDirectory, rdfServerUrl, repositoryID);

		getEngine().getLogger().info("Repositories are ready");
	}

	// Accessors
	public OTEEngine getEngine() {
		return engine;
	}

	public File getFileDirectory() {
		return fileDirectory;
	}

	public RdfConnector getRdfConnector() {
		return rdfConnector;
	}

	// Init
	private void initialize(File fileDirectory, String rdfServerUrl, String repositoryID) throws OTEEngineException {
		this.fileDirectory = fileDirectory;
		try {
			this.rdfConnector = new RdfConnector(rdfServerUrl, repositoryID);
		} catch (RepositoryException e) {
			throw new OTEEngineException(e);
		}

		// we create the data folder if necessary
		if (!fileDirectory.exists()) {
			fileDirectory.mkdir();
			getEngine().getLogger().warn("The directory (" + fileDirectory.getAbsolutePath() + ") to store the files does not exists. The system will create it.");
		}
	}

	// Manage content
	/**
	 * Erase all data in repositories
	 * @throws OTEEngineException
	 */
	public void clearAllDataAndFiles() throws OTEEngineException {
		this.getRdfConnector().clear();

		// remove all files
		String[] list = getFileDirectory().list();
		for (int i = 0; i < list.length; i++) {
			File current = new File(this.getFileDirectory().getAbsolutePath() + File.separator + list[i]);
			current.delete();
		}

		getEngine().getLogger().trace("The repositories are empty.");
	}

}
