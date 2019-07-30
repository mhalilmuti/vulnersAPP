package com.muti.VulnersAPP.vulnerAPP.repository;

import com.muti.VulnersAPP.vulnerAPP.model.Role;
import org.springframework.data.repository.CrudRepository;


public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);
    Role findById(long id);
}
