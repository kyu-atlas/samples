package pers.sample.sb;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface RandomNumberRepository extends CrudRepository<RandomNumber, Integer> {

    @Query("select * from number_world where id = :id")
    Optional<RandomNumber> findById(int id);

    @Modifying
    @Query("update number_world set hit_count = hit_count + 1 where id = :id")
    Boolean updateHitCountById(int id);

}
