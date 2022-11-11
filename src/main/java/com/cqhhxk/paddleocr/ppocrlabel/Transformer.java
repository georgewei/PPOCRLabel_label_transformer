package com.cqhhxk.paddleocr.ppocrlabel;

import com.cqhhxk.utils.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * PPOCRLabel Transformer
 * @author george
 */
public class Transformer {
    private final TransformerArgs args;
    /**
     * If extending image canvas required
     */
    private boolean extendImageCanvasRequired;
    /**
     * Target folder to save transformed label file and modified images
     */
    private final String targetDir;

    public Transformer(TransformerArgs args) {
        this.args = args;
        extendImageCanvasRequired = args.getWidth() > 0 && args.getHeight() > 0;
        targetDir = extendImageCanvasRequired ? args.getOutputDir() : args.getCroppedImageDir();
    }

    /**
     * Transform label file
     */
    public void transform() {
        List<String> imageFiles = listCroppedImageFiles(args.getCroppedImageDir());
        List<OcrLabel> srcLabels = loadLabels(args.getLabelFile());
        if (imageFiles.size() > 0 && srcLabels.size() > 0) {
            List<OcrLabel> interLabels = new ArrayList<>();
            for (OcrLabel srcLabel: srcLabels) {
                String originalImageFilename = FileUtils.extractFilename(srcLabel.getImageFile());
                List<String> interImageFiles =
                        imageFiles.stream()
                                .filter(s -> s.startsWith(originalImageFilename + "_crop_"))
                                .sorted((a, b) -> {
                                    int indexA = getCroppedImageIndex(a);
                                    int indexB = getCroppedImageIndex(b);
                                    return indexA - indexB;
                                })
                                .collect(Collectors.toList());
                if (interImageFiles.size() == srcLabel.getAnnotations().length) {
                    for (int i = 0; i < interImageFiles.size(); i++) {
                        OcrLabel interLabel = new OcrLabel();
                        interLabel.setImageFile(interImageFiles.get(i));

                        OcrLabel.OcrAnnotation srcAnnotation = srcLabel.getAnnotations()[i];
                        OcrLabel.OcrAnnotation interAnnotation = new OcrLabel.OcrAnnotation();
                        interAnnotation.setTranscription(srcAnnotation.getTranscription());
                        interAnnotation.setPoints(getTransformedPoints(interLabel.getImageFile()));
                        interAnnotation.setDifficult(srcAnnotation.getDifficult());
                        interAnnotation.setKey_cls(srcAnnotation.getKey_cls());

                        OcrLabel.OcrAnnotation[] interAnnotations = new OcrLabel.OcrAnnotation[1];
                        interAnnotations[0] = interAnnotation;

                        interLabel.setAnnotations(interAnnotations);

                        interLabels.add(interLabel);
                    }
                } else {
                    System.out.println(String.format("Cropped image count does not match labeled annotation count of original image file %s", srcLabel.getImageFile()));
                }
            }
            List<String> targetLabels = new ArrayList<>();
            toStrings(interLabels, targetLabels);
            String targetLabelFile = getTargetLabelFile();
            saveLabels(targetLabelFile, targetLabels);
        }
    }

    /**
     * List files in cropped image directory
     * @param croppedImageDir Cropped image directory
     * @return List of filenames
     */
    private @NotNull List<String> listCroppedImageFiles(@NotNull String croppedImageDir) {
        List<String> imageFiles = new ArrayList<>();
        File f = new File(croppedImageDir);
        if (!f.exists()) {
            System.out.println(String.format("Folder %s not found", croppedImageDir));
        } else if (!f.isDirectory()) {
            System.out.println(String.format("%s is not a directory", croppedImageDir));
        } else {
            imageFiles = Arrays.stream(Objects.requireNonNull(f.list())).collect(Collectors.toList());
        }
        return imageFiles;
    }

    /**
     * Load labels from label file
     * @param labelFile PPOCRLabel label file
     * @return List of PPOCRLabel labels
     */
    private @NotNull List<OcrLabel> loadLabels(@NotNull String labelFile) {
        List<OcrLabel> labels = new ArrayList<>();
        File f = new File(labelFile);
        if (!f.exists()) {
            System.out.println(String.format("File %s not found", labelFile));
        } else if (!f.isFile()) {
            System.out.println(String.format("%s is not a file", labelFile));
        } else {
            try {
                List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
                toOcrLabels(lines, labels);
            } catch (IOException e) {
                System.out.println(String.format("Error on reading file %s: %s", labelFile, e.getMessage()));
            }
        }
        return labels;
    }

