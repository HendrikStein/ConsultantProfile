package de.jastech;

import de.jastech.input.InputReader;
import de.jastech.model.Profile;
import de.jastech.output.MarkdownWriter;
import de.jastech.output.OutputWriter;
import de.jastech.output.WordWriter;

import java.io.FileNotFoundException;
import java.util.Locale;

/**
 * @author Hendrik Stein
 */
public class ProfileGenerator {

    private static final String DEFAULT_PROFILE = "hs";

    /**
     * Firestarter
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        final String profileName = args.length > 0 && args[0] != null ? args[0] : DEFAULT_PROFILE;
        final Locale locale = args.length > 1 && args[1] != null ? Locale.forLanguageTag(args[1]) : Locale.GERMANY;

        System.out.println("Profile Parameter: args[0] = " + profileName + " ; args[1]= " + locale);

        Profile profile = InputReader.readProfile("resources//" + profileName);
        OutputWriter markdownWriter = new MarkdownWriter(profile, locale);
        markdownWriter.write(profileName);

        OutputWriter wordWriter = new WordWriter(profile, locale);
        wordWriter.write(profileName);


    }

}
