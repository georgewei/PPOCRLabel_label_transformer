package com.cqhhxk.paddleocr.ppocrlabel;

import lombok.Data;

/**
 * Parsed PPOCRLabel Transformer CommandLine Arguments
 * @author george
 */
@Data
public class TransformerArgs {
    /**
     * The folder contains cropped images generated by PPOCRLabel
     */
    private String croppedImageDir;
    /**
     * The label file saved by PPOCRLabel
     */
    private String labelFile;
    /**
     * The new width of canvas (px)
     */
    private int width;
    /**
     * The new height of canvas (px)
     */
    private int height;
    /**
     * The folder to save modified images
     */
    private String outputDir;
}
