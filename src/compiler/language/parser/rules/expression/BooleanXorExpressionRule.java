package compiler.language.parser.rules.expression;

import static compiler.language.parser.ParseType.BOOLEAN_AND_EXPRESSION;
import static compiler.language.parser.ParseType.BOOLEAN_XOR_EXPRESSION;
import static compiler.language.parser.ParseType.DOUBLE_CARET;
import static compiler.language.parser.ParseType.QNAME_OR_LESS_THAN_EXPRESSION;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.LexicalPhrase;
import compiler.language.ast.expression.BooleanXorExpressionAST;
import compiler.language.ast.expression.ExpressionAST;
import compiler.language.parser.ParseType;

/*
 * Created on 4 Aug 2010
 */

/**
 * @author Anthony Bryant
 */
public final class BooleanXorExpressionRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> START_PRODUCTION           = new Production<ParseType>(BOOLEAN_AND_EXPRESSION);
  private static final Production<ParseType> XOR_PRODUCTION             = new Production<ParseType>(BOOLEAN_XOR_EXPRESSION,        DOUBLE_CARET, BOOLEAN_AND_EXPRESSION);
  private static final Production<ParseType> XOR_QNAME_PRODUCTION       = new Production<ParseType>(BOOLEAN_XOR_EXPRESSION,        DOUBLE_CARET, QNAME_OR_LESS_THAN_EXPRESSION);
  private static final Production<ParseType> QNAME_XOR_PRODUCTION       = new Production<ParseType>(QNAME_OR_LESS_THAN_EXPRESSION, DOUBLE_CARET, BOOLEAN_AND_EXPRESSION);
  private static final Production<ParseType> QNAME_XOR_QNAME_PRODUCTION = new Production<ParseType>(QNAME_OR_LESS_THAN_EXPRESSION, DOUBLE_CARET, QNAME_OR_LESS_THAN_EXPRESSION);

  @SuppressWarnings("unchecked")
  public BooleanXorExpressionRule()
  {
    super(BOOLEAN_XOR_EXPRESSION, START_PRODUCTION, XOR_PRODUCTION, XOR_QNAME_PRODUCTION, QNAME_XOR_PRODUCTION, QNAME_XOR_QNAME_PRODUCTION);
  }

  /**
   * {@inheritDoc}
   * @see parser.Rule#match(parser.Production, java.lang.Object[])
   */
  @Override
  public Object match(Production<ParseType> production, Object[] args) throws ParseException
  {
    if (START_PRODUCTION.equals(production))
    {
      // return the existing ExpressionAST
      return args[0];
    }
    if (XOR_PRODUCTION.equals(production) || XOR_QNAME_PRODUCTION.equals(production) || QNAME_XOR_PRODUCTION.equals(production) || QNAME_XOR_QNAME_PRODUCTION.equals(production))
    {
      ExpressionAST firstExpression = (ExpressionAST) args[0];
      ExpressionAST secondExpression = (ExpressionAST) args[2];
      return new BooleanXorExpressionAST(firstExpression, secondExpression, LexicalPhrase.combine(firstExpression.getLexicalPhrase(), (LexicalPhrase) args[1], secondExpression.getLexicalPhrase()));
    }
    throw badTypeList();
  }

}
