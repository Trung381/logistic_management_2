package com.project.logistic_management_2.repository.role;


import com.project.logistic_management_2.entity.QRole;
import com.project.logistic_management_2.entity.Role;
import com.project.logistic_management_2.repository.BaseRepo;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepoImpl extends BaseRepo implements RoleRepoCustom {
    public RoleRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Role findRoleById(Integer id) {
        QRole qRole = QRole.role;
        return query.selectFrom(qRole)
                .where(qRole.id.eq(id))
                .fetchOne();
    }

    public List<Role> getAll(){
        QRole qRole = QRole.role;
        return query.selectFrom(qRole).fetch();
    }

    @Modifying
    @Transactional
    public void deleteRoleById(Integer id){
        QRole qRole = QRole.role;
        query.delete(qRole).where(qRole.id.eq(id));
    }


}
