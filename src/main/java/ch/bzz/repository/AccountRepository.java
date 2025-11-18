package ch.bzz.repository;

import ch.bzz.model.Account;
import ch.bzz.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByProject(Project project);
}
