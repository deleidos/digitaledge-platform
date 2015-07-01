package com.saic.rtws.commons.net.dns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.dns.exception.DNSException;
import com.saic.rtws.commons.net.listener.util.UserDataUtil;
import com.saic.rtws.commons.util.regex.Internet;

/**
 * Utility class used to bind new entries to a local DNS servicer. Castor is
 * used as the XML marshaller.
 */
public class UnixDnsClient implements DnsClient {

	private static final Logger logger = LogManager
			.getLogger(UnixDnsClient.class);

	/** Pattern for validating the host name portion of a domain name. */
	private static final Pattern HOST_PATTERN = Internet.HOST_NAME_PATTERN;

	/** Pattern for validating the domain portion of a domain name. */
	private static final Pattern DOMAIN_PATTERN = Internet.DOMAIN_NAME_PATTERN;

	/** Pattern for validating a fully qualified domain name. */
	private static final Pattern FQDN_PATTERN = Internet.FQDN_PATTERN;

	/** File location of the nsupdate executable. */
	private File nsupdateExecutable = new File("/usr/bin/nsupdate");
	/** File location of the host executable. */
	private File hostExecutable = new File("/usr/bin/host");

	private File zoneAdd = new File("/usr/local/rtws/dns/bin/add_dns_zone.sh");
	private File zoneDelete = new File(
			"/usr/local/rtws/dns/bin/remove_dns_zone.sh");
	private File forwarderAdd = new File(
			"/usr/local/rtws/dns/bin/add_forwarder.sh");
	private File forwarderDelete = new File(
			"/usr/local/rtws/dns/bin/remove_forwarder.sh");

	private final String zoneFile = "/etc/bind/named.conf.local";
	// private File zoneFile = new File("/etc/bind/named.conf.local");

	private final String reverseDnsZone = "in-addr.arpa";
	/**
	 * File location of the private key file used to authenticate to the DNS
	 * server.
	 */
	private File key;

	private final int startLen = 6;
	private final int endLen = 1;
	private final int ipStartLen = 12;
	private final int ipEndLen = 2;

	private final static Object zoneFileLock = new Object();

	/**
	 * Constructor. Assumes the default location of the nsupdate executable.
	 */
	public UnixDnsClient() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param executable
	 *            The absolute path to the nsupdate executable.
	 */
	public UnixDnsClient(File executable) {
		this.nsupdateExecutable = executable;
	}

	/**
	 * Returns the path to the private key file used to authenticate to the DNS
	 * server.
	 */
	public File getKeyFile() {
		return key;
	}

	/**
	 * Sets the path to the private key file used to authenticate to the DNS
	 * server.
	 */
	public void setKeyFile(File file) {
		key = file;
	}

	/**
	 * Binds the given ip address to the given domain name.
	 */
	public void bind(String fqdn, String ip) throws IOException {
		Matcher matcher = FQDN_PATTERN.matcher(fqdn);
		if (matcher.matches()) {
			String domain = matcher.group(2);
			bindInternal(fqdn, domain, ip);
		} else {
			throw new IllegalArgumentException("Invalid domain name '" + fqdn
					+ "'.");
		}
	}

	/**
	 * Binds the given ip address to the given domain name.
	 */
	public void bind(String host, String domain, String ip) throws IOException {
		if (HOST_PATTERN.matcher(host).matches()
				&& DOMAIN_PATTERN.matcher(domain).matches()) {
			bindInternal(host + "." + domain, domain, ip);
		} else {
			throw new IllegalArgumentException("Invalid domain name '" + host
					+ "." + domain + "'.");
		}
	}

	/**
	 * Binds the given ip address to the given domain name.
	 */
	private void bindInternal(String fqdn, String domain, String ip)
			throws IOException {

		String[] updateCommands = new String[] {
				"server 127.0.0.1",
				"zone " + domain,
				"update delete " + fqdn + " A",
				"update add " + fqdn + " 60 A " + ip,
				"show",
				"send"
			};
		
		runNsupdateExecutable(updateCommands);
		
		// Also the bind reverse DNS
		bindReverseInternal(fqdn, ip);
		
	}
	
