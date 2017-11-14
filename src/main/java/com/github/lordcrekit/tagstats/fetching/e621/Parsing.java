package com.github.lordcrekit.tagstats.fetching.e621;

import com.github.lordcrekit.tagstats.Page;
import com.github.lordcrekit.tagstats.Tag;
import com.github.lordcrekit.tagstats.TagCategory;
import com.github.lordcrekit.tagstats.fetching.BrowseParseResult;
import com.github.lordcrekit.tagstats.fetching.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.*;

/**
 * Parses pages on e621
 */
class Parsing implements Parser {

    static final String WEB_ROOT = "https://e621.net";

    @Override
    public Page parsePage(final String html) {

        final String title;
        final Map<TagCategory, List<Tag>> tags = new LinkedHashMap<>();

        // <editor-fold>
        final Document doc = Jsoup.parse(html);

        title = Objects.requireNonNull(doc.head().select("title").text());

        final Element tagsSidebar = doc.select("ul[id=tag-sidebar]").first();

        final Elements categoryElements = tagsSidebar
                .select("li[id*=\"category-\"]")
                .not("li[style*=\"display\"]");

        for (int i = 0; i < categoryElements.size(); i++) {
            final Element e = categoryElements.get(i);

            final String categoryID = Objects.requireNonNull(e.attr("id").substring("category-".length()));
            final TagCategory category = new TagCategory(e.text());
            System.out.println(category);

            final Elements tagsInCategoryElements = tagsSidebar
                    .select("li[class=tag-type-" + categoryID + "]")
                    .select("a")
                    .not("a[style]");

            final List<Tag> tagsL = new ArrayList<>();
            for (int j = 0; j < tagsInCategoryElements.size(); j++)
                tagsL.add(new Tag(tagsInCategoryElements.get(j).text()));

            tags.put(category, tagsL);
        }
        // </editor-fold>

        return new Page(title, tags);
    }

    @Override
    public BrowseParseResult parseBrowse(final String html) throws IOException {
        try {
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
        } catch (URISyntaxException e) {
            throw new IOException("Failed to parse uri", e);
        }
    }
}
