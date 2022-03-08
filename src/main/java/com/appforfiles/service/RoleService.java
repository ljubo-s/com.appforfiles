package com.appforfiles.service;

import java.util.List;
import java.util.Optional;
//import org.springframework.data.domain.Example;
import com.appforfiles.model.Role;

public interface RoleService {

    public List<Role> getAll();

    public Optional<Role> getById(Integer id);

    public void saveOrUpdate(Role role);

    public void deleteById(Integer id);

    void delete(Role role);
    
}
