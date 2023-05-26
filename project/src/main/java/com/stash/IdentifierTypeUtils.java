package com.stash;
public class IdentifierTypeUtils {
    private static final String CHECKSUM_STRING = "MD5";
    private static final String HASH_STRING = "OSHASH";
    private static final String PHASH_STRING = "PHASH";

    public static IdentifierType GetIndentifierType(String s) {
        if(s.equalsIgnoreCase(CHECKSUM_STRING)) {
            return IdentifierType.CHECKSUM;
        }
        else if(s.equalsIgnoreCase(HASH_STRING)) {
            return IdentifierType.HASH;
        }
        else if(s.equalsIgnoreCase(PHASH_STRING)) {
            return IdentifierType.PHASH;
        }
        return null;
    }
}
