package tech.harmless.simplecupbuilder.utils;

public final class OS {

    public enum EnumOS {
        WINDOWS,
        LINUX,
        MACOS
    }

    public static EnumOS getOs() {
        String os = System.getProperty("os.name");

        if(os.toLowerCase().contains("win"))
            return EnumOS.WINDOWS;

        if(os.toLowerCase().contains("mac"))
            return EnumOS.MACOS;

        return EnumOS.LINUX; // Defaults to Linux.
    }

    public static String getPathName() {
        EnumOS os = getOs();

        if(os == EnumOS.WINDOWS)
            return "Path";

        return "PATH";
    }

    public static String getPathSep() {
        EnumOS os = getOs();

        if(os == EnumOS.WINDOWS)
            return ";";

        return ":";
    }
}
