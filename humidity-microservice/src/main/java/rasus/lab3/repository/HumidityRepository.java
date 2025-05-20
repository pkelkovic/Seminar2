package rasus.lab3.repository;
//import org.springframework.data.jpa.repository.JpaRepository;

import rasus.lab3.entity.HumidityReading;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HumidityRepository
    extends CrudRepository<HumidityReading, Long> {
}
