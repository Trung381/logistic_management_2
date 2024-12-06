package com.project.logistic_management_2.repository.user;

import com.project.logistic_management_2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>, UserRepoCustom {
}
