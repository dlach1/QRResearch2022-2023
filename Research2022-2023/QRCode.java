 
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.google.zxing.qrcode.*;
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.qrcode.detector.Detector;
import java.util.Scanner;

public class QRCode {
    static String[][] condensedArr;
    private static String decodeQRCode(File qrCodeimage) throws ChecksumException, FormatException, IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Binarizer bits = (new HybridBinarizer(source)).createBinarizer(source);
        BitMatrix matrix;
        QRCodeReader qreader = new QRCodeReader();
        
        
        try
        {
            matrix = bits.getBlackMatrix();
        }catch (NotFoundException e1) {
            // TODO Auto-generated catch block
            matrix = new BitMatrix(1);
            e1.printStackTrace();
        }
        //System.out.println(matrix.getHeight());
            Decoder d = new Decoder();
        //System.out.println(bitmap);
        
        try {
            DetectorResult dResult;
            DecoderResult decResult;
            Decoder decoder = new Decoder();
            dResult = new Detector(matrix).detect(null);
            decResult = decoder.decode(dResult.getBits(), null);
            //System.out.println(decResult.getText());
            System.out.println("Number of Errors: " + decResult.getErrorsCorrected());
            System.out.println("Error Correction Level: " + decResult.getECLevel());
            
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }
    private static byte[] extractMessage(String[][] arr, int messLength) {
        int count = 0; 
        int i = 0;
        byte[] temp = new byte[messLength];
        while (i < arr[0].length) {
            if (i < 6) {
                int j = 8;
                while (j < arr[0].length - 7) {
                    if (count < messLength) {
                        if (arr[i][j].equals("X")) 
                            temp[count] = 1;
                        else 
                            temp[count] = 0;
                        //System.out.println("row:" + i + " col: " + j + " val: " + temp[count]);
                        count ++;
                    }
                    j += 2;
                }
            }
            if (i > 7 && i < arr.length - 7) {
                int j = 0;
                while (j < arr[0].length - 1) {
                    if (count < messLength) {
                        if (arr[i][j].equals("X")) 
                            temp[count] = 1;
                        else 
                            temp[count] = 0;
                        //System.out.println("row:" + i + " col: " + j + " val: " + temp[count]);
                        count ++;
                        
                    }
                    j += 2;
                }
            }
            if (i > arr.length - 8 && i < arr.length) {
                int j = 8;
                while (j < arr[0].length) {
                    if (count < messLength) {
                        if (arr[i][j].equals("X")) 
                            temp[count] = 1;
                        else 
                            temp[count] = 0;
                        //System.out.println("row:" + i + " col: " + j + " val: " + temp[count]);
                        count ++;
                    }
                    j += 2;
                }
            }
            i += 4;
        }
        return temp;
    }
    private static String BinarytoString(String letters) {
             //java solution
         String s = " ";
         for(int index = 0; index < letters.length(); index+=9) {
             String temp = letters.substring(index, index+8);
             int num = Integer.parseInt(temp,2);
             char letter = (char) num;
             s = s+letter;
         }
         return s;
         }
    
    private static String[][] condenseArr(BitMatrix matrix, String[][] array, int scale) {
        String[][] condensedArr = new String[29][29];
        for (int i = matrix.getTopLeftOnBit()[0]; i < matrix.getBottomRightOnBit()[0] + matrix.getTopLeftOnBit()[0] + 1; i += scale) {
            for (int j = matrix.getTopLeftOnBit()[0]; j < matrix.getBottomRightOnBit()[0] +matrix.getTopLeftOnBit()[0]+ 1; j += scale) {
                condensedArr[(i-matrix.getTopLeftOnBit()[0])/scale][(j-matrix.getTopLeftOnBit()[0])/scale] = array[i][j];
            }
        }
        return condensedArr;
    }
    
    //public static 
    private static BitMatrix getmatrix(File qrCodeimage) throws IOException{
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Binarizer bits = (new HybridBinarizer(source)).createBinarizer(source);
        BitMatrix matrix;
        try {
            matrix = bits.getBlackMatrix();
        } catch (NotFoundException e1) {
            // TODO Auto-generated catch block
            matrix = new BitMatrix(1);
            e1.printStackTrace();
        }
        return matrix;
    }
    private static String condensedArrToString(String[][] condensedArr1) {
        String output = "";
        for (String[] row : condensedArr1) {
            for (String col : row) {
                output += col;
                output += " ";
                    
            }
            output += "\n";
        }
        return output;
    }
    
