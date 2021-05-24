/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrCodes;



import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import model.Agents;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.glxn.qrgen.vcard.VCard;

/**
 *
 * @author alexi
 */
public class QRCodeUtil {

    public static BufferedImage generateQRCodeImage(Agents propID) throws Exception {
        //QRCodeWriter barcodeWriter = new QRCodeWriter();
        //BitMatrix bitMatrix = barcodeWriter.encode(johnDoe, BarcodeFormat.QR_CODE, 200, 200);
        //return MatrixToImageWriter.toBufferedImage(bitMatrix);

        // encode contact data as vcard using defaults
        VCard johnDoe = new VCard(propID.getName())
                        .setEmail(propID.getEmail())
                        .setPhonenumber(propID.getPhone());
        QRCode.from(johnDoe).file();

        ByteArrayOutputStream stream = QRCode
              .from(johnDoe)
              .withSize(250, 250)
              .to(ImageType.PNG)  
              .stream();
        ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());
        return ImageIO.read(bis);
    }
}