	/**
	 * Binds the reverse DNS
	 */
	private void bindReverseInternal(String fqdn, String ip) throws IOException {
		
		// Reverse DNS is only configured on master instances.  Only attempt to bind Reverse
		// DNS if on a master instance or else this call will fail.
		String host = UserDataUtil.getHostFromFqdn();
		if(host.equals("master")) {
			// Convert the IP into an in-addr.arpa domain
			String inAddrArpaDomain = buildInAddrArpaDomain(ip);
			
			String[] updateCommands = new String[] {
					"server 127.0.0.1",
					"zone " + reverseDnsZone,
					"update delete " + inAddrArpaDomain + " PTR",
					"update add " + inAddrArpaDomain + " 60 PTR " + fqdn + ".",
					"show",
					"send"
				};
			
			runNsupdateExecutable(updateCommands);
		}
	}
	
	/**
	 * Builds the in-addr.arpa domain required to perform reverse
	 * resolution with DNS.
	 * 
	 * An in-addr.arpa domain is the original IP reversed with
	 * in-addr.arpa appended.
	 * 		Example:
	 * 			127.0.0.1	becomes
	 * 			1.0.0.127.in-addr.arpa
	 */
	private String buildInAddrArpaDomain(String ip) {
		String[] ipOctets = ip.split("\\.");
		
		String inAddrArpaDomain = String.format("%s.%s.%s.%s.in-addr.arpa", ipOctets[3], ipOctets[2], ipOctets[1], ipOctets[0]);
	
		return inAddrArpaDomain;
	}

	/**
	 * Reads text from the given stream.
	 */
	private static String readStream(InputStream stream) {
		StringBuilder buffer = new StringBuilder();
		try {
			String line;
			BufferedReader stderr = new BufferedReader(new InputStreamReader(
					stream));
			while ((line = stderr.readLine()) != null) {
				buffer.append(line).append("\n");
			}
		} catch (Exception e) {
			// Ignore
		} finally {
			try {
				stream.close();
			} catch (Exception ignore) {
			}
		}
		return buffer.toString();
	}

	/**
	 * Validates expectation about the given file. Throws an exception if the
	 * expectations are not met.
	 * 
	 * @param file
	 *            The file to test.
	 * @param exists
	 *            Whether the file needs to exist.
	 * @param executable
	 *            Whether the file must be executable.
	 * @param notNull
	 *            Whether the file reference can be a null object. If null is
	 *            allowed, other checks can be skipped.
	 * @param description
	 *            A description of what the file is; used in exception text.
	 */
	private void validateFile(File file, boolean exists, boolean executable,
			boolean notNull, String description) throws IOException {
		if (file == null) {
			if (notNull) {
				throw new FileNotFoundException(description + " not provided.");
			}
		} else {
			if (exists && !file.exists()) {
				throw new FileNotFoundException(description
						+ " does not exist. '" + file.getAbsolutePath() + "'.");
			} else if (executable && !file.canExecute()) {
				throw new IOException(description + " is not exacutable. '"
						+ file.getAbsolutePath() + "'.");
			}
		}
	}

