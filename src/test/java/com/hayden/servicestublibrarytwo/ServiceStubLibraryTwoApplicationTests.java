package com.hayden.servicestublibrarytwo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ActiveProfiles("service")
@ExtendWith(SpringExtension.class)
class ServiceStubLibraryTwoApplicationTests {

    @Autowired
    ServiceToStub serviceToStub;

    @Test
    void contextLoads()
    {
        Assertions.assertEquals(serviceToStub.getClass().getSimpleName(), ServiceStubbed.class.getSimpleName());
    }

}
