package team.guest.tgbotty.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.guest.tgbotty.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {


}
