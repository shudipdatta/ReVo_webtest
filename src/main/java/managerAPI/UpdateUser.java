package managerAPI;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.mission;
import entity.user;

/**
 * Servlet implementation class UpdateUser
 */
@WebServlet("/UpdateUser")
public class UpdateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		OutputStream os = response.getOutputStream();
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
				JSONObject input = HelperFunctions.readJsonObject(request);
				String oldUserName = input.getString("OldUserName").trim();
				String newUserName = input.getString("NewUserName").trim();
				String password = input.getString("password").trim();
				String attributes = input.getString("attributes").trim();
				String interests = input.getString("interests").trim();
				String firstname = HelperFunctions.getFieldFromJsonRequest("firstname", input);
				String lastname = HelperFunctions.getFieldFromJsonRequest("lastname", input);
				String exp = HelperFunctions.getFieldFromJsonRequest("expirationDate", input);
				System.out.println(String.format("%s,%s,%s",oldUserName,newUserName,password));
				
				user curUser = new user(newUserName, password, firstname, lastname, HelperFunctions.stringToStringList(attributes), HelperFunctions.stringToStringList(interests));
				user existingUser = conn.searchUser(oldUserName);
				
				if (existingUser == null && conn.insertUser(curUser)) {
					
					System.out.println("Old user not exists but new user is inserted successfully added");
					response.sendError(204);
				}
				else {
					existingUser.setUserName(newUserName);
					existingUser.setPassword(password);
					existingUser.setFirstname(firstname);
					existingUser.setLastname(lastname);
					existingUser.setAttributes(attributes);
					existingUser.setInterests(interests);
					Date localTime = new Date();
					Timestamp expTimestamp = exp.length()>10? 
							HelperFunctions.convertStringToTimestamp(exp)
							:new Timestamp(localTime.getTime()+ConstantForServer.DEFAULT_EXPIRATION_DURATION);
					existingUser.setExpirationDate(expTimestamp);
					conn.updateUser(existingUser);
					System.out.println("user update successfully");
					response.sendError(200);
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
