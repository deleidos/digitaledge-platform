println "JSON Object passed in from Java to Groovy is:  " + jsonObject;

println "Object name:  " + jsonObject.name;
println "Object number:  " + jsonObject.number;
println "Object phone:  " + jsonObject.phone;

println "check for parameters:  " + paramList;

for(i in paramList){
	println i;
}