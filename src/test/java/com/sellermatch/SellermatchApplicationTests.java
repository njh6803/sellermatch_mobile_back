package com.sellermatch;

import com.sellermatch.config.constant.MemberType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SellermatchApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(MemberType.SELLER.label);
        System.out.println(MemberType.SELLER.name());
    }

}
