package evaluator.extension;

import com.fathzer.soft.javaluator.*;

import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class EvaluatorExtension<T> extends AbstractEvaluator<T> {

    public static final Constant PI = new Constant("pi");
    public static final Constant E = new Constant("e");
    public static final Function CEIL = new Function("ceil", 1);
    public static final Function FLOOR = new Function("floor", 1);
    public static final Function ROUND = new Function("round", 1);
    public static final Function ABS = new Function("abs", 1);
    public static final Function SINE = new Function("sin", 1);
    public static final Function COSINE = new Function("cos", 1);
    public static final Function TANGENT = new Function("tan", 1);
    public static final Function ACOSINE = new Function("acos", 1);
    public static final Function ASINE = new Function("asin", 1);
    public static final Function ATAN = new Function("atan", 1);
    public static final Function SINEH = new Function("sinh", 1);
    public static final Function COSINEH = new Function("cosh", 1);
    public static final Function TANGENTH = new Function("tanh", 1);
    public static final Function MIN = new Function("min", 1, 2147483647);
    public static final Function MAX = new Function("max", 1, 2147483647);
    public static final Function SUM = new Function("sum", 1, 2147483647);
    public static final Function AVERAGE = new Function("avg", 1, 2147483647);
    public static final Function LN = new Function("ln", 1);
    public static final Function LOG = new Function("log", 1);
    public static final Function RANDOM = new Function("random", 0);
    public static final Operator NEGATE;
    public static final Operator NEGATE_HIGH;
    public static final Operator MINUS;
    public static final Operator PLUS;
    public static final Operator MULTIPLY;
    public static final Operator DIVIDE;
    public static final Operator EXPONENT;
    public static final Operator MODULO;
    protected static final Operator[] OPERATORS;
    protected static final Operator[] OPERATORS_EXCEL;
    protected static final Function[] FUNCTIONS;
    protected static final Constant[] CONSTANTS;
    protected static Parameters DEFAULT_PARAMETERS;
    protected static final Pattern SCIENTIFIC_NOTATION_PATTERN;
    protected static final ThreadLocal<NumberFormat> FORMATTER;
    protected boolean supportsScientificNotation;

    static {
        NEGATE = new Operator("-", 1, Operator.Associativity.RIGHT, 3);
        NEGATE_HIGH = new Operator("-", 1, Operator.Associativity.RIGHT, 5);
        MINUS = new Operator("-", 2, Operator.Associativity.LEFT, 1);
        PLUS = new Operator("+", 2, Operator.Associativity.LEFT, 1);
        MULTIPLY = new Operator("*", 2, Operator.Associativity.LEFT, 2);
        DIVIDE = new Operator("/", 2, Operator.Associativity.LEFT, 2);
        EXPONENT = new Operator("^", 2, Operator.Associativity.LEFT, 4);
        MODULO = new Operator("%", 2, Operator.Associativity.LEFT, 2);
        OPERATORS = new Operator[]{NEGATE, MINUS, PLUS, MULTIPLY, DIVIDE, EXPONENT, MODULO};
        OPERATORS_EXCEL = new Operator[]{NEGATE_HIGH, MINUS, PLUS, MULTIPLY, DIVIDE, EXPONENT, MODULO};
        FUNCTIONS = new Function[]{SINE, COSINE, TANGENT, ASINE, ACOSINE, ATAN, SINEH, COSINEH, TANGENTH, MIN, MAX, SUM, AVERAGE, LN, LOG, ROUND, CEIL, FLOOR, ABS, RANDOM};
        CONSTANTS = new Constant[]{PI, E};
        SCIENTIFIC_NOTATION_PATTERN = Pattern.compile("([+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)[eE][+-]?\\d+)$");
        FORMATTER = new ThreadLocal<NumberFormat>() {
            protected NumberFormat initialValue() {
                return NumberFormat.getNumberInstance(Locale.US);
            }
        };
    }

    public static enum Style {
        STANDARD,
        EXCEL;

        private Style() {
        }
    }

    public EvaluatorExtension(Parameters parameters) {
        super(parameters);
    }

    public EvaluatorExtension(Parameters parameters, boolean supportsScientificNotation) {
        super(parameters);
        this.supportsScientificNotation = supportsScientificNotation;
    }

    protected EvaluatorExtension() {
        super(getParameters());
    }

    private static Parameters getParameters() {
        if (DEFAULT_PARAMETERS == null) {
            DEFAULT_PARAMETERS = getDefaultParameters();
        }

        return DEFAULT_PARAMETERS;
    }

    public static Parameters getDefaultParameters() {
        return getDefaultParameters(DoubleEvaluator.Style.STANDARD);
    }

    public static Parameters getDefaultParameters(DoubleEvaluator.Style style) {
        Parameters result = new Parameters();
        result.addOperators(style == DoubleEvaluator.Style.STANDARD ? Arrays.asList(OPERATORS) : Arrays.asList(OPERATORS_EXCEL));
        result.addFunctions(Arrays.asList(FUNCTIONS));
        result.addConstants(Arrays.asList(CONSTANTS));
        result.addFunctionBracket(BracketPair.PARENTHESES);
        result.addExpressionBracket(BracketPair.PARENTHESES);
        return result;
    }

    protected Iterator<String> tokenize(String expression) {
        if (!this.supportsScientificNotation) {
            return super.tokenize(expression);
        } else {
            List<String> tokens = new ArrayList();
            Iterator rawTokens = super.tokenize(expression);

            while(rawTokens.hasNext()) {
                tokens.add(String.valueOf(rawTokens.next()));
            }

            for(int i = 1; i < tokens.size() - 1; ++i) {
                this.testScientificNotation(tokens, i);
            }

            return tokens.iterator();
        }
    }

    private void testScientificNotation(List<String> tokens, int index) {
        String previous = (String)tokens.get(index - 1);
        String next = (String)tokens.get(index + 1);
        String current = (String)tokens.get(index);
        String candidate = previous + current + next;
        if (isScientificNotation(candidate)) {
            tokens.set(index - 1, candidate);
            tokens.remove(index);
            tokens.remove(index);
        }
    }

    public static boolean isScientificNotation(String str) {
        Matcher matcher = SCIENTIFIC_NOTATION_PATTERN.matcher(str);
        if (!matcher.find()) {
            return false;
        } else {
            String matched = matcher.group();
            return matched.length() == str.length();
        }
    }
}
