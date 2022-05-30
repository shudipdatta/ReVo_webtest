package test;

import java.awt.image.BufferedImage;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.mission;
import qrcode.QRCodeHelper;

public class testQR {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnection conn = DBConnectionFactory.getConnection();	
		mission curMission = conn.searchMissionByCode("1959017339");
		try {
			BufferedImage bi = QRCodeHelper.generateEAN13BarcodeImage(curMission.getMissionCode());
			System.out.println(bi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
