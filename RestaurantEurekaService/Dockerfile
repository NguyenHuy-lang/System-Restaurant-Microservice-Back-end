
ARG image_name=restaurant-eureka-server-image
FROM openjdk:17
EXPOSE 8761
ADD target/RestaurantEurekaService-0.0.1-SNAPSHOT.jar RestaurantEurekaService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/RestaurantEurekaService-0.0.1-SNAPSHOT.jar"]

LABEL image.name=$image_name

#cd RestaurantEurekaService
#docker build --build-arg image_name=restaurant-eureka-server-image -t restaurant-eureka-server-image .
#docker network create restaurant-network
#docker run -p 8761:8761 --network restaurant-network --name restaurant-eureka-server restaurant-eureka-server-image
#docker stop restaurant-eureka-server
#docker rm restaurant-eureka-server