/*
 * Copyright (c) 2018 Helmut Neemann.
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.hdl.model2.expression;


import de.neemann.digital.hdl.model2.HDLNet;
import de.neemann.digital.hdl.printer.CodePrinter;

import java.io.IOException;

/**
 * Represents a NOT operation
 */
public class ExprNot implements Expression {

    private Expression expr;

    /**
     * Creates a new NOT expression
     *
     * @param expr the enxpression to invert
     */
    public ExprNot(Expression expr) {
        this.expr = expr;
    }

    /**
     * @return the expression
     */
    public Expression getExpression() {
        return expr;
    }

    @Override
    public void print(CodePrinter out) throws IOException {
        out.print("NOT ");
        expr.print(out);
    }

    @Override
    public void replace(HDLNet net, Expression expression) {
        if (isVar(expr, net))
            expr = expression;
        else
            expr.replace(net, expression);
    }

    /**
     * Help er to check if a expression is a net reference
     *
     * @param expr the expression to check
     * @param net  the net
     * @return true if the expression is a reference to the given net
     */
    static boolean isVar(Expression expr, HDLNet net) {
        return expr instanceof ExprVar && ((ExprVar) expr).getNet() == net;
    }
}
