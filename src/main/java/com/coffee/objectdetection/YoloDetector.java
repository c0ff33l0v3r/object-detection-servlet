package com.coffee.objectdetection;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;

public class YoloDetector {
    private static final double SCALE_FACTOR = 1 / 255.0;
    private static final Scalar MEAN = new Scalar(0);
    private final Net net;
    private final boolean swapRB = true;
    private final boolean crop = false;
    private final List<String> outBlobNames = new ArrayList<>();
    private final Size size = new Size(608, 608);
    private final float confidenceThreshold = 0.6f;
    private final float nmsThreshold = 0.5f;
    List<Float> confidences;
    List<Rect2d> rects;
    Mat image;
    private List<Integer> classIds;


    public YoloDetector(String cfgFile, String darknetModel) {
        this.net = Dnn.readNetFromDarknet(cfgFile, darknetModel);
        List<Integer> outLayers = this.net.getUnconnectedOutLayers().toList();
        List<String> layerNames = this.net.getLayerNames();
        outLayers.forEach(layer -> this.outBlobNames.add(layerNames.get(layer - 1)));
    }

    public DetectionResult performDetection(CVImage cvImage) {
        this.image = cvImage.getMat();
        Mat blob = Dnn.blobFromImage(image, SCALE_FACTOR, size, MEAN, swapRB, crop);
        net.setInput(blob);
        List<Mat> outputBlobs = new ArrayList<>();
        net.forward(outputBlobs, outBlobNames);

        classIds = new ArrayList<>();
        confidences = new ArrayList<>();
        rects = new ArrayList<>();

        outputBlobs.forEach(mat -> processMatRows(mat));

        MatOfFloat scores = new MatOfFloat(Converters.vector_float_to_Mat(confidences));
        Rect2d[] boxesArray = rects.toArray(new Rect2d[0]);
        MatOfRect2d bboxes = new MatOfRect2d(boxesArray);
        MatOfInt indices = new MatOfInt();
        Dnn.NMSBoxes(bboxes, scores, confidenceThreshold, nmsThreshold, indices);

        for (int idx : indices.toArray()) {
            drawBoundingBoxes(boxesArray[idx]);
        }

        return new DetectionResult(matToByteArray(image), scores, indices, boxesArray);
    }

    private Rect2d calculateRects(Mat row) {
        int centerX = (int) (row.get(0, 0)[0] * image.cols());
        int centerY = (int) (row.get(0, 1)[0] * image.rows());
        int width = (int) (row.get(0, 2)[0] * image.cols());
        int height = (int) (row.get(0, 3)[0] * image.rows());
        int left = centerX - width / 2;
        int top = centerY - height / 2;

        return new Rect2d(left, top, width, height);
    }

    private void processMatRows(Mat mat) {
        for (int i = 0; i < mat.rows(); i++) {
            Mat row = mat.row(i);
            Mat scores = row.colRange(5, mat.cols());
            Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(scores);
            float confidence = (float) minMaxLocResult.maxVal;
            Point classIdPoint = minMaxLocResult.maxLoc;

            if (confidence > confidenceThreshold) {
                classIds.add((int) classIdPoint.x);
                confidences.add(confidence);
                rects.add(calculateRects(row));
            }
        }
    }

    private void drawBoundingBoxes(Rect2d boundingBox) {
        Imgproc.rectangle(image, boundingBox.tl(), boundingBox.br(), new Scalar(0, 0, 255), 2);
    }

    byte[] matToByteArray(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        return matOfByte.toArray();
    }

}
