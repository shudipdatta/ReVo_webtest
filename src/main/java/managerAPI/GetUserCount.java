package managerAPI;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.user;

/**
 * Servlet implementation class GetUserCount
 */
@WebServlet("/GetUserCount")
public class GetUserCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserCount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		DBConnection conn = DBConnectionFactory.getConnection();	
		//Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println("create gson successfully");
		try {
			List<user> curUsers = conn.getAllUsers();
			System.out.println("get list of user infos successfully");
			if(curUsers!=null&&curUsers.size()!=0) {
				//System.out.println(gson.toJson(curUsers));

				PrintWriter out = response.getWriter();
				String resp = String.format("{\"UserCount\": %d}", curUsers.size());
				out.print(resp);
				out.close();
				//RpcHelper.writeJsonString(response, gson.toJson(curMission));
				System.out.println("write response successfully");
				//os.close();
			}else {
				PrintWriter out = response.getWriter();
				String resp = String.format("{\"UserCount\": %d}", 0);
				out.print(resp);
				out.close();
				//response.sendError(204);
			}
			
		  
		 } catch (Exception e) { 
			 response.sendError(500); 
		 } finally{
			 conn.close();
			 //os.close();
		 }
	}

}
