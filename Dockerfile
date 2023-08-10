
ARG image_name=restaurant_mysql

FROM mysql

# Set the root password for the MySQL server
ENV MYSQL_ROOT_PASSWORD=root

COPY ./init.sql /docker-entrypoint-initdb.d/
# Copy custom configuration file (optional)
# COPY my.cnf /etc/mysql/my.cnf
# Create a directory for data persistence
VOLUME /var/lib/rest_mysql

# Expose the MySQL port
EXPOSE 3306

label image.name=$image_name
