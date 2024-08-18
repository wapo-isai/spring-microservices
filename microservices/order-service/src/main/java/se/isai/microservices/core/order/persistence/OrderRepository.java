package se.isai.microservices.core.order.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    public List<OrderEntity> findAllByUserId(String userId);
}
