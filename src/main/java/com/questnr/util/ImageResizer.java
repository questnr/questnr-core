package com.questnr.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizer implements Runnable {

    private File inputFile;
    private File outputFile;
    private String outputFileName;
    private int scaledWidth;
    private int scaledHeight;
    private String fileName;
    private String format;

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public int getScaledWidth() {
        return scaledWidth;
    }

    public void setScaledWidth(int scaledWidth) {
        this.scaledWidth = scaledWidth;
    }

    public int getScaledHeight() {
        return scaledHeight;
    }

    public void setScaledHeight(int scaledHeight) {
        this.scaledHeight = scaledHeight;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void run() {
        try {
            // reads input image
            BufferedImage inputImage = ImageIO.read(inputFile);

            // Maintaining the aspect ratio
            int imgWidth = inputImage.getWidth();
            int imgHeight = inputImage.getHeight();
            if (imgWidth * scaledHeight < imgHeight * scaledWidth) {
                scaledWidth = imgWidth * scaledHeight / imgHeight;
            } else {
                scaledHeight = imgHeight * scaledWidth / imgWidth;
            }

            // creates output image
            BufferedImage outputImage = new BufferedImage(
                    scaledWidth, scaledHeight, inputImage.getType());

            // scales the input image to the output image
            Graphics2D g2d = outputImage.createGraphics();
            try{
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.clearRect(0, 0, scaledWidth, scaledHeight);
                g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
            }catch (Exception e){

            }finally {
                g2d.dispose();
            }

            String outputImagePath = this.getInputFile().getName();

            // extracts extension of output file
//            format  = outputImagePath.substring(outputImagePath
//                    .lastIndexOf(".") + 1);

            this.outputFile = new File(this.getOutputFileName()+this.getFileName());
            // writes to output file
            ImageIO.write(outputImage, format, this.getOutputFile());
        } catch (IOException io) {

        }
    }

    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     *
     * @param inputImagePath  Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param scaledWidth     absolute width in pixels
     * @param scaledHeight    absolute height in pixels
     * @throws IOException
     */
    public static void resize(String inputImagePath,
                              String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    /**
     * Resizes an image by a percentage of original size (proportional).
     *
     * @param inputImagePath  Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param percent         a double number specifies percentage of the output image
     *                        over the input image.
     * @throws IOException
     */
    public static void resize(String inputImagePath,
                              String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }
}
