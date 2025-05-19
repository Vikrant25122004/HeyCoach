package com.HeyCoach.HeyCoach.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TrendingJobs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExistingJobs {
    @Id
    private ObjectId id;
    private String applicationCount;
    private String companyName;
    private String contractType;
    private String description;
    private String experienceLevel;
    private String location;
    private String salary;

}
