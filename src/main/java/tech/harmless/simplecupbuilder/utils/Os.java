package tech.harmless.simplecupbuilder.utils;

public final class Os {

    public enum EnumOs {
        WINDOWS,
        LINUX,
        MACOS
    }

    public static EnumOs getOs() {
        String os = System.getProperty("os.name");

        if(os.toLowerCase().contains("win"))
            return EnumOs.WINDOWS;

        if(os.toLowerCase().contains("mac"))
            return EnumOs.MACOS;

        return EnumOs.LINUX; // Defaults to Linux.
    }

    public static String getPathName() {
        EnumOs os = getOs();

        if(os == EnumOs.WINDOWS)
            return "Path";

        return "PATH";
    }

    public static String getPathSep() {
        EnumOs os = getOs();

        if(os == EnumOs.WINDOWS)
            return ";";

        return ":";
    }
}
