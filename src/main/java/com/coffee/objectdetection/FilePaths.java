package com.coffee.objectdetection;

import com.coffee.handler.FileHandler;

public enum FilePaths {

    OPENCV_DLL_PATH("/dll/opencv_java450.dll"),
    LP_CFG("/models/yolov3_tiny_lp.cfg"),
    LP_WEIGHTS("/models/yolov3_tiny_lp_final.weights"),
    OCR_CFG("/models/yolov3_tiny_ocr.cfg"),
    OCR_WEIGHTS("/models/yolov3_tiny_ocr_last.weights");


    private final String path;

    FilePaths(String path) {
        this.path = FileHandler.getFilepath(path);
    }

    public String getPath() {
        return path;
    }
}
