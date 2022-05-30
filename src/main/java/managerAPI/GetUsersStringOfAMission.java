package managerAPI;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
@WebServlet("/GetUsersStringOfAMission")
public class GetUsersStringOfAMission extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUsersStringOfAMission() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		DBConnection conn = DBConnectionFactory.getConnection();
		
		try {
			JSONObject input = HelperFunctions.readJsonObject(request);
			String missionCode = input.getString("missionCode").trim();

			System.out.println(String.format("Get all users for mission %s",missionCode));
			
			mission curMission = conn.searchMissionByCode(missionCode);
			
			if (curMission != null) {
				List<user> userList = conn.getUsersForMission(curMission.getMissionName());
				System.out.println("Get user list successfully with size: "+userList.size());
				String report = "";
				for (user curUser:userList) {
					report+=curUser.getFirstname()+" "+curUser.getLastname()+"\n"+curUser.getAttributesString();
					report+=";";
				}
				try {
					response.setContentType("text/html");
					response.addHeader("Access-Control-Allow-Origin", "*");
					PrintWriter out = response.getWriter();
					out.append(report.substring(0, report.length()-1));
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
