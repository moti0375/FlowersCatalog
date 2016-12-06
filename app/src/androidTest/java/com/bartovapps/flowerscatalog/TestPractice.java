package com.bartovapps.flowerscatalog;

import android.test.AndroidTestCase;

/**
 * Created by BartovMoti on 08/08/15.
 */
public class TestPractice extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testThatDemonstrateAssertion () throws Throwable{
        int a = 5;
        int b = 3;
        int c = 5;
        int d = 10;

        assertEquals("A = " + a + ", C = " + c + " should be equal", a, c);
        assertTrue("X should be true", d > c);
        assertFalse("Y should be false", a == b);
    }


}
