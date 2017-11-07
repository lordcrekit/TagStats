package com.github.lordcrekit.tagstats.fetching.e621;

import com.github.lordcrekit.tagstats.fetching.BrowseParseResult;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class parsingTest {

    /**
     * It's too hard to do this by hand. Make sure you look over the results so that you know they are correct!
     */
    public static void generateParseBrowsingResults() {

    }

    @Test
    public void testParseBrowsing() throws IOException, URISyntaxException {
        System.out.println("Test parseBrowsing(STRING)");

        final Path testCases = Paths.get( parsingTest.class.getResource("examples/browse").toURI() );
        for (final Path p : Files.list(testCases).collect(Collectors.toList())) {
            System.out.println("\t" + p.getFileName());

            final String testHtml = new String(Files.readAllBytes(Paths.get(p.toString(), "source.html")));
            final BrowseParseResult got = Parsing.parseBrowsing(testHtml);

            final Path expectedP = Paths.get(p.toString(), "expected.json");
            if (Files.exists(expectedP)) {
                // Expected results exist. Run test.
                final BrowseParseResult expected = new BrowseParseResult(
                        new JSONObject(new JSONTokener(new String(Files.readAllBytes(expectedP)))));
                Assert.assertEquals(expected, got);
            } else {
                // We haven't created the expected result yet. Create it for manual checking, and then fail.
                Files.write(Paths.get(p.getFileName() + ".json"), got.toJSON().toString(2).getBytes());
                Assert.fail("Check results, and add as `expected.json`");
            }
        }
    }
}