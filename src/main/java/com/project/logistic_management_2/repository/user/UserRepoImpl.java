package com.project.logistic_management_2.repository.user;

import com.project.logistic_management_2.entity.QUser;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepoImpl extends BaseRepo implements UserRepoCustom {
    public UserRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<User> getAll(Boolean all){
        QUser qUser = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();
        if(!all){
            builder.and(qUser.status.eq(1));
        }
        return query.selectFrom(qUser).where(builder).fetch();
    }

    @Override
    public User getUserById(String id, Boolean all){
        QUser qUser = QUser.user;
        BooleanBuilder builder = new BooleanBuilder()
                .and(qUser.id.eq(id));
        if(!all){
            builder.and(qUser.status.eq(1));
        }
        return query.selectFrom(qUser)
                .where(builder)
                .fetchOne();
    }

    @Modifying
    @Transactional
    public long deleteUser(String id){
        QUser qUser = QUser.user;
//        query.delete(qUser)
//                .where(qUser.id.eq(id))
//                .execute();
        return query.update(qUser)
                .where(qUser.id.eq(id))
                .set(qUser.status,0)
                .execute();
    }

    @Override
    public User findByUsername(String username) {
        QUser qUser = QUser.user;

        return query.selectFrom(qUser)
                .where(qUser.username.eq(username))
                .fetchOne();

    }
}
