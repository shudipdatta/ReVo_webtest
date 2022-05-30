package entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import it.unisa.dia.gas.jpbc.Element;
import revoabe.MasterKey;
import revoabe.PrivateKey;
import revoabe.PublicKey;
import revoabe.ReVo_ABE;

public class Match {
	
	private mission ms;
	private user ur;
	private int user_tree_id;
	private Timestamp registerationTime;
	private byte[] L;
	private List<String> attributes;
	private List<String> attrSizes;
	private byte[] k_is;
	private List<String> revoNodes;
	private List<String> revoNodeSizes;
	private byte[] k_ys;
	
	public Match(mission m, user u, int treeID, boolean init) {
		this.ms = m;
		this.ur = u;
		this.user_tree_id = treeID;
		if(init)this.keyGen();
	}
	
	public Match(mission m, user u, int treeID, PrivateKey pk) {
		this.ms = m;
		this.ur = u;
		this.user_tree_id = treeID;
		this.assignPrivateKey(pk);
		
	}
	
	public void keyGen() {
		PublicKey pk = this.ms.getPublicKey();
		MasterKey mk = this.ms.getMasterKey();
	
		this.assignPrivateKey(ReVo_ABE.keyGen(pk, mk, 
				this.ur.getAttributes(), this.user_tree_id));
	}
	
	public void setTimestamp(Timestamp ts) {
		this.registerationTime = ts;
	}
	
	public void setUserTreeID(int nID) {
		this.user_tree_id = nID;
		this.keyGen();
	}
	

	public void assignPrivateKey(PrivateKey pk) {
		if (pk == null) {
			this.L = null;
			this.attributes = null;
			this.attrSizes = null;
			this.k_is = null;
			this.revoNodes = null;
			this.revoNodeSizes = null;
			this.k_ys = null;
		}
		
		this.attributes = pk.getAttributes();
		this.L = pk.getL();
		this.attrSizes = pk.getAttrSizes();
		this.k_is = pk.getKIs();
		this.revoNodes = pk.getReVoNodes();
		this.revoNodeSizes = pk.getReVoNodeSizes();
		this.k_ys = pk.getKYs();
	}
	
	
	public void assignPrivateKey(byte[] l, String attrs, String attrSizes, byte[] kis, String nodes, String nodeSizes, byte[] kys) {
		this.L = l;
		this.k_is = kis;
		this.k_ys = kys;
		this.attributes = Match.stringToStringList(attrs);
		this.attrSizes = Match.stringToStringList(attrSizes);
		this.revoNodes = Match.stringToStringList(nodes);
		this.revoNodeSizes = Match.stringToStringList(nodeSizes);
		
	}
	
	public mission getMission() {
		return this.ms;
	}
	
	public int getMissionID() {
		return this.ms.getMissionID();
	}
	
	public int getUserID() {
		return this.ur.getUserID();
	}
	
	public int getUserTreeID(){
		return this.user_tree_id;
	}
	
	public byte[] getL() {
		return this.L;
	}
	
	public List<String> getAttributes(){
		return this.attributes;
	}
	
	public String getAttributesString() {
		return String.join(",", this.attributes);
	}
	
	public List<String> attrSizes(){
		return this.attrSizes;
	}
	
	public String getAttrSizesString() {
		return String.join(",", this.attrSizes);
	}
	
	public byte[] getKIs() {
		return this.k_is;
	}
	
	public List<String> getReVoNodes(){
		return this.revoNodes;
	}
	
	public String getReVoNodesString() {
		return String.join(",", this.revoNodes);
	}
	
	public List<String> getReVoNodeSizes(){
		return this.revoNodeSizes;
	}
	
	public String getReVoNodeSizesString() {

		return String.join(",", this.revoNodeSizes);
	}
	
	public byte[] getKYs() {
		return this.k_ys;
	}
	
	public Timestamp getRegisterationTime() {
		return this.registerationTime;
	}
	
	public static List<String> stringToStringList(String str){
		List<String> ret = new ArrayList<String>();
		for(String s:str.split(",")) {
			ret.add(s.trim());
		}
		return ret;
	}
	
	public void printMatch() {
		System.out.println("Mission: "+this.ms.getMissionName());
		System.out.println("User: "+this.ur.getUsername());
	}

	//	The following data are private keys
	//	private byte[] L;
	//	private List<String> attributes;
	//	private List<String> attrSizes;
	//	private byte[] k_is;
	//	private List<String> revoNodes;
	//	private List<String> revoNodeSizes;
	//	private byte[] k_ys;
	public byte[] toPrivateKeyByteArray() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try {
			os.write(EntityHelper.int_to_bytes(L.length));
			os.write(L);
			os.write(EntityHelper.stringList_to_bytes(attributes));
			os.write(EntityHelper.stringList_to_bytes(attrSizes));
			os.write(EntityHelper.int_to_bytes(k_is.length));
			os.write(k_is);
			os.write(EntityHelper.stringList_to_bytes(revoNodes));
			os.write(EntityHelper.stringList_to_bytes(revoNodeSizes));
			os.write(EntityHelper.int_to_bytes(k_ys.length));
			os.write(k_ys);
		} catch (IOException e) {
			e.printStackTrace();
			
		}		
		return os.toByteArray();		
	}
	
	
	
	
	
	
	
	//	The following are privatekey format
	//	List<String> attr_list; //List of attributes
	//	Element L; //An element of the pairing group
	//	HashMap<String,Element> k_i; //Map of valid attributes and element
	//	HashMap<Integer,Element> k_y;//Map of valid memberID and element
	//	Function to convert match key types to privatekey types
	//	PrivateKey(List<String> al, HashMap<String,Element> ki, 
	//			Element l, HashMap<Integer,Element> ky)
	public PrivateKey getPrivateKey() {
		HashMap<String,Element> k_i = new HashMap();
		HashMap<Integer,Element> k_y = new HashMap();
		Element l = ms.getPairing().getG2().newElementFromBytes(this.L);
		int counter = 0;
		int start = 0;
		for(String attr:this.attributes) {
			int curSize = Integer.parseInt(this.attrSizes.get(counter));
			k_i.put(attr.trim(), ms.getPairing().getG1().newElementFromBytes(
					Arrays.copyOfRange(this.k_is, start, start+curSize)));
			start+=curSize;
			counter+=1;
		}
		counter = 0;
		start = 0;
		for(String node:this.revoNodes) {
			int nodeID = Integer.parseInt(node);
			int curSize = Integer.parseInt(this.revoNodeSizes.get(counter));
			k_y.put(nodeID, ms.getPairing().getG1().newElementFromBytes(
					Arrays.copyOfRange(this.k_ys, start, start+curSize)));
			start+=curSize;
			counter+=1;
		}
		
		
		return new PrivateKey(this.attributes,k_i,l,k_y);
	}
	
	

	
}
