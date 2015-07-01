package com.saic.rtws.commons.util;

import java.util.regex.Pattern;

/**
 * Helper class used for the conversion and validation of IP addresses.
 * @author floydac
 *
 */
public class IPFormat {

	private IPFormat(){}
	
	private static final String IPRANGE = "[01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5]"; //0-255
	private static final Pattern IPREGEX = Pattern.compile("(" + IPRANGE + ")\\.(" + IPRANGE + ")\\.(" + IPRANGE + ")\\.(" + IPRANGE + ")");
	
	/**
	 * Validates a string IP address
	 * @param s The string IP
	 * @return Whether or not the input is a valid IP address
	 */
	public static boolean isValidIPString(String s){
		/*
		String[] ipParts = s.split("[.]");
		if(ipParts.length != 4)
			return false;
		
		for(int i = 0; i < 4; i++){
			try{
				int part = Integer.parseInt(ipParts[i]);
				if(part < 0 || part > 255)
					return false;
			}catch(NumberFormatException e){
				return false;
			}
		}

		return true;
		*/
		
		//possibly faster than above code because there is no expensive exception catching?
		return IPREGEX.matcher(s.trim()).matches();
	}
	
	/**
	 * Validates an IP number
	 * @param l The IP in number form
	 * @return Whether or not the input is a valid IP address
	 */
	public static boolean isValidIPNumber(long l){
		//If the IP number is greater than 0 and less than 2^32 (the number of IPs that can exist), it is valid
		return l >= 0 && l>>32 == 0;
	}
	
	/**
	 * Converts an IP String to an IP number
	 * @param s The string IP
	 * @return The IP number corresponding to the input IP
	 */
	public static long IPStringToNumber(String s){
		String[] ipParts = s.trim().split("[.]");
		long ip = 0;
		for(int i = 0; i < 4; i++){
			long cur = Long.parseLong(ipParts[i]);
			ip |= cur<<(8*(3-i));
		}
		return ip;
	}
	
	/**
	 * Converts an IP number to an IP string
	 * @param ip The number IP
	 * @return The IP String corresponding to the input IP
	 */
	public static String IPNumberToString(long ip){
		long p1 = (ip & (255<<24))>>24;
		long p2 = (ip & (255<<16))>>16;
		long p3 = (ip & (255<<8))>>8;
		long p4 = ip & 255;
		
		return p1 + "." + p2 + "." + p3 + "." + p4;
	}
	
	/**
	 * Converts a start IP address and an end IP address to its corresponding CIDR prefix value
	 * @param ipstart The start IP address
	 * @param ipend The end IP address
	 * @return The CIDR prefix corresponding to the input IPs
	 */
	public static int getCIDRPrefix(long ipstart, long ipend){
		return getCIDRPrefix(1+ipend-ipstart);
	}
	
	/**
	 * Converts a subnet size to its corresponding CIDR prefix value
	 * @param subnetSize The size of the subnet
	 * @return The CIDR prefix corresponding to the input size
	 */
	public static int getCIDRPrefix(long subnetSize){
		//Math.log() fails to work at the higher long values
		int fromRight = Long.numberOfTrailingZeros(subnetSize);
		int fromLeft = Long.numberOfLeadingZeros(subnetSize);
		
		if((fromRight + fromLeft + 1 == 64) && fromRight<=32){ //implies value is a power of 2 and is smaller than maximum subnet size of 2^32 - 1
			return -fromRight + 32;
		}else
			return -1;

	}
	
}
