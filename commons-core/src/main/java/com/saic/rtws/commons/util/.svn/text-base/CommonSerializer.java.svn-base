package com.saic.rtws.commons.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import com.saic.rtws.commons.config.PropertiesUtils;

/**
 * This class is used specifically as a serializer for marshalling/unmarshalling
 * with interfaces configurations. It can be set to use either jaxb or castor to
 * perform the marshalling.
 * @author HAMILTONERI
 *
 */
public class CommonSerializer {
	
	private static final Logger LOGGER = Logger.getLogger(CommonSerializer.class);
	
	
	//********************************************************************************************************
	// Marshal to file
	
	public static <T extends Object> void marshalToFile(T object, File outputFile) throws MarshalException {
		// Default serializer used is castor
		marshalToFile(object, outputFile, null, SerializerType.Castor);
	}	
	
	public static <T extends Object> void marshalToFile(T object, File outputFile, String mappingFile) throws MarshalException {
		// Default serializer used is castor
		marshalToFile(object, outputFile, mappingFile, SerializerType.Castor);
	}
	
	public static <T extends Object> void marshalToFile(T object, File outputFile, SerializerType serializerType) throws MarshalException {
		// Default serializer used is castor
		marshalToFile(object, outputFile, null, serializerType);
	}
	
	public static <T extends Object> void marshalToFile(T object, File outputFile, String mappingFile, SerializerType serializerType)
			throws MarshalException {		
		switch (serializerType) {
			case Jaxb :
				marshalJaxbToFile(object, outputFile);
				break;
			case Castor :
			default:
				marshalCastorToFile(object, outputFile, mappingFile);
				break;				
		}
	}
		
	public static <T extends Object> void marshalAsJaxbElementToFile(T object, File outputFile) throws MarshalException {
		marshalAsJaxbElementToFile(object, outputFile, SerializerType.Jaxb);
	}
		
	public static <T extends Object> void marshalAsJaxbElementToFile(T object, File outputFile, SerializerType serializerType)
			throws MarshalException {
		switch (serializerType) {
		case Jaxb :
			marshalJaxbAsJaxbElementToFile(object, outputFile);
			break;
		case Castor :
		default:
			throw new MarshalException("Castor cannot marshal a Jaxb Element");
		}
	}

	
	//********************************************************************************************************
	// Marshal to string
	
	public static <T extends Object> String marshalToString(T object) throws MarshalException {
		// Default serializer used is castor
		return marshalToString(object, null, SerializerType.Castor);
	}	
	
	public static <T extends Object> String marshalToString(T object, String mappingFile) throws MarshalException {
		// Default serializer used is castor
		return marshalToString(object, mappingFile, SerializerType.Castor);
	}
	
	public static <T extends Object> String marshalToString(T object, SerializerType serializerType) throws MarshalException {
		// Default serializer used is castor
		return marshalToString(object, null, serializerType);
	}
	
	public static <T extends Object> String marshalToString(T object, String mappingFile, SerializerType serializerType)
			throws MarshalException {
		switch (serializerType) {
			case Jaxb :
				return marshalJaxbToString(object);
			case Castor :
			default:
				return marshalCastorToString(object, mappingFile);
		}
	}
	
	public static <T extends Object> String marshalAsJaxbElementToString(T object) throws MarshalException {
		return marshalAsJaxbElementToString(object, SerializerType.Jaxb);
	}
	
	public static <T extends Object> String marshalAsJaxbElementToString(T object, SerializerType serializerType) throws MarshalException {
		switch (serializerType) {
		case Jaxb :
			return marshalJaxbAsJaxbElementToString(object);
		case Castor :
		default:
			throw new MarshalException("Castor cannot marshal a Jaxb Element");
		}
	}

	
	//********************************************************************************************************
	// Unmarshal from file
	public static <T extends Object> T unmarshalFromFile(String inputFile, Class<T> classType) throws MarshalException, IOException {
		// The default unmarshaller is castor
		return unmarshalFromFile(inputFile, classType, null, SerializerType.Castor);
	}
	
