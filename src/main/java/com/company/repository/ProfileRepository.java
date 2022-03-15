package com.company.repository;

import com.company.entity.ProfileEntity;
import com.company.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer>,
        JpaSpecificationExecutor<ProfileEntity> {

    Optional<ProfileEntity> findByLoginAndPassword(String login, String pswd);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity p SET p.name = ?2 WHERE p.id = ?1")
    void updateNameById(Integer id, String name);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity p SET p.surname = ?2 WHERE p.id = ?1")
    void updateSurnameById(Integer id, String surname);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity p SET p.email = ?2 WHERE p.id = ?1")
    void updateEmailById(Integer id, String email);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity p SET p.login = ?2 WHERE p.id = ?1")
    void updateLoginById(Integer id, String login);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity p SET p.password = ?2 WHERE p.id = ?1")
    void updatePasswordById(Integer id, String password);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity p SET p.role = ?2 WHERE p.id = ?1")
    void updateRoleById(Integer id, Role role);

    Optional<ProfileEntity> findByEmail(String email);

    Optional<ProfileEntity> findByLogin(String email);



}
