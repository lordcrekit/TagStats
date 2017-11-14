package com.github.lordcrekit.tagstats;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class PageTest {

    final static Map<String, Map<TagCategory, List<Tag>>> testCases;

    // <editor-fold desc="Generate jsonIO test cases">
    static {
        testCases = new LinkedHashMap<>();
        {   // Empty
            testCases.put("", new HashMap<>());
        }
        {   // With name
            testCases.put("Name-Only", new HashMap<>());
        }
        {   // With tags
            final Map<TagCategory, List<Tag>> m = new HashMap<>();
            m.put(
                    new TagCategory("Author"),
                    Arrays.asList(
                            new Tag("Besty123"),
                            new Tag("Alpha356")
                    )
            );

            testCases.put("SingleTagCat", m);
        }
        {   // With multiple tags
            final Map<TagCategory, List<Tag>> m = new HashMap<>();
            m.put(
                    new TagCategory("Author"),
                    Arrays.asList(
                            new Tag("Besty123"),
                            new Tag("Alpha356")
                    )
            );
            m.put(
                    new TagCategory("Flags"),
                    Arrays.asList(
                            new Tag("Tagy1"),
                            new Tag("Tagy2")
                    )
            );

            testCases.put("TwoTagCats", m);
        }
    }
    // </editor-fold>

    /**
     * Makes sure equals is correct. If this doesn't work, other tests can't be trusted.
     */
    @Test
    public void testEquals() {
        System.out.println("Test #equals()");

        final Map<TagCategory, List<Tag>> emptyTags = Collections.unmodifiableMap(new HashMap<>());

        final Map<TagCategory, List<Tag>> basicTags01 = new LinkedHashMap<>();
        basicTags01.put(
                new TagCategory("Author"),
                Arrays.asList(
                        new Tag("Iron"),
                        new Tag("Copper")
                )
        );

        final Map<TagCategory, List<Tag>> basicTags02 = new LinkedHashMap<>();
        basicTags02.put(
                new TagCategory("Author"),
                Arrays.asList(
                        new Tag("Jessie"),
                        new Tag("Beta")
                )
        );

        Assert.assertNotEquals(basicTags01, basicTags02);

        final Map<TagCategory, List<Tag>> basicTags02_identical = new LinkedHashMap<>();
        basicTags02_identical.put(
                new TagCategory("Author"),
                Arrays.asList(
                        new Tag("Jessie"),
                        new Tag("Beta")
                )
        );

        Page o1;
        Page o2;

        // Both empty
        o1 = new Page("", emptyTags);
        o2 = new Page("", emptyTags);
        Assert.assertEquals("Empty", o1, o2);

        // Different name
        o1 = new Page("Name", emptyTags);
        Assert.assertNotEquals("Name-only (fail)", o1, o2);
        Assert.assertNotEquals("Name-only (fail)", o2, o1);
        o2 = new Page("Name", emptyTags);
        Assert.assertEquals("Name-only", o1, o2);

        // Different tags
        o1 = new Page("Name", basicTags01);
        o2 = new Page("Name", emptyTags);
        Assert.assertNotEquals("Tags, one empty (fail)", o1, o2);
        Assert.assertNotEquals("Tags, one empty (fail)", o2, o1);

        o2 = new Page("Name", basicTags02);
        Assert.assertNotEquals("Tags, different (fail 1)", o1, o2);
        Assert.assertNotEquals("Tags, different (fail 2)", o2, o1);
        o2 = new Page("Name", basicTags01);
        Assert.assertEquals("Tags, same", o1, o2);

        o1 = new Page("Name", basicTags02);
        o2 = new Page("Name", basicTags02_identical);
        Assert.assertEquals("Identically created", o1, o2);
    }

    /**
     * Makes sure it correctly reads/writes to json.
     */
    @Test
    public void testToJson() {
        System.out.println("Test #toJson()");

        for (Map.Entry<String, Map<TagCategory, List<Tag>>> testCase : testCases.entrySet()) {
            final String title = testCase.getKey();
            final Map<TagCategory, List<Tag>> tags = testCase.getValue();
            System.out.println("\tcase: " + title);

            final Page expected = new Page(title, tags);

            // Test str8 json no loading
            final Page loaded_memory = new Page(expected.toJson());
            Assert.assertEquals(loaded_memory, expected);

            // Test with actual loading
            final Page loaded_io = new Page(new JSONObject(new JSONTokener(expected.toJson().toString())));
            Assert.assertEquals(loaded_io, expected);
        }
    }
}