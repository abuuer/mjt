/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.ERole;
import com.journal.journal.bean.Role;
import java.util.Optional;

/**
 *
 * @author anoir
 */
public interface RoleService {

    int save(Role role);

    Optional<Role> findByName(ERole name);
}
