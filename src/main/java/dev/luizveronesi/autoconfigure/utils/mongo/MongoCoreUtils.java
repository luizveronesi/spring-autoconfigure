package dev.luizveronesi.autoconfigure.utils.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.JsonNode;

public class MongoCoreUtils {

	public static String getProjectFields(String[] includes, String[] excludes) {
		var sb = "{";
		
		if (includes != null) 
			for (var inc : includes)
				sb += "'" + inc + "':1,";

		if (excludes != null) 
			for (var exc : excludes)
				sb += "'" + exc + "':0,";

		// removes the last comma
		sb = sb.substring(0, sb.length() - 1);
		sb += "}";
		return sb;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static String getCollection(Class cls) {
		Document document = (Document) cls.getAnnotation(Document.class);
		if (document.collection() == null) {
			throw new RuntimeException("collection not informada para a entidade: " + cls);
		}
		return document.collection();
	}
	
	public static Object execute(String key, JsonNode element) {
		String value = element.asText();
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			try {
				return Double.parseDouble(value);
			} catch (Exception e2) {
				if (value.equals("true") || value.equals("false"))
					return Boolean.parseBoolean(value);
				return value;
			}
		}
	}

	public static String replaceDiacritics(String value) {
		value = value.replaceAll("a|à|â|ä", "[aàâä]");
		value = value.replaceAll("c|ç", "[cç]");
		value = value.replaceAll("e|é|è|ê|ë", "[eéèêë]");
		value = value.replaceAll("i|î|ï", "[iîï]");
		value = value.replaceAll("o|ô|ö", "[oôö]");
		value = value.replaceAll("û|ü|ù", "[ûüù]");
		value = value.replaceAll("y|ÿ", "[yÿ]");
		return value;
	}

	@SuppressWarnings("rawtypes")
	public static Long executeCountAggregation(
			Class baseClass, 
			String query, 
			MongoTemplate mongoTemplate) {

		var results = getResults(CountModel.class, baseClass, query, mongoTemplate).getUniqueMappedResult();
		if (results == null) return 0L;
		return results.getTotal();
	}

	@SuppressWarnings("rawtypes")
	public static <T> T executeUniqueAggregation(
			Class<T> entity, 
			Class baseClass, 
			String query, 
			MongoTemplate mongoTemplate) {
		return getResults(entity, baseClass, query, mongoTemplate)
					.getUniqueMappedResult();
	}
	
	@SuppressWarnings("rawtypes")
	public static <T> List<T> executeListAggregation(
			Class<T> entity, 
			Class baseClass, 
			String query, 
			MongoTemplate mongoTemplate) {
		return getResults(entity, baseClass, query, mongoTemplate)
					.getMappedResults();
	}
	
	@SuppressWarnings("rawtypes")
	private static <T> AggregationResults<T> getResults(
			Class<T> entity, 
			Class baseClass, 
			String query, 
			MongoTemplate mongoTemplate) {
		
		Aggregation aggregation = Aggregation
				.newAggregation(AggregationConverter.convert(query));
		return mongoTemplate.aggregate(
				aggregation, 
				getCollection(baseClass),
				entity);

	}
}
