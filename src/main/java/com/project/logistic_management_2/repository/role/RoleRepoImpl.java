package com.project.logistic_management_2.repository.role;

import com.project.logistic_management_2.entity.Role;
import com.project.logistic_management_2.repository.BaseRepo;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.logistic_management_2.entity.QRole.role;

@Repository
public class RoleRepoImpl extends BaseRepo implements RoleRepoCustom {
    public RoleRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Role findRoleById(Integer id) {
        return query.selectFrom(role)
                .where(role.id.eq(id))
                .fetchOne();
    }

    public List<Role> getAll(){
        return query.selectFrom(role).fetch();
    }

    @Modifying
    @Transactional
    public void deleteRoleById(Integer id){
        query.delete(role).where(role.id.eq(id));
    }
}
