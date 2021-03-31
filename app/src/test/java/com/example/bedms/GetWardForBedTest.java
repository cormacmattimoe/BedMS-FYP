package com.example.bedms;

import com.google.firebase.firestore.FirebaseFirestore;

import junit.framework.Test;
import junit.framework.TestCase;


import org.junit.Assert;

public class GetWardForBedTest extends TestCase {



    public void testGetWard() {
        GetWardForBed gwb = new GetWardForBed();
        String ward = gwb.GetWard("0s9DBoYTnK3rd1jvojEl");
        assertEquals("St Joes", ward);
    }
}