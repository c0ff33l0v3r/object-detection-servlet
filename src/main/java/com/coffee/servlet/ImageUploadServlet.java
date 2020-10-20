package com.coffee.servlet;

import com.coffee.objectdetection.CVImage;
import com.coffee.objectdetection.DetectionResult;
import com.coffee.objectdetection.YoloDetector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import static com.coffee.objectdetection.FilePaths.*;

@WebServlet("/upload")
@MultipartConfig
@WebListener
public class ImageUploadServlet extends HttpServlet implements ServletContextListener {
    private static YoloDetector lpDetector;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("image-file");
        String filename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        InputStream fileContent = filePart.getInputStream();

        CVImage originalImage = new CVImage(fileContent);
        DetectionResult processedImage = lpDetector.performDetection(originalImage);
        CVImage croppedLicensePlate = new CVImage(processedImage.getCroppedResults().get(0));

        request.setAttribute("original", originalImage.getBase64Encoding());
        request.setAttribute("processed", processedImage.getBase64Encoding());
        request.setAttribute("extracted", croppedLicensePlate.getBase64Encoding());

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.load(OPENCV_DLL_PATH.getPath());
        lpDetector = new YoloDetector(LP_CFG.getPath(), LP_WEIGHTS.getPath());
    }

}
