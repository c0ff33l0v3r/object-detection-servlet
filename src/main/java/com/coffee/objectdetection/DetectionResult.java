package com.coffee.objectdetection;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

public class DetectionResult extends CVImage {
    private final MatOfFloat confidences;
    private final MatOfInt indices;
    private final Rect2d[] boundingBoxes;
    private final Mat mat;

    public DetectionResult(byte[] matByteArray, MatOfFloat confidences, MatOfInt indices,
                           Rect2d[] boundingBoxes) {
        super(matByteArray);
        this.confidences = confidences;
        this.indices = indices;
        this.boundingBoxes = boundingBoxes;
        this.mat = super.getMat();
    }

    public List<byte[]> getCroppedResults() {
        List<byte[]> crops = new ArrayList<>();
        for (int idx : indices.toArray()) {
            Rect2d box = boundingBoxes[idx];
            Mat crop = new Mat(mat, new Rect((int) box.x, (int) box.y, (int) box.width,
                    (int) box.height));
            crops.add(matToByteArray(crop));
        }
        return crops;
    }

    byte[] matToByteArray(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        return matOfByte.toArray();
    }
}
