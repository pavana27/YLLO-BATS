package org.temple.cis.kantlab.edgecomputing.server.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.temple.cis.kantlab.edgecomputing.server.repo.resources.FramesQuery;

public interface ScheduledQueryRepository extends MongoRepository<FramesQuery, String> {

	@Query("{scheduledTime : {$lt : ?0}, processed: {$ne: true}})")
	public List<FramesQuery> findByDateRange(Date scheduleTime);

}