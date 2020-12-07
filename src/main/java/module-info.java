module tech.harmless.simplecupbuilder {
    // Third-Party Annotations
    requires static lombok;
    requires static org.jetbrains.annotations;
    requires static com.github.spotbugs.annotations;

    // Third-Party Libs
    requires com.github.jezza.toml;
    requires com.google.gson;

    // Java Libs
    requires java.base;

    // Exports
}