package tech.qiantong.qdata.datax;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** Builds standard DataX WHERE predicates; custom Reader plugins are not required. */
public final class DateIncrementFilter {
    private DateIncrementFilter() { }

    /** Applies one immutable read window and returns its successful-run cursor candidates. */
    public static Map<String, String> apply(Map<String, Object> reader, Map<String, String> cached, Date now) {
        Map<String, String> cursors = new LinkedHashMap<>();
        if (!"3".equals(String.valueOf(reader.get("readModeType")))) return cursors;
        JSONObject config = JSONObject.parseObject(JSONObject.toJSONString(reader.get("dateIncrementConfig")));
        if (config == null) throw new IllegalArgumentException("Time increment configuration is required");
        String format = config.getString("dateFormat");
        if (format == null || format.trim().isEmpty()) throw new IllegalArgumentException("Time increment dateFormat is required");
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        String upper = formatter.format(now);
        JSONArray conditions = config.getJSONArray("column");
        if (conditions == null || conditions.isEmpty()) throw new IllegalArgumentException("At least one time increment condition is required");
        String logic = config.getString("logic");
        if (logic == null || logic.trim().isEmpty()) logic = "and";
        if (!"and".equalsIgnoreCase(logic) && !"or".equalsIgnoreCase(logic)) throw new IllegalArgumentException("Invalid time condition logic");
        List<String> predicates = new ArrayList<>();
        for (int i = 0; i < conditions.size(); i++) {
            JSONObject item = conditions.getJSONObject(i);
            String column = item.getString("incrementColumn");
            if (column == null || !column.matches("[\\p{L}_][\\p{L}\\p{N}_$#]*(\\.[\\p{L}_][\\p{L}\\p{N}_$#]*)*")) {
                throw new IllegalArgumentException("Invalid time increment column: " + column);
            }
            String type = item.getString("type");
            if ("2".equals(type)) {
                String start = item.getString("cursorTime");
                Date startDate = parse(start, formatter);
                String saved = cached.get(column.toUpperCase(Locale.ROOT));
                if (saved != null && parse(saved, formatter).after(startDate)) start = saved;
                predicates.add("(" + column + " > '" + start + "' AND " + column + " <= '" + upper + "')");
                cursors.put(column.toUpperCase(Locale.ROOT), upper);
            } else if ("1".equals(type) || "3".equals(type)) {
                String operator = item.getString("operator");
                if (!Arrays.asList(">", ">=", "<", "<=", "=", "!=", "<>").contains(operator)) throw new IllegalArgumentException("Invalid time comparison operator");
                String data = item.getString("data");
                if (data == null || data.trim().isEmpty()) throw new IllegalArgumentException("Time comparison value is required");
                if ("1".equals(type)) {
                    parse(data, formatter);
                    data = "'" + data + "'";
                }
                predicates.add("(" + column + " " + operator + " " + data + ")");
            } else {
                throw new IllegalArgumentException("Unknown time condition type: " + type);
            }
        }
        String timeWhere = "(" + String.join(" " + logic.toUpperCase(Locale.ROOT) + " ", predicates) + ")";
        Object original = reader.get("where");
        String where = original == null ? "" : original.toString().trim();
        reader.put("where", where.isEmpty() ? timeWhere : "(" + where + ") AND " + timeWhere);
        // Standard WHERE now owns filtering. Prevent extended plugins applying another window.
        reader.put("readModeType", "1");
        return cursors;
    }

    private static Date parse(String value, SimpleDateFormat formatter) {
        try {
            if (value == null) throw new ParseException("Missing time cursor", 0);
            Date date = formatter.parse(value);
            if (!value.equals(formatter.format(date)) || value.contains("'")) throw new ParseException(value, 0);
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Time value does not match " + formatter.toPattern() + ": " + value, e);
        }
    }
}
