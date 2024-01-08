package dev.luizveronesi.autoconfigure.utils.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.luizveronesi.autoconfigure.utils.JsonUtil;

public class AggregationConverter {
    public static List<CustomAggregationOperation> convert(String query) {
        var array = JsonUtil.deserialize(query);

        List<CustomAggregationOperation> list = new ArrayList<>();
        for (var element : array) {
            String val = element.toString();
            // convert ObjectId
            if (val.contains("ObjectId")) {
                val = replaceObjectId(val);
            }
            list.add(new CustomAggregationOperation(val));
        }
        return list;
    }

    private static String replaceObjectId(String str) {
        String pattern = "\"ObjectId\\((.*)\\)\"";
        String allPattern = "(.*)" + pattern + "(.*)";
        String replacement = "ObjectId(\"$0\")";

        Pattern p = Pattern.compile(allPattern);
        Matcher m = p.matcher(str);
        if (m.matches()) {
            replacement = replacement.replace("$0", m.group(2));
            str = str.replaceAll(pattern, replacement);
        }
        return str;
    }
}
