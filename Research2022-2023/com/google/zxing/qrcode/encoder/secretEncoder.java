package com.google.zxing.qrcode.encoder;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.StringUtils;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonEncoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Write a description of class secretEncoder here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class secretEncoder extends Encoder
{
    private static boolean[] secretArray;
    private static int next = 0;
    /**
     * Constructor for objects of class secretEncoder
     */
    public secretEncoder(String s)
    {
        super();// initialise instance variable
        secretArray=convertStringToBinary(s);
        
    }
    // public static QRCode encode(String content,
                              // ErrorCorrectionLevel ecLevel,
                              // Map<EncodeHintType,?> hints, String secretMessage) throws WriterException {
         // QRCode q = Encoder.encode(content, ecLevel, hints);
         // q.getVersion();
         // Version.ECBlocks ecBlocks = q.getVersion().getECBlocksForLevel(ecLevel);
         // int numDataBytes = q.getVersion().getTotalCodewords() - ecBlocks.getTotalECCodewords();
         // byte[] ecBytes = super.generateECBytes(numDataBytes, ecBlocks.getTotalECCodewords());
         
         // String result = convertStringToBinary(secretMessage);
         // for (int i = 0; i < result.length(); i ++) {
             // if (ecBytes[i].substring(ecBytes[i].length - 1) != result.charAt(i)) {
                 // ecBytes[i] = ecBytes[i] | 0xFE;
             // }
             
         // }
         
         // //byte[] ecBytes = generateECBytes(dataBytes, numEcBytesInBlock[0]);
         // //super.generateECBytes(q.getVersion().getTotalCodewords(), q.getECLevel().getTotalECCodewords());
         // //super.generateECBytes(byte[] super.dataBytes, int numEcBytesInBlock);
         // return q;
    // }
    public static boolean[] convertStringToBinary(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                    .replaceAll(" ", "0")                         // zero pads
                    );
        }
        String resultString = result.toString();
        boolean[] arr = new boolean[resultString.length()];
        
        for (int i = 0; i < resultString.length(); i ++) {
            char c = resultString.charAt(i);
            if (c == '1') {
                arr[i] = true;
            }
            else
                arr[i] = false;
        }
        return arr;
        
    }
    static byte[] generateECBytes(byte[] dataBytes, int numEcBytesInBlock) {
        byte[] ecBytes = Encoder.generateECBytes(dataBytes, numEcBytesInBlock);
         for (int i = 0; i < numEcBytesInBlock; i ++) {
            if (next >= secretArray.length)
                break;
             if (secretArray[next]) 
                ecBytes[next] = (byte)((int)ecBytes[next] | 0x1);
            else 
                ecBytes[next] = (byte)((int)ecBytes[next] & 0xFE);
            next++;
        }
            
        
        return ecBytes;
    }
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public int sampleMethod(int y)
    {
        return 0;
    }
}
