package com.coffee.objectdetection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class CVImage {
    private byte[] byteArray;

    public CVImage(InputStream imageStream) {
        byte[] data = new byte[16 * 2048];
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            while (imageStream.read(data, 0, data.length) != -1) {
                outputStream.write(data);
            }
            outputStream.flush();
            outputStream.close();
            this.byteArray = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CVImage(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public String getBase64Encoding() {
        return Base64.getEncoder().encodeToString(byteArray);
    }

    public Mat getMat() {
        return Imgcodecs.imdecode(new MatOfByte(byteArray), Imgcodecs.IMREAD_UNCHANGED);
    }
}
