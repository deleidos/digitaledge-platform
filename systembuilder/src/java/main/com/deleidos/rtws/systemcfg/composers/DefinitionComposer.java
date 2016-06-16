package com.deleidos.rtws.systemcfg.composers;

import com.deleidos.rtws.commons.util.Initializable;
import com.deleidos.rtws.systemcfg.beans.SystemConfig;

import javax.xml.bind.MarshalException;
import java.io.InputStream;

/**
 * 
 * @author rainerr
 *
 * Compose the objects need to build an actual file.
 * 
 */

public interface DefinitionComposer extends Initializable {
	public void writeFile(String version, String fileName);
	public void loadDefaults(InputStream  stream) throws MarshalException;
	public String compose(SystemConfig systemConfig);
}



