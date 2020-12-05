package tech.harmless.simplecupbuilder.utils;

import tech.harmless.simplecupbuilder.utils.enums.EnumExitCodes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Security {

    /**
     * Uses the SHA-256 algorithm in a unsecured way to generate a hash for comparison purposes.
     * @param input A {@code String} that is used to generate the hash.
     * @return A {@code String} that is the hashed version of the input.
     */
    public static String unsecureSha256(String input) {
        String generated = "";
        try {
            generated = unsecureGenerate("SHA-256", input);
        }
        catch(NoSuchAlgorithmException e) {
            Log.exception(e);
            Log.fatal(EnumExitCodes.NO_HASH, "The hashing algorithm SHA-256 does not exist.");
        }

        return generated;
    }

    /**
     * Uses the SHA-512 algorithm in a unsecured way to generate a hash for comparison purposes.
     * Fallbacks to the SHA-256 algorithm if SHA-512 is not found.
     * @param input A {@code String} that is used to generate the hash.
     * @return A {@code String} that is the hashed version of the input.
     */
    public static String unsecureSha512(String input) {
        String generated = "";
        try {
            generated = unsecureGenerate("SHA-512", input);
        }
        catch(NoSuchAlgorithmException e) {
            Log.warn("No algorithm SHA-512, falling back to SHA-256.");
            generated = unsecureSha256(input);
        }

        return generated;
    }

    private static String unsecureGenerate(String algorithm, String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return new String(hash, StandardCharsets.UTF_8);
    }
}
