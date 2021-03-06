package compiler.language.parser.rules.statement;

import static compiler.language.parser.ParseType.BLOCK;
import static compiler.language.parser.ParseType.COLON;
import static compiler.language.parser.ParseType.EXPRESSION;
import static compiler.language.parser.ParseType.FOR_EACH_STATEMENT;
import static compiler.language.parser.ParseType.FOR_KEYWORD;
import static compiler.language.parser.ParseType.NAME;
import static compiler.language.parser.ParseType.TYPE;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.LexicalPhrase;
import compiler.language.ast.expression.ExpressionAST;
import compiler.language.ast.statement.BlockAST;
import compiler.language.ast.statement.ForEachStatementAST;
import compiler.language.ast.terminal.NameAST;
import compiler.language.ast.type.TypeAST;
import compiler.language.parser.ParseType;

/*
 * Created on 28 Aug 2010
 */

/**
 * @author Anthony Bryant
 */
public final class ForEachStatementRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> PRODUCTION = new Production<ParseType>(FOR_KEYWORD, TYPE, NAME, COLON, EXPRESSION, BLOCK);

  @SuppressWarnings("unchecked")
  public ForEachStatementRule()
  {
    super(FOR_EACH_STATEMENT, PRODUCTION);
  }

  /**
   * {@inheritDoc}
   * @see parser.Rule#match(parser.Production, java.lang.Object[])
   */
  @Override
  public Object match(Production<ParseType> production, Object[] args) throws ParseException
  {
    if (PRODUCTION.equals(production))
    {
      TypeAST type = (TypeAST) args[1];
      NameAST name = (NameAST) args[2];
      ExpressionAST expression = (ExpressionAST) args[4];
      BlockAST block = (BlockAST) args[5];
      return new ForEachStatementAST(type, name, expression, block,
                                  LexicalPhrase.combine((LexicalPhrase) args[0], type.getLexicalPhrase(), name.getLexicalPhrase(),
                                                    (LexicalPhrase) args[3], expression.getLexicalPhrase(), block.getLexicalPhrase()));
    }
    throw badTypeList();
  }

}
