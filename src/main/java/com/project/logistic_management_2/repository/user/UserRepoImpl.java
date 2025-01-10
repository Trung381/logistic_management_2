package com.project.logistic_management_2.repository.user;

import com.project.logistic_management_2.dto.user.UpdateUserDTO;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.QUser;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.exception.def.EditNotAllowedException;
import com.project.logistic_management_2.exception.def.InvalidFieldException;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
                user.imagePath.as("imagePath"), //
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

    @Override
    @Transactional
    public Boolean updateUser(User exitingUser,String id, UserDTO updateUserDTO) {
        QUser qUser = QUser.user;

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qUser.id.eq(id));

        JPAUpdateClause updateClause = query.update(qUser).where(whereClause);

        boolean isUpdated = false;
        boolean isChanged = false;

        if (updateUserDTO.getFullName() != null) {
            if(!updateUserDTO.getFullName().equals(exitingUser.getFullName())){
                isChanged = true;
            }
            updateClause.set(qUser.fullName, updateUserDTO.getFullName());
            isUpdated = true;
        }
        if (updateUserDTO.getPhone() != null) {
            if(!updateUserDTO.getPhone().equals(exitingUser.getPhone())){
                isChanged = true;
            }
            updateClause.set(qUser.phone, updateUserDTO.getPhone());
            isUpdated = true;
        }
        if (updateUserDTO.getDateOfBirth() != null) {
            if(!updateUserDTO.getDateOfBirth().equals(exitingUser.getDateOfBirth())){
                isChanged = true;
            }
            updateClause.set(qUser.dateOfBirth, updateUserDTO.getDateOfBirth());
            isUpdated = true;
        }
        if (updateUserDTO.getImagePath() != null) {
            if(!updateUserDTO.getImagePath().equals(exitingUser.getImagePath())){
                isChanged = true;
            }
            updateClause.set(qUser.imagePath, updateUserDTO.getImagePath());
            isUpdated = true;
        }
        if (updateUserDTO.getNote() != null) {
            if(!updateUserDTO.getNote().equals(exitingUser.getNote())){
                isChanged = true;
            }
            updateClause.set(qUser.note, updateUserDTO.getNote());
            isUpdated = true;
        }
        if (updateUserDTO.getUsername() != null) {
            if(!updateUserDTO.getUsername().equals(exitingUser.getUsername())){
                isChanged = true;
            }
            updateClause.set(qUser.username, updateUserDTO.getUsername());
            isUpdated = true;
        }
        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            if(!updateUserDTO.getPassword().equals(exitingUser.getPassword())){
                isChanged = true;
            }
            updateClause.set(qUser.password, updateUserDTO.getPassword());
            isUpdated = true;
        }
        if (updateUserDTO.getRoleId() != null) {
            if(!updateUserDTO.getRoleId().equals(exitingUser.getRoleId())){
                isChanged = true;
            }
            updateClause.set(qUser.roleId, updateUserDTO.getRoleId());
            isUpdated = true;
        }
        if (updateUserDTO.getStatus() != null) {
            if(!updateUserDTO.getStatus().equals(exitingUser.getStatus())){
                isChanged = true;
            }
            updateClause.set(qUser.status, updateUserDTO.getStatus());
            isUpdated = true;
        }

        if (isUpdated) {
            updateClause.set(qUser.updatedAt, new Date());
        } else {
            throw new InvalidFieldException("No data fields are updated!!!");
        }

        if(!isChanged) {
            throw new EditNotAllowedException("Data is not changed!!!");
        }

        return updateClause.execute() > 0;
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
