package tech.qiantong.qdata.datax;

import com.alibaba.fastjson2.JSONObject;
import java.text.SimpleDateFormat;
import java.util.*;

/** Standalone regression checks, requiring only fastjson2 and the production helper. */
public class DateIncrementFilterRegression {
    private static Map<String, Object> reader(String rules) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("readModeType", "3");
        result.put("dateIncrementConfig", JSONObject.parseObject(
                "{\"dateFormat\":\"yyyy-MM-dd HH:mm:ss\",\"logic\":\"and\",\"column\":" + rules + "}"));
        return result;
    }
    private static String range = "[{\"type\":\"2\",\"incrementColumn\":\"create_time\",\"cursorTime\":\"2026-11-01 00:00:00\"}]";
    private static void check(boolean value, String label) {
        if (!value) throw new AssertionError(label);
    }
    public static void main(String[] args) throws Exception {
        Date now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2026-11-02 12:00:00");
        Map<String, Object> first = reader(range);
        Map<String, String> candidates = DateIncrementFilter.apply(first, Collections.emptyMap(), now);
        String where = first.get("where").toString();
        check(where.contains("create_time > '2026-11-01 00:00:00'"), "initial lower bound");
        check(where.contains("create_time <= '2026-11-02 12:00:00'"), "captured upper bound");
        check("2026-11-02 12:00:00".equals(candidates.get("CREATE_TIME")), "success cursor candidate");
        check("1".equals(first.get("readModeType")), "no duplicate plugin window");
        DateIncrementFilter.apply(first, Collections.emptyMap(), now);
        check(where.equals(first.get("where")), "no duplicate builder predicate");
        Map<String, Object> second = reader(range);
        second.put("where", "city = 'Beijing' OR city = 'Shanghai'");
        DateIncrementFilter.apply(second, Collections.singletonMap("CREATE_TIME", "2026-11-02 10:00:00"), now);
        check(second.get("where").toString().startsWith("(city = 'Beijing' OR city = 'Shanghai') AND"), "manual WHERE grouping");
        check(second.get("where").toString().contains("> '2026-11-02 10:00:00'"), "cached lower bound");
        Map<String, Object> older = reader(range);
        DateIncrementFilter.apply(older, Collections.singletonMap("CREATE_TIME", "2026-10-01 00:00:00"), now);
        check(older.get("where").toString().contains("> '2026-11-01 00:00:00'"), "cursor never precedes configured start");
        Map<String, Object> full = reader(range);
        full.put("readModeType", "1");
        DateIncrementFilter.apply(full, Collections.emptyMap(), now);
        check(!full.containsKey("where"), "full read unchanged");
        Map<String, Object> fixed = reader("[{\"type\":\"1\",\"incrementColumn\":\"UPDATETIME\",\"operator\":\">=\",\"data\":\"2026-11-01 00:00:00\"},{\"type\":\"3\",\"incrementColumn\":\"UPDATETIME\",\"operator\":\"<\",\"data\":\"GETDATE()\"}]");
        check(DateIncrementFilter.apply(fixed, Collections.emptyMap(), now).isEmpty(), "fixed conditions have no moving cursor");
        check(fixed.get("where").toString().contains("UPDATETIME < GETDATE()"), "SQL Server expression preserved");
        try {
            DateIncrementFilter.apply(reader("[]"), Collections.emptyMap(), now);
            throw new AssertionError("empty rules silently read all rows");
        } catch (IllegalArgumentException expected) { }
        try {
            DateIncrementFilter.apply(reader(range.replace("2026-11-01", "2026-02-30")), Collections.emptyMap(), now);
            throw new AssertionError("invalid date accepted");
        } catch (IllegalArgumentException expected) { }
        try {
            DateIncrementFilter.apply(reader(range), Collections.singletonMap("CREATE_TIME", "invalid"), now);
            throw new AssertionError("invalid cached cursor accepted");
        } catch (IllegalArgumentException expected) { }
        System.out.println("PASS: 14 time-increment regression checks");
    }
}
