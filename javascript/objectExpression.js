"use strict"

function Const(value) {
    this.evaluate = function() { return Number(value); };
    this.toString = function() { return value.toString(); };
}

function Variable(name) {
    this.evaluate = function(...args) {
        switch (name) {
            case "x":
                return args[0];
            case "y":
                return args[1];
            case "z":
                return args[2];
        }
    };
    this.toString = function() { return name; };
}

function Operation() {}

Operation.prototype.evaluate = function(...args) {};

Operation.prototype.toString = function() {};

function operationFactory(evaluateFunc, symbol) {
    function Operation(...operands) {
        this.operands = operands;
    }
    Operation.prototype = Object.create(Operation.prototype);
    Operation.prototype.constructor = Operation;
    Operation.prototype.evaluate = function(...args) {
        return evaluateFunc(...this.operands.map(operand => operand.evaluate(...args)));
    };
    Operation.prototype.toString = function() {
        return this.operands.map(operand => operand.toString()).join(" ") + " " + symbol;
    };
    return Operation;
}

const Add = operationFactory((...args) => args.reduce((a, b) => a + b), "+");

const Subtract = operationFactory((...args) => args.reduce((a, b) => a - b), "-");

const Multiply = operationFactory((...args) => args.reduce((a, b) => a * b), "*");

const Divide = operationFactory((...args) => args.reduce((a, b) => a / b), "/");

const Negate = operationFactory(a => -a, "negate");

const Sinh = operationFactory(Math.sinh, "sinh");

const Cosh = operationFactory(Math.cosh, "cosh");

const operations = {
    "+": [Add, 2],
    "-": [Subtract, 2],
    "*": [Multiply, 2],
    "/": [Divide, 2],
    "negate": [Negate, 1],
    "sinh": [Sinh, 1],
    "cosh": [Cosh, 1]
};

function parse(expression) {
    const tokens = expression.split(" ").filter(token => token.length);
    const stack = [];
    tokens.forEach(token => {
        if (!isNaN(token)) {
            stack.push(new Const(token));
        } else {
            const operationSignature = operations[token];
            if (operationSignature === undefined) {
                stack.push(new Variable(token));
            } else {
                const Op = operationSignature[0];
                const arity = operationSignature[1];
                let operands = [];
                for (let i = 0; i < arity; i++) {
                    operands.unshift(stack.pop());
                }
                stack.push(new Op(...operands));
            }
        }
    });
    return stack.pop();
}