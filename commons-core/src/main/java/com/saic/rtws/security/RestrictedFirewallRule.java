package com.saic.rtws.security;

public class RestrictedFirewallRule
{
	private String protocol;
	
	private String fromPort;
	
	private String toPort;
	
	private String [] source;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getFromPort() {
		return fromPort;
	}

	public void setFromPort(String fromPort) {
		this.fromPort = fromPort;
	}

	public String getToPort() {
		return toPort;
	}

	public void setToPort(String toPort) {
		this.toPort = toPort;
	}

	public String [] getSource() {
		return source;
	}

	public void setSource(String [] source) {
		this.source = source;
	}
	
	public String find(String source) {
		if (this.source != null) {
			for (String value : this.source) {
				if (value.equals(source)) {
					return value;
				}
			}
		}
		
		return null;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[RestrictedFirewallRule");
		sb.append(" protocol: ").append(this.protocol);
		sb.append(" toPort: ").append(this.toPort);
		sb.append(" fromPort: ").append(this.fromPort);
		sb.append(" source: ");
		for (int i = 0; i < source.length; i++) {
			sb.append(source[i]);
			
			if (i != (source.length - 1)) {
				sb.append(" ");
			}
		}
		sb.append(']');
		
		return sb.toString();
	}
}