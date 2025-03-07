package pl.lodz.coordinationsystem.communication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.coordinationsystem.communication.entity.MessageEntity;
import pl.lodz.coordinationsystem.security.entity.RoleEntity;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<MessageEntity, Long> {
    List<MessageEntity> findAllByRole(RoleEntity role);
}