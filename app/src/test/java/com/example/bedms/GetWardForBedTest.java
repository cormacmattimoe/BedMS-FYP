package com.example.bedms;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GetWardForBedTest extends TestCase {
    @org.junit.Test
        public static void main (String[] args) {
            junit.textui.TestRunner.run (suite());
        }

        public static junit.framework.Test suite() {
            return new TestSuite(GetWardForBedTest.class);
        }

    @org.junit.Test
    public void testGetWard() {
        GetWardForBed gwb = new GetWardForBed();
        String ward = gwb.GetWard("0s9DBoYTnK3rd1jvojEl");
        assertEquals("St Joes", ward);
    }
}

