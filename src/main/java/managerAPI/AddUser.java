package managerAPI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.MissionInfo;
import entity.mission;
import entity.user;
import qrcode.QRCodeHelper;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/AddUser")
public class AddUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OutputStream os = response.getOutputStream();
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
				JSONObject input = HelperFunctions.readJsonObject(request);
				String username = input.getString("username").trim();
				String password = input.getString("password").trim();
				String firstname = input.getString("firstname").trim();
				String lastname = input.getString("lastname").trim();
				String exp = input.getString("expirationDate").trim();
				JSONArray attributes = input.getJSONArray("attributes");
				JSONArray interests = input.getJSONArray("interests");
				System.out.println("Attributes:\t" + attributes);
				List<String> attr = new ArrayList<String>();
				if (attributes != null) { 
					   for (int i=0;i<attributes.length();i++){
						   //System.out.println(attributes.getString(i));
						   attr.add(attributes.getString(i));
					   } 
				} 
				System.out.println("Interests:\t" + interests);
				List<String> ints = new ArrayList<String>();
				if (interests != null) { 
					   for (int i=0;i<interests.length();i++){
						   //System.out.println(attributes.getString(i));
						   ints.add(interests.getString(i));
					   } 
				} 
		        
				System.out.println(String.format("%s,%s,%s,%s",username,password,firstname,lastname));
				
				user curUser = new user(username,password,firstname,lastname, attr, ints);
				Date localTime = new Date();
				Timestamp expTimestamp = exp.length()>10? 
						HelperFunctions.convertStringToTimestamp(exp)
						:new Timestamp(localTime.getTime()+ConstantForServer.DEFAULT_EXPIRATION_DURATION);
				curUser.setExpirationDate(expTimestamp);
				user existingUser = conn.searchUser(username);
				if (existingUser == null && conn.insertUser(curUser)) {
					
					System.out.println("user successfully added");
					response.sendError(200);
				}
				else {
					System.out.println("user can't be added");
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
		response.addHeader("Access-Control-Allow-Origin", "*");
		OutputStream os = response.getOutputStream();
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
				JSONObject input = HelperFunctions.readJsonObject(request);
				String username = input.getString("username").trim();
				String password = input.getString("password").trim();
				String firstname = HelperFunctions.getFieldFromJsonRequest("firstname", input);
				String lastname = HelperFunctions.getFieldFromJsonRequest("lastname", input);
				String exp = HelperFunctions.getFieldFromJsonRequest("expirationDate", input);
				JSONArray attributes = input.getJSONArray("attributes");
				System.out.println("Attributes:\t" + attributes);
				List<String> attr = new ArrayList<String>();
				if (attributes != null) { 
					   for (int i=0;i<attributes.length();i++){
						   //System.out.println(attributes.getString(i));
						   attr.add(attributes.getString(i));
					   } 
				} 
				JSONArray interests = input.getJSONArray("interests");
				System.out.println("Interests:\t" + interests);
				List<String> ints = new ArrayList<String>();
				if (interests != null) { 
					   for (int i=0;i<interests.length();i++){
						   ints.add(interests.getString(i));
					   } 
				} 
		        
				System.out.println(String.format("%s,%s,%s,%s",username,password,firstname,lastname));
				
				user curUser = new user(username,password,firstname,lastname, attr, ints);
				Date localTime = new Date();
				Timestamp expTimestamp = exp.length()>10? 
						HelperFunctions.convertStringToTimestamp(exp)
						:new Timestamp(localTime.getTime()+ConstantForServer.DEFAULT_EXPIRATION_DURATION);
				curUser.setExpirationDate(expTimestamp);
				System.out.println("user: "+curUser.getUsername()+" expires at: "+ curUser.getExpirationDate());
				user existingUser = conn.searchUser(username);
				if (existingUser == null && conn.insertUser(curUser)) {
					
					System.out.println("user successfully added");
					response.sendError(200);
				}
				else {
					System.out.println("user can't be added");
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
