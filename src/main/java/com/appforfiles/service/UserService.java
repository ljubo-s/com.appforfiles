package com.appforfiles.service;

import java.util.List;
import com.appforfiles.model.User;

public interface UserService {

    public List<User> getAllUsers();

    public User getUserById(Integer id);

    public User getUserByUsernameAndPassword(String username, String password);

    public void saveOrUpdate(User user);

    public void deleteById(Integer id);

    boolean isUserByUsernameAndPassword(String username, String password);

    void delete(User user);
    
    public User getUserByUsername(String username);

}
