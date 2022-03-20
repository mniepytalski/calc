package evaluator.extension;

import com.fathzer.soft.javaluator.StaticVariableSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class CustomDecimalEvaluatorTest {

    @Test
    public void test1() {
        String expression = "(3*x+2*y)/lowLevel";
        CustomDecimalEvaluator eval = new CustomDecimalEvaluator();
        StaticVariableSet<Double> variables = new StaticVariableSet<>();
        variables.set("x", 2.0);
        variables.set("y", 3.0);
        variables.set("lowLevel", 4.0);

        Double result = eval.evaluate(expression, variables);

        logger.debug("expression:{}",expression);
        logger.debug("result:{}",result);
    }
}
