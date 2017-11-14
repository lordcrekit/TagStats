package com.github.lordcrekit.tagstats.fetching;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

public final class BrowseParseResult {

    /**
     * The next browse page.
     */
    public final URI Next;

    /**
     * The actual media pages linked to by this browse page.
     */
    public final URI[] Pages;

    public BrowseParseResult(final URI next, final URI[] pages) {
        this.Next = next;
        this.Pages = Objects.requireNonNull(pages);
    }

    public BrowseParseResult(final JSONObject json) throws URISyntaxException {
        this.Next = !json.has("Next") ? null : new URI(json.getString("Next"));

        final JSONArray p = json.getJSONArray("Pages");
        this.Pages = new URI[p.length()];
        for (int i = 0; i < p.length(); i++)
            this.Pages[i] = new URI(p.getString(i));
    }

    /**
     * Write this browse result to a JSON object.
     *
     * @return This as a JSON object.
     */
    public JSONObject toJSON() {
        final JSONObject out = new JSONObject();
        if (this.Next != null)
            out.put("Next", this.Next.toString());

        final JSONArray p = new JSONArray();
        for (int i = 0; i < this.Pages.length; i++)
            p.put(this.Pages[i].toString());
        out.put("Pages", p);

        return out;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof BrowseParseResult ? equals((BrowseParseResult) o) : false;
    }

    private boolean equals(final BrowseParseResult o) {
        return (Next == null ? o.Next == null : Next.equals(o.Next))
                && Arrays.equals(Pages, o.Pages);
    }

    @Override
    public String toString() {
        return this.toJSON().toString(2);
    }
}
