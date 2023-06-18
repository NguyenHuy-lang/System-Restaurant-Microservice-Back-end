ARG image_name=food-micro-image

FROM openjdk:17
EXPOSE 8082
ADD target/FoodMicro-0.0.1-SNAPSHOT.jar FoodMicro-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/FoodMicro-0.0.1-SNAPSHOT.jar"]

LABEL image.name=$image_name

#cd FoodMicro
#docker build --build-arg image_name=food-micro-image -t food-micro-image .
#docker run -p 8082:8082 --network restaurant-network --name food-micro food-micro-image
#docker stop food-micro
#docker rm food-micro
