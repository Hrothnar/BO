package com.tech.neo.bo.repository;

import com.tech.neo.bo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository()
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying()
    @Query(value = "UPDATE phone_numbers " +
            "SET phone_number = :newPhoneNumber " +
            "WHERE phone_number = :oldPhoneNumber AND user_id = :id", nativeQuery = true)
    void updatePhoneNumber(@Param("id") long id,
                           @Param("oldPhoneNumber") String oldPhoneNumber,
                           @Param("newPhoneNumber") String newPhoneNumber);

    @Modifying()
    @Query(value = "UPDATE emails " +
            "SET email = :newEmail " +
            "WHERE email = :oldEmail AND user_id = :id", nativeQuery = true)
    void updateEmail(@Param("id") long id,
                     @Param("oldEmail") String oldEmail,
                     @Param("newEmail") String newEmail);


    Optional<User> findByLogin(String login);

    @Query(value = "SELECT * FROM users WHERE birth_date > :birthDate", nativeQuery = true)
    List<User> findPeopleYoungerThan(@Param("birthDate") LocalDate birthDate);

    List<User> findByFullNameContaining(String fullName);

    @Query(value = "SELECT * FROM users AS u " +
            "INNER JOIN phone_numbers AS pn ON u.id = pn.user_id " +
            "WHERE pn.phone_number = :phoneNumber", nativeQuery = true)
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT * FROM users AS u " +
            "INNER JOIN emails AS e ON u.id = e.user_id " +
            "WHERE e.email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);
}
