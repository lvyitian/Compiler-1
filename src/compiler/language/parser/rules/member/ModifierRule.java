package compiler.language.parser.rules.member;

import static compiler.language.parser.ParseType.ABSTRACT_KEYWORD;
import static compiler.language.parser.ParseType.FINAL_KEYWORD;
import static compiler.language.parser.ParseType.IMMUTABLE_KEYWORD;
import static compiler.language.parser.ParseType.MODIFIER;
import static compiler.language.parser.ParseType.MUTABLE_KEYWORD;
import static compiler.language.parser.ParseType.NATIVE_SPECIFIER;
import static compiler.language.parser.ParseType.SINCE_SPECIFIER;
import static compiler.language.parser.ParseType.STATIC_KEYWORD;
import static compiler.language.parser.ParseType.SYNCHRONIZED_KEYWORD;
import static compiler.language.parser.ParseType.TRANSIENT_KEYWORD;
import static compiler.language.parser.ParseType.VOLATILE_KEYWORD;

import compiler.language.ast.ParseInfo;
import compiler.language.ast.member.Modifier;
import compiler.language.ast.member.ModifierType;
import compiler.parser.Rule;

/*
 * Created on 30 Jun 2010
 */

/**
 * @author Anthony Bryant
 */
public class ModifierRule extends Rule
{

  private static final Object[] STATIC_PRODUCTION = new Object[] {STATIC_KEYWORD};
  private static final Object[] ABSTRACT_PRODUCTION = new Object[] {ABSTRACT_KEYWORD};
  private static final Object[] FINAL_PRODUCTION = new Object[] {FINAL_KEYWORD};
  private static final Object[] MUTABLE_PRODUCTION = new Object[] {MUTABLE_KEYWORD};
  private static final Object[] IMMUTABLE_PRODUCTION = new Object[] {IMMUTABLE_KEYWORD};
  private static final Object[] SYNCHRONIZED_PRODUCTION = new Object[] {SYNCHRONIZED_KEYWORD};
  private static final Object[] TRANSIENT_PRODUCTION = new Object[] {TRANSIENT_KEYWORD};
  private static final Object[] VOLATILE_PRODUCTION = new Object[] {VOLATILE_KEYWORD};
  private static final Object[] NATIVE_SPECIFIER_PRODUCTION = new Object[] {NATIVE_SPECIFIER};
  private static final Object[] SINCE_SPECIFIER_PRODUCTION = new Object[] {SINCE_SPECIFIER};

  public ModifierRule()
  {
    super(MODIFIER, STATIC_PRODUCTION, ABSTRACT_PRODUCTION, FINAL_PRODUCTION, MUTABLE_PRODUCTION, IMMUTABLE_PRODUCTION,
                    SYNCHRONIZED_PRODUCTION, TRANSIENT_PRODUCTION, VOLATILE_PRODUCTION, NATIVE_SPECIFIER_PRODUCTION, SINCE_SPECIFIER_PRODUCTION);
  }

  /**
   * @see compiler.parser.Rule#match(java.lang.Object[], java.lang.Object[])
   */
  @Override
  public Object match(Object[] types, Object[] args)
  {
    if (types == STATIC_PRODUCTION)
    {
      return new Modifier(ModifierType.STATIC, (ParseInfo) args[0]);
    }
    if (types == ABSTRACT_PRODUCTION)
    {
      return new Modifier(ModifierType.ABSTRACT, (ParseInfo) args[0]);
    }
    if (types == FINAL_PRODUCTION)
    {
      return new Modifier(ModifierType.FINAL, (ParseInfo) args[0]);
    }
    if (types == MUTABLE_PRODUCTION)
    {
      return new Modifier(ModifierType.MUTABLE, (ParseInfo) args[0]);
    }
    if (types == IMMUTABLE_PRODUCTION)
    {
      return new Modifier(ModifierType.IMMUTABLE, (ParseInfo) args[0]);
    }
    if (types == SYNCHRONIZED_PRODUCTION)
    {
      return new Modifier(ModifierType.SYNCHRONIZED, (ParseInfo) args[0]);
    }
    if (types == TRANSIENT_PRODUCTION)
    {
      return new Modifier(ModifierType.TRANSIENT, (ParseInfo) args[0]);
    }
    if (types == VOLATILE_PRODUCTION)
    {
      return new Modifier(ModifierType.VOLATILE, (ParseInfo) args[0]);
    }
    if (types == NATIVE_SPECIFIER_PRODUCTION || types == SINCE_SPECIFIER_PRODUCTION)
    {
      // NativeSpecifier and SinceSpecifier are subclasses of Modifier, so we can just return them directly
      return args[0];
    }
    return null;
  }

}