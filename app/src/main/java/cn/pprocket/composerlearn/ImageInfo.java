package cn.pprocket.composerlearn;

import java.util.List;

@lombok.NoArgsConstructor
@lombok.Data
public class ImageInfo {

    private String error;
    private List<Data> data;

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class Data {
        private Integer pid;
        private Integer p;
        private Integer uid;
        private String title;
        private String author;
        private Boolean r18;
        private Integer width;
        private Integer height;
        private String ext;
        private Integer aiType;
        private Long uploadDate;
        private Urls urls;
        private List<String> tags;

        @lombok.NoArgsConstructor
        @lombok.Data
        public static class Urls {
            private String original;
        }
    }
}