	public static <T extends Object> T unmarshalFromFile(String inputFile, Class<T> classType, String mappingFile) throws MarshalException, IOException {
		// The default unmarshaller is castor
		return unmarshalFromFile(inputFile, classType, mappingFile, SerializerType.Castor);
	}
	
	public static <T extends Object> T unmarshalFromFile(String inputFile, Class<T> classType, SerializerType serializerType)
			throws MarshalException, IOException {
		// The default unmarshaller is castor
		return unmarshalFromFile(inputFile, classType, null, serializerType);
	}
	
	public static <T extends Object> T unmarshalFromFile(String inputFile, Class<T> classType, String mappingFile, SerializerType serializerType)
			throws MarshalException, IOException {
		switch (serializerType) {
			case Jaxb :
				return unmarshalJaxbFromFile(inputFile, classType);
		case Castor :
			default:
				return unmarshalCastorFromFile(inputFile, classType, mappingFile);				
		}
	}
	
	
//========================================================================================================================
//========================================================================================================================
// Castor
		
	private static <T extends Object> org.exolab.castor.xml.Marshaller initializeCastorMarshaller(T object, String mappingFile) {
		org.exolab.castor.xml.Marshaller castorMarshaller = new org.exolab.castor.xml.Marshaller();
		
		// Set up mapping for castor if a mapping file is specified
		if( (mappingFile != null) && (!mappingFile.equals("")) ) {
			Mapping castorMapping = new Mapping();
			try {
				InputSource cMapIS = new InputSource(object.getClass().getClassLoader().getResourceAsStream(mappingFile));
				castorMapping.loadMapping(cMapIS);
				castorMarshaller.setMapping(castorMapping);
			} catch (MappingException e) {
				LOGGER.error("Marshaller unable to load Castor mapping. Proceeding without any mapping... Error cause: " + e.toString());
				// Make sure the marshaller is mapping free
				castorMarshaller = new org.exolab.castor.xml.Marshaller();
			}
		}
		
		return castorMarshaller;
	}
	
	private static <T extends Object> org.exolab.castor.xml.Unmarshaller initializeCastorUnmarshaller(Class<T> classType, String mappingFile) {
		org.exolab.castor.xml.Unmarshaller castorUnmarshaller = new org.exolab.castor.xml.Unmarshaller();
		
		// Set up mapping for castor if a mapping file is specified
		if( (mappingFile != null) && (!mappingFile.equals("")) ) {
			InputSource cMapIS = new InputSource(classType.getResourceAsStream(mappingFile));
			Mapping castorMapping = new Mapping();
			castorMapping.loadMapping(cMapIS);
			castorUnmarshaller = new org.exolab.castor.xml.Unmarshaller(classType);
			try {
				castorUnmarshaller.setMapping(castorMapping);
			} catch (MappingException e) {
				LOGGER.error("Unmarshaller unable to load Castor mapping. Proceeding without any mapping... Error cause: " + e.toString());
				// Make sure the unmarshaller is mapping free
				castorUnmarshaller = new org.exolab.castor.xml.Unmarshaller(classType);
			}
		}
		
		return castorUnmarshaller;
	}
	
	@SuppressWarnings("static-access")
	private static <T extends Object> String marshalCastorToString(T object, String mappingFile) throws MarshalException {
		StringWriter writer = new StringWriter();
		
		// Initialize the mashaller
		org.exolab.castor.xml.Marshaller castorMarshaller = initializeCastorMarshaller(object, mappingFile);
		
		// Marshal the object to the stream
		try {
			castorMarshaller.marshal(object, writer);
		}catch(Exception castore){
			LOGGER.error(castore.toString(), castore);
			throw new MarshalException("Error marshalling class as castor: " + castore.getMessage());
		}

		// The xml in the output stream contains the xml version and encoding tag as the
		// first tag, we need to remove that
		return writer.toString().replaceAll("<\\?(.*)\\?>", "");
	}

	private static <T extends Object> void marshalCastorToFile(T object, File outputFile, String mappingFile) throws MarshalException {
		// Initialize the mashaller
		org.exolab.castor.xml.Marshaller castorMarshaller = initializeCastorMarshaller(object, mappingFile);
		
		// Marshal the object to a file
		try{
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile));
		