    /**
     * Deserialize labels
     * @param src text of labels
     * @param target labels
     */
    private void toOcrLabels(@NotNull List<String> src, @NotNull List<OcrLabel> target) {
        ObjectMapper mapper = new ObjectMapper();
        for (String srcLabel: src) {
            String[] split = srcLabel.split("\t");
            if (split.length > 1) {
                OcrLabel targetLabel = new OcrLabel();
                targetLabel.setImageFile(split[0]);

                try {
                    OcrLabel.OcrAnnotation[] ocrAnnotations = mapper.readValue(split[1], OcrLabel.OcrAnnotation[].class);
                    targetLabel.setAnnotations(ocrAnnotations);
                } catch (JsonProcessingException e) {
                    targetLabel.setAnnotations(new OcrLabel.OcrAnnotation[0]);
                    e.printStackTrace();
                }
                target.add(targetLabel);
            } else {
                System.out.println(String.format("Invalid label: %s", srcLabel));
            }
        }
    }

    /**
     * Serialize labels
     * @param src labels
     * @param target text of labels
     */
    private void toStrings(@NotNull List<OcrLabel> src, @NotNull List<String> target) {
        ObjectMapper mapper = new ObjectMapper();
        for (OcrLabel srcLabel: src) {
            try {
                String targetLabel = srcLabel.getImageFile() + "\t" + mapper.writeValueAsString(srcLabel.getAnnotations());
                target.add(targetLabel);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get file index of cropped image
     * @param filename Cropped image filename
     * @return File index
     */
    private int getCroppedImageIndex(@NotNull String filename) {
        filename = FileUtils.extractFilename(filename);
        int indexOfLastUnderscore = filename.lastIndexOf("_");
        String imageIndex = filename.substring(indexOfLastUnderscore + 1);
        try {
            return Integer.parseInt(imageIndex);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    /**
     * Get transformed label points
     * @param imageFile Cropped image filename
     * @return Label points
     */
    private Integer[][] getTransformedPoints(@NotNull String imageFile) {
        int width = 0;
        int height = 0;

        BufferedImage image;
        try {
            image = ImageIO.read(new File(FileUtils.jointPath(args.getCroppedImageDir(), imageFile)));
            width = image.getWidth();
            height = image.getHeight();
            if (extendImageCanvasRequired) {
                extendImageCanvas(image, imageFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer[][] points = new Integer[4][2];
        points[0][0] = 0;
        points[0][1] = 0;
        points[1][0] = 0;
        points[1][1] = height;
        points[2][0] = width;
        points[2][1] = height;
        points[3][0] = width;
        points[3][1] = 0;

        return points;
    }

    /**
     * Extend image canvas
     * @param src Source image
     * @param filename Source image's filename
     * @throws IOException exception on writing image to disk
     */
    private void extendImageCanvas(@NotNull BufferedImage src, @NotNull String filename) throws IOException {
        int width = Math.max(src.getWidth(), args.getWidth());
        int height = Math.max(src.getHeight(), args.getHeight());
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = target.createGraphics();
        graphics.drawImage(src, 0, 0, null);
        graphics.dispose();

        String targetFile = FileUtils.jointPath(targetDir, filename);
        String formatName = filename.substring(filename.lastIndexOf(".") + 1);
        ImageIO.write(target, formatName, new File(targetFile));
    }

    /**
     * Get target label file path
     * @return Label file path
     */
    private String getTargetLabelFile() {
        String targetLabelFile = FileUtils.extractFilename(args.getLabelFile(), true);
        String prefix = FileUtils.extractFilename(targetLabelFile);
        targetLabelFile = targetLabelFile.replace(prefix, prefix + "_transformed");

        return FileUtils.jointPath(targetDir, targetLabelFile);
    }

    /**
     * Save transformed labels
     * @param labelFile target label file path
     * @param labels text of labels
     */
    private void saveLabels(@NotNull String labelFile, @NotNull List<String> labels) {
        File f = new File(labelFile);
        try {
            Files.write(f.toPath(), labels);
            System.out.println(String.format("Transformed label file is saved to %s", labelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
