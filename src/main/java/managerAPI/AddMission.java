package managerAPI;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.mission;
import entity.user;

/**
 * Servlet implementation class AddMission
 */
@WebServlet("/AddMission")
public class AddMission extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMission() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
    
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		OutputStream os = response.getOutputStream();
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
				JSONObject input = HelperFunctions.readJsonObject(request);
				String missionName = input.getString("missionName").trim();
				int capacity = input.getInt("capacity");
				String startTime = HelperFunctions.getFieldFromJsonRequest("startTime", input);
				String endTime = HelperFunctions.getFieldFromJsonRequest("endTime", input);
				
				Date localTime = new Date();
				Timestamp ts_startTime = startTime.length()>10? 
						HelperFunctions.convertStringToTimestamp(startTime)
						:new Timestamp(localTime.getTime());
				Timestamp ts_endTime = endTime.length()>10? 
						HelperFunctions.convertStringToTimestamp(endTime)
						:new Timestamp(localTime.getTime()
								+ConstantForServer.DEFAULT_EXPIRATION_DURATION);
				
				
				System.out.println(String.format("%s,%d,%s,%s",missionName,capacity,startTime,endTime));
				
				mission curMission = new mission(missionName,capacity,ts_startTime,ts_endTime);
				mission existingMission = conn.searchMission(missionName);
				if (existingMission == null && conn.insertMission(curMission)) {
					
					System.out.println("mission successfully added");
					response.sendError(200);
				}
				else {
					System.out.println("mission can't be added");
					response.sendError(204);
				}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			conn.close();
			os.close();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.addHeader("Access-Control-Allow-Origin", "*");
		OutputStream os = response.getOutputStream();
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
				JSONObject input = HelperFunctions.readJsonObject(request);
				String missionName = input.getString("missionName").trim();
				int capacity = input.getInt("capacity");
				String startTime = HelperFunctions.getFieldFromJsonRequest("startTime", input);
				String endTime = HelperFunctions.getFieldFromJsonRequest("endTime", input);
				
				Date localTime = new Date();
				Timestamp ts_startTime = startTime.length()>10? 
						HelperFunctions.convertStringToTimestamp(startTime)
						:new Timestamp(localTime.getTime());
				localTime = new Date();
				Timestamp ts_endTime = endTime.length()>10? 
						HelperFunctions.convertStringToTimestamp(endTime)
						:new Timestamp(localTime.getTime()+ConstantForServer.DEFAULT_EXPIRATION_DURATION);
				
				
				System.out.println(String.format("%s,%d,%s,%s,%d",
						missionName,capacity,ts_startTime,ts_endTime, 
						localTime.getTime()));
				
				mission curMission = new mission(missionName,capacity,ts_startTime,ts_endTime);
				mission existingMission = conn.searchMission(missionName);
				if (existingMission == null && conn.insertMission(curMission)) {
					
					System.out.println("mission successfully added");
					response.sendError(200);
				}
				else {
					System.out.println("mission can't be added");
					response.sendError(204);
				}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			conn.close();
			os.close();
		}
	}

}
