package test.expression;

import parser.ParseException;
import parser.Production;
import parser.Rule;

/*
 * Created on 7 Apr 2010
 */

/**
 * @author Anthony Bryant
 *
 */
public class ValueRule extends Rule<ExpressionType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ExpressionType> NUMBER_PRODUCTION = new Production<ExpressionType>(ExpressionType.NUMBER);
  private static final Production<ExpressionType> EXPRESSION_PRODUCTION = new Production<ExpressionType>(ExpressionType.LPAREN, ExpressionType.EXPRESSION, ExpressionType.RPAREN);

  @SuppressWarnings("unchecked")
  public ValueRule()
  {
    super(ExpressionType.VALUE, NUMBER_PRODUCTION, EXPRESSION_PRODUCTION);
  }

  /**
   * @see parser.Rule#match(java.lang.Object[])
   */
  @Override
  public Object match(Production<ExpressionType> production, Object[] args) throws ParseException
  {
    if (NUMBER_PRODUCTION.equals(production))
    {
      // just a number
      return new Value((Number) args[0]);
    }
    if (EXPRESSION_PRODUCTION.equals(production))
    {
      // ( ExpressionAST )
      return new Value((Expression) args[1]);
    }
    throw new IllegalArgumentException();
  }

}
