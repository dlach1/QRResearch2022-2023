/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.qrcode.decoder;
import java.util.Arrays;
import com.google.zxing.qrcode.decoder.Version.*;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import com.google.zxing.qrcode.decoder.Version;
import java.util.Map;

/**
 * <p>The main class which implements QR Code decoding -- as opposed to locating and extracting
 * the QR Code from an image.</p>
 *
 * @author Sean Owen
 */
public final class Decoder {

  private final ReedSolomonDecoder rsDecoder;
  private int numErrors = 0;
  public Decoder() {
    rsDecoder = new ReedSolomonDecoder(GenericGF.QR_CODE_FIELD_256);
  }

  public DecoderResult decode(boolean[][] image) throws ChecksumException, FormatException {
    return decode(image, null);
  }

  /**
   * <p>Convenience method that can decode a QR Code represented as a 2D array of booleans.
   * "true" is taken to mean a black module.</p>
   *
   * @param image booleans representing white/black QR Code modules
   * @param hints decoding hints that should be used to influence decoding
   * @return text and bytes encoded within the QR Code
   * @throws FormatException if the QR Code cannot be decoded
   * @throws ChecksumException if error correction fails
   */
  public DecoderResult decode(boolean[][] image, Map<DecodeHintType,?> hints)
      throws ChecksumException, FormatException {
    return decode(BitMatrix.parse(image), hints);
  }

  public DecoderResult decode(BitMatrix bits) throws ChecksumException, FormatException {
    return decode(bits, null);
    }

