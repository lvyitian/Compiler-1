package compiler.language.parser.rules.type;

import static compiler.language.parser.ParseType.LANGLE;
import static compiler.language.parser.ParseType.TYPE_ARGUMENTS;
import static compiler.language.parser.ParseType.TYPE_ARGUMENT_LIST_RANGLE;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.LexicalPhrase;
import compiler.language.ast.type.TypeArgumentAST;
import compiler.language.parser.ParseContainer;
import compiler.language.parser.ParseList;
import compiler.language.parser.ParseType;

/*
 * Created on 13 Jul 2010
 */

/**
 * @author Anthony Bryant
 */
public final class TypeArgumentsRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> PRODUCTION = new Production<ParseType>(LANGLE, TYPE_ARGUMENT_LIST_RANGLE);

  @SuppressWarnings("unchecked")
  public TypeArgumentsRule()
  {
    super(TYPE_ARGUMENTS, PRODUCTION);
  }

  /**
   * @see parser.Rule#match(parser.Production, java.lang.Object[])
   */
  @Override
  public Object match(Production<ParseType> production, Object[] args) throws ParseException
  {
    if (PRODUCTION.equals(production))
    {
      @SuppressWarnings("unchecked")
      ParseContainer<ParseList<TypeArgumentAST>> container = (ParseContainer<ParseList<TypeArgumentAST>>) args[1];
      ParseList<TypeArgumentAST> list = container.getItem();
      list.setLexicalPhrase(LexicalPhrase.combine((LexicalPhrase) args[0], container.getLexicalPhrase()));
      return list;
    }
    throw badTypeList();
  }

}
