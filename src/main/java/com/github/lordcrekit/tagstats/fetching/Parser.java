package com.github.lordcrekit.tagstats.fetching;

import com.github.lordcrekit.tagstats.Page;

import java.io.IOException;

public interface Parser {

    BrowseParseResult parseBrowse(final String html) throws IOException;

    Page parsePage(final String html) throws IOException;
}
