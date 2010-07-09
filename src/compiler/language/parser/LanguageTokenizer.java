package compiler.language.parser;

import static compiler.language.parser.ParseType.ABSTRACT_KEYWORD;
import static compiler.language.parser.ParseType.CLASS_KEYWORD;
import static compiler.language.parser.ParseType.COMMA;
import static compiler.language.parser.ParseType.DOT;
import static compiler.language.parser.ParseType.EQUALS;
import static compiler.language.parser.ParseType.EXPRESSION;
import static compiler.language.parser.ParseType.EXTENDS_KEYWORD;
import static compiler.language.parser.ParseType.HASH;
import static compiler.language.parser.ParseType.IMMUTABLE_KEYWORD;
import static compiler.language.parser.ParseType.IMPLEMENTS_KEYWORD;
import static compiler.language.parser.ParseType.IMPORT_KEYWORD;
import static compiler.language.parser.ParseType.INTERFACE_KEYWORD;
import static compiler.language.parser.ParseType.LANGLE;
import static compiler.language.parser.ParseType.LBRACE;
import static compiler.language.parser.ParseType.NAME;
import static compiler.language.parser.ParseType.PACKAGE_KEYWORD;
import static compiler.language.parser.ParseType.PRIVATE_KEYWORD;
import static compiler.language.parser.ParseType.PUBLIC_KEYWORD;
import static compiler.language.parser.ParseType.RANGLE;
import static compiler.language.parser.ParseType.RBRACE;
import static compiler.language.parser.ParseType.SEMICOLON;
import static compiler.language.parser.ParseType.STATIC_KEYWORD;

import compiler.language.ast.Expression;
import compiler.language.ast.Name;
import compiler.parser.Token;
import compiler.parser.Tokenizer;

/*
 * Created on 30 Jun 2010
 */

/**
 * @author Anthony Bryant
 */
public class LanguageTokenizer extends Tokenizer
{

  private int state = 0;

  private static final Token[] TEST1 =
  {
    new Token(PACKAGE_KEYWORD, null),
    new Token(NAME, new Name("bryant")),
    new Token(DOT, null),
    new Token(NAME, new Name("anthony")),
    new Token(DOT, null),
    new Token(NAME, new Name("packageName")),
    new Token(SEMICOLON, null),

    new Token(IMPORT_KEYWORD, null),
    new Token(NAME, new Name("test")),
    new Token(DOT, null),
    new Token(NAME, new Name("import")),
    new Token(SEMICOLON, null),

    new Token(IMPORT_KEYWORD, null),
    new Token(NAME, new Name("test")),
    new Token(DOT, null),
    new Token(NAME, new Name("second")),
    new Token(DOT, null),
    new Token(NAME, new Name("import")),
    new Token(SEMICOLON, null),

    new Token(PUBLIC_KEYWORD, null),
    new Token(ABSTRACT_KEYWORD, null),
    new Token(CLASS_KEYWORD, null),
    new Token(NAME, new Name("Test")),
    new Token(LANGLE, null),
    new Token(NAME, new Name("K")),
    new Token(EXTENDS_KEYWORD, null),
    new Token(NAME, new Name("Object")),
    new Token(COMMA, null),
    new Token(NAME, new Name("V")),
    new Token(RANGLE, null),
    new Token(EXTENDS_KEYWORD, null),
    new Token(HASH, null),
    new Token(NAME, new Name("Object")),
    new Token(IMPLEMENTS_KEYWORD, null),
    new Token(HASH, null),
    new Token(NAME, new Name("test")),
    new Token(DOT, null),
    new Token(NAME, new Name("Interface")),
    new Token(COMMA, null),
    new Token(NAME, new Name("other")),
    new Token(DOT, null),
    new Token(NAME, new Name("Interface")),
    new Token(LBRACE, null),

    new Token(PUBLIC_KEYWORD, null),
    new Token(STATIC_KEYWORD, null),
    new Token(CLASS_KEYWORD, null),
    new Token(NAME, new Name("NestedTest")),
    new Token(EXTENDS_KEYWORD, null),
    new Token(NAME, new Name("SecondBase")),
    new Token(LBRACE, null),
    new Token(RBRACE, null),

    new Token(RBRACE, null),

    new Token(PACKAGE_KEYWORD, null),
    new Token(IMMUTABLE_KEYWORD, null),
    new Token(INTERFACE_KEYWORD, null),
    new Token(NAME, new Name("Test")),
    new Token(EXTENDS_KEYWORD, null),
    new Token(NAME, new Name("Base")),
    new Token(COMMA, null),
    new Token(HASH, null),
    new Token(NAME, new Name("test")),
    new Token(DOT, null),
    new Token(NAME, new Name("Interface")),
    new Token(COMMA, null),
    new Token(HASH, null),
    new Token(NAME, new Name("other")),
    new Token(DOT, null),
    new Token(NAME, new Name("Interface")),
    new Token(LBRACE, null),

    new Token(PRIVATE_KEYWORD, null),
    new Token(NAME, new Name("TestType")),
    new Token(NAME, new Name("variable")),
    new Token(COMMA, null),
    new Token(NAME, new Name("var2")),
    new Token(EQUALS, null),
    new Token(EXPRESSION, new Expression()),
    new Token(SEMICOLON, null),

    new Token(RBRACE, null),
  };

  private Token[] tokens = TEST1;

  /**
   * @see compiler.parser.Tokenizer#generateToken()
   */
  @Override
  protected Token generateToken()
  {
    if (state < tokens.length)
    {
      state++;
      return tokens[state - 1];
    }

    return null;
  }

}