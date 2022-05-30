package managerAPI;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.EntityHelper;
import entity.Match;
import entity.mission;

/**
 * Servlet implementation class Bootstrap
 */
@WebServlet("/Bootstrap")
public class Bootstrap extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Bootstrap() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();

		out.print("The get is doing nothing right now");
		out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.addHeader("Access-Control-Allow-Origin", "*");
		OutputStream os = response.getOutputStream();
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
				JSONObject input = HelperFunctions.readJsonObject(request);
				String username = input.getString("username").trim();
				String password = input.getString("password").trim();
				String missionCode = input.getString("missionCode").trim();
				
				mission curMission = conn.searchMissionByCode(missionCode);
				Match curMatch = null;
				if (curMission !=null)
					curMatch = conn.searchMatch(curMission.getMissionName(), username);
				if (conn.verifyLogin(username, password) && curMatch !=null) {
					response.setContentType("keys");
					
					byte[] publicKey = curMission.toPublicKeyByteArray();
					byte[] privateKey = curMatch.toPrivateKeyByteArray();
					ByteArrayOutputStream bs = new ByteArrayOutputStream();
					
					bs.write(EntityHelper.int_to_bytes(publicKey.length));
					bs.write(publicKey);
					bs.write(EntityHelper.int_to_bytes(privateKey.length));
					bs.write(privateKey);
					byte[] res = bs.toByteArray();
					response.setContentLength(res.length);
					os.write(res);
				}
				else {
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
