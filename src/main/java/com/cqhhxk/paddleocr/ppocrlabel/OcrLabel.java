package com.cqhhxk.paddleocr.ppocrlabel;

import lombok.Data;

/**
 * OcrLabel
 * @author george
 */
@Data
public class OcrLabel {
    private String imageFile;
    private OcrAnnotation[] annotations;

    @Data
    public static class OcrAnnotation {
        private String transcription;
        private Integer[][] points;
        private Boolean difficult;
        private String key_cls;
    }
}
