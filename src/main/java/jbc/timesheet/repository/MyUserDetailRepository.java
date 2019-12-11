package jbc.timesheet.repository;

import jbc.timesheet.model.MyUserDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserDetailRepository extends CrudRepository<MyUserDetail, Long> {
    Optional<MyUserDetail> findByUsername(String username);
}
