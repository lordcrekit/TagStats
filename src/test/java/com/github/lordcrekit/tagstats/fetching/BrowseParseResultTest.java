package com.github.lordcrekit.tagstats.fetching;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public final class BrowseParseResultTest {

    /**
     * Tests all IO.
     */
    @Test
    public void toJSON() throws MalformedURLException {
        final URL google = new URL("https://www.google.com");
        final URL yahoo = new URL("https://www.yahoo.com");
        final URL[] google_yahoo = new URL[]{google, yahoo};

        final BrowseParseResult orig = new BrowseParseResult(google, google_yahoo);
        final BrowseParseResult copy = new BrowseParseResult(orig.toJSON());

        Assert.assertEquals(orig, copy);
    }

    @Test
    public void equals() throws MalformedURLException {
        final URL google = new URL("https://www.google.com");
        final URL yahoo = new URL("https://www.yahoo.com");
        final URL bing = new URL("https://www.bing.com");

        final URL[] google_yahoo = new URL[]{google, yahoo};
        final URL[] yahoo_bing = new URL[]{yahoo, bing};

        BrowseParseResult o1 = new BrowseParseResult(google, google_yahoo);
        BrowseParseResult o2 = new BrowseParseResult(google, google_yahoo);
        Assert.assertEquals(o1, o2);
        o2 = new BrowseParseResult(bing, google_yahoo);
        Assert.assertNotEquals(o1, o2);
        Assert.assertNotEquals(o2, o1);
        o1 = new BrowseParseResult(bing, google_yahoo);
        Assert.assertEquals(o1, o2);

        o2 = new BrowseParseResult(bing, yahoo_bing);
        Assert.assertNotEquals(o1, o2);
        Assert.assertNotEquals(o2, o1);
    }
}