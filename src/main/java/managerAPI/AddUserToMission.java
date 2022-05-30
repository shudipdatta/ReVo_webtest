package managerAPI;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Match;
import entity.mission;
import entity.user;

/**
 * Servlet implementation class AddUserToMission
 */
@WebServlet("/AddUserToMission")
public class AddUserToMission extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUserToMission() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		OutputStream os = response.getOutputStream();
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
				JSONObject input = HelperFunctions.readJsonObject(request);
				String missionName = input.getString("missionName").trim();
				String userName = input.getString("userName").trim();

				System.out.println(String.format("%s,%s",missionName,userName));
				
				mission curMission = conn.searchMission(missionName);
				user curUser = conn.searchUser(userName);
				
				if (curMission != null && curUser != null) {
					int matchUserCount = conn.getUsersForMission(missionName).size();
					Match mc = new Match(curMission, curUser, matchUserCount+1,true);
					conn.insertMatch(mc);
					System.out.println("add match successfully");
					response.sendError(200);
				}
				else {
					
					System.out.println("Fail to recognize mission or user");
					response.sendError(204);
				}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(500);
			
		}finally {
			conn.close();
			os.close();
		}
	}

}
