package team.guest.tgbotty.dao;

import org.springframework.data.repository.CrudRepository;
import team.guest.tgbotty.entity.Request;

public interface RequestRepository extends CrudRepository<Request, Long> {

}
