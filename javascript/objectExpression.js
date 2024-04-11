"use strict"

const variables = ['x', 'y', 'z'];

function Const(value) {
    this.evaluate = function () {
        return Number(value);
    };
    this.toString = function () {
        return value.toString();
    };
    this.prefix = function () {
        return value.toString();
    };
}

function Variable(name) {
    this.evaluate = function (...args) {
        return (args[variables.indexOf(name)]);
    };
    this.toString = function () {
        return name;
    };
    this.prefix = function () {
        return name;
    };
}

let operations = new Map();

function operationFactory(evaluateFunc, symbol) {
    function Operation(...operands) {
        this.operands = operands;
    }
    Operation.prototype.constructor = Operation;
    Operation.prototype.evaluate = function (...args) {
        return evaluateFunc(...this.operands.map(operand => operand.evaluate(...args)));
    };
    Operation.prototype.toString = function () {
        return this.operands.map(operand => operand.toString()).join(" ") + " " + symbol;
    };
    Operation.prototype.prefix = function () {
        return "(" + symbol + " " + this.operands.map(operand => operand.prefix()).join(" ") + ")";
    };
    operations.set(symbol, [Operation, evaluateFunc.length]);
    return Operation;
}

const Add = operationFactory((a, b) => a + b, "+");

const Subtract = operationFactory((a, b) => a - b, "-");

const Multiply = operationFactory((a, b) => a * b, "*");

const Divide = operationFactory((a, b) => a / b, "/");

const Negate = operationFactory(a => -a, "negate");

const Sinh = operationFactory(Math.sinh, "sinh");

const Cosh = operationFactory(Math.cosh, "cosh");

const Product = operationFactory((...args) => args.reduce((a, b) => a * b, 1), "product");

const Geom = operationFactory((...args) => {
    const product = args.reduce((a, b) => Math.abs(a * b), 1);
    return Math.pow(product, 1 / args.length);
}, "geom");

function parse(expression) {
    const tokens = expression.split(" ").filter(token => token.length);
    const stack = [];
    tokens.forEach(token => {
        if (!isNaN(token)) {
            stack.push(new Const(token));
        } else {
            const operationSignature = operations.get(token);
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

function customErrorFactory(name) {
    function CustomError(message) {
        Error.call(this, message);
        this.name = name;
        this.message = message;
    }
    CustomError.prototype = Object.create(Error.prototype);
    CustomError.prototype.constructor = CustomError;
    return CustomError;
}

const IncorrectBracketSequenceError = customErrorFactory("IncorrectBracketSequenceError");

const UnknownOperationError = customErrorFactory("UnknownOperationError");

const UnknownVariableError = customErrorFactory("UnknownVariableError");

const InvalidExpression = customErrorFactory("InvalidExpression");

const InvalidArgumentsError = customErrorFactory("InvalidArgumentsError");

const EmptyInputError = customErrorFactory("EmptyInputError");

const MissingOperationError = customErrorFactory("MissingOperationError");

function isCorrectBracketSequence(expression) {
    let balance = 0;
    for (let i = 0; i < expression.length; i++) {
        if (expression[i] === '(') {
            balance++;
        } else if (expression[i] === ')') {
            balance--;
        }
        if (balance < 0) {
            throw new IncorrectBracketSequenceError("Unmatched bracket '${expression[i]}' found on position '${i}'");
        }
    }
    return balance === 0;
}

function parsePrefix(expression) {
    if (expression.length === 0) {
        throw new EmptyInputError("Empty input");
    }
    let i = 0;
    isCorrectBracketSequence(expression);
    let tokens = expression.match(/\(|\)|[^\s()]+/g);
    if (tokens.length > 1 && variables.includes(tokens[0])) {
        throw new InvalidExpression(`Expression cannot start with variable (${tokens[0]})`);
    }

    function parseToken(tokens) {
        let token = tokens.shift();
        if (token === '(') {
            let op = tokens.shift();
            let operands = [];
            while (tokens[0] !== ')') {
                operands.push(parseToken(tokens));
            }
            tokens.shift();
            if (operations.get(op) === undefined) {
                throw new UnknownOperationError("Unknown operation: " + op);
            }
<<<<<<< HEAD
            if (operations.get(op)[1] !== 0 && operands.length !== operations.get(op)[1]) {
                throw new InvalidArgumentsError(`Operation "${op}" should have ${operations.get(op)[1]} operands`);
=======
            if (operations.get(op)[1] !== undefined && operands.length !== operations.get(op)[1]) {
                throw new InvalidArgumentsError(`Operation ${op} should have ${operations[op][1]} operands`);
>>>>>>> d770cbdd862d397e50deba3345adb9b86b2d09af
            }
            const operationSignature = operations.get(op);
            const Op = operationSignature[0];
            return new Op(...operands);
        } else if (!isNaN(token)) {
            return new Const(token);
        } else {
            if (!(variables.includes(token))) {
                throw new UnknownVariableError(`Unknown variable: ${token}`);
            }
            return new Variable(token);
        }
    }

    let res = parseToken(tokens);
    if (tokens.length > 0) {
        throw new InvalidExpression(`Unexpected tokens after valid expression: ${tokens.join(" ")}`);
    }
    return res;
}
