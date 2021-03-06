package compiler.language.parser.rules.statement;

import static compiler.language.parser.ParseType.ELSE_IF_CLAUSE;
import static compiler.language.parser.ParseType.ELSE_IF_CLAUSES;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.LexicalPhrase;
import compiler.language.ast.statement.ElseIfClauseAST;
import compiler.language.parser.ParseList;
import compiler.language.parser.ParseType;

/*
 * Created on 27 Aug 2010
 */

/**
 * @author Anthony Bryant
 */
public final class ElseIfClausesRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> START_PRODUCTION        = new Production<ParseType>();
  private static final Production<ParseType> CONTINUATION_PRODUCTION = new Production<ParseType>(ELSE_IF_CLAUSES, ELSE_IF_CLAUSE);

  @SuppressWarnings("unchecked")
  public ElseIfClausesRule()
  {
    super(ELSE_IF_CLAUSES, START_PRODUCTION, CONTINUATION_PRODUCTION);
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
      return new ParseList<ElseIfClauseAST>(null);
    }
    if (CONTINUATION_PRODUCTION.equals(production))
    {
      @SuppressWarnings("unchecked")
      ParseList<ElseIfClauseAST> list = (ParseList<ElseIfClauseAST>) args[0];
      ElseIfClauseAST clause = (ElseIfClauseAST) args[1];
      list.addLast(clause, LexicalPhrase.combine(list.getLexicalPhrase(), clause.getLexicalPhrase()));
      return list;
    }
    throw badTypeList();
  }

}
