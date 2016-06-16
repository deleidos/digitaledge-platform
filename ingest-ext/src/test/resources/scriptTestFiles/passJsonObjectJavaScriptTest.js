//Test script for JSR-223 Script Engine, JSON object passed to script then prints the object
//Test passes if no exception occurs while executing the script with the JSON object
//parse json string into object then access elements

println("JSON Object passed in from Java to Javascript is:  " + jsonObject)

var myObject = eval('(' +jsonObject + ')');

println("Object Name:  " + myObject.name);
println("Object Number:  " + myObject.number);
println("Object Phone:  " + myObject.phone);
