package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.TmpBirthday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TmpBirthdayRepository extends JpaRepository<TmpBirthday, Long> {
    @Modifying
    @Query(value = "INSERT INTO tmp_birthday (birth_date, phone_number) VALUES (:birthDate, :phoneNumber) " +
            "ON CONFLICT (birth_date, phone_number) DO NOTHING", nativeQuery = true)
    void saveOrUpdateBirthday(@Param("birthDate") String birthDate, @Param("phoneNumber") String phoneNumber);
}
