package compiler.language.parser.rules.statement;

import static compiler.language.parser.ParseType.ASSIGNEE;
import static compiler.language.parser.ParseType.DECREMENT;
import static compiler.language.parser.ParseType.DOUBLE_MINUS;
import static compiler.language.parser.ParseType.QNAME;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.LexicalPhrase;
import compiler.language.QName;
import compiler.language.ast.expression.FieldAccessExpressionAST;
import compiler.language.ast.misc.AssigneeAST;
import compiler.language.ast.misc.FieldAssigneeAST;
import compiler.language.ast.statement.DecrementStatementAST;
import compiler.language.parser.ParseType;

/*
 * Created on 29 Aug 2010
 */

/**
 * @author Anthony Bryant
 */
public final class DecrementRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> PRE_PRODUCTION        = new Production<ParseType>(DOUBLE_MINUS, ASSIGNEE);
  private static final Production<ParseType> PRE_QNAME_PRODUCTION  = new Production<ParseType>(DOUBLE_MINUS, QNAME);
  private static final Production<ParseType> POST_PRODUCTION       = new Production<ParseType>(ASSIGNEE, DOUBLE_MINUS);
  private static final Production<ParseType> POST_QNAME_PRODUCTION = new Production<ParseType>(QNAME,    DOUBLE_MINUS);

  @SuppressWarnings("unchecked")
  public DecrementRule()
  {
    super(DECREMENT, PRE_PRODUCTION, PRE_QNAME_PRODUCTION, POST_PRODUCTION, POST_QNAME_PRODUCTION);
  }

  /**
   * {@inheritDoc}
   * @see parser.Rule#match(parser.Production, java.lang.Object[])
   */
  @Override
  public Object match(Production<ParseType> production, Object[] args) throws ParseException
  {
    if (PRE_PRODUCTION.equals(production))
    {
      AssigneeAST assignee = (AssigneeAST) args[1];
      return new DecrementStatementAST(assignee, LexicalPhrase.combine((LexicalPhrase) args[0], assignee.getLexicalPhrase()));
    }
    if (PRE_QNAME_PRODUCTION.equals(production))
    {
      QName qname = (QName) args[1];
      FieldAccessExpressionAST fieldAccess = new FieldAccessExpressionAST(qname, qname.getLexicalPhrase());
      AssigneeAST assignee = new FieldAssigneeAST(fieldAccess, fieldAccess.getLexicalPhrase());
      return new DecrementStatementAST(assignee, LexicalPhrase.combine((LexicalPhrase) args[0], assignee.getLexicalPhrase()));
    }
    if (POST_PRODUCTION.equals(production))
    {
      AssigneeAST assignee = (AssigneeAST) args[0];
      return new DecrementStatementAST(assignee, LexicalPhrase.combine(assignee.getLexicalPhrase(), (LexicalPhrase) args[1]));
    }
    if (POST_QNAME_PRODUCTION.equals(production))
    {
      QName qname = (QName) args[0];
      FieldAccessExpressionAST fieldAccess = new FieldAccessExpressionAST(qname, qname.getLexicalPhrase());
      AssigneeAST assignee = new FieldAssigneeAST(fieldAccess, fieldAccess.getLexicalPhrase());
      return new DecrementStatementAST(assignee, LexicalPhrase.combine(assignee.getLexicalPhrase(), (LexicalPhrase) args[1]));
    }
    throw badTypeList();
  }

}