    public static void getSecretMessage(File qrCodeimage) throws IOException {
        BitMatrix matrix;
        matrix = getmatrix(qrCodeimage);
        //System.out.println(matrix);
        
        String[][] array = matrix.toArray();
        String output = ""; 
        for (String[] row : array) {
            for (String col : row) {
                output += col;
            }
            output += "\n";
        }
        //System.out.println(output);
        //System.out.println(matrix.getBottomRightOnBit()[0]);
        //System.out.println(matrix.getTopLeftOnBit()[0]);
        condensedArr = condenseArr(matrix, array, 10);
        
        output = ""; 
        output = condensedArrToString(condensedArr);
        
        byte[] binArray = extractMessage(condensedArr, 32);
        //System.out.println(output);
        output = "";
        int counts = 0;
        for (int i = 0; i < binArray.length; i ++) {
            if (counts == 8) {
                output += " ";
                counts = 0;
            }
            counts ++;
            output += binArray[i];
        }
            
        System.out.println(output);
        System.out.println("Secret Message: " + BinarytoString(output).substring(1));
        
    }
    private static int howSimilar(String decodedText, String[][] compare) throws FormatException, ChecksumException, NotFoundException, WriterException, IOException {
        EncoderBase code = new EncoderBase();
        File file = new File(code.returnCode(decodedText));
        BitMatrix matrix = getmatrix(file);
        decodeQRCode(file);
        //System.out.println(matrix);
        
        String[][] array = matrix.toArray();
//        String out = "";
//        for (String[] row : array) {
//            for (String str : row)
//                out += str;
//            out += "\n";
//        }
//        System.out.println(out);
        //String[][] condense = condenseArr(matrix, array, 6);
        String[][] condense = new String[29][29];
        //System.out.println(matrix.getTopLeftOnBit()[0]);System.out.println(matrix.getTopLeftOnBit()[1]);
        //System.out.println(matrix.getBottomRightOnBit()[0]);
        //System.out.println(matrix.getBottomRightOnBit()[1]);
        int top = matrix.getTopLeftOnBit()[0];
        int bottom = matrix.getBottomRightOnBit()[0];
        int left = matrix.getTopLeftOnBit()[1];
        int right = matrix.getBottomRightOnBit()[1];
        //System.out.println(top);
        //System.out.println(bottom);
        //System.out.println(left);
        //System.out.println(right);
        for (int row = left; row < right; row ++) {
            for (int col = left; col < right; col ++) {
                condense[(row - left)/5][(col - left)/5] = array[row][col];
            }
        }
        
        
        //System.out.println(condensedArrToString(condense));
        
        int count = 0;
        for (int i = 0; i < 29; i ++) {
            for (int j = 0; j < 29; j ++) {
                if (condense[i][j] == compare[i][j])
                    count ++;
            }
        }
        if (count >= 0)
            return count;
        return -1;
    }

    public static void main(String[] args) throws FormatException, ChecksumException, NotFoundException, WriterException {
        try {
            Scanner source = new Scanner(System.in);
            System.out.println("Input the image name:");
            String filename = source.next();
            File file = new File("..\\" + filename + ".png");//C:\\Users\\Greg\\Flash\\ModifiedQR.png");
            System.out.println("Start");
            //String decodedText = decodeQRCode(file);
            BitMatrix matrix = getmatrix(file);
            //System.out.println(matrix);dw
            
            Decoder d = new Decoder();
            //System.out.println("Test 123");
            // try
            // {
                // Thread.sleep(4000);
            // }
            // catch (InterruptedException ie)
            // {
                // ie.printStackTrace();
            // }
            QRCodeReader qrReader = new QRCodeReader();
            //System.out.println(matrix);
            BitMatrix pureMatrix = QRCodeReader.extractPureBits(matrix);
            System.out.println(pureMatrix);
            String s = d.decodeSecret(pureMatrix, null);
            //System.out.println("after");
            
            //getSecretMessage(file);
            // if(decodedText == null) {
                // System.out.println("No QR Code found in the image");
            // } else {
                // System.out.println("Decoded text: " + decodedText);
                // //System.out.println(howSimilar(decodedText, condensedArr));
                 
            // }
            System.out.println("Decoded Text: "+decodeQRCode(file));
        } catch (IOException e) {
            System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
        }
        
        //System.out.println(condensedArrToString());
    }
}