	@Override
	public void addZone(String domain) throws DNSException {
		try {
			validateFile(zoneAdd, true, true, true, "Add zone script.");

			synchronized (zoneFileLock) {
				if (!doesZoneExist(domain)) {
					// Build the command line call to add a new dns zone.
					Process process = null;
					String command = null;
					if (key != null) {
						command = String.format("sudo %s %s",
								zoneAdd.getAbsolutePath(), domain);
					}
	
					try {
	
						// Run the command.
						process = Runtime.getRuntime().exec(command);
				
						// Throw an exception if the command returned abnormally.
						if (process.waitFor() != 0) {
							String error = readStream(process.getErrorStream());
							logger.warn(error);
							throw new ProtocolException("Add Zone failed.\n" + error);
						}
	
					} catch (Exception e) {
						throw new SocketTimeoutException(
								"Timeout waiting for server response.");
					} finally {
						try {
							process.destroy();
						} catch (Exception ignore) {
						}
					}
				} else {
					logger.info(String.format(
							"%s already exists in dns configuration.", domain));
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			throw new DNSException(String.format("Unable to add a zone for the domain:  %s", domain), e);
		}
	}

	@Override
	public void removeZone(String domain) throws DNSException {
		try {
			validateFile(zoneDelete, true, true, true, "Remoze zone script.");

			synchronized (zoneFileLock) {
				if (doesZoneExist(domain)) {
					// Build the command line call to delete the dns zone.
					Process process = null;
					String command = null;
					if (key != null) {
						command = String.format("sudo %s %s",
								zoneDelete.getAbsolutePath(), domain);
					}
	
					try {
	
						// Run the command.
						process = Runtime.getRuntime().exec(command);
	
						// Throw an exception if the command returned abnormally.
						if (process.waitFor() != 0) {
							String error = readStream(process.getErrorStream());
							logger.warn(error);
							throw new ProtocolException("Delete Zone failed.\n" + error);
						}
	
					} catch (InterruptedException e) {
						throw new SocketTimeoutException(
								"Timeout waiting for server response.");
					} finally {
						try {
							process.destroy();
						} catch (Exception ignore) {
						}
					}
				} else {
					logger.info(String.format(
							"%s does not exist in dns configuration.", domain));
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			throw new DNSException(String.format("Unable to remove zone for domain:  %s", domain), e);
		}
	}

	@Override
	public void unbind(String domain, String fqdn) throws DNSException {

		// Unbind the reverse DNS first! The fqdn still needs to be
		// bound for this step to succeed(it looks up the IP based
		// on the fqdn).
		try {
			unbindReverseInternal(fqdn);
		} catch (Exception e) {
			logger.error(String.format("Unable to unbind Reverse DNS entry %s", fqdn), e);
		}
		
		try {
			
			String[] updateCommands = new String[] {
					"server 127.0.0.1",
					"zone " + domain,
					"update delete " + fqdn + " A",
					"show",
					"send"
				};
			
			runNsupdateExecutable(updateCommands);
			
		} catch (Exception e) {
			logger.error(e.toString(), e);
			throw new DNSException(String.format("Unable to unbind DNS entry %s, zone %s", fqdn, domain), e);
		}
	}
	
	private void unbindReverseInternal(String fqdn) throws Exception {

		// Reverse DNS is only configured on master instances.  Only attempt to unbind Reverse
		// DNS if on a master instance or else this call will fail.
		String host = UserDataUtil.getHostFromFqdn();
		if(host.equals("master")) {
			try {
				// Get an IP for the domain
				String ip = runHostExecutable(fqdn);
				
				if(ip != null) {
					// Convert the IP into an in-addr.arpa domain
					String inAddrArpaDomain = buildInAddrArpaDomain(ip);
					
					String[] updateCommands = new String[] {
							"server 127.0.0.1",
							"zone " + reverseDnsZone,
							"update delete " + inAddrArpaDomain + " PTR",
							"show",
							"send"
						};
					
					runNsupdateExecutable(updateCommands);
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	private void runNsupdateExecutable(String[] updateCommands) throws IOException {
		
		// Verify that supporting files exist and are executable.
		validateFile(nsupdateExecutable, true, true, true, "DNS client library");
		validateFile(key, true, false, false, "Private key file");

		// Build the command line call to invoke the DNS client.
		Process process = null;
		String command = nsupdateExecutable.getAbsolutePath() + " -v";
		if (key != null) {
			command = command + " -k " + key.getAbsolutePath();
		}

		try {

			// Run the command.
			process = Runtime.getRuntime().exec(command);

			// Send the update request.
			PrintStream stdin = new PrintStream(process.getOutputStream());
			for(String nsupdateCommand : updateCommands) {
				stdin.println(nsupdateCommand);
			}
			stdin.close();

			// Throw an exception if the command returned abnormally.
			if (process.waitFor() != 0) {
				String error = readStream(process.getErrorStream());
				throw new ProtocolException("Executable 'nsupdate' failed.\n" + error);
			}

		} catch (InterruptedException e) {
			throw new SocketTimeoutException(
					"Timeout waiting for server response.");
		} finally {
			try {
				process.destroy();
			} catch (Exception ignore) {
			}
		}
		
	}
	
	private String runHostExecutable(String domain) throws IOException {
		
		// Verify that supporting files exist and are executable.
		validateFile(hostExecutable, true, true, true, "DNS query tool");
		
		// The value to return
		String hostResult = null;
		
		// Build the command line call to invoke the DNS client.
		Process process = null;
		String command = null;
		if(key != null) {
			command = String.format("%s %s", hostExecutable.getAbsolutePath(), domain);
		}

		try {

			// Run the command.
			process = Runtime.getRuntime().exec(command);

			if (process.waitFor() != 0) {
				// Throw an exception if the command returned abnormally.
				String error = readStream(process.getErrorStream());
				throw new ProtocolException("Executable 'host' failed.\n" + error);
			}
			else {
				// Parse the output from the lookup for a valid answer
				String hostOutput = readStream(process.getInputStream());
				hostOutput = hostOutput.replace("\n", "");
				
				if(!hostOutput.toLowerCase().contains("nxdomain")) {
					// There was a valid result, it is at the end of the output
					hostResult = hostOutput.substring(hostOutput.lastIndexOf(" ") + 1);
				}
			}

		} catch (InterruptedException e) {
			throw new SocketTimeoutException(
					"Timeout waiting for server response.");
		} finally {
			try {
				process.destroy();
			} catch (Exception ignore) {
			}
		}
		
		return hostResult;
	}

	private boolean doesZoneExist(String domain) throws IOException {
		boolean exists = false;
		String regex = String.format("zone \"%s\"", domain);
		Pattern pattern = Pattern.compile(regex);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(zoneFile));

			String line = null;
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);

				if (matcher.find()) {
					if (!line.contains("forward")) {
						exists = true;
					}
				}
			}
		} finally {
			if (br != null)
				IOUtils.closeQuietly(br);
		}

		return exists;
	}


	@Override
	public void addForwarder(String domain, String dnsIP, String tmsDomain, String publicIPAddress) {
		try {
			validateFile(forwarderAdd, true, true, true,
					"Add forwarder script.");

			// Build the command line call to add a new dns zone forwarder.
			Process process = null;
			String command = null;
			if (key != null) {
				command = String.format("sudo %s %s %s %s %s",
						forwarderAdd.getAbsolutePath(), domain, dnsIP,
						tmsDomain, publicIPAddress);
			}

			try {

				// Run the command.
				process = Runtime.getRuntime().exec(command);

				// Throw an exception if the command returned abnormally.
				if (process.waitFor() != 0) {
					String error = readStream(process.getErrorStream());
					logger.warn(error);
					throw new ProtocolException(
							"Update failed for adding a new zone forwarder.\n"
									+ error);
				}

			} catch (InterruptedException e) {
				throw new SocketTimeoutException(
						"Timeout waiting for server response.");
			} finally {
				try {
					process.destroy();
				} catch (Exception ignore) {
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	@Override
	public void removeForwarder(String domain, String dnsIP, String tmsDomain) {
		try {
			validateFile(forwarderDelete, true, true, true,
					"Remove forwarder script script.");

			// Build the command line call to add a new dns zone forwarder.
			Process process = null;
			String command = null;
			if (key != null) {
				command = String.format("sudo %s %s %s %s",
						forwarderDelete.getAbsolutePath(), domain, dnsIP,
						tmsDomain);
			}

			try {

				// Run the command.
				process = Runtime.getRuntime().exec(command);

				// Throw an exception if the command returned abnormally.
				if (process.waitFor() != 0) {
					String error = readStream(process.getErrorStream());
					logger.warn(error);
					throw new ProtocolException(
							"Update failed for removing a zone forwarder.\n"
									+ error);
				}

			} catch (InterruptedException e) {
				throw new SocketTimeoutException(
						"Timeout waiting for server response.");
			} finally {
				try {
					process.destroy();
				} catch (Exception ignore) {
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	@Override
	public Collection<Map<String, Map<String, String>>> getForwarderStatus()
			throws IOException {
		Collection<Map<String, Map<String, String>>> statusList = new ArrayList<Map<String, Map<String, String>>>();

		Pattern pattern = Pattern.compile("zone \".*?\"");

		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(zoneFile));
		
			String line = null;
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);

				if (matcher.find()) {
					if (line.contains("forward")) {
						// add domain and ip for a forwarder
						Map<String, String> forwarder = new HashMap<String, String>();
						forwarder.put(
							"domain",
							line.substring(matcher.start() + startLen,
									matcher.end() - endLen));
						// find ip
						Pattern ipPattern = Pattern
							.compile("forwarders \\{.*?;\\}");
						matcher = ipPattern.matcher(line);
						if (matcher.find()) {
						forwarder.put("ipAddress", line.substring(
								matcher.start() + ipStartLen, matcher.end()
										- ipEndLen));
						} else {
							forwarder.put("ipAddress", null);
						}
						Map<String, Map<String, String>> forwarders = new HashMap<String, Map<String, String>>();
						forwarders.put("forwarder", forwarder);
						statusList.add(forwarders);
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(br);
		}

		return statusList;
	}

	@Override
	public Collection<String> getZoneStatus() throws IOException {
		Collection<String> forwarders = new ArrayList<String>();
		Pattern pattern = Pattern.compile("zone \".*?\"");

		
		BufferedReader br = null;
		
		try {
			
			br = new BufferedReader(new FileReader(zoneFile));
		

			String line = null;
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);

				if (matcher.find()) {
					if (!line.contains("forward")) {
						forwarders.add(line.substring(matcher.start() + startLen,
						matcher.end() - endLen));
					}
				}
			}
	} finally {
		IOUtils.closeQuietly(br);
	}

		return forwarders;
	}
}
