package com.appforfiles.repository;

import java.util.List;
import com.appforfiles.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    public List<Role> findAllByOrderByIdAsc();   

}
