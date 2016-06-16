package com.deleidos.rtws.tools.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.deleidos.rtws.commons.util.repository.AbstractSystemRepository.Visibility;
import com.deleidos.rtws.commons.util.repository.DataModelZipFile;
import com.deleidos.rtws.core.util.DataModelRepositoryRetrieve;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class BulkDataModelDownloadUtil {

	public static class Changes {
		private String newName;
		private String oldName;

		public String getNewName() {
			return newName;
		}

		public String getOldName() {
			return oldName;
		}

		public void setNewName(String newName) {
			this.newName = newName;
		}

		public void setOldName(String oldName) {
			this.oldName = oldName;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Changes [newName=").append(newName)
					.append(", oldName=").append(oldName).append("]");
			return builder.toString();
		}

		public Changes withNewName(String name) {
			newName = name;
			return this;
		}

		public Changes withOldName(String name) {
			oldName = name;
			return this;
		}

	}

	public static class ModelToUpdate {
		private String modelFileName;
		private String modelName;
		private Visibility visibility;
		private String modifiedModelFileName;

		public String getModelFileName() {
			return modelFileName;
		}

		public void setModelFileName(String modelFileName) {
			this.modelFileName = modelFileName;
			this.modifiedModelFileName = "modified_" + this.modelFileName;
		}

		public String getModelName() {
			return modelName;
		}

		public void setModelName(String modelName) {
			this.modelName = modelName;
		}

		public Visibility getVisibility() {
			return visibility;
		}

		public void setVisibility(Visibility visibility) {
			this.visibility = visibility;
		}

		public ModelToUpdate withModelName(String name) {
			this.modelName = name;
			return this;
		}

		public ModelToUpdate withModelFileName(String filename) {
			this.modelFileName = filename;
			this.modifiedModelFileName = "modified_" + this.modelFileName;
			return this;
		}

		public ModelToUpdate withVisibility(Visibility v) {
			this.setVisibility(v);
			return this;
		}

		public String getModifiedModelFileName() {
			return modifiedModelFileName;
		}

		public void setModifiedModelFileName(String modifiedModelFileName) {
			this.modifiedModelFileName = modifiedModelFileName;
		}

		public ModelToUpdate withModifiedModelFileName(String newName) {
			this.modifiedModelFileName = newName;
			return this;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ModelToUpdate [modelFileName=")
					.append(modelFileName).append(", modelName=")
					.append(modelName).append(", visibility=")
					.append(visibility).append(", modifiedModelFileName=")
					.append(modifiedModelFileName).append("]");
			return builder.toString();
		}

	}

	private static Client REST_CLIENT_INSTANCE;

	static {
		ClientConfig config = new DefaultClientConfig();
		config.getProperties()
				.put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10000);
		config.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, 30000);

		REST_CLIENT_INSTANCE = Client.create(config);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException,
			URISyntaxException {

		System.out
				.println("Assuming PWD is: " + System.getProperty("user.dir"));
		File modelList = new File(System.getProperty("user.dir")
				+ "/src/config/models_to_download.txt");
		if (modelList.exists()) {

			List<ModelToUpdate> models = new ArrayList<ModelToUpdate>();

			System.out.println("Populating list of models to update using: "
					+ modelList.getCanonicalPath());

			BufferedReader br = new BufferedReader(new FileReader(modelList));
			while (br.ready()) {
				String line = br.readLine();
				if (!line.startsWith("#") && line.length() > 0) {
					String[] modelInfo = line.split(";");
					models.add(new ModelToUpdate().withModelName(modelInfo[0])
							.withModelFileName(modelInfo[1])
							.withVisibility(Visibility.Private));
				}

			}
			br.close();

			Properties updatedProperties = System.getProperties();
			updatedProperties.setProperty("RTWS_TENANT_ID", "aws-dev");
			System.setProperties(updatedProperties);
			BulkDataModelDownloadUtil util = new BulkDataModelDownloadUtil();

			for (ModelToUpdate model : models) {
				util.downloadModel(model);
			}
		}
	}

	private void downloadModel(ModelToUpdate model) throws IOException {

		try {
			InputStream is = DataModelRepositoryRetrieve
					.getRawDataModelFromRepository(model.getModelFileName());
			FileOutputStream fos = new FileOutputStream(new File("/tmp/"
					+ model.getModelFileName()));
			IOUtils.copyLarge(is, fos);
			IOUtils.closeQuietly(is);
			System.out.println("Wrote: " + "/tmp/" + model.getModelFileName());
		} catch (IOException e) {
			System.err.println("Failed to download: "
					+ model.getModelFileName());
		}

	}
}
