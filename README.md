# QR Code Research 2022-2023

The code from my research paper that I created in order to discover the effectiveness of secret data sharing in QR codes.

I conducted this research at the University of Connecticut under the mentorshop of Mr. Greg Johnson in the VoTer Center. The published paper can be found [here]().

In creating and testing my implementation I used the BlueJ IDE and the base QR Code reader Zebra Crossing which can be found here: [ZXING](https://zxing.github.io/zxing/apidocs/).

I created the surface level classes Encoder, EncoderBase, and Decoder and I also changed many of ZXING's classes including the MultiFormatWriter, MultiFormatReader, QRCodeReader, QRCodeWriter, Version, Decoder, and Encoder classes.

By taking advantage of the error correction algorithm built into QR codes and their readers, I was able to encode a secret message. As seen below, both QR codes will still scan properly but the one on the right houses a secret message that would
go undetected by most scanners. 
<p align="center">
  <img src="https://github.com/dlach1/QRResearch2022-2023/assets/94641554/bd6d5e5f-f217-48de-b5ec-4b9c20e6c23a">
</p>

This research also outlines a key flaw in most QR code scanners. Most scanners do not output the number of errors in a code so it is impossible to find out if the code has been tampered with. In order to combat this, my scanner outputs the number of
errors in a scanned code. Since QR codes are being used more and more in daily life and even in voting machines, it is important to maintain security by being able to detect errors when a code has possibly been tampered with.
