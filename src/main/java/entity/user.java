package entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class user {
	private String username;
	private String password;
	private List<String> attributes;
	private List<String> interests;
	private String firstname;
	private String lastname;
	private Timestamp registerationTime;
	private Timestamp expirationDate;
	private int userID;
	
	public user(String un, String pw) {
		this.username = un;
		this.password = pw;
		this.attributes=new ArrayList<String>();
		this.interests=new ArrayList<String>();
	}
	
	public user(String un, String firstname, String lastname) {
		this.username = un;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	public user(String un, String pw, String firstname, String lastname, List<String> attr, List<String> ints) {
		this.username = un;
		this.firstname = firstname;
		this.lastname = lastname;
		this.attributes=new ArrayList<String>();
        for(String attrItem: attr){
            this.attributes.add( attrItem);
        }
        this.interests=new ArrayList<String>();
        for(String intItem: ints){
            this.interests.add( intItem);
        }
        this.password = pw;
		
	}
	
	public void setUserID(int id) {
		this.userID=id;
	}
	
	public void setUserName(String userName) {
		this.username= userName;
	}
	
	public void setPassword(String pw) {
		this.password = pw;
	}
	
	public void setFirstname(String first) {
		this.firstname = first;
	}
	public void setLastname(String last) {
		this.lastname = last;
	}
	
	public void addAttributes(String attr) {
		this.attributes.add(attr);
	}
	
	public void setAttributes(String attrs) {
		this.attributes.clear();
		for(String attr: attrs.split(",")) {
			this.addAttributes(attr);
		}
	}
	
	public void addInterests(String interest) {
		this.interests.add(interest);
	}
	
	public void setInterests(String ints) {
		this.interests.clear();
		for(String interest: ints.split(",")) {
			this.addInterests(interest);
		}
	}
	
	public void setRegisterTime(Timestamp ts) {
		this.registerationTime = ts;
	}
	
	public void setExpirationDate(Timestamp ts) {
		this.expirationDate = ts;
	}
	
	public Timestamp getExpirationDate() {
		return this.expirationDate;
	}
	
	public Timestamp getRegisterTime() {
		return this.registerationTime;
	}
	
	public String getHashedPassword() {
		return this.password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public String getAttributesString() {
		StringBuilder sb = new StringBuilder();
		for(String attr:attributes) {
			sb.append(attr.trim());
			sb.append(",");
		}
		if (sb.length()<=0) {
			return "";
		}
		return sb.substring(0, sb.length()-1);
	}
	
	public List<String> getAttributes(){
		return this.attributes;
	}
	
	public String getInterestsString() {
		StringBuilder sb = new StringBuilder();
		for(String interest:interests) {
			sb.append(interest.trim());
			sb.append(",");
		}
		if (sb.length()<=0) {
			return "";
		}
		return sb.substring(0, sb.length()-1);
	}
	
	public List<String> getInterests(){
		return this.interests;
	}
	
	public int getUserID() {
		return this.userID;
	}
	
}
