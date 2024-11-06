public class Main{
  public static void main(String[] args){
	  
    SourceFile sourceFile = new SourceFile();
    Scanner scanner = new Scanner(sourceFile.openFile());
    Token token;
    
    // Scanner - Comment out if you want the parser, just scans the program for characters
    do {
      token = scanner.scan();
    } while (token.kind != Token.EOT);
	
    // Parser - checks syntax, grammar and scans
    Parser p = new Parser();
    p.parse();
    System.out.println("The syntax of the source program is correct.");
  }
}
