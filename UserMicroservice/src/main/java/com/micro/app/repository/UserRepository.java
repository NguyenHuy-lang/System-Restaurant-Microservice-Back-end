package com.micro.app.repository;
import com.micro.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM tbl_user u where u.password = :password AND u.username = :username", nativeQuery = true)
    public User checkLogin(@Param("password") String password,
                           @Param("username") String username);
    @Query(value = "SELECT * FROM tbl_user u where u.email = :email", nativeQuery = true)
    public User findByEmail(@Param("email") String email);
}
