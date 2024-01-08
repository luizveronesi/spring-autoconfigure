package dev.luizveronesi.autoconfigure.utils.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

import dev.luizveronesi.autoconfigure.utils.JsonUtil;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GenericUpdateRepository {

	private final MongoTemplate mongoTemplate;

	public void update(Class<?> entityClass, MongoUpdatableField field) {
		if (StringUtils.isEmpty(field.getId()))
			return;

		var value = field.getStringValue();
		if (value == null)
			value = "null";

		this.update(entityClass, field.getId(), field.getName(), value);
	}

	public Long updateWithBson(Class<?> entityClass, String query, String set) {
		UpdateResult result = this.getCollection(entityClass).updateMany(
				BasicDBObject.parse(query), BasicDBObject.parse(set));
		return result.getMatchedCount();
	}

	public Long update(Class<?> entityClass, String mquery, String set) {
		List<Criteria> list = new ArrayList<>();

		var object = (ObjectNode) JsonUtil.deserialize(mquery);
		var it = object.fields();
		while (it.hasNext()) {
			Map.Entry<String, JsonNode> entry = it.next();
			list.add(Criteria.where(entry.getKey()).is(MongoQueryConverter.execute(entry.getKey(), entry.getValue())));
		}

		Criteria criteria = new Criteria().andOperator(list.toArray(new Criteria[list.size()]));
		Query query = new Query(criteria);

		UpdateResult result = mongoTemplate.updateMulti(
				query,
				this.createUpdateObject(set),
				entityClass);
		return result.getMatchedCount();
	}

	public Long update(Class<?> entityClass, String id, String attr, Object value) {
		return this.updateByUid(
				entityClass,
				id,
				JsonUtil.serialize(Map.of(attr, value)));
	}

	public Long updateByUid(Class<?> entityClass, String id, String values) {
		Query query = new Query(Criteria.where("uid").is(id));
		UpdateResult result = mongoTemplate.updateMulti(query, this.createUpdateObject(values), entityClass);
		return result.getMatchedCount();
	}

	private Update createUpdateObject(String set) {
		Update update = new Update();

		var object = (ObjectNode) JsonUtil.deserialize(set);
		var it = object.fields();
		while (it.hasNext()) {
			Map.Entry<String, JsonNode> entry = it.next();
			if (entry.getValue().asText().equals("null")) {
				update.set(entry.getKey(), null);
			} else {
				Object value = MongoQueryConverter.execute(entry.getKey(), entry.getValue());
				update.set(entry.getKey(), value);
			}
		}
		return update;

	}

	private MongoCollection<Document> getCollection(Class<?> entityClass) {
		var name = mongoTemplate.getCollectionName(entityClass);
		return mongoTemplate.getCollection(name);
	}
}
