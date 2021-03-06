package compiler.language.parser.rules.member;

import static compiler.language.parser.ParseType.ACCESS_SPECIFIER;
import static compiler.language.parser.ParseType.PACKAGE_KEYWORD;
import static compiler.language.parser.ParseType.PRIVATE_KEYWORD;
import static compiler.language.parser.ParseType.PROTECTED_KEYWORD;
import static compiler.language.parser.ParseType.PUBLIC_KEYWORD;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.LexicalPhrase;
import compiler.language.ast.member.AccessSpecifierAST;
import compiler.language.ast.member.AccessSpecifierTypeAST;
import compiler.language.parser.ParseType;

/*
 * Created on 30 Jun 2010
 */

/**
 * @author Anthony Bryant
 */
public final class AccessSpecifierRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> EMPTY_PRODUCTION             = new Production<ParseType>();
  private static final Production<ParseType> PUBLIC_PRODUCTION            = new Production<ParseType>(PUBLIC_KEYWORD);
  private static final Production<ParseType> PACKAGE_PRODUCTION           = new Production<ParseType>(PACKAGE_KEYWORD);
  private static final Production<ParseType> PACKAGE_PROTECTED_PRODUCTION = new Production<ParseType>(PACKAGE_KEYWORD, PROTECTED_KEYWORD);
  private static final Production<ParseType> PROTECTED_PACKAGE_PRODUCTION = new Production<ParseType>(PROTECTED_KEYWORD, PACKAGE_KEYWORD);
  private static final Production<ParseType> PROTECTED_PRODUCTION         = new Production<ParseType>(PROTECTED_KEYWORD);
  private static final Production<ParseType> PRIVATE_PRODUCTION           = new Production<ParseType>(PRIVATE_KEYWORD);

  @SuppressWarnings("unchecked")
  public AccessSpecifierRule()
  {
    super(ACCESS_SPECIFIER, EMPTY_PRODUCTION,
                            PUBLIC_PRODUCTION,
                            PACKAGE_PRODUCTION,
                            PACKAGE_PROTECTED_PRODUCTION,
                            PROTECTED_PACKAGE_PRODUCTION,
                            PROTECTED_PRODUCTION,
                            PRIVATE_PRODUCTION);
  }

  /**
   * @see parser.Rule#match(parser.Production, java.lang.Object[])
   */
  @Override
  public Object match(Production<ParseType> production, Object[] args) throws ParseException
  {

    if (EMPTY_PRODUCTION.equals(production))
    {
      return null;
    }
    if (PUBLIC_PRODUCTION.equals(production))
    {
      return new AccessSpecifierAST(AccessSpecifierTypeAST.PUBLIC, (LexicalPhrase) args[0]);
    }
    if (PACKAGE_PRODUCTION.equals(production))
    {
      return new AccessSpecifierAST(AccessSpecifierTypeAST.PACKAGE, (LexicalPhrase) args[0]);
    }
    if (PACKAGE_PROTECTED_PRODUCTION.equals(production) || PROTECTED_PACKAGE_PRODUCTION.equals(production))
    {
      return new AccessSpecifierAST(AccessSpecifierTypeAST.PACKAGE_PROTECTED, LexicalPhrase.combine((LexicalPhrase) args[0], (LexicalPhrase) args[1]));
    }
    if (PROTECTED_PRODUCTION.equals(production))
    {
      return new AccessSpecifierAST(AccessSpecifierTypeAST.PROTECTED, (LexicalPhrase) args[0]);
    }
    if (PRIVATE_PRODUCTION.equals(production))
    {
      return new AccessSpecifierAST(AccessSpecifierTypeAST.PRIVATE, (LexicalPhrase) args[0]);
    }
    throw badTypeList();
  }

}
