package com.github.lordcrekit.tagstats;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public final class Page {
    public final String Title;
    public final Map<TagCategory, List<Tag>> Tags;

    public Page(final String title, final Map<TagCategory, List<Tag>> tags) {
        this.Title = Objects.requireNonNull(title);

        final HashMap<TagCategory, List<Tag>> tempTags = new HashMap<>();
        for (final Map.Entry<TagCategory, List<Tag>> e : tags.entrySet())
            tempTags.put(e.getKey(), Collections.unmodifiableList(e.getValue()));

        this.Tags = Collections.unmodifiableMap(tempTags);

        System.out.println(tags.toString());
        System.out.println(Tags.toString());
    }

    public Page(final JSONObject json) {
        Title = json.getString("Title");

        final Map<TagCategory, List<Tag>> rslt;
        {
            rslt = new HashMap<>();
            final JSONObject tagsO = json.getJSONObject("Tags");
            for (Iterator<String> i = tagsO.keys(); i.hasNext(); ) {
                final String catName = i.next();
                final TagCategory category = new TagCategory(catName);

                final List<Tag> tagL = new ArrayList<>();
                final JSONArray tagsO2 = tagsO.getJSONArray(catName);
                for (int j = 0; j < tagsO2.length(); j++) {
                    final String tagName = tagsO2.getString(j);
                    tagL.add(new Tag(tagName));
                }

                rslt.put(category, Collections.unmodifiableList(tagL));
            }
        }
        Tags = Collections.unmodifiableMap(rslt);
    }

    public JSONObject toJson() {
        final JSONObject output = new JSONObject();

        output.put("Title", Title);

        final JSONObject tagsObject = new JSONObject();
        for (final Map.Entry<TagCategory, List<Tag>> e : Tags.entrySet()) {
            final TagCategory tagCat = e.getKey();
            final List<Tag> tags = e.getValue();

            final JSONArray a = new JSONArray();
            for (final Tag tag : tags)
                a.put(tag.Name);

            tagsObject.put(tagCat.Name, a);
        }
        output.put("Tags", tagsObject);

        return output;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Page ? equals((Page) o) : false;
    }

    public boolean equals(Page o) {
        if (!Title.equals(o.Title))
            return false;

        final Set<TagCategory> c = Tags.keySet();
        final Set<TagCategory> oc = o.Tags.keySet();

        if (!c.equals(oc)) {
//            if (c.size() == 1 && oc.size() == 1) {
//                System.out.println("okay...");
//                if (!c.iterator().next().equals(oc.iterator().next())) {
//                    System.out.println("dafuq" + c.iterator().next());
//                    System.out.println("dafuq" + oc.iterator().next());
//                    assert false; // What the fuck
//                }
//            }
//            System.out.println(c.toString() + oc.toString());
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 47 * Title.hashCode()
                + 47 * Tags.hashCode();
    }

    @Override
    public String toString() {
        return this.toJson().toString(2);
    }
}
