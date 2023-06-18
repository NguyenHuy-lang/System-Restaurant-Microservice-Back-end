ARG image_name=FoodsMicroservice

FROM openjdk:17
EXPOSE 8082
ADD target/FoodMicroservice-1.0-SNAPSHOT.jar FoodMicroservice-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/FoodMicroservice-1.0-SNAPSHOT.jar"]

LABEL image.name=$image_name

#cd FoodMicroservice
#docker network create restaurant-network(optional)
#docker build --build-arg image_name=food-microservice-image -t food-microservice-image .
#docker stop food-microservice
#docker rm food-microservice
#docker run -p 8082:8082 --network restaurant-network --name food-microservice food-microservice-image