package com.questnr.services.ImageResize;

import com.questnr.util.ImageResizer;

import java.nio.file.Paths;

public class ImageResizeJobRequest {
    private ImageResizer imageResizer;
    private String pathToDir;
    private String format;

    public ImageResizer getImageResizer() {
        return imageResizer;
    }

    public void setImageResizer(ImageResizer imageResizer) {
        this.imageResizer = imageResizer;
    }

    public String getPathToDir() {
        return pathToDir;
    }

    public void setPathToDir(String pathToDir) {
        this.pathToDir = pathToDir;
    }

    public String getPathToFile(){
        return Paths.get(pathToDir, imageResizer.getOutputFile().getName()).toString();
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
