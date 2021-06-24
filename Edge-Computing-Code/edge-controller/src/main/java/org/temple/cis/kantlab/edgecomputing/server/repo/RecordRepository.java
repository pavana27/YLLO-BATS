package org.temple.cis.kantlab.edgecomputing.server.repo;

import java.util.Date;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.temple.cis.kantlab.edgecomputing.server.repo.resources.Record;

public interface RecordRepository extends MongoRepository<Record, String> {

	public Record findByLicensePlate(String licensePlate);
	
	@Query("{licensePlate: ?0,  lastUpdate : {$gt : ?1}})")
	public Record findByLicensePlateAndLastUpdateRange(String licensePlate, Date dateRange);
	
}
