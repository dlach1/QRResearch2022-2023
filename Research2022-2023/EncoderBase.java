 
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Scanner;
 
public class EncoderBase {
 
    // Function to create the QR code
    public static void createQR(String data, String path,
                                String charset, Map hashMap,
                                int height, int width)
        throws WriterException, IOException
    {
 
        BitMatrix matrix = new MultiFormatWriter().encode(
            new String(data.getBytes(charset), charset),
            BarcodeFormat.QR_CODE, width, height);
 
        MatrixToImageWriter.writeToFile(
            matrix,
            path.substring(path.lastIndexOf('.') + 1),
            new File(path));
    }
    public static void embedInformation(String secretMessage, String data, String path,
            String charset, Map hashMap,
            int height, int width)
                    throws WriterException, IOException
    {
        BitMatrix matrix = new MultiFormatWriter().encode(
            new String(data.getBytes(charset), charset),
            BarcodeFormat.QR_CODE, width, height, hashMap, secretMessage);
 
        MatrixToImageWriter.writeToFile(
            matrix,
            path.substring(path.lastIndexOf('.') + 1),
            new File(path));
    }
    public static String convertStringToBinary(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                    .replaceAll(" ", "0")                         // zero pads
                    );
        }
        return result.toString();

    }
    
    public void getTamperedQR(String dataStr, String secret, String filename) throws WriterException, IOException,
    NotFoundException{
        String data = dataStr;
        String path = "..\\" + filename + ".png";
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        
        embedInformation(secret, data, path, charset, hashMap, 200, 200);
        //System.out.println(convertStringToBinary("st"));
        System.out.println("QR Code Generated!!! ");
    }
    public void getNoErrors(String dataStr) throws WriterException, IOException, NotFoundException {
        String data = dataStr;
        String path = "..\\LAnoErrors.png";
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        createQR(dataStr, path, charset, hashMap, 200, 200);
        System.out.println("QR Code Generated!!");
    }
    
    public String returnCode(String dataStr) throws WriterException, IOException,
    NotFoundException{
        String data = dataStr;
        
        String path = "..\\test.png";
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
      = new HashMap<EncodeHintType,
                    ErrorCorrectionLevel>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        
        createQR(data, path, charset, hashMap, 200, 200);
        System.out.println("QR Code Generated!!! ");
        return path;
    }
    
//    public static BitArray getQR(String datas) 
//        throws WriterException, IOException,
//               NotFoundException
//    {
// 
//        // The data that the QR code will contain
//        String data = datas;
// 
//        // The path where the image will get saved
//        String path = "address.png";
// 
//        // Encoding charset
//        String charset = "UTF-8";
// 
//        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
//            = new HashMap<EncodeHintType,
//                          ErrorCorrectionLevel>();
// 
//        hashMap.put(EncodeHintType.ERROR_CORRECTION,
//                    ErrorCorrectionLevel.H);
// 
//        // Create the QR code and save
//        // in the specified folder
//        // as a jpg file
//        createQR(data, path, charset, hashMap, 200, 200);
//        System.out.println("QR Code Generated!!! ");
//    }
}