/*
1. You do not need a separate token (number) for each operator. All of them should be translated to one token (number) which is OPERATOR. See below
2. In the switch the code for identifier and literal is complete. Do not change it

3. Symbol e means epsilon.

 BNF grammar of Mini Language

 Program" --> "("Sequence State")".
 Sequence --> "("Statements")".
 Statements --> Statements  Stmt | e
 Stmt --> "(" {NullStatement | Assignment | Conditional | Loop | Block}")".
 State -->  "("Pairs")".
 Pairs -->  Pairs Pair | e.
 Pair --> "("Identifier Literal")".
 NullStatement --> "skip".
 Assignment --> "assign" Identifier Expression.
 Conditional --> "conditional" Expression Stmt Stmt.
 Loop --> "loop" Expression Stmt.
 Block --> "block" Statements.
 Expression --> Identifier | Literal | "("Operation Expression Expression")".
 Operation --> "+" |"-" | "*" | "/" | "<" | "<=" | ">" | ">=" | "=" | "!=" | "or" | "and".

 Note: Treat Identifier and Literal as terminal symbols. Every symbol inside " and " is a terminal symbol. The rest are non terminals.

Input file: test.txt
Output:
 Line: 1, spelling = [(], kind = 9
 Line: 1, spelling = [)], kind = 10
 Line: 1, spelling = [sum], kind = 0
 Line: 1, spelling = [a], kind = 0
 Line: 1, spelling = [2], kind = 1
 Line: 1, spelling = [xyz], kind = 0
 Line: 2, spelling = [skip], kind = 6
 Line: 2, spelling = [assign], kind = 2
 Line: 2, spelling = [conditional], kind = 3
 Line: 2, spelling = [loop], kind = 4
 Line: 2, spelling = [block], kind = 5
 Line: 3, spelling = [1234], kind = 1
 Line: 4, spelling = [+], kind = 11
 Line: 4, spelling = [-], kind = 11
 Line: 4, spelling = [*], kind = 11
 Line: 4, spelling = [/], kind = 11
 Line: 4, spelling = [<], kind = 11
 Line: 4, spelling = [<=], kind = 11
 Line: 4, spelling = [>], kind = 11
 Line: 4, spelling = [>=], kind = 11
 Line: 4, spelling = [=], kind = 11
 Line: 4, spelling = [!=], kind = 11
 Line: 4, spelling = [or], kind = 8
 Line: 4, spelling = [and], kind = 7
 Line: 5, spelling = [-], kind = 11
 Line: 5, spelling = [1234], kind = 1
 Line 6: wrong token !

Note: After you get an error message for the symbol = remove this symbol and
run the program. Repeat this until the last wrong token which is: ?

You should get the following error messges:
 Line 6: wrong token !
 Line 6: wrong token ?
*/

// @author William Horton
import java.io.*;

public class Scanner{
  private char currentChar;
  private byte currentKind;
  private StringBuffer currentSpelling;
  private BufferedReader inFile;
  private static int line = 1;

  public Scanner(BufferedReader inFile){
    this.inFile = inFile;
    try{
      int i = this.inFile.read();
      if(i == -1) //end of file
        currentChar = '\u0000';
      else
        currentChar = (char)i;
    }
    catch(IOException e){
        System.out.println(e);
    }
  }

  private void takeIt(){
    currentSpelling.append(currentChar);
    try{
      int i = inFile.read();
      if(i == -1) //end of file
        currentChar = '\u0000';
      else
        currentChar = (char)i;
    }
    catch(IOException e){
        System.out.println(e);
    }
  }

  private void discard(){
    try{
      int i = inFile.read();
      if(i == -1) //end of file
        currentChar = '\u0000';
      else
        currentChar = (char)i;
    }
    catch(IOException e){
        System.out.println(e);
    }
  }

  private byte scanToken() {
	  byte kind;
      switch(currentChar) {
          case '\u0000': 
              kind = Token.EOT;
              return kind;
              
          case '(':
              takeIt();
              kind = Token.LPAREN;  // 9
              System.out.println("Line: " + line + ", spelling = [" + currentSpelling + "], kind = " + kind);
              return kind;
              
          case ')':
              takeIt();
              kind = Token.RPAREN;  // 10
              System.out.println("Line: " + line + ", spelling = [" + currentSpelling + "], kind = " + kind);
              return kind;
              
          // Handle minus sign for negative numbers
          case '-':
              takeIt();
              if (isDigit(currentChar)) {
                  while (isDigit(currentChar))
                      takeIt();
                  kind = Token.LITERAL;  // 1
              } else {
                  kind = Token.OPERATOR;  // 11
              }
              System.out.println("Line: " + line + ", spelling = [" + currentSpelling + "], kind = " + kind);
              return kind;

          // Handle operators
          case '+': case '*': case '/': case '=':
              takeIt();
                  
              kind = Token.OPERATOR;  // 11
              System.out.println("Line: " + line + ", spelling = [" + currentSpelling + "], kind = " + kind);
              return kind;
          case '<': case '>': 
        	  takeIt();
        	  if (currentChar == '=') {
        		  takeIt();
        	  } 
        	  return Token.OPERATOR;
          case '!': 
        	  takeIt();
        	  if (currentChar == '=') {
        		  return Token.OPERATOR;
        	  } 
        	  new Error("wrong token " + "[!]", line);
          // Handle literals (numbers)
          case '0': case '1': case '2': case '3': case '4':
          case '5': case '6': case '7': case '8': case '9':
              takeIt();
              while (isDigit(currentChar))
                  takeIt();
              kind = Token.LITERAL;  // 1
              System.out.println("Line: " + line + ", spelling = [" + currentSpelling + "], kind = " + kind);
              return kind;
              
          default:
              // Handle identifiers and keywords
              if (isLetter(currentChar)) {
                  takeIt();
                  while (isLetter(currentChar) || isDigit(currentChar))
                      takeIt();
                      
                  String spelling = currentSpelling.toString();
                  switch(spelling) {
                      case "assign":
                          kind = Token.ASSIGN;      // 2
                          break;
                      case "conditional":
                          kind = Token.CONDITIONAL; // 3
                          break;
                      case "loop":
                          kind = Token.LOOP;        // 4
                          break;
                      case "block":
                          kind = Token.BLOCK;       // 5
                          break;
                      case "skip":
                          kind = Token.SKIP;        // 6
                          break;
                      case "and":
                          kind = Token.AND;         // 7
                          break;
                      case "or":
                          kind = Token.OR;          // 8
                          break;
                      default:
                          kind = Token.IDENTIFIER;  // 0
                          break;
                  }
                  System.out.println("Line: " + line + ", spelling = [" + currentSpelling + "], kind = " + kind);
                  return kind;
              }
              
              // Invalid token
              new Error("wrong token " + currentChar, line);
              return Token.EOT; // This line won't execute due to System.exit in Error
      }
  }
  

  private void scanSeparator(){
    switch(currentChar){
      case ' ': case '\n': case '\r': case '\t':
        if(currentChar == '\n')
          line++;
        discard();
    }
  }

  public Token scan(){
    currentSpelling = new StringBuffer("");
    while(currentChar == ' ' || currentChar == '\n' || currentChar == '\r')
      scanSeparator();
    currentKind = scanToken();
    return new Token(currentKind, currentSpelling.toString(), line);
  }

  private boolean isGraphic(char c){
    return c == '\t' ||(' ' <= c && c <= '~');
  }

  private boolean isDigit(char c){
    return '0' <= c && c <= '9';
  }

  private boolean isLetter(char c){
    return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
  }
}
