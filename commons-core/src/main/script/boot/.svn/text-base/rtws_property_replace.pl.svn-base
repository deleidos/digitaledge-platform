#!/usr/bin/perl


use warnings;
use strict;

my %o;
my $properties_file;
my $config_file;
my $key;
my $value;
my $ext_key;
my $ext_val;

#replace single property
#args: 1) file to replace property in, 2) property name, 3) property value
if(@ARGV == 3){
	$config_file = $ARGV[0];
	$ext_key = $ARGV[1]; #external passed in key/value pair
	$ext_val = $ARGV[2]; #external passed in key/value pair

	#check if ext key/value pair passed in, used to overwrite any param in the property file
	if($ext_key && $ext_val){
		$ext_val =~ s/([\/])/\\$1/g;
		system("/usr/bin/perl -p -i -e \"s/\\@" . $ext_key . "\\@/" . $ext_val . "/g\" " . $config_file);
		#print "/usr/bin/perl -p -i -e \"s/\\@" . $ext_key . "\\@/" . $ext_val . "/g\" " . $config_file . "\n";
	}
}

#replace properties from file, one override param possible, 
#args:  1)property file, 2) file/dir to filter, 3) property name, 4) property value
elsif(@ARGV == 4 || @ARGV == 2){
	$properties_file = $ARGV[0];
	$config_file = $ARGV[1];
	$ext_key = $ARGV[2]; #external passed in key/value pair
	$ext_val = $ARGV[3]; #external passed in key/value pair

	#check if ext key/value pair passed in, used to overwrite any param in the property file
	if($ext_key && $ext_val){
		$ext_val =~ s/([\/])/\\$1/g;
		system("/usr/bin/perl -p -i -e \"s/\\@" . $ext_key . "\\@/" . $ext_val . "/g\" " . $config_file);
		#print "/usr/bin/perl -p -i -e \"s/\\@" . $ext_key . "\\@/" . $ext_val . "/g\" " . $config_file . "\n";
	}

	if(-f $config_file){
		filePropertyReplace($config_file);
	}
	elsif(-d $config_file){
		opendir(DIR, $config_file);
		my @FILES= readdir(DIR); 
		foreach(@FILES){
			if($_ ne "." && $_ ne ".."){
				#print $_ . "\n";
				filePropertyReplace($config_file . "/" . $_);
			}
	
		}
		closedir(DIR);
	}
	else{
		die "Couldn't open $config_file for reading.";
	}

}
else{
	print "wrong number of args passed in "  . @ARGV . "\n";
	print "Usage:\n";
	print "rtws_property_replace {file_with_prop_to_replace} {property_name} {property_value}\n";
	print "rtws_property_replace {property_file} {file_with_prop_to_replace} {property_name} {property_value}\n";
}

#function common code to call the property replace.
sub filePropertyReplace {
	my $tmp_file = $_[0];
#	print $tmp_file ."\n";
	open(PROPERTIES_FILE, $properties_file) or die "Couldn't open $properties_file for reading.";

	while(<PROPERTIES_FILE>){
		$o{$1}=$2 while m/([a-zA-Z0-9\.\_\#]+)+=(\S+)/g;
    	if($1 && $2 && $1 !~ "\n" && $2 !~ "\n"){
    		#print "$1 and $2\n";
    		$key = $1;
    		$value = $2;
    		$value =~ s/([\/])/\\$1/g;

    		if($key !~ "#"){
				system("/usr/bin/perl -p -i -e \"s/\\@" . $key . "\\@/" . $value . "/g\" " . $tmp_file);
				#print "/usr/bin/perl -p -i -e \"s/\\@" . $key . "\\@/" . $value . "/g\" " . $tmp_file . "\n";
			}
    	}	
    	$key=$value=undef;  #not needed?
	} 

	close(PROPERTIES_FILE);
}
