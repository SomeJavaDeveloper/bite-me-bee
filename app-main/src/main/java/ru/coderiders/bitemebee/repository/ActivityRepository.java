package ru.coderiders.bitemebee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.coderiders.bitemebee.entity.Activity;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findByTitle(String title);
}
