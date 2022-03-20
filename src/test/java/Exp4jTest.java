import lombok.extern.slf4j.Slf4j;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.junit.jupiter.api.Test;

@Slf4j
public class Exp4jTest {

    @Test
    public void test1() {
        Expression expression = new ExpressionBuilder("3*(x+2*y)")
                .variables("x", "y")
                .build()
                .setVariable("x", 2.01)
                .setVariable("y", 3.3);

        double result = expression.evaluate();

        logger.info("result={}", result);
    }
}
