package com.github.lordcrekit.tagstats.fetching.e621;

import com.github.lordcrekit.tagstats.fetching.BrowseParseResult;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URI;

/**
 * Parses pages on e621
 */
class Parsing {

    static final String WEB_ROOT = "https://e621.net";

    static BrowseParseResult parseBrowsing(final String html) throws URISyntaxException, IOException {
        final org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(html);

        final Elements links = doc.select("div span.thumb a");
        final URI[] pages = new URI[links.size()];
        for (int i = 0; i < links.size(); i++) {
            final Element e = links.get(i);
            pages[i] = new URI(WEB_ROOT + e.attr("href"));
        }

        final URI next;
        Elements nextPage = doc.select("a.next_page");
        if (nextPage != null && nextPage.size() > 0) {
            next = new URI(WEB_ROOT + nextPage.get(0).attr("href"));
        } else {
            nextPage = doc.select("div[id=paginator] a");
            next = new URI(WEB_ROOT + nextPage.get(1).attr("href"));
        }

        return new BrowseParseResult(next, pages);
    }
}
