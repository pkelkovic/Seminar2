package rasus.lab3.repository;
//import org.springframework.data.jpa.repository.JpaRepository;

import rasus.lab3.entity.TemperatureReading;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TemperatureRepository
    extends CrudRepository<TemperatureReading, Long> {
}
