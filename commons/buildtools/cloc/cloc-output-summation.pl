#!/usr/bin/perl -w 
# titlebytes - find the title and size of documents 
use LWP::UserAgent; 
use HTTP::Request; 
use HTTP::Response; 
use URI::Heuristic;
my $raw_url = "http://ec2-23-21-234-240.compute-1.amazonaws.com:8080/jenkins/job/Tools_Cloc-All/lastBuild/consoleText"; #shift                      or die "usage: $0 url\n"; 
my $url = URI::Heuristic::uf_urlstr($raw_url);
$| = 1;                                  # to flush next line 
printf "%s =>\n\t", $url;
my $ua = LWP::UserAgent->new(); 
$ua->agent("Schmozilla/v9.14 Platinum"); # give it time, it'll get there
my $req = HTTP::Request->new(GET => $url); 
$req->referer("http://dev-ci.rtsaic.com");

# perplex the log analysers
my $response = $ua->request($req);
if ($response->is_error()) {
     printf " Cannot connect to %s<br>\n", $raw_url;
     printf " %s\n", $response->status_line;
} 
else {
     my $count;
     my $bytes;
     my $content = $response->content();
     $bytes = length $content;
     $count = ($content =~ tr/\n/\n/);
     printf " Connection response: %s\n", $response->status_line;
     printf "%d lines, %d bytes\n", $response->title(), $count, $bytes; 
     #printf "\n%s\n", $content;
     
     # \d+ matches one or more integer numbers
     my @contentLines = split(/\n/, $content);
     
     my %langCode = ();
     my %langFiles = ();
     my %langBlank = ();
     my %langComment = ();
     my $inBlock = "0";
     
     foreach my $line (@contentLines) {
         #trim whitespace
         my $contentLine = $line;
         $contentLine =~ s/^\s+//;
         $contentLine =~ s/\s+$//;
    
         #print "line: $contentLine\n";
         if ($inBlock eq "0" && $contentLine =~ /\[exec\]\s+Language\s+files\s+blank\s+comment\s+code/) {
             #print "In Block\n";
             $inBlock = "1";
         }
         elsif ($inBlock eq "1" && $contentLine =~ /\[exec\]\s+SUM:\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)/) {
             #print "Out Block\n";
             $inBlock = "0";
         }
         elsif ($inBlock eq "1") {
             #print "Check Block: $contentLine\n";
             if ($contentLine =~ /\[exec\]\s+(\S+)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)/) {
                 #print "Str2 Match $0 $1 $2 $3 $4\n";
                 $langFiles{$1} += $2;
                 $langBlank{$1} += $3;
                 $langComment{$1} += $4;
                 $langCode{$1} += $5;
             }
             else {
                 #print "Str2 No Match on $contentLine\n";
             }
         }
     }
     
     my $sumFiles = 0;
     my $sumBlank = 0;
     my $sumComment = 0;
     my $sumCode  = 0;
     
     print "-------------------------------------------------------------------------------\n";
     print "Language                     files          blank        comment           code\n";
     print "-------------------------------------------------------------------------------\n";
     foreach my $lang (keys %langFiles) {
         # sum counts
         $sumFiles += $langFiles{$lang};
         $sumBlank += $langBlank{$lang};
         $sumComment += $langComment{$lang};
         $sumCode  += $langCode{$lang};
         
         # output in formatted form
         printf "%-19s %14s %14s %14s %14s\n", $lang, $langFiles{$lang}, $langBlank{$lang}, $langComment{$lang}, $langCode{$lang};
     }
     print "-------------------------------------------------------------------------------\n";
     printf "SUM:                %14s %14s %14s %14s\n", $sumFiles, $sumBlank, $sumComment, $sumCode;
     print "-------------------------------------------------------------------------------\n";
     
     #my $str1 = "     [exec] Language                     files          blank        comment           code";
     #my $str2 = "     [exec] Java                            30            665            457           1822";
     #my $str3 = "     [exec] SUM:                            31            667            458           1823";
     #
     #if ($str1 =~ /\s+\[exec\]\s+Language\s+files\s+blank\s+comment\s+code\s*/) {
     #   print "Str1 Match\n";
     #}
     #if ($str2 =~ /\s+\[exec\]\s+(\S+)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s*/) {
     #   print "Str2 Match $0 $1 $2 $3 $4 $5 $6\n";
     #}
     #if ($str3 =~ /\s+\[exec\]\s+SUM:\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s*/) {
     #   print "Str3 Match\n";
     #}
} 
