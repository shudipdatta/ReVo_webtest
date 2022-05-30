package managerAPI;

import java.io.IOException;
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
import entity.mission;
import entity.user;

/**
 * Servlet implementation class SearchUser
 */
@WebServlet("/SearchUser")
public class SearchUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchUser() {
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
			String username = input.getString("username").trim();

			System.out.println(String.format("Find user by username %s",username));
			
			user curUser = conn.searchUser(username);
			//TO-DO, add password verification.
			if (curUser != null) {
				
				System.out.println("Get user  successfully: ");
				
				HelperFunctions.writeJsonString(response, gson.toJson(curUser));
			}
			else {
				
				System.out.println("Fail to find user ");
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