  /**
   * <p>Decodes a QR Code represented as a {@link BitMatrix}. A 1 or "true" is taken to mean a black module.</p>
   *
   * @param bits booleans representing white/black QR Code modules
   * @param hints decoding hints that should be used to influence decoding
   * @return text and bytes encoded within the QR Code
   * @throws FormatException if the QR Code cannot be decoded
   * @throws ChecksumException if error correction fails
   */
  public DecoderResult decode(BitMatrix bits, Map<DecodeHintType,?> hints)
      throws FormatException, ChecksumException {

    // Construct a parser and read version, error-correction level
    BitMatrixParser parser = new BitMatrixParser(bits);
    FormatException fe = null;
    ChecksumException ce = null;
    try {
      return decode(parser, hints);
    } catch (FormatException e) {
      fe = e;
    } catch (ChecksumException e) {
      ce = e;
    }

    try {

      // Revert the bit matrix
      parser.remask();

      // Will be attempting a mirrored reading of the version and format info.
      parser.setMirror(true);

      // Preemptively read the version.
      parser.readVersion();

      // Preemptively read the format information.
      parser.readFormatInformation();

      /*
       * Since we're here, this means we have successfully detected some kind
       * of version and format information when mirrored. This is a good sign,
       * that the QR code may be mirrored, and we should try once more with a
       * mirrored content.
       */
      // Prepare for a mirrored reading.
      parser.mirror();

      DecoderResult result = decode(parser, hints);

      // Success! Notify the caller that the code was mirrored.
      result.setOther(new QRCodeDecoderMetaData(true));

      return result;

    } catch (FormatException | ChecksumException e) {
      // Throw the exception from the original reading
      if (fe != null) {
        throw fe;
      }
      throw ce; // If fe is null, this can't be
    }
  }
  public String decodeSecret(BitMatrix bits, Map<DecodeHintType,?> hints) throws FormatException, ChecksumException {
      
    // Construct a parser and read version, error-correction level
    BitMatrixParser parser = new BitMatrixParser(bits);
    FormatException fe = null;
    ChecksumException ce = null;
    // try {
      // return decode(parser, hints).getText();
    // } catch (FormatException e) {
      // fe = e;
    // } catch (ChecksumException e) {
      // ce = e;
    // }

    // try {

      // // Revert the bit matrix
      // parser.remask();

      // // Will be attempting a mirrored reading of the version and format info.
      // parser.setMirror(true);

      // // Preemptively read the version.
      // parser.readVersion();

      // // Preemptively read the format information.
      // parser.readFormatInformation();

      // /*
       // * Since we're here, this means we have successfully detected some kind
       // * of version and format information when mirrored. This is a good sign,
       // * that the QR code may be mirrored, and we should try once more with a
       // * mirrored content.
       // */
      // // Prepare for a mirrored reading.
      // parser.mirror();

      // DecoderResult result = decode(parser, hints);

      // // Success! Notify the caller that the code was mirrored.
      // result.setOther(new QRCodeDecoderMetaData(true));

      // //return result.getText;
      // return null;

    // } catch (FormatException | ChecksumException e) {
      // // Throw the exception from the original reading
      // if (fe != null) {
        // throw fe;
      // }
      // throw ce; // If fe is null, this can't be
    // }
      // BitMatrixParser parser = new BitMatrixParser(bits);
      // FormatException fe = null;
      // ChecksumException ce = null;
        try {
        DecoderResult dR =  decode(parser, hints);
      } catch (FormatException e) {
        fe = e;
      } catch (ChecksumException e) {
        ce = e;
      }
      try {
          ////System.out.println(bits.getHeight());
          
          parser.remask();

          // Will be attempting a mirrored reading of the version and format info.
          //parser.setMirror(true);
          //parser.readFormatInformation();
          Version v = parser.readVersion();
          ECBlocks ec;
          ErrorCorrectionLevel ecLevel = parser.readFormatInformation().getErrorCorrectionLevel();
          ec = v.getECBlocksForLevel(ecLevel);
          ECB[] ecbs = ec.getECBlocks();
          int[] ecbsLength = new int[ecbs.length];
          for (int i = 0; i < ecbs.length; i ++){
              ecbsLength[i] = ecbs[i].getDataCodewords();
              //System.out.println("Block " + i + " Data Codewords: " + ecbsLength[i] + " Count: " + ecbs[i].getCount());
          }
          int numECCodewords = ec.getECCodewordsPerBlock();
          //System.out.println("Number of Codewords in Block: " + numECCodewords);
          //System.out.println(parser.readFormatInformation().getErrorCorrectionLevel());
          int numBlocks = ec.getNumBlocks();
          int NumecCodewords = ec.getTotalECCodewords();
          String secretBits = "";
          int[] ecCodewords = new int[NumecCodewords];
          byte[] codewords = parser.readCodewords();
          //System.out.println("here:");
          //System.out.println(Arrays.toString(codewords));
          int numCodewords = v.getTotalCodewords();
          System.out.println("Version: " + v);
          //System.out.println("Error Correction Codewords: " + NumecCodewords * numBlocks + " Number of Blocks: " + numBlocks);
          //System.out.println("length: " + codewords.length);
          DataBlock[] d = DataBlock.getDataBlocks(codewords, v, ecLevel);
          String message = "";
          double eMax;
          if (ecLevel == ErrorCorrectionLevel.L)
              eMax = 0.11;
          else if (ecLevel == ErrorCorrectionLevel.M)
              eMax = 0.238;
          else if (ecLevel == ErrorCorrectionLevel.Q)
              eMax = 0.397;
          else
              eMax = 0.476;
          
          for (int j = 0; j < d.length; j ++) {//DataBlock block : d) {
              DataBlock block = d[j];
              int numDC = block.getNumDataCodewords();
              
              int numEC = ec.getECCodewordsPerBlock();
              //System.out.println(numEC);
              int numBlockCW = numEC + numDC;
              byte[] bCodewords = block.getCodewords();
              int start;
              if (j>=1){
                  start = numBlockCW-numECCodewords;
              }
              else {
                  start = numBlockCW-numECCodewords;
              }
              
              byte t;// = bCodewords[start];
              //System.out.println("t=" +t);
              int max = (int)(numEC*eMax);
              for (int i = 0; i < max; i ++){
                  t = bCodewords[start+i];
                  if (t>0) {
                      for (int x = 7; x > -1; x --) {
                          //System.out.println((t>>i) %2);
                          message+=(t>>x) %2;
                      }
                  }
              }
              
              // t = bCodewords[start+1];
              // //System.out.println("t=" +t);
              // if (t>0) {
                  // for (int i = 7; i > -1; i --) {
                      // //System.out.println((t>>i) %2);
                      // message+=(t>>i) %2;
                  // }
              // }
              // t = bCodewords[start+2];
              // //System.out.println("t=" +t);
              // if (t>0) {
                  // for (int i = 7; i > -1; i --) {
                      // //System.out.println((t>>i) %2);
                      // message+=(t>>i) %2;
                  // }
              // }
              // t = bCodewords[start+3];
              // //System.out.println("t=" +t);
              // if (t>0) {
                  // for (int i = 7; i > -1; i --) {
                      // //System.out.println((t>>i) %2);
                      // message+=(t>>i) %2;
                  // }
              // }
              //System.out.println("Message:" +message);
              // for (int i = start; i < numBlockCW; i ++) {
                  // byte b = bCodewords[i];
                  // byte t = (byte)(((int)b)&0x1);
                  // message+= t;
                  
                  // //System.out.println(bCodewords[i]);
                  
                  // ////System.out.println();
              // }
              
              try {
                   String s = new String(bCodewords, "UTF-8");
                   if (s.equals("") ==false){
                      ////System.out.println(s); 
                   }
              }
              catch (java.io.UnsupportedEncodingException e){
                  
              }

              
          }
          System.out.println(message);
          String shortened = message.substring(1);
          String s = shortened.substring(0,5);
          int x = BinarytoNum(s);
          //System.out.println(x);
          String bin;
          if (5+x*8 < shortened.length())
              bin = shortened.substring(5, 5+x*8);
          else 
              bin = shortened.substring(5);
          //System.out.println(bin);
          System.out.println("Secret Message: " + BinarytoString(message));
          
          // Only works if the assumption that the first N of codewords are ecCodewords.
          
          //System.out.println((secretBits));
          //System.out.println(BinarytoString(secretBits));
          return secretBits;
          //return BinarytoString(secretBits);
      }
      catch (FormatException f){
          System.out.println("Cause: " + fe.getCause());
      }
      
      ////System.out.println(ec);
      
      
      
      return null;
  }
  private static int BinarytoNum(String s){
      int num = 0;
      for (int i = 0; i < s.length(); i ++){
          num<<=1;
          if (s.charAt(i) == '1'){
              num+=1;
          }
          
      }
      return num;
  }
  private static String BinarytoString(String letters) {
             //java solution
         String s = " ";
         for(int index = 0; index < letters.length(); index+=8) {
             String temp;
             if (index+8 < letters.length())
                 temp = letters.substring(index, index+8);
             else 
                 break;
             int num = Integer.parseInt(temp,2);
             char letter = (char) num;
             s = s+letter;
         }
         return s;
         }
  private DecoderResult decode(BitMatrixParser parser, Map<DecodeHintType,?> hints)
      throws FormatException, ChecksumException {
    Version version = parser.readVersion();
    ErrorCorrectionLevel ecLevel = parser.readFormatInformation().getErrorCorrectionLevel();

    // Read codewords
    byte[] codewords = parser.readCodewords();
    // Separate into data blocks
    DataBlock[] dataBlocks = DataBlock.getDataBlocks(codewords, version, ecLevel);

    // Count total number of data bytes
    int totalBytes = 0;
    for (DataBlock dataBlock : dataBlocks) {
      totalBytes += dataBlock.getNumDataCodewords();
    }
    byte[] resultBytes = new byte[totalBytes];
    int resultOffset = 0;
    
    // Error-correct and copy data blocks together into a stream of bytes
    for (DataBlock dataBlock : dataBlocks) {
      byte[] codewordBytes = dataBlock.getCodewords();
      int numDataCodewords = dataBlock.getNumDataCodewords();
      numErrors += correctErrors(codewordBytes, numDataCodewords);
      //System.out.println(numErrors);
      for (int i = 0; i < numDataCodewords; i++) {
        resultBytes[resultOffset++] = codewordBytes[i];
      }
    }

    // Decode the contents of that stream of bytes
    DecoderResult d = DecodedBitStreamParser.decode(resultBytes, version, ecLevel, hints);
    d.setErrorsCorrected(numErrors);
    return d;
  }

