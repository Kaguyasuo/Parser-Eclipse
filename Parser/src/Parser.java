/* Complete all the methods.
EBNF of Mini Language
Program" --> "("Sequence State")".
Sequence --> "("Statements")".
Statements --> Stmt*
Stmt --> "(" {NullStatement | Assignment | Conditional | Loop | Block}")".
State -->  "("Pairs")".
Pairs -->  Pair*.
Pair --> "("Identifier Literal")".
NullStatement --> "skip".
Assignment --> "assign" Identifier Expression.
Conditional --> "conditional" Expression Stmt Stmt.
Loop --> "loop" Expression Stmt.
Block --> "block" Statements.
Expression --> Identifier | Literal | "("Operation Expression Expression")".
Operation --> "+" |"-" | "*" | "/" | "<" | "<=" | ">" | ">=" | "=" | "!=" | "or" | "and".

Note: Treat Identifier and Literal as terminal symbols. Every symbol inside " and " is a terminal symbol. The rest are non terminals.

*/

// @Author William Horton
public class Parser {
    private Token currentToken;
    Scanner scanner;

    private void accept(byte expectedKind) {
        if (currentToken.kind == expectedKind)
            currentToken = scanner.scan();
        else
            new Error("Syntax error: " + currentToken.spelling + " is not expected.",
                    currentToken.line);
    }

    private void acceptIt() {
        currentToken = scanner.scan();
    }

    public void parse() {
        SourceFile sourceFile = new SourceFile();
        scanner = new Scanner(sourceFile.openFile());
        currentToken = scanner.scan();
        parseProgram();
        if (currentToken.kind != Token.EOT)
            new Error("Syntax error: Redundant characters at the end of program.",
                    currentToken.line);
    }

    //Program --> "("Sequence State")".
    private void parseProgram() {
        accept(Token.LPAREN);
        parseSequence();
        parseState();
        accept(Token.RPAREN);
    }

    //Sequence --> "("Statements")".
    private void parseSequence() {
        accept(Token.LPAREN);
        parseStatements();
        accept(Token.RPAREN);
    }

    //Statements --> Stmt*
    private void parseStatements() {
        while (currentToken.kind == Token.LPAREN) {
            parseStmt();
        }
    }

    //Stmt --> "(" {NullStatement | Assignment | Conditional | Loop | Block}")".
    private void parseStmt() {
        accept(Token.LPAREN);
        switch (currentToken.kind) {
            case Token.SKIP:
                parseNullStatement();
                break;
            case Token.ASSIGN:
                parseAssignment();
                break;
            case Token.CONDITIONAL:
                parseConditional();
                break;
            case Token.LOOP:
                parseLoop();
                break;
            case Token.BLOCK:
                parseBlock();
                break;
            default:
                new Error("Syntax error: Invalid statement type", currentToken.line);
        }
        accept(Token.RPAREN);
    }

    //State --> "("Pairs")".
    private void parseState() {
        accept(Token.LPAREN);
        parsePairs();
        accept(Token.RPAREN);
    }

    //Pairs --> Pair*
    private void parsePairs() {
        while (currentToken.kind == Token.LPAREN) {
            parsePair();
        }
    }

    //Pair --> "("Identifier Literal")".
    private void parsePair() {
        accept(Token.LPAREN);
        if (currentToken.kind != Token.IDENTIFIER)
            new Error("Syntax error: Identifier expected", currentToken.line);
        acceptIt();
        if (currentToken.kind != Token.LITERAL)
            new Error("Syntax error: Literal expected", currentToken.line);
        acceptIt();
        accept(Token.RPAREN);
    }

    //NullStatement --> "skip".
    private void parseNullStatement() {
        accept(Token.SKIP);
    }

    //Assignment --> "assign" Identifier Expression.
    private void parseAssignment() {
        accept(Token.ASSIGN);
        if (currentToken.kind != Token.IDENTIFIER)
            new Error("Syntax error: Identifier expected", currentToken.line);
        acceptIt();
        parseExpression();
    }

    //Conditional --> "conditional" Expression Stmt Stmt.
    private void parseConditional() {
        accept(Token.CONDITIONAL);
        parseExpression();
        parseStmt();
        parseStmt();
    }

    //Loop --> "loop" Expression Stmt.
    private void parseLoop() {
        accept(Token.LOOP);
        parseExpression();
        parseStmt();
    }

    //Block --> "block" Statements.
    private void parseBlock() {
        accept(Token.BLOCK);
        parseStatements();
    }

    //Expression --> Identifier | Literal | "("Operation Expression Expression")".
    private void parseExpression() {
        if (currentToken.kind == Token.IDENTIFIER || currentToken.kind == Token.LITERAL) {
            acceptIt();
        } else if (currentToken.kind == Token.LPAREN) {
            accept(Token.LPAREN);
            if (currentToken.kind != Token.OPERATOR && 
                currentToken.kind != Token.AND && 
                currentToken.kind != Token.OR) {
                new Error("Syntax error: Operation expected", currentToken.line);
            }
            parseOperation(); // accept the operation
            parseExpression();
            parseExpression();
            accept(Token.RPAREN);
        } else {
            new Error("Syntax error: Invalid expression", currentToken.line);
        }
    }
    
    //Operation --> "+" |"-" | "*" | "/" | "<" | "<=" | ">" | ">=" | "=" | "!=" | "or" | "and".
    private void parseOperation() {
        switch (currentToken.kind) {
            case Token.OPERATOR:
                // Handle arithmetic and comparison operators
                switch (currentToken.spelling) {
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                    case "<":
                    case "<=":
                    case ">":
                    case ">=":
                    case "=":
                    case "!=":
                        acceptIt();
                        break;
                    default:
                        new Error("Syntax error: Invalid operator '" + currentToken.spelling + "'", 
                                currentToken.line);
                }
                break;
                
            case Token.OR:
                // Handle logical OR
                acceptIt();
                break;
                
            case Token.AND:
                // Handle logical AND
                acceptIt();
                break;
                
            default:
                new Error("Syntax error: Expected operator but found '" + currentToken.spelling + "'",
                         currentToken.line);
        }
    }
}