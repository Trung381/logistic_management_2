package com.project.logistic_management_2.repository.user;

import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.QUser;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.logistic_management_2.entity.QGoods.goods;
import static com.project.logistic_management_2.entity.QRole.role;
import static com.project.logistic_management_2.entity.QUser.user;
import static com.project.logistic_management_2.entity.QTransaction.transaction;

@Repository
public class UserRepoImpl extends BaseRepo implements UserRepoCustom {
    public UserRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<UserDTO> expression() {
        return Projections.constructor(UserDTO.class,
                user.id.as("id"),
                user.fullName.as("fullName"),
                user.phone.as("phone"),
                user.dateOfBirth.as("dateOfBirth"),
                user.note.as("note"),
                user.username.as("username"),
                user.password.as("password"),
                user.roleId.as("roleId"),
                JPAExpressions.select(role.name.as("roleName"))
                        .from(role)
                        .where(role.id.eq(user.roleId)),
                user.status.as("status"),
                user.createdAt.as("createdAt")
        );
    }

    @Override
    public List<User> getAll(){
        QUser qUser = QUser.user;
//        BooleanBuilder builder = new BooleanBuilder();
//        if(!all){
//            builder.and(qUser.status.eq(1));
//        }
//        return query.selectFrom(qUser).where(builder).fetch();
        return query.selectFrom(qUser).fetch();
    }

    @Override
    public User getUserById(String id){
        QUser qUser = QUser.user;
        BooleanBuilder builder = new BooleanBuilder()
                .and(qUser.id.eq(id));
        return query.selectFrom(qUser)
                .where(builder)
                .fetchOne();
    }

    @Modifying
    @Transactional
    public long deleteUser(String id){
        QUser qUser = QUser.user;
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

    @Override
    public List<UserDTO> getDriver() {

        return query.from(user)
                .leftJoin(role).on(role.id.eq(user.roleId))
                .where(role.name.eq("Driver"))
                .select(expression())
                .fetch();
    }

    @Override
    public List<UserDTO> getAdmin() {

        BooleanBuilder builder = new BooleanBuilder()
                .and(role.name.eq("Admin"))
                .or(role.name.eq("Accountant"))
                .or(role.name.eq("Manager"));

        return query.from(user)
                .leftJoin(role).on(role.id.eq(user.roleId))
                .where(builder)
                .select(expression())
                .fetch();
    }
}
