package compiler.language.parser.rules.misc;

import static compiler.language.parser.ParseType.ARGUMENT;
import static compiler.language.parser.ParseType.AT;
import static compiler.language.parser.ParseType.EQUALS;
import static compiler.language.parser.ParseType.EXPRESSION_NO_TUPLE;
import static compiler.language.parser.ParseType.NAME;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.LexicalPhrase;
import compiler.language.ast.expression.ExpressionAST;
import compiler.language.ast.misc.DefaultArgumentAST;
import compiler.language.ast.misc.NormalArgumentAST;
import compiler.language.ast.terminal.NameAST;
import compiler.language.parser.ParseType;

/*
 * Created on 31 Jul 2010
 */

/**
 * @author Anthony Bryant
 */
public final class ArgumentRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> NORMAL_PRODUCTION = new Production<ParseType>(EXPRESSION_NO_TUPLE);
  private static final Production<ParseType> DEFAULT_PRODUCTION = new Production<ParseType>(AT, NAME, EQUALS, EXPRESSION_NO_TUPLE);

  @SuppressWarnings("unchecked")
  public ArgumentRule()
  {
    super(ARGUMENT, NORMAL_PRODUCTION, DEFAULT_PRODUCTION);
  }

  /**
   * @see parser.Rule#match(parser.Production, java.lang.Object[])
   */
  @Override
  public Object match(Production<ParseType> production, Object[] args) throws ParseException
  {
    if (NORMAL_PRODUCTION.equals(production))
    {
      ExpressionAST expression = (ExpressionAST) args[0];
      return new NormalArgumentAST(expression, expression.getLexicalPhrase());
    }
    if (DEFAULT_PRODUCTION.equals(production))
    {
      NameAST name = (NameAST) args[1];
      ExpressionAST expression = (ExpressionAST) args[3];
      return new DefaultArgumentAST(name, expression, LexicalPhrase.combine((LexicalPhrase) args[0], name.getLexicalPhrase(), (LexicalPhrase) args[2], expression.getLexicalPhrase()));
    }
    throw badTypeList();
  }

}
