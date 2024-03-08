package hw06;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public record Expr(String text) {

    static ArrayDeque<String> stack;

    int eval() {
        stack = new ArrayDeque<>();

        for (var cdpt : text.chars().toArray()) {
            var ch = Character.toString(cdpt);

            if (isSpace(ch) || ch.equals("(")) {
                continue;
            }

            if (isOperator(ch)) {
                stack.push(ch);
                continue;
            }

            if (isDigits(ch)) {
                if (isDigits(stack.peek())) {
                    String num = stack.pop();
                    var xx = num + ch;
                    stack.push(xx);
                } else {
                    stack.push(ch);
                }
                continue;
            }

            if (ch.equals(")")) {    5
                ArrayList<String> values = new ArrayList<>();
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    values.add(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop(); // Remove "("
                }
                Collections.reverse(values);
                String op = null;
                for (String val : values) {
                    if (isOperator(val)) {
                        op = val;
                    } else {
                        if (op != null) {
                            String num1 = stack.pop();
                            stack.push(applyOp(num1, op, val));
                            op = null;
                        } else {
                            stack.push(val);
                        }
                    }
                }
                continue;
            }
        }

        try {
            return Integer.parseInt(stack.pop());
        } catch (NumberFormatException ee) {
            dumpStack();
            throw new RuntimeException("expected number: " + ee.toString());
        }
    }

    static boolean isSpace(String xx) {
        return xx != null && Pattern.matches("^\\s+$", xx);
    }

    static boolean isDigits(String xx) {
        return xx != null && Pattern.matches("^[0-9]+$", xx);
    }

    static boolean isOperator(String xx) {
        String ops = "+-*/";
        return xx != null && ops.contains(xx);
    }

    static String applyOp(String a1, String op, String a2) {
        try {
            var xx = Integer.parseInt(a1);
            var yy = Integer.parseInt(a2);

            if (op.equals("+")) {
                return Integer.toString(xx + yy);
            }

            if (op.equals("-")) {
                return Integer.toString(xx - yy);
            }

            if (op.equals("*")) {
                return Integer.toString(xx * yy);
            }

            if (op.equals("/")) {
                return Integer.toString(xx / yy);
            }

            dumpStack();
            throw new RuntimeException("bad operator: " + op);
        } catch (NumberFormatException ee) {
            dumpStack();
            throw new RuntimeException("bad number: " + ee.toString());
        }
    }

    static void dumpStack() {
        var xs = new ArrayList<>(stack);
        Collections.reverse(xs);
        for (var xx : xs) {
            System.out.println(xx);
        }
    }
}
