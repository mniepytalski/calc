package evaluator.extension;

import com.fathzer.soft.javaluator.Constant;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Operator;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Iterator;

public class CustomDecimalEvaluator extends EvaluatorExtension<Double> {

    protected CustomDecimalEvaluator() {
        super();
    }

    protected Double toValue(String literal, Object evaluationContext) {
        ParsePosition p = new ParsePosition(0);
        Number result = ((NumberFormat)FORMATTER.get()).parse(literal, p);
        if (p.getIndex() != 0 && p.getIndex() == literal.length()) {
            return result.doubleValue();
        } else if (this.supportsScientificNotation && isScientificNotation(literal)) {
            return Double.valueOf(literal);
        } else {
            throw new IllegalArgumentException(literal + " is not a number");
        }
    }

    protected Double evaluate(Constant constant, Object evaluationContext) {
        if (PI.equals(constant)) {
            return 3.141592653589793D;
        } else {
            return E.equals(constant) ? 2.718281828459045D : (Double)super.evaluate(constant, evaluationContext);
        }
    }

    protected Double evaluate(Operator operator, Iterator<Double> operands, Object evaluationContext) {
        if (!NEGATE.equals(operator) && !NEGATE_HIGH.equals(operator)) {
            if (MINUS.equals(operator)) {
                return (Double)operands.next() - (Double)operands.next();
            } else if (PLUS.equals(operator)) {
                return (Double)operands.next() + (Double)operands.next();
            } else if (MULTIPLY.equals(operator)) {
                return (Double)operands.next() * (Double)operands.next();
            } else if (DIVIDE.equals(operator)) {
                return (Double)operands.next() / (Double)operands.next();
            } else if (EXPONENT.equals(operator)) {
                return Math.pow((Double)operands.next(), (Double)operands.next());
            } else {
                return MODULO.equals(operator) ? (Double)operands.next() % (Double)operands.next() : (Double)super.evaluate(operator, operands, evaluationContext);
            }
        } else {
            return -(Double)operands.next();
        }
    }

    protected Double evaluate(Function function, Iterator<Double> arguments, Object evaluationContext) {
        Double result;
        if (ABS.equals(function)) {
            result = Math.abs((Double)arguments.next());
        } else if (CEIL.equals(function)) {
            result = Math.ceil((Double)arguments.next());
        } else if (FLOOR.equals(function)) {
            result = Math.floor((Double)arguments.next());
        } else if (ROUND.equals(function)) {
            Double arg = (Double)arguments.next();
            if (arg != -1.0D / 0.0 && arg != 1.0D / 0.0) {
                result = (double)Math.round(arg);
            } else {
                result = arg;
            }
        } else if (SINEH.equals(function)) {
            result = Math.sinh((Double)arguments.next());
        } else if (COSINEH.equals(function)) {
            result = Math.cosh((Double)arguments.next());
        } else if (TANGENTH.equals(function)) {
            result = Math.tanh((Double)arguments.next());
        } else if (SINE.equals(function)) {
            result = Math.sin((Double)arguments.next());
        } else if (COSINE.equals(function)) {
            result = Math.cos((Double)arguments.next());
        } else if (TANGENT.equals(function)) {
            result = Math.tan((Double)arguments.next());
        } else if (ACOSINE.equals(function)) {
            result = Math.acos((Double)arguments.next());
        } else if (ASINE.equals(function)) {
            result = Math.asin((Double)arguments.next());
        } else if (ATAN.equals(function)) {
            result = Math.atan((Double)arguments.next());
        } else if (MIN.equals(function)) {
            for(result = (Double)arguments.next(); arguments.hasNext(); result = Math.min(result, (Double)arguments.next())) {
            }
        } else if (MAX.equals(function)) {
            for(result = (Double)arguments.next(); arguments.hasNext(); result = Math.max(result, (Double)arguments.next())) {
            }
        } else if (SUM.equals(function)) {
            for(result = 0.0D; arguments.hasNext(); result = result + (Double)arguments.next()) {
            }
        } else if (AVERAGE.equals(function)) {
            result = 0.0D;

            int nb;
            for(nb = 0; arguments.hasNext(); ++nb) {
                result = result + (Double)arguments.next();
            }

            result = result / (double)nb;
        } else if (LN.equals(function)) {
            result = Math.log((Double)arguments.next());
        } else if (LOG.equals(function)) {
            result = Math.log10((Double)arguments.next());
        } else if (RANDOM.equals(function)) {
            result = Math.random();
        } else {
            result = (Double)super.evaluate(function, arguments, evaluationContext);
        }

        this.errIfNaN(result, function);
        return result;
    }

    private void errIfNaN(Double result, Function function) {
        if (result.equals(0.0D / 0.0)) {
            throw new IllegalArgumentException("Invalid argument passed to " + function.getName());
        }
    }
}
