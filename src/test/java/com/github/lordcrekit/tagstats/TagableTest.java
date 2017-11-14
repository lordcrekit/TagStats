package com.github.lordcrekit.tagstats;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagableTest {

    @Test
    public void testEquals() {
        Tag o1;
        Tag o2;

        o1 = new Tag("");
        o2 = new Tag("");
        Assert.assertEquals(o1, o2);
    }

    @Test
    public void testBullshit() {
        Map<String, String> o1 = new HashMap<>();
        Map<String, String> o2 = new HashMap<>();

        o1.put("Alpha", "Beta");
        o2.put("Alpha", "Beta");
        Assert.assertEquals(o1, o2);
    }
}