  /**
   * <p>Given data and error-correction codewords received, possibly corrupted by errors, attempts to
   * correct the errors in-place using Reed-Solomon error correction.</p>
   *
   * @param codewordBytes data and error correction codewords
   * @param numDataCodewords number of codewords that are data bytes
   * @throws ChecksumException if error correction fails
   */
  private int correctErrors(byte[] codewordBytes, int numDataCodewords) throws ChecksumException {
    int numCodewords = codewordBytes.length;
    // First read into an array of ints
    int[] codewordsInts = new int[numCodewords];
    for (int i = 0; i < numCodewords; i++) {
      codewordsInts[i] = codewordBytes[i] & 0xFF;
    }
    try {
      rsDecoder.decode(codewordsInts, codewordBytes.length - numDataCodewords);
    } catch (ReedSolomonException ignored) {
      throw ChecksumException.getChecksumInstance();
    }
    // Copy back into array of bytes -- only need to worry about the bytes that were data
    // We don't care about errors in the error-correction codewords
    byte[] errors = new byte[numCodewords];
    int sum = 0;
    ////System.out.println(Arrays.deeptoString(codewords));
    
    for (int i = 0; i < numCodewords; i++) {
        errors[i] = (byte)((((int) codewordBytes[i]) & 0xFF) ^ codewordsInts[i]);
        // if ((((int)codewordBytes[i])&0xFF) % 2 == 0)
            // System.out.println("1");
        // else 
            // System.out.println("0");
        //System.out.println("codeword: " + (((int)codewordBytes[i])&0xFF));
        //System.out.println("Word: "+ i + "codeword: " + (((int)codewordBytes[i])&0xFF) + "corrected: " + codewordsInts[i]);
        // if ((((int)(errors[i]))&0xFF) == 1){
            // System.out.println("Changed errors: " + (((int)(errors[i]))&0xFF) + " at i: " + i);
        // }
        sum += Integer.bitCount(((int)(errors[i]))&0xFF);
        
    }
    for (int i = 0; i < numDataCodewords; i++) {
      if (codewordBytes[i] != (byte) codewordsInts[i]) {
          
          //System.out.println((byte) codewordsInts[i]);
          numErrors+=1;
        }
      codewordBytes[i] = (byte) codewordsInts[i];
    }
    
    //System.out.println("sum: " + sum);
    return sum;
  }

}
