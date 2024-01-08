package dev.luizveronesi.autoconfigure.utils.mongo;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoQueryConverter {

	public static final String NUMBER_AS_STRING = "&N";

	public static Object execute(String key, JsonNode element) {
		String value = element.asText();
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			if (!value.contains("[a-zA-Z]+")) {
				try {
					return element.asDouble();
				} catch (Exception e2) {
					if (value.equals("true") || value.equals("false")) {
						return element.asBoolean();
					}
					return MongoQueryConverter.execute(key, value);
				}
			}
			return MongoQueryConverter.execute(key, value);
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

	// TODO: refactor this
	private static Object execute(String key, String value) {
		// value = value.replaceAll("\"", "");
		value = value.replace("\\\\", "\\");

		if (value.startsWith(NUMBER_AS_STRING)) {
			return value.replace(NUMBER_AS_STRING, "");
		} else if (value.equals("null")) {
			BasicDBObject existsFalse = new BasicDBObject(key, new BasicDBObject("$exists", false));
			BasicDBObject equalsNull = new BasicDBObject(key, new BasicDBObject("$eq", null));
			BasicDBList or = new BasicDBList();
			or.add(existsFalse);
			or.add(equalsNull);
			return new BasicDBObject("$or", or);
		} else if (value.equals("null!")) {
			return new BasicDBObject("$exists", true);
		} else if (value.startsWith("/") && (value.endsWith("/"))) {
			return new BasicDBObject("$regex",
					replaceDiacritics(value.replaceAll("/", ""))).append("$options", "i");
		} else if (value.startsWith("/")) {
			return new BasicDBObject("$regex",
					replaceDiacritics(value.replaceAll("/", ""))).append("$options", "i");
		} else if (value.startsWith(">=") || value.startsWith("&gt;=")) {
			value = value.replaceAll(">=", "");
			value = value.replaceAll("&gt;=", "");
			DBObject regexObj;
			try {
				regexObj = new BasicDBObject("$gte", Double.parseDouble(value));
			} catch (Exception e2) {
				regexObj = new BasicDBObject("$gte", value);
			}
			return regexObj;
		} else if (value.startsWith("<=") || value.startsWith("&lt;=")) {
			value = value.replaceAll("<=", "");
			value = value.replaceAll("&lt;=", "");
			DBObject regexObj;
			try {
				regexObj = new BasicDBObject("$lte", Double.parseDouble(value));
			} catch (Exception e2) {
				regexObj = new BasicDBObject("$lte", value);
			}
			return regexObj;
		} else if (value.startsWith("!&[")) { // not empty array
			return new BasicDBObject("$not", new BasicDBObject("$size", 0));
		} else if (value.startsWith("&[")) { // empty array
			return new BasicDBObject("$size", 0);
		} else if (value.startsWith("!&")) { // ne
			return new BasicDBObject("$ne", value.replaceAll("!&", ""));
		}
		return value;
	}
}