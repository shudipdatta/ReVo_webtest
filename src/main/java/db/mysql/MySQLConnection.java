/**
 * 
 */
package db.mysql;
import db.DBConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Match;
import entity.MissionInfo;
import entity.mission;
import entity.user;

/**
 * @author XiaofeiCao
 *
 */
public class MySQLConnection implements DBConnection{

	
	private Connection conn;

	public MySQLConnection() {
	// Step 1 Connect to MySQL.
		try {
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			conn = DriverManager.getConnection(MySQLDBUtil.URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public boolean verifyLogin(String username, String password) {
		// TODO Auto-generated method stub
		
		user curUser= searchUser(username);
		if(curUser!=null && curUser.getHashedPassword().equals(password)) {
			return true;
		}
		
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	
	@Override
	public boolean insertMatch(Match mc) {
		String sql ="INSERT INTO `match` (missionID,userID,registerationTime,"
				+ "L,attributes,attrSizes,k_is,revoNodes,revoNodeSize,k_ys, user_tree_id)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?);";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, mc.getMissionID());
			stmt.setInt(2, mc.getUserID());
			stmt.setTimestamp(3, mc.getRegisterationTime());
			stmt.setBytes(4, mc.getL());
			stmt.setString(5, mc.getAttributesString());
			stmt.setString(6, mc.getAttrSizesString());
			stmt.setBytes(7, mc.getKIs());
			stmt.setString(8, mc.getReVoNodesString());
			stmt.setString(9, mc.getReVoNodeSizesString());
			stmt.setBytes(10, mc.getKYs());
			stmt.setInt(11, mc.getUserTreeID());
			System.out.println(stmt);
			stmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	

	@Override
	public boolean insertUser(user us) {
		

		String sql = "INSERT INTO user (username,password,attributes,interests,firstname,lastname,expirationDate)"
				+ "VALUES (?,?,?,?,?,?,?);";
		try {
			
			user curUser = searchUser(us.getUsername());
			if(curUser == null) {
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, us.getUsername());
				stmt.setString(2, us.getPassword());
				stmt.setString(3, us.getAttributesString());
				stmt.setString(4, us.getInterestsString());
				stmt.setString(5, us.getFirstname());
				stmt.setString(6, us.getLastname());
				stmt.setTimestamp(7, us.getExpirationDate());
				stmt.executeUpdate();
				return true;
			}
			else {
				
				System.out.println("User already exisits");
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return false;
	}

	@Override
	public boolean insertMission(mission ms) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO mission (missionName,missionCode,startTime,"
				+ "endTime,capacity,g1_alpha,beta,g1,g2,g2_beta,e_gg_alpha,g1_a)"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			mission curMission1 = searchMission(ms.getMissionName());
			mission curMission2 = searchMissionByCode(ms.getMissionCode());
			if(curMission1 == null && curMission2 == null) {
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, ms.getMissionName());
				stmt.setString(2, ms.getMissionCode());
				stmt.setTimestamp(3, ms.getStartTime());
				stmt.setTimestamp(4, ms.getEndTime());
				stmt.setInt(5, ms.getCapacity());
				stmt.setBytes(6, ms.getG1_alpha());
				stmt.setBytes(7, ms.getBeta());
				stmt.setBytes(8, ms.getG1());
				stmt.setBytes(9, ms.getG2());
				stmt.setBytes(10, ms.getG2_beta());
				stmt.setBytes(11, ms.getE_gg_alpha());
				stmt.setBytes(12, ms.getG1a());
				stmt.executeUpdate();
				return true;
			}
			else {
				System.out.println("Mission already exisits");
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Match searchMatch(String missionName, String userName) {
		
		mission curMission = this.searchMission(missionName);
		user curUser = this.searchUser(userName);
		
		if(curMission != null && curUser != null) {
			String sql = "SELECT * FROM `match` WHERE missionID = "
						+curMission.getMissionID()+ " AND userID = "
						+curUser.getUserID()+";";
			
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) {
					Match curMatch = new Match(curMission,curUser, rs.getInt("user_tree_id"),false);
					curMatch.assignPrivateKey(rs.getBytes("L"), 
							rs.getString("attributes"), rs.getString("attrSizes"), 
							rs.getBytes("k_is"), rs.getString("revoNodes"),
							rs.getString("revoNodeSize"), rs.getBytes("k_ys"));
					return curMatch;
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return null;
	}
	
	
	@Override
	public user searchUser(String username) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM user WHERE username = '"+username+"';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				user curUser = new user(rs.getString("username"),rs.getString("password")
						);
				curUser.setAttributes(rs.getString("attributes"));
				curUser.setInterests(rs.getString("interests"));
				curUser.setUserID(rs.getInt("userID"));
				curUser.setFirstname(rs.getString("firstname"));
				curUser.setLastname(rs.getString("lastname"));
				curUser.setRegisterTime(rs.getTimestamp("registerTime"));
				curUser.setExpirationDate(rs.getTimestamp("expirationDate"));
				return curUser;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	

	@Override
	public user searchUser(int userID) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM user WHERE userID = '"+userID+"';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				user curUser = new user(rs.getString("username"),rs.getString("password")
						);
				curUser.setAttributes(rs.getString("attributes"));
				curUser.setInterests(rs.getString("interests"));
				curUser.setUserID(rs.getInt("userID"));
				curUser.setFirstname(rs.getString("firstname"));
				curUser.setLastname(rs.getString("lastname"));
				curUser.setRegisterTime(rs.getTimestamp("registerTime"));
				curUser.setExpirationDate(rs.getTimestamp("expirationDate"));
				return curUser;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@Override
	public mission searchMission(String missionName) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT * FROM mission WHERE missionName = '"+missionName+"';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				mission curMission = new mission(rs.getString("missionName"),
						rs.getInt("capacity"),rs.getString("missionCode"));
				curMission.setMissionID(rs.getInt("missionID"));
				curMission.setStartTime(rs.getTimestamp("startTime"));
				curMission.setEndTime(rs.getTimestamp("endTime"));
				curMission.setupPublicKey(rs.getBytes("g1"), rs.getBytes("g2"),
						rs.getBytes("g2_beta"), rs.getBytes("g1_a"), rs.getBytes("e_gg_alpha"));
				curMission.setupMasterKey(rs.getBytes("beta"),rs.getBytes("g1_alpha"));
				return curMission;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	

	@Override
	public mission searchMissionByCode(String missionCode) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM mission WHERE missionCode = '"+missionCode+"';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				mission curMission = new mission(rs.getString("missionName"),rs.getInt("capacity")
						);
				curMission.setMissionID(rs.getInt("missionID"));
				curMission.setMissionCode(rs.getString("missionCode"));
				curMission.setStartTime(rs.getTimestamp("startTime"));
				curMission.setEndTime(rs.getTimestamp("endTime"));
				curMission.setupPublicKey(rs.getBytes("g1"), rs.getBytes("g2"), 
						rs.getBytes("g2_beta"), rs.getBytes("g1_a"), rs.getBytes("e_gg_alpha"));
				curMission.setupMasterKey(rs.getBytes("beta"),rs.getBytes("g1_alpha"));
				return curMission;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<MissionInfo> getAllMissions() {
		String sql = "SELECT * FROM mission;";
		List<MissionInfo> res = new ArrayList<MissionInfo>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				res.add(new MissionInfo(rs.getString("missionName"),rs.getString("missionCode"),rs.getInt("capacity")));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	@Override
	public List<user> getAllUsers() {
		String sql = "SELECT * FROM user;";
		List<user> res = new ArrayList<user>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				res.add(new user(rs.getString("username"),rs.getString("firstname"),rs.getString("lastname")));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return res;
	}

	@Override
	public boolean deleteUser(user us) {
		
		String sql = "DELETE FROM user WHERE username=?;";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, us.getUsername());
			stmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean deleteMission(mission ms) {
		String sql = "DELETE FROM mission WHERE missionName=?;";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, ms.getMissionName());
			stmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	@Override
	public boolean deleteMatch(Match mc) {
		String sql = "DELETE FROM `match` WHERE missionID="
					+mc.getMissionID()+" AND"+" userID="+mc.getUserID()+";";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean updateUser(user us) {
		String sql = "UPDATE user SET username = ?, password = ?,"
					+" attributes = ?, interests = ?, firstname = ?, lastname = ?, expirationDate = ?"
					+"WHERE userID= ?;";
				
		try {
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, us.getUsername());
			stmt.setString(2, us.getPassword());
			stmt.setString(3, us.getAttributesString());
			stmt.setString(4, us.getInterestsString());
			stmt.setString(5, us.getFirstname());
			stmt.setString(6, us.getLastname());
			stmt.setTimestamp(7, us.getExpirationDate());
			stmt.setInt(8, us.getUserID());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return false;
	}

	
	
	@Override
	//updateMission, must have missionID.
	public boolean updateMission(mission ms) {
		String sql = "UPDATE mission SET missionName = ?, missionCode = ?, "
					+"startTime = ?, endTime = ?, capacity = ?, g1_alpha = ?,"
					+" beta = ?, g1 = ?, g2 = ?, g2_beta = ?, e_gg_alpha = ?,"
					+" g1_a = ? WHERE missionID = ?;";
				//"UPDATE mission (missionName,missionCode,startTime,endTime,capacity,g1_alpha,beta,g1,g2,g2_beta,e_gg_alpha,g1_a)"
				
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, ms.getMissionName());
			stmt.setString(2, ms.getMissionCode());
			stmt.setTimestamp(3, ms.getStartTime());
			stmt.setTimestamp(4, ms.getEndTime());
			stmt.setInt(5, ms.getCapacity());
			stmt.setBytes(6, ms.getG1_alpha());
			stmt.setBytes(7, ms.getBeta());
			stmt.setBytes(8, ms.getG1());
			stmt.setBytes(9, ms.getG2());
			stmt.setBytes(10, ms.getG2_beta());
			stmt.setBytes(11, ms.getE_gg_alpha());
			stmt.setBytes(12, ms.getG1a());
			stmt.setInt(13, ms.getMissionID());
			stmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	



	@Override
	public boolean updateMatch(Match mc) {
		String sql ="UPDATE `match` SET "
				+ "registerationTime = ?, L = ?, attributes = ?, "
				+ "attrSizes = ?, k_is = ?, revoNodes = ?, "
				+ "revoNodeSize = ?,k_ys = ?, user_tree_id = ?"
				+ " WHERE missionID =? AND userID =?;";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(10, mc.getMissionID());
			stmt.setInt(11, mc.getUserID());
			stmt.setTimestamp(1, mc.getRegisterationTime());
			stmt.setBytes(2, mc.getL());
			stmt.setString(3, mc.getAttributesString());
			stmt.setString(4, mc.getAttrSizesString());
			stmt.setBytes(5, mc.getKIs());
			stmt.setString(6, mc.getReVoNodesString());
			stmt.setString(7, mc.getReVoNodeSizesString());
			stmt.setBytes(8, mc.getKYs());
			stmt.setInt(9, mc.getUserTreeID());
			stmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	
	
	
	@Override
	public List<user> getUsersForMission(String missionName) {
		
		int missionID = getMissionIDByName(missionName);
		if(missionID==-1)return null;
		String sql = "SELECT userID FROM `match` WHERE missionID="+missionID+";";
		List<user> res = new ArrayList<user>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				res.add(this.searchUser(rs.getInt("userID")));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return res;
	}



	@Override
	public List<mission> getMissionsForUser(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMissionIDByName(String missionName) {
		String sql = "SELECT missionID FROM mission WHERE missionName = '"+missionName+"';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				
				return rs.getInt("missionID");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public int getMissionIDByCode(int missionCode) {
		String sql = "SELECT missionID FROM mission WHERE missionCode = '"+missionCode+"';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				
				return rs.getInt("missionID");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public int getUserIDByUsername(String userName) {
		String sql = "SELECT userID FROM user WHERE username = '"+userName+"';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				
				return rs.getInt("userID");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}


	
	

}
