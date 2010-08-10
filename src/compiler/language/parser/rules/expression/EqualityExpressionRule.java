package compiler.language.parser.rules.expression;

import compiler.language.ast.ParseInfo;
import compiler.language.ast.expression.EqualityExpression;
import compiler.language.ast.expression.EqualityExpressionType;
import compiler.language.ast.expression.Expression;
import compiler.language.parser.ParseType;
import compiler.parser.ParseException;
import compiler.parser.Rule;

/*
 * Created on 4 Aug 2010
 */

/**
 * @author Anthony Bryant
 */
public class EqualityExpressionRule extends Rule
{

  private static final Object[] START_PRODUCTION = new Object[] {ParseType.RELATIONAL_EXPRESSION};
  private static final Object[] EQUALS_PRODUCTION = new Object[] {ParseType.EQUALITY_EXPRESSION, ParseType.DOUBLE_EQUALS, ParseType.RELATIONAL_EXPRESSION};
  private static final Object[] NOT_EQUALS_PRODUCTION = new Object[] {ParseType.EQUALITY_EXPRESSION, ParseType.EXCLAIMATION_MARK_EQUALS, ParseType.RELATIONAL_EXPRESSION};

  public EqualityExpressionRule()
  {
    super(ParseType.EQUALITY_EXPRESSION, START_PRODUCTION, EQUALS_PRODUCTION, NOT_EQUALS_PRODUCTION);
  }

  /**
   * {@inheritDoc}
   * @see compiler.parser.Rule#match(java.lang.Object[], java.lang.Object[])
   */
  @Override
  public Object match(Object[] types, Object[] args) throws ParseException
  {
    if (types == START_PRODUCTION)
    {
      // return the existing expression
      return args[0];
    }

    EqualityExpressionType separator = null;
    if (types == EQUALS_PRODUCTION)
    {
      separator = EqualityExpressionType.EQUAL;
    }
    else if (types == NOT_EQUALS_PRODUCTION)
    {
      separator = EqualityExpressionType.NOT_EQUAL;
    }
    else
    {
      throw badTypeList();
    }
    Expression newExpression = (Expression) args[2];

    if (args[0] instanceof EqualityExpression)
    {
      // continue the current EqualityExpression if we've started one
      EqualityExpression startExpression = (EqualityExpression) args[0];
      return new EqualityExpression(startExpression, separator, newExpression, ParseInfo.combine(startExpression.getParseInfo(), (ParseInfo) args[1], newExpression.getParseInfo()));
    }
    Expression firstExpression = (Expression) args[0];
    return new EqualityExpression(firstExpression, separator, newExpression, ParseInfo.combine(firstExpression.getParseInfo(), (ParseInfo) args[1], newExpression.getParseInfo()));
  }

}
