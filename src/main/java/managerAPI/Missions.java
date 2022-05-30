package managerAPI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.MissionInfo;
import entity.mission;
import qrcode.QRCodeHelper;
import com.google.gson.*;
import org.json.JSONObject;
/**
 * Servlet implementation class Missions
 */
@WebServlet("/Missions")
public class Missions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Missions() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		DBConnection conn = DBConnectionFactory.getConnection();	
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.create();
		System.out.println("create gson successfully");
		try {
			List<MissionInfo> curMission = conn.getAllMissions();
			System.out.println("get list of mission info successfully");
			if(curMission!=null&&curMission.size()!=0) {
				//System.out.println(gson.toJson(curMission));

				HelperFunctions.writeJsonString(response, gson.toJson(curMission));
				System.out.println("write response successfully");
				//os.close();
			}else {
				response.sendError(204);
			}
			
		  
		 } catch (Exception e) { 
			 response.sendError(500); 
		 } finally{
			 conn.close();
			 //os.close();
			 
			 
		 }
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
