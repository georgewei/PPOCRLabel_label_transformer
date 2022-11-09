package com.cqhhxk.utils;

import jakarta.validation.constraints.NotNull;

/**
 * FileUtils
 * @author george
 */
public class FileUtils {
    /**
     * Extract filename without extension from given path
     * @param path File path
     * @return Filename without extension
     */
    public static String extractFilename(String path) {
        return extractFilename(path, false);
    }

    /**
     * Extract filename from given path
     * @param path File path
     * @param keepExtension If keep file extension
     * @return Filename
     */
    public static String extractFilename(String path, boolean keepExtension) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        int indexOfPathSeparator = path.lastIndexOf("/");
        if (indexOfPathSeparator > 0) {
            path = path.substring(indexOfPathSeparator + 1);
        }
        if (!keepExtension) {
            int indexOfExtSeparator = path.lastIndexOf(".");
            if (indexOfExtSeparator > 0) {
                path = path.substring(0, indexOfExtSeparator);
            }
        }
        return path;
    }

    /**
     * Extract directory from given path
     * @param path File path
     * @return Directory without trailing slash
     */
    public static String extractDirectory(String path) {
        return extractDirectory(path, false);
    }

    /**
     * Extract directory from given path
     * @param path File path
     * @param keepTrailingSlash If keep trailing slash
     * @return Directory
     */
    public static String extractDirectory(String path, boolean keepTrailingSlash) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        int indexOfPathSeparator = path.lastIndexOf("/");
        if (indexOfPathSeparator > 0) {
            if (keepTrailingSlash) {
                ++indexOfPathSeparator;
            }
            return path.substring(0, indexOfPathSeparator);
        }
        return "";
    }

    /**
     * Joint path
     * @param path Path or path prefix
     * @param filename Filename or path suffix
     * @return Jointed path
     */
    public static String jointPath(@NotNull String path, @NotNull String filename) {
        int pathLength = path.length();
        boolean hasTrailingSlash = "/".equals(path.substring(pathLength - 1));
        return path + (hasTrailingSlash ? "" : "/") + filename;
    }
}
