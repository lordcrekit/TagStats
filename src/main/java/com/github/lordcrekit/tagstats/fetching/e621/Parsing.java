package com.github.lordcrekit.tagstats.fetching.e621;

import com.github.lordcrekit.tagstats.fetching.BrowseParseResult;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Parses pages on e621
 */
class Parsing {

    static final String WEB_ROOT = "https://e621.net";

    static BrowseParseResult parseBrowsing(final String html) throws URISyntaxException, IOException {
        final org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(html);

        final Elements links = doc.select("div span.thumb a");
        final URL[] pages = new URL[links.size()];
        for (int i = 0; i < links.size(); i++) {
            final Element e = links.get(i);
            pages[i] = new URL(WEB_ROOT + e.attr("href"));
        }

        final Elements nextPage = doc.select("a.next_page");
        final URL next = new URL(WEB_ROOT + nextPage.get(0).attr("href"));

        return new BrowseParseResult(next, pages);
    }
}
