package com.realestate.math;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.realestate.math.interest.Interest;

public class InterestTest {

    @Test
    public void InterestTests() {
        assertEquals(1000.0, Interest.simpleInterest(1000, 0.1, 1), 0.0001);



    }
}
