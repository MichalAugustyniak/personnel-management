package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.Schedule;
import com.pm.personnelmanagement.schedule.model.UserSchedule;
import com.pm.personnelmanagement.schedule.model.UserScheduleId;
import com.pm.personnelmanagement.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserScheduleRepository extends JpaRepository<UserSchedule, UserScheduleId>, JpaSpecificationExecutor<UserSchedule> {
    @Modifying
    @Transactional
    @Query("UPDATE UserSchedule us SET us.isActive = :isActive WHERE us.user IN :users")
    int updateIsActiveUserIn(boolean isActive, Collection<User> users);

    Optional<UserSchedule> findByIsActiveAndUser(boolean isActive, User user);

    void deleteByScheduleAndUserIn(Schedule schedule, Collection<User> users);

    Optional<UserSchedule> findByUserAndSchedule(User user, Schedule schedule);

    Set<UserSchedule> findAllByScheduleAndUserIn(Schedule schedule, Collection<User> users);
}
