package com.deleidos.rtws.systemcfg.serialize;

import com.deleidos.rtws.commons.util.Initializable;

import javax.xml.bind.MarshalException; 
import java.io.File;

public interface DefinitionSerializer extends Initializable {
	<T extends Object> void createDefinitionFile( T object, File outputFile) throws MarshalException;
	<T extends Object> T createObject(File inputFile, Class<T> type) throws MarshalException;
}
