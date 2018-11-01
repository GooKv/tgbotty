package team.guest.tgbotty.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.guest.tgbotty.entity.Chat;

import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {

    Optional<Chat> findByChatId(Long chatId);

}