			castorMarshaller.setWriter(writer);
			castorMarshaller.marshal(object);
			castorMarshaller.setProperty("org.exolab.castor.indent", "true");  //format output xml file
			
			writer.close();
		}catch(Exception castore){
			LOGGER.error(castore.toString(), castore);
			throw new MarshalException("Error marshalling class as castor: " + castore.getMessage());
		}
	}
	
	private static <T extends Object> T unmarshalCastorFromFile(String inputFile, Class<T> classType, String mappingFile)
			throws IOException, MarshalException {
		org.exolab.castor.xml.Unmarshaller castorUnmarshaller = initializeCastorUnmarshaller(classType, mappingFile);
		
		T object = null;
		
		InputStreamReader objectInputSource;
		if(CommonSerializer.class.getResourceAsStream(inputFile) != null) {
			objectInputSource = new InputStreamReader(CommonSerializer.class.getResourceAsStream(inputFile));
		}
		else {
			// The resource is null so load it from the system
			objectInputSource = new InputStreamReader(PropertiesUtils.loadResource(inputFile));
		}
		
		try {
			object = classType.cast(castorUnmarshaller.unmarshal(objectInputSource));
		} catch (org.exolab.castor.xml.MarshalException e) {
			LOGGER.error(e.toString(), e);
			throw new MarshalException(e);
		} catch (ValidationException e) {
			LOGGER.error(e.toString(), e);
			throw new MarshalException(e);
		}
		finally {
			objectInputSource.close();
		}
				
		return object;
	}
	

