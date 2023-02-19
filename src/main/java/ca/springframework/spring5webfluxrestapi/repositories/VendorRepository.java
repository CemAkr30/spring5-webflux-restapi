package ca.springframework.spring5webfluxrestapi.repositories;

import ca.springframework.spring5webfluxrestapi.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends ReactiveMongoRepository<Vendor,String> {
}
