package com.cqhhxk.utils;

/**
 * StringUtils
 * @author george
 */
public class StringUtils {
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * Check if a CharSequence is blank
     * @param cs CharSequence
     * @return True if blank, or false if not
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * Check if a CharSequence is empty
     * @param cs CharSequence
     * @return True if empty, or false if not
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
