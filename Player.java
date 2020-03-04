package client;

public class Player {
	
	
	private String nickName;
	private String mail;
	private String passWord;
	public int scores;
	private Fleet fleet;
	
	public Player( String nickName, String mail, String passWord) {
		super();
		
		this.nickName = nickName;
		this.mail = mail;
		this.passWord = passWord;
		this.scores = 0;
	}
	public String getPassWord() {
		return passWord;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getScores() {
		return scores;
	}
	public void setScores(int scores) {
		this.scores = scores;
	}
	public Fleet getFleet() {
		return fleet;
	}
	public void setFleet(Fleet fleet) {
		this.fleet = fleet;
	}
	
	
	
	
	

}
