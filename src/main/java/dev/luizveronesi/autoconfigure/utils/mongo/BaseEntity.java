package dev.luizveronesi.autoconfigure.utils.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class BaseEntity {
	
    @Id
    private ObjectId id;
    
    public String getId() {
    	return this.id.toString();
    }
    
    public void resetId() {
    	this.id = null;
    }
}
