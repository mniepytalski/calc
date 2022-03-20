import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ScriptEngineTest {

    private Map<String,String> variables;

    @BeforeEach
    public void init() {
        variables = new HashMap<>();
    }

    @Test
    public void test1() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        javax.script.ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

        Variables variables = new Variables();
        variables.add("alfa","10");
        variables.add("betaBig","2.01");
        variables.add("y","3.3");

        String expression = variables.formatVariables() + "3*(betaBig+2*y)/alfa;";
        Double result = null;
        try {
            result = (Double) scriptEngine.eval(expression);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        logger.info("expression:{}", expression);
        logger.info("result:{}", result);
    }
}
