package com.appforfiles.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.appforfiles.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

//	 One of the solutions for “N+1 select” problem in Hibernate is a custom query with JOIN FETCH. 

	@Query("SELECT user FROM User user LEFT JOIN FETCH user.logSet l WHERE user.username = :username AND user.password = :password")
	public User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

	@Query("SELECT user FROM User user LEFT JOIN FETCH user.logSet l WHERE user.username = :username")
	public User getUserByUsername(@Param("username") String username);

	@Query("SELECT user FROM User user LEFT JOIN FETCH user.logSet l")
	public List<User> findAll();

}
