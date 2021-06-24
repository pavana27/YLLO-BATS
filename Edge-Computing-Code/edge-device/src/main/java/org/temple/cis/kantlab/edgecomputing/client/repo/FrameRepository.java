package org.temple.cis.kantlab.edgecomputing.client.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.temple.cis.kantlab.edgecomputing.client.repo.resources.Frame;

public interface FrameRepository extends MongoRepository<Frame, String> {

	@Query("{makeModel: ?0,  lastUpdate : {$gt : ?1}})")
	public List<Frame> findByMakeModelAndLastUpdate(String makeModel, Date lastUpdate);
	
}
