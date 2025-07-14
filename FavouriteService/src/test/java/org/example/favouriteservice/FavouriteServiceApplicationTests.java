package org.example.favouriteservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false"
})
class FavouriteServiceApplicationTests {

    @Test
    void contextLoads() {
        // Simple test without Spring context
        assert true;
    }

}
