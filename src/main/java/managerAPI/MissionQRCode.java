package managerAPI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.mission;
import qrcode.QRCodeHelper;
/**
 * Servlet implementation class MissionQRCode
 */
@WebServlet("/MissionQRCode")
public class MissionQRCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MissionQRCode() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		OutputStream os = response.getOutputStream();
		DBConnection conn = DBConnectionFactory.getConnection();		
		String paramValue = request.getParameter("missionCode");
		
		System.out.println(paramValue);
		try {
			mission curMission = conn.searchMissionByCode(paramValue);
			if(curMission!=null) {
				BufferedImage bi = QRCodeHelper.generatePDF417Image(curMission.getMissionCode());
			
				//--Send the data to response
				response.setContentType("image/jpeg");
				System.out.println("OK");

				ImageIO.write(bi, "jpg", os);
				System.out.println("Still Still OK");
				os.close();
			}else {
				response.sendError(204);
			}
			
		  
		 } catch (Exception e) { 
			 response.sendError(500); 
		 } finally{
			 conn.close();
			 os.close();
			 
			 
		 }
	}

}
