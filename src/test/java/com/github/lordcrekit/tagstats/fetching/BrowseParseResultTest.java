package com.github.lordcrekit.tagstats.fetching;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public final class BrowseParseResultTest {

    /**
     * Tests all IO.
     */
    @Test
    public void toJSON() throws URISyntaxException {
        final URI google = new URI("https://www.google.com");
        final URI yahoo = new URI("https://www.yahoo.com");
        final URI[] google_yahoo = new URI[]{google, yahoo};

        final BrowseParseResult orig = new BrowseParseResult(google, google_yahoo);
        final BrowseParseResult copy = new BrowseParseResult(orig.toJSON());

        Assert.assertEquals(orig, copy);
    }

    @Test
    public void equals() throws URISyntaxException {
        final URI google = new URI("https://www.google.com");
        final URI yahoo = new URI("https://www.yahoo.com");
        final URI bing = new URI("https://www.bing.com");

        final URI[] google_yahoo = new URI[]{google, yahoo};
        final URI[] yahoo_bing = new URI[]{yahoo, bing};
        final URI[] google_yahoo_bing = new URI[]{google, yahoo, bing};

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

        o2 = new BrowseParseResult(yahoo, google_yahoo_bing);
        Assert.assertNotEquals(o1, o2);
        Assert.assertNotEquals(o2, o1);
        o1 = new BrowseParseResult(yahoo, google_yahoo_bing);
        Assert.assertEquals(o1, o2);
    }
}