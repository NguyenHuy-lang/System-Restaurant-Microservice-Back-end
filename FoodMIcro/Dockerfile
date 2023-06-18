ARG image_name=food-micro-image

FROM openjdk:17
EXPOSE 8082
ADD target/FoodMIcro-0.0.1-SNAPSHOT.jar FoodMIcro-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/FoodMIcro-0.0.1-SNAPSHOT.jar"]

LABEL image.name=$image_name

#cd FoodMicro
#docker build --build-arg image_name=food-micro-image -t food-micro-image .
#docker run -p 8082:8082 --network restaurant-network --name food-micro food-micro-image
#docker stop food-micro
#docker rm food-micro
