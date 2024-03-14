"use strict"

const calculate = operation => (...operands) => (...args) => operation(...operands.map(operand => operand(...args)));

const cnst = constant => () => constant;

const variable = argument => (...args) => {
    switch (argument) {
        case "x" :
            return args[0];
        case "y" :
            return args[1];
        case "z" :
            return args[2];
        default :
            console.error("Unsupported variable type: " + argument);
            return undefined;
    }
}

const add = calculate((firstOperand, secondOperand) => firstOperand + secondOperand);

const subtract = calculate((firstOperand, secondOperand) => firstOperand - secondOperand);

const multiply = calculate((firstOperand, secondOperand) => firstOperand * secondOperand);

const divide = calculate((firstOperand, secondOperand) => firstOperand / secondOperand);

const negate = calculate((value) => -value);

const pi = cnst(Math.PI);

const e = cnst(Math.E);

const cube = calculate((value) => value * value * value);

const cbrt = calculate((value) => Math.cbrt(value));

// little test
let expression = add(
            subtract(
                multiply(
                    variable("x"),
                    variable("x")
                ),
                multiply(
                    cnst(2),
                    variable("x")
                ),
            ),
            cnst(1)
           );

for (let x = 0; x <= 10; x++) {
    console.log(x, expression(x));
}
