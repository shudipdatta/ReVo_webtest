package managerAPI;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Match;
import entity.mission;
import entity.user;

/**
 * Servlet implementation class GetUsersOfAMission
 */
@WebServlet("/GetUsersOfAMission")
public class GetUsersOfAMission extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUsersOfAMission() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		DBConnection conn = DBConnectionFactory.getConnection();
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.create();
		try {
			JSONObject input = HelperFunctions.readJsonObject(request);
			String missionName = input.getString("missionName").trim();

			System.out.println(String.format("Get all users for mission %s",missionName));
			
			mission curMission = conn.searchMission(missionName);
			
			if (curMission != null) {
				List<user> userList = conn.getUsersForMission(missionName);
				System.out.println("Get user list successfully with size: "+userList.size());
				
				HelperFunctions.writeJsonString(response, gson.toJson(userList));
			}
			else {
				
				System.out.println("Fail to recognize mission ");
				response.sendError(204);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(500);
			
		}finally {
			conn.close();
		}
	}

}
