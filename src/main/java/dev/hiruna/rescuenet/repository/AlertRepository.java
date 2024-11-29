package dev.hiruna.rescuenet.repository;

import dev.hiruna.rescuenet.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    List<Alert> findByStatus(String status);

}