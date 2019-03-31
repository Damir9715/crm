package com.example.crm2.repo;

import com.example.crm2.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findById(Integer id);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query(value = "select * from usr " +
            "where usr.id " +
            "in (select u.id from role r " +
            "join user_role ur on r.id= ur.role_id " +
            "join usr u on ur.user_id=u.id " +
            "join user_subject us on u.id=us.user_id " +
            "join subject s on us.subject_id=s.id " +
            "where s.id=?1 " +
            "and r.id=?2)", nativeQuery = true)
    List<User> findTeachersOfSubject(Integer subjectId, Integer roleId);

    @Query(value = "select * from usr " +
            "inner join user_role on usr.id = user_role.user_id " +
            "inner join role on user_role.role_id = role.id " +
            "where role.id = ?1", nativeQuery = true)
    List<User> findUserWithRole(Integer role);


    @Query(value = "select count(username) from usr " +
            "inner join user_role on usr.id = user_role.user_id " +
            "inner join role on user_role.role_id = role.id " +
            "where role.id =?1", nativeQuery = true)
    Integer countOfRoles(Integer roleId);


    @Query(value = "select count(username) from usr " +
            "inner join user_role on usr.id = user_role.user_id " +
            "inner join role on user_role.role_id = role.id " +
            "where role.id = ?1 and usr.active = ?2", nativeQuery = true)
    Integer countOfRolesNonActive(Integer roleId, boolean s);


    @Query(value = "select usr.id, usr.username, usr.password, usr.active from role " +
            "inner join user_role on role.id = user_role.role_id " +
            "inner join usr on user_role.user_id = usr.id " +
            "inner join user_subject on usr.id = user_subject.user_id " +
            "inner join subject on user_subject.subject_id = subject.id " +
            "where subject.id = ?1 and role.id = ?2", nativeQuery = true)
    List<User> usersWhichTeachThisSubject(Integer subjectId, Integer roleId);
}