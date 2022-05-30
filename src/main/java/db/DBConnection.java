package db;
import java.util.List;
import java.util.Set;

import java.sql.*;

import entity.Match;
import entity.MissionInfo;
import entity.mission;
import entity.user;
public interface DBConnection {

	public void close();
	
	public user searchUser(String username);
	
	public user searchUser(int userID);
	
	public List<MissionInfo> getAllMissions();
	
	public List<user> getAllUsers();
	
	public boolean verifyLogin(String username, String password);
	
	public boolean insertUser(user us);

	public boolean insertMission(mission ms);

	public mission searchMission(String missionName);
	
	public mission searchMissionByCode(String missionCode);
	
	public boolean deleteUser(user us);
	
	public boolean deleteMission(mission ms);
	
	public boolean updateUser(user us);
	
	public boolean updateMission(mission ms);
	
	public boolean insertMatch(Match mc);
	
	public boolean deleteMatch(Match mc);
	
	public boolean updateMatch(Match mc);
	
	public Match searchMatch(String ms, String us);
	
	public List<user> getUsersForMission(String missionName);
	
	public List<mission> getMissionsForUser(String userName);
	
	public int getMissionIDByName(String missionName);
	
	public int getMissionIDByCode(int missionCode);
	
	public int getUserIDByUsername(String userName);
	
}
