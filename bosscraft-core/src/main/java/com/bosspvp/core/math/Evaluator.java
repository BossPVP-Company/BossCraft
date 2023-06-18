package com.bosspvp.core.math;

import com.bosspvp.api.placeholders.PlaceholderManager;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;
import redempt.crunch.functional.Function;

public class Evaluator {
    private EvaluationEnvironment environment = new EvaluationEnvironment();
    public Evaluator(){
        environment.addFunction(
                new Function(
                        "min",
                        2,
                        (it)->Math.min(it[0],it[1])
                )
        );
    }

    public double evaluate(String expression, PlaceholderContext context){
        return Crunch.compileExpression(PlaceholderManager.translatePlaceholders(expression,context),
                        environment
                )
                .evaluate();
    }

}
