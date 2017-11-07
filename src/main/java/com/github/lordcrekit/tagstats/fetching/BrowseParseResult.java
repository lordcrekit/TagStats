package com.github.lordcrekit.tagstats.fetching;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public final class BrowseParseResult {

    /**
     * The next browse page.
     */
    public final URL Next;

    /**
     * The actual media pages linked to by this browse page.
     */
    public final URL[] Pages;

    public BrowseParseResult(final URL next, final URL[] pages) {
        this.Next = next;
        this.Pages = Objects.requireNonNull(pages);
    }

    public BrowseParseResult(final JSONObject json) throws MalformedURLException {
        this.Next = !json.has("Next") ? null : new URL(json.getString("Next"));

        final JSONArray p = json.getJSONArray("Pages");
        this.Pages = new URL[p.length()];
        for (int i = 0; i < p.length(); i++)
            this.Pages[i] = new URL(p.getString(i));
    }

    public JSONObject toJSON() {
        final JSONObject out = new JSONObject();
        out.put("Next", this.Next.toString());

        final JSONArray p = new JSONArray();
        for (int i = 0; i < this.Pages.length; i++)
            p.put(this.Pages[i].toString());
        out.put("Pages", p);

        return out;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BrowseParseResult ? equals((BrowseParseResult) o) : false;
    }

    public boolean equals(BrowseParseResult o) {
        return Next == null ? o.Next == null : Next.equals(o.Next)
                && Pages.equals(o.Pages);
    }

    @Override
    public String toString() {
        return this.toJSON().toString(2);
    }
}
