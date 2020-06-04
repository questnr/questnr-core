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
import java.util.Date;

public class ImageCompression {

    private File inputFile;

    public ImageCompression(File inputFile) {
        this.inputFile = inputFile;
    }

    private String generateFileName(String fileName) {
        return new Date().getTime() + "-" + fileName.replace(" ", "_");
    }

    public File doCompression() throws Exception {
        if (this.inputFile == null) throw new FileNotFoundException();

        BufferedImage image = ImageIO.read(inputFile);

        File compressedImageFile = new File(this.inputFile.getName());
        OutputStream os = new FileOutputStream(compressedImageFile);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.05f);
        }

        writer.write(null, new IIOImage(image, null, null), param);

        os.close();
        ios.close();
        writer.dispose();
        return compressedImageFile;
    }
}
