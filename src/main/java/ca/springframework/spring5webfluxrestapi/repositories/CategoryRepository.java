package ca.springframework.spring5webfluxrestapi.repositories;

import ca.springframework.spring5webfluxrestapi.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends ReactiveMongoRepository<Category,String> {
}
