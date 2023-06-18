package micro.app.restauranteurekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RestaurantEurekaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantEurekaServiceApplication.class, args);
    }

}
