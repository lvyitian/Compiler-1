package compiler.language.parser.rules.type;

import static compiler.language.parser.ParseType.POINTER_TYPE_NO_TRAILING_ARGS_NOT_QNAME;
import static compiler.language.parser.ParseType.POINTER_TYPE_TRAILING_ARGS_TRIPLE_RANGLE;
import static compiler.language.parser.ParseType.POINTER_TYPE_TRIPLE_RANGLE;
import static compiler.language.parser.ParseType.QNAME;
import static compiler.language.parser.ParseType.TRIPLE_RANGLE;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.ast.ParseInfo;
import compiler.language.ast.misc.QNameAST;
import compiler.language.ast.type.PointerTypeAST;
import compiler.language.parser.ParseContainer;
import compiler.language.parser.ParseType;
import compiler.language.parser.ParseUtil;

/*
 * Created on 20 Aug 2010
 */

/**
 * @author Anthony Bryant
 */
public final class PointerTypeTripleRAngleRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> QNAME_PRODUCTION = new Production<ParseType>(QNAME, TRIPLE_RANGLE);
  private static final Production<ParseType> NO_TRAILING_PARAMS_PRODUCTION = new Production<ParseType>(POINTER_TYPE_NO_TRAILING_ARGS_NOT_QNAME, TRIPLE_RANGLE);
  private static final Production<ParseType> TRAILING_PARAMS_PRODUCTION = new Production<ParseType>(POINTER_TYPE_TRAILING_ARGS_TRIPLE_RANGLE);

  @SuppressWarnings("unchecked")
  public PointerTypeTripleRAngleRule()
  {
    super(POINTER_TYPE_TRIPLE_RANGLE, QNAME_PRODUCTION, NO_TRAILING_PARAMS_PRODUCTION, TRAILING_PARAMS_PRODUCTION);
  }

  /**
   * {@inheritDoc}
   * @see parser.Rule#match(parser.Production, java.lang.Object[])
   */
  @Override
  public Object match(Production<ParseType> production, Object[] args) throws ParseException
  {
    if (TRAILING_PARAMS_PRODUCTION.equals(production))
    {
      // return the existing ParseContainer<ParseContainer<ParseContainer<PointerTypeAST>>>
      return args[0];
    }

    PointerTypeAST type;
    if (QNAME_PRODUCTION.equals(production))
    {
      QNameAST qname = (QNameAST) args[0];
      type = new PointerTypeAST(qname, qname.getParseInfo());
    }
    else if (NO_TRAILING_PARAMS_PRODUCTION.equals(production))
    {
      type = (PointerTypeAST) args[0];
    }
    else
    {
      throw badTypeList();
    }

    ParseInfo tripleRAngleInfo = (ParseInfo) args[1];
    ParseInfo firstAngleInfo     = ParseUtil.splitTripleRAngleFirst(tripleRAngleInfo);
    ParseInfo firstTwoAnglesInfo = ParseUtil.splitTripleRAngleFirstTwo(tripleRAngleInfo);
    ParseContainer<PointerTypeAST> firstContainer = new ParseContainer<PointerTypeAST>(type, ParseInfo.combine(type.getParseInfo(), firstAngleInfo));
    ParseContainer<ParseContainer<PointerTypeAST>> secondContainer =
      new ParseContainer<ParseContainer<PointerTypeAST>>(firstContainer, ParseInfo.combine(type.getParseInfo(), firstTwoAnglesInfo));
    return new ParseContainer<ParseContainer<ParseContainer<PointerTypeAST>>>(secondContainer, ParseInfo.combine(type.getParseInfo(), tripleRAngleInfo));
  }

}
