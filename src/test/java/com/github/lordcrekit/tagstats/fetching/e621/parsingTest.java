package com.github.lordcrekit.tagstats.fetching.e621;

import com.github.lordcrekit.tagstats.Page;
import com.github.lordcrekit.tagstats.fetching.BrowseParseResult;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class parsingTest {

    final Parsing parser = new Parsing();

    @Test
    public void testParsePage() throws URISyntaxException, IOException {
        System.out.println("Test parsePage(STRING)");

        final Path testCases = Paths.get( parsingTest.class.getResource("examples/page").toURI() );
        for ( final Path p : Files.list(testCases).collect(Collectors.toList())) {
            System.out.println("\t" + p.getFileName());

            final String testHtml = new String(Files.readAllBytes(Paths.get(p.toString(), "source.html")));
            final Page got = parser.parsePage(testHtml);

            final Path expectedP = Paths.get(p.toString(), "expected.json");
            if (Files.exists(expectedP)) {
                final Page expected;
                try (InputStream is = Files.newInputStream(expectedP)) {
                    expected = new Page(new JSONObject(new JSONTokener(is)));
                }
                Assert.assertEquals(got, expected);
            } else {
                Files.write(Paths.get(expectedP.getFileName().toString()), got.toJson().toString(2).getBytes());
                Assert.fail("Manually check " + expectedP.getFileName());
            }
        }
    }

    @Test
    public void testParseBrowsing() throws IOException, URISyntaxException {
        System.out.println("Test parseBrowsing(STRING)");

        final Path testCases = Paths.get( parsingTest.class.getResource("examples/browse").toURI() );

        for (final Path p : Files.list(testCases).collect(Collectors.toList())) {
            System.out.println("\t" + p.getFileName());

            final String testHtml = new String(Files.readAllBytes(Paths.get(p.toString(), "source.html")));
            final BrowseParseResult got = parser.parseBrowse(testHtml);

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