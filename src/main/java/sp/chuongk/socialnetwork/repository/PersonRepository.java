package sp.chuongk.socialnetwork.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sp.chuongk.socialnetwork.entity.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, String>{

}
