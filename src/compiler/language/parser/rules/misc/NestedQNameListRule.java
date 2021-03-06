package compiler.language.parser.rules.misc;

import static compiler.language.parser.ParseType.LPAREN;
import static compiler.language.parser.ParseType.NESTED_QNAME_LIST;
import static compiler.language.parser.ParseType.QNAME_LIST;
import static compiler.language.parser.ParseType.RPAREN;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.LexicalPhrase;
import compiler.language.parser.ParseList;
import compiler.language.parser.ParseType;
import compiler.language.parser.QNameElementAST;

/*
 * Created on 17 Aug 2010
 */

/**
 * @author Anthony Bryant
 */
public final class NestedQNameListRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> PRODUCTION = new Production<ParseType>(LPAREN, QNAME_LIST, RPAREN);

  @SuppressWarnings("unchecked")
  public NestedQNameListRule()
  {
    super(NESTED_QNAME_LIST, PRODUCTION);
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
      @SuppressWarnings("unchecked")
      ParseList<QNameElementAST> list = (ParseList<QNameElementAST>) args[1];
      return new QNameElementAST(list, LexicalPhrase.combine((LexicalPhrase) args[0], list.getLexicalPhrase(), (LexicalPhrase) args[2]));
    }
    throw badTypeList();
  }

}
