import java.util.HashMap;
import java.util.Map;

public class Variables {

    private Map<String,String> variables = new HashMap<>();

    public boolean isAnuNull() {
        return variables.values().stream().anyMatch(v -> v == null);
    }

    public void add(String key, String value) {
        variables.put(key, value);
    }

    public String formatVariables() {
        StringBuffer variablesList = new StringBuffer();
        variables.forEach((k,v) -> variablesList.append(k).append("=").append(v).append("; "));
        return variablesList.toString();
    }
}
