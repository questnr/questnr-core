package com.questnr.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageCompression {

    private File inputFile;

    private String format;

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public File doCompression() throws Exception {
        if (this.inputFile == null) throw new FileNotFoundException();

        BufferedImage image = ImageIO.read(inputFile);

        File compressedImageFile = new File("out_" + this.inputFile.getName());
        OutputStream os = new FileOutputStream(compressedImageFile);

        String outputImagePath = this.inputFile.getName();
        // extracts extension of output file
        format = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.6f);
        }

        writer.write(null, new IIOImage(image, null, null), param);

        os.close();
        ios.close();
        writer.dispose();
        return compressedImageFile;
    }
}
