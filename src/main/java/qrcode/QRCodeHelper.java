package qrcode;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javax.imageio.*;


public class QRCodeHelper {
	
	
	public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
	    EAN13Writer barcodeWriter = new EAN13Writer();
	    BitMatrix bitMatrix = barcodeWriter.encode(fixedLengthString(barcodeText,12), BarcodeFormat.EAN_13, 300, 150);

	    return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	
	public static String fixedLengthString(String string, int length) {
	    return String.format("%1$"+length+ "s", string).replace(' ', '0');
	}


	public static BufferedImage generatePDF417Image(String codeText) throws Exception{
		PDF417Writer pdf417Writer = new PDF417Writer();
		BitMatrix bitMatrix = pdf417Writer.encode(codeText, BarcodeFormat.PDF_417, 500, 250);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	
}