//========================================================================================================================
//========================================================================================================================
// Jaxbe

	private static <T extends Object> javax.xml.bind.Marshaller initializeJaxbMarshaller(T object) throws MarshalException {
		javax.xml.bind.Marshaller jaxbMarshaller = null;
		try {
			javax.xml.bind.JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty("jaxb.formatted.output", true);
		} catch (JAXBException jaxbe) {
			LOGGER.error(jaxbe.toString(), jaxbe);
			throw new MarshalException("Error creating jaxbe marshaller " + jaxbe.getErrorCode() + " : " + jaxbe.getMessage());
		}
		
		return jaxbMarshaller;
	}

	private static <T extends Object> javax.xml.bind.Unmarshaller initializeJaxbUnmarshaller(Class<T> classType) throws MarshalException {
		try {
			javax.xml.bind.JAXBContext jaxbContext = JAXBContext.newInstance(classType);
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException jaxbe) {
			LOGGER.error(jaxbe.toString(), jaxbe);
			throw new MarshalException("Error creating jaxbe unmarshaller " + jaxbe.getErrorCode() + " : " + jaxbe.getMessage());
		}
	}
	
	private static <T extends Object> void marshalJaxbToFile(T object, File outputFile) throws MarshalException {
		// Initialize the mashaller
		javax.xml.bind.Marshaller jaxbMarshaller = initializeJaxbMarshaller(object);
		
		// Marshal the object to a file
		try {
			jaxbMarshaller.marshal(object, outputFile);
		} catch(JAXBException jaxbe) {
			LOGGER.error(jaxbe.toString(), jaxbe);
			throw new MarshalException("Error marshalling class as jaxb: " + jaxbe.getErrorCode() + " : " + jaxbe.getMessage());
		}
	}
	
	private static <T extends Object> void marshalJaxbAsJaxbElementToFile(T object, File outputFile) throws MarshalException {
		
		// Initialize the mashaller
		javax.xml.bind.Marshaller jaxbMarshaller = initializeJaxbMarshaller(object);
		
		// Make the object a JaxbElement
		JAXBElement<T> jaxbElement = makeJaxbElement(object);
		
		// Marshal the object to a file
		try {
			jaxbMarshaller.marshal(jaxbElement, outputFile);
		} catch(JAXBException jaxbe) {
			LOGGER.error(jaxbe.toString(), jaxbe);
			throw new MarshalException("Error marshalling class as jaxb: " + jaxbe.getErrorCode() + " : " + jaxbe.getMessage());
		}	
	}
	
	private static <T extends Object> String marshalJaxbToString(T object) throws MarshalException {
		OutputStream os = new ByteArrayOutputStream();
		// Initialize the mashaller
		javax.xml.bind.Marshaller jaxbMarshaller = initializeJaxbMarshaller(object);
		
		// Marshal the object to the stream
		try {
			jaxbMarshaller.marshal(object, os);
		} catch (JAXBException jaxbe) {
			LOGGER.error(jaxbe.toString(), jaxbe);

			throw new MarshalException("Error marshalling class as jaxb: " + jaxbe.getErrorCode() + " : " + jaxbe.getMessage());
		}

		// The xml in the output stream contains the xml version and encoding tag as the
		// first tag, we need to remove that
		return os.toString().replaceAll("<\\?(.*)\\?>", "");
	}
	
	private static <T extends Object> String marshalJaxbAsJaxbElementToString(T object) throws MarshalException {
		OutputStream os = new ByteArrayOutputStream();
		
		// Initialize the mashaller
		javax.xml.bind.Marshaller jaxbMarshaller = initializeJaxbMarshaller(object);

		// Make the object a JaxbElement
		JAXBElement<T> jaxbElement = makeJaxbElement(object);
		
		// Marshal the object to the stream
		try {
			jaxbMarshaller.marshal(jaxbElement, os);
		} catch (JAXBException jaxbe) {
			LOGGER.error(jaxbe.toString(), jaxbe);

			throw new MarshalException("Error marshalling class as jaxb: " + jaxbe.getErrorCode() + " : " + jaxbe.getMessage());
		}

		// The xml in the output stream contains the xml version and encoding tag as the
		// first tag, we need to remove that
		return os.toString().replaceAll("<\\?(.*)\\?>", "");		
	}
	
	private static <T extends Object> T unmarshalJaxbFromFile(String inputFile, Class<T> classType) throws MarshalException {
		// Initialize the unmashaller
		javax.xml.bind.Unmarshaller jaxbUnmarshaller = initializeJaxbUnmarshaller(classType);
		
		T object = null;
		
		// Unmarshal the object from the file
		InputStream resource = null;
		try {
			resource = CommonSerializer.class.getResourceAsStream(inputFile);
			if(resource != null) {
				object = classType.cast(jaxbUnmarshaller.unmarshal(CommonSerializer.class.getResource(inputFile)));
			}
			else {
				// The resource is null so load it from the system
				object = classType.cast(jaxbUnmarshaller.unmarshal(PropertiesUtils.loadResource(inputFile)));
			}
		} catch(JAXBException jaxbe) {
			LOGGER.error(jaxbe.toString(), jaxbe);
			throw new MarshalException("Error unmarshalling class as jaxb: " + jaxbe.getErrorCode() + " : " +jaxbe.getMessage());
		} catch(Exception ex) {
			LOGGER.error(ex.toString(), ex);
			throw new MarshalException("Unknown error: " + ex.getMessage());
		}finally{
			if(resource!=null){
				try{
					resource.close();
				}catch(IOException ignore){}
			}
		}
		
		return object;
	}
		
	@SuppressWarnings("unchecked")
	private static <T extends Object> JAXBElement<T> makeJaxbElement(T object) {
		String className = object.getClass().getName();
		className = className.substring(className.lastIndexOf('.')+1);  // Get only the class name
		className = className.substring(0,1).toLowerCase() + className.substring(1);
		QName qName = new QName(null, className);
		
		return new JAXBElement<T>(qName, (Class<T>) object.getClass(), object);
	}

	
//========================================================================================================================
//========================================================================================================================
// SerializerType Enumeration

	public enum SerializerType {
		Jaxb("jaxb"), Castor("castor");
		
		private String name;
		
		private SerializerType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public static SerializerType fromValue(String value) {
			for (SerializerType serializerType : SerializerType.values()) {
				if (serializerType.getName().equalsIgnoreCase(value)) {
					return serializerType;
				}
			}
			
			return null;
		}
	}
	
}
