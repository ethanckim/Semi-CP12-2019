package ca.kim.accordchat;

public class DmAddress {

	private String myPort = "";
	private String myIp = "";
	private String myUsername = "";
	private String theirPort = "";
	private String theirIp = "";
	private String theirUsername = "";

	public DmAddress(String myPort, String myIp, String myUsername, String theirPort, String theirIp,
			String theirUsername) {
		super();
		this.myPort = myPort;
		this.myIp = myIp;
		this.myUsername = myUsername;
		this.theirPort = theirPort;
		this.theirIp = theirIp;
		this.theirUsername = theirUsername;
	}
	
	public DmAddress() {
		super();
	}
	
	public String getMyPort() {
		return myPort;
	}

	public void setMyPort(String myPort) {
		this.myPort = myPort;
	}

	public String getMyIp() {
		return myIp;
	}

	public void setMyIp(String myIp) {
		this.myIp = myIp;
	}

	public String getMyUsername() {
		return myUsername;
	}

	public void setMyUsername(String myUsername) {
		this.myUsername = myUsername;
	}

	public String getTheirPort() {
		return theirPort;
	}

	public void setTheirPort(String theirPort) {
		this.theirPort = theirPort;
	}

	public String getTheirIp() {
		return theirIp;
	}

	public void setTheirIp(String theirIp) {
		this.theirIp = theirIp;
	}

	public String getTheirUsername() {
		return theirUsername;
	}

	public void setTheirUsername(String theirUsername) {
		this.theirUsername = theirUsername;
	}

}
