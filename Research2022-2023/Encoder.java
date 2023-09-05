 

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class Encoder {

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

    public static String prettyBinary(String binary, int blockSize, String separator) {

        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < binary.length()) {
            result.add(binary.substring(index, Math.min(index + blockSize, binary.length())));
            index += blockSize;
        }

        return result.stream().collect(Collectors.joining(separator));
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

    public static void main(String[] args) throws NotFoundException, WriterException, IOException {
        Scanner s = new Scanner(System.in);
        System.out.println("QR Code Message: ");
        String message = s.nextLine();
        System.out.println("Secret Message: ");
        String secret = s.nextLine();
        System.out.println("Filename: ");
        String filename = s.nextLine();
        //String result = convertStringToBinary(input);
        EncoderBase qr = new EncoderBase();
        System.out.println("Start");
        //qr.getNoErrors("Clarence;R.P.;Sellers;1976-03-13;586 Middle Treasure;86240;Las Angeles;California;President:McCaffrey;VP:Jordan");
        
        qr.getTamperedQR(message, secret, filename);
        
        //System.out.println(result);

    }
}
