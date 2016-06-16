package com.deleidos.rtws.tools.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.deleidos.rtws.commons.config.RtwsConfig;
import com.deleidos.rtws.commons.util.repository.AbstractSystemRepository.Visibility;
import com.deleidos.rtws.commons.util.repository.DataModelZipFile;
import com.deleidos.rtws.core.util.DataModelRepositoryRetrieve;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class DataModelNameUpdateUtil {

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
				+ "/src/config/models_to_update.txt");
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
			DataModelNameUpdateUtil util = new DataModelNameUpdateUtil();

			List<Changes> changes = new ArrayList<Changes>();
			changes.add(new Changes().withOldName("keyed_dimension")
					.withNewName("dimension_table"));
			changes.add(new Changes().withOldName("cidrip").withNewName(
					"ip_network"));
			changes.add(new Changes().withOldName("grid").withNewName(
					"mgrs_grid"));
			changes.add(new Changes().withOldName("locnet").withNewName(
					"flag_local_network"));

			for (ModelToUpdate model : models) {
				if (util.updateEnrichmentName(changes,
						util.downloadModel(model), model)) {

					File modifiedModel = new File("/tmp/modified_"
							+ model.getModelFileName());

					util.updloadModifiedModel(modifiedModel,
							model.getModelFileName());

				} else {
					System.out.println("Model modification not required: "
							+ model.getModelFileName());
				}
			}
		} else {
			System.err
					.println("ERROR: Expected list of models to modify not found.");
		}

	}

	private DataModelZipFile downloadModel(ModelToUpdate model)
			throws IOException {

		DataModelZipFile origZipFile = DataModelRepositoryRetrieve
				.getDataModelFromRepository(model.getModelFileName());

		return origZipFile;

	}

	@SuppressWarnings("rawtypes")
	private boolean updateEnrichmentName(List<Changes> changes,
			DataModelZipFile origZipFile, ModelToUpdate modelInfo)
			throws IOException {
		boolean isModified = false;
		int zipEntryCount = 0, expectedMinZipEntryCount = 4;

		File modZip = new File("/tmp/" + modelInfo.getModifiedModelFileName());
		ZipOutputStream modifiedDataModelZip = new ZipOutputStream(
				new FileOutputStream(modZip));

		List<String> datasourceNames = new ArrayList<String>();

		InputStream cis = origZipFile.getCanonicalModel();
		if (cis != null) {
			JSONObject canonicalJson = JSONObject.fromObject(IOUtils
					.toString(cis));
			System.out.println("canonicalJson: " + canonicalJson);

			ZipEntry zipEntry = new ZipEntry(DataModelZipFile.CANONICAL_PREFIX
					+ DataModelZipFile.JSON_TYPE);

			modifiedDataModelZip.putNextEntry(zipEntry);
			modifiedDataModelZip.write(canonicalJson.toString().getBytes(
					"UTF-8"));
			modifiedDataModelZip.closeEntry();
			zipEntryCount++;
		}

		InputStream dsis = origZipFile.getDataSourceParams();
		if (dsis != null) {
			JSONArray datasourcesJson = JSONArray.fromObject(IOUtils
					.toString(dsis));
			System.out.println("datasourcesJson: " + datasourcesJson);
			Iterator iterator = datasourcesJson.iterator();
			while (iterator.hasNext()) {
				JSONObject jso = (JSONObject) iterator.next();
				datasourceNames.add(jso.getString("name"));
				System.out.println(jso);
			}

			ZipEntry zipEntry = new ZipEntry(DataModelZipFile.DATASRC_PREFIX
					+ DataModelZipFile.JSON_TYPE);
			modifiedDataModelZip.putNextEntry(zipEntry);
			modifiedDataModelZip.write(datasourcesJson.toString().getBytes(
					"UTF-8"));
			modifiedDataModelZip.closeEntry();
			zipEntryCount++;
		}

		InputStream eis = origZipFile.getEnrichmentConfig();
		if (eis != null) {
			JSONArray enrichmentConfigJson = JSONArray.fromObject(IOUtils
					.toString(eis));
			System.out.println("enrichmentConfigJson: " + enrichmentConfigJson);
			JSONArray modifiedEnrichmentConfigJson = new JSONArray();

			for (Object arr : enrichmentConfigJson) {
				JSONObject enrichment = JSONObject.fromObject(arr);

				for (Changes change : changes) {

					if (enrichment.getString("enrichName").equals(
							change.getOldName())) {
						enrichment.element("enrichName", change.getNewName());
						isModified = true;
					}
				}
				modifiedEnrichmentConfigJson.add(enrichment);
			}

			System.out.println("modifiedEnrichmentConfigJson: "
					+ modifiedEnrichmentConfigJson);
			ZipEntry zipEntry = new ZipEntry(DataModelZipFile.ENRICHCFG_PREFIX
					+ DataModelZipFile.JSON_TYPE);
			modifiedDataModelZip.putNextEntry(zipEntry);
			modifiedDataModelZip.write(modifiedEnrichmentConfigJson.toString()
					.getBytes("UTF-8"));
			modifiedDataModelZip.closeEntry();
			zipEntryCount++;
		}

		InputStream emis = origZipFile.getEnrichmentModel();
		if (emis != null) {
			JSONObject enrichmentModelJson = JSONObject.fromObject(IOUtils
					.toString(emis));

			ZipEntry zipEntry = new ZipEntry(DataModelZipFile.ENRICHMENT_PREFIX
					+ DataModelZipFile.JSON_TYPE);
			modifiedDataModelZip.putNextEntry(zipEntry);
			modifiedDataModelZip.write(enrichmentModelJson.toString().getBytes(
					"UTF-8"));
			modifiedDataModelZip.closeEntry();
			zipEntryCount++;
		}

		for (String dsn : datasourceNames) {

			InputStream tis = origZipFile.getTranslationMapping(dsn);
			if (tis != null) {
				JSONObject xlateJson = JSONObject.fromObject(IOUtils
						.toString(tis));
				System.out.println("xlateJson: " + xlateJson);

				ZipEntry zipEntry = new ZipEntry(DataModelZipFile.XLATE_PREFIX
						+ "." + dsn + DataModelZipFile.JSON_TYPE);
				modifiedDataModelZip.putNextEntry(zipEntry);
				modifiedDataModelZip.write(xlateJson.toString().getBytes(
						"UTF-8"));
				modifiedDataModelZip.closeEntry();
				zipEntryCount++;
			}

		}
		modifiedDataModelZip.close();

		System.out.println("zipEntryCount: " + zipEntryCount
				+ " expectedMinZipEntryCount: " + expectedMinZipEntryCount);

		if (!isModified) {
			if (!modZip.delete())
				modZip.deleteOnExit();
		}

		return isModified;
	}

	private void updloadModifiedModel(File modifiedModel, String saveToFileName)
			throws URISyntaxException, IOException {
		System.out.println("Saving updated model to repository");

		FormDataMultiPart f = new FormDataMultiPart();
		f.bodyPart(new FileDataBodyPart("file", modifiedModel));
		f.field("filename", saveToFileName);
		f.field("userId", System.getProperty("RTWS_TENANT_ID"));
		f.field("password",
				RtwsConfig.getInstance().getString(
						"webapp.repository.tenant.password"));

		WebResource resource = REST_CLIENT_INSTANCE.resource(RtwsConfig
				.getInstance().getString("webapp.repository.url.path"));
		resource = resource.path("/rest/content/add/private/models");

		ClientResponse response = resource.type(MediaType.MULTIPART_FORM_DATA)
				.post(ClientResponse.class, f);
		System.out.println("response:" + response);

		if (!modifiedModel.delete())
			modifiedModel.deleteOnExit();
	}
}
