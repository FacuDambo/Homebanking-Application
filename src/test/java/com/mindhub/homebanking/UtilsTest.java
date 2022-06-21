package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class UtilsTest {

    @Test
    public void cardNumber(){
        long cardNumber = Utils.getRandomLong(3000000000000000L, 5999999999999999L);
        assertThat(cardNumber,is(not(equalTo(0))));
    }

    @Test
    public void cardCVV(){
        int cardNumber = Utils.getRandomInteger(100, 999);
        assertThat(cardNumber, is(greaterThan(0)));
    }
}
