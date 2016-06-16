/*
The first parameter passed from Java to the script is the output file path.  For the default a script is used 
now to show/save the output of the data sink.  This file will work for any type of json object, it will work
as a generic data sink for all types as long as it processes a json object.
*/
new File(paramList[0]).withWriterAppend { out ->
	out.write("[");
      jsonObject.each() { key, value ->
        out.write("${key}=${value}, ")
    }
    out.writeLine("]");
    out.close();
}