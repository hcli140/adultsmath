package main.net.adultsmath;


import java.util.ArrayList;
import java.util.Arrays;

//  can either hold a number, a variable, an operator on two expressions
public class Expression {
    enum Types {
        NUM,VAR,NULL,
        ADD,SUBTRACT,
        MULTIPLY,DIVIDE,
        POWER,ROOT
    }

    private final Types type;
    private final RealNumber value;
    private final Variable var;
    private final int tier;
    private final ArrayList<Expression> operands;
    private final ArrayList<Variable> containedVars;
    private final ArrayList<Types> containedOperations;

    //  acting as a number
    public Expression (double a) {
        this.type = Types.NUM;
        this.value = new RealNumber(a);
        this.var = null;
        this.tier = 0;
        this.operands = null;
        this.containedVars = null;
        this.containedOperations = null;
    }
    public Expression (RealNumber a) {
        this.type = Types.NUM;
        this.value = a;
        this.var = null;
        this.tier = 0;
        this.operands = null;
        this.containedVars = null;
        this.containedOperations = null;
    }

    //  acting as a variable
    public Expression (Variable var) {
        this.type = Types.VAR;
        this.value = null;
        this.var = var;
        this.tier = 0;
        this.operands = null;
        this.containedVars = new ArrayList<>();
        this.containedVars.add(var);
        this.containedOperations = null;
    }

    //  acting as an operator
    /*
        ADD: op1 + op2
        SUBTRACT: op1 - op2
        MULTIPLY: op1 * op2
        DIVIDE: op1 / op2
        POWER: op1 ^ op2
        ROOT: op1 ^ (1 / op2)
     */
    public Expression (Types type, Expression op1, Expression op2) {
        this.type = type;
        this.value = null;
        this.var = null;
        this.tier = registerTier(op1,op2);
        this.operands = new ArrayList<>(Arrays.asList(new Expression[]{op1, op2}));
        this.containedVars = registerContainedVars(op1,op2);
        this.containedOperations = registerContainedOperations(op1,op2);
    }

    private ArrayList<Variable> registerContainedVars (Expression... expressions) {
        ArrayList<Variable> variables = new ArrayList<>();
        ArrayList<String> thisVarKeys = new ArrayList<>();
        for (Expression expression : expressions) {
            try {
                variables.addAll(expression.containedVars);
            }
            catch (NullPointerException e) {
            }
            try {
                thisVarKeys.addAll(expression.getContainedVarKeys());
            }
            catch (NullPointerException e) {
            }
        }
        return variables;
    }

    private ArrayList<Types> registerContainedOperations (Expression... expressions) {
        ArrayList<Types> types = new ArrayList<>();
        Types expressionType;
        for (Expression expression : expressions) {
            expressionType = expression.getType();
            if (expression.isOperator() && !containedOperations.contains(expressionType)) {
                types.add(expressionType);
            }
        }
        return types;
    }

    private int registerTier (Expression... expressions) {
        int currentMax = -1;
        for (Expression expression : expressions) {
            currentMax = Math.max(expression.tier,currentMax);
        }
        return currentMax;
    }



    public Types getType() {return type;}
    public RealNumber getValue() {return value;}
    public Variable getVar() {return var;}
    public ArrayList<Variable> getContainedVars() {return containedVars;}
    public Variable getContainedVars(int index) {return containedVars.get(index);}
    public ArrayList<Expression> getOperands() {return operands;}


    public ArrayList<String> getContainedVarKeys () {
        ArrayList<String> keys = new ArrayList<>();
        for (int i = 0; i < this.containedVars.size(); i++) {
            keys.add(this.getContainedVars(i).key);
        }
        return keys;
    }



    public boolean isNull () {return this.type == Types.NULL;}
    public boolean isNumOrVar () {return isNum() || isVar();}
    public boolean isNum () {return this.type == Types.NUM;}
    public boolean isVar () {return this.type == Types.VAR;}
    public boolean isOperator () {
        return isAdd() || isSubtract() || isMultiply() || isDivide() || isPower() || isRoot();
    }
    public boolean isAdd () {return this.type == Types.ADD;}
    public boolean isSubtract () {return this.type == Types.SUBTRACT;}
    public boolean isMultiply () {return this.type == Types.MULTIPLY;}
    public boolean isDivide () {return this.type == Types.DIVIDE;}
    public boolean isPower () {return this.type == Types.POWER;}
    public boolean isRoot () {return this.type == Types.ROOT;}
    public boolean hasVars () {return this.isVar() || this.containedVars != null;}


}
