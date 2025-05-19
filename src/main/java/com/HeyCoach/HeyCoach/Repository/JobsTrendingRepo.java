package com.HeyCoach.HeyCoach.Repository;

import com.HeyCoach.HeyCoach.Entity.ExistingJobs;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobsTrendingRepo extends MongoRepository<ExistingJobs, ObjectId> {

}
