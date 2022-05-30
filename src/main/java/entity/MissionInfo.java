package entity;

public class MissionInfo {
	public String MissionName;
	public String MissionCode;
	public int MissionCapacity;
	
	public MissionInfo(String name, String code, int capacity) {
		MissionName = name;
		MissionCode = code;
		MissionCapacity = capacity;
	}
	
	public String toString() {
		return "["+MissionName+" "+MissionCode+" "+MissionCapacity+"]";
	}
}
