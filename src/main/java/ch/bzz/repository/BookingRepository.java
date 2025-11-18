package ch.bzz.repository;

import ch.bzz.model.Booking;
import ch.bzz.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByProject(Project project);
}
