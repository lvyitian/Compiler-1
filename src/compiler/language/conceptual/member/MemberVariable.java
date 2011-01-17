package compiler.language.conceptual.member;

import compiler.language.ast.member.FieldAST;
import compiler.language.ast.misc.DeclarationAssigneeAST;
import compiler.language.ast.misc.ModifierAST;
import compiler.language.ast.terminal.SinceSpecifierAST;
import compiler.language.conceptual.ConceptualException;
import compiler.language.conceptual.misc.AccessSpecifier;
import compiler.language.conceptual.misc.SinceSpecifier;
import compiler.language.conceptual.type.Type;

/*
 * Created on 21 Oct 2010
 */

/**
 * @author Anthony Bryant
 */
public class MemberVariable
{

  // TODO: this should include an access specifier, modifiers - as separate boolean variables (excluding static), a type, and a variable name
  // any initialization should be added elsewhere

  private AccessSpecifier accessSpecifier;
  private boolean isFinal;
  private boolean isMutable;
  private boolean isStatic;
  private boolean isVolatile;
  private boolean isTransient;
  private SinceSpecifier sinceSpecifier;

  private Type type;

  private String name;

  /**
   * Creates a new MemberVariable with the specified parameters.
   * @param accessSpecifier
   * @param isFinal
   * @param isMutable
   * @param isStatic
   * @param isVolatile
   * @param isTransient
   * @param sinceSpecifier
   * @param name
   */
  public MemberVariable(AccessSpecifier accessSpecifier, boolean isFinal, boolean isMutable, boolean isStatic, boolean isVolatile, boolean isTransient, SinceSpecifier sinceSpecifier, String name)
  {
    this.accessSpecifier = accessSpecifier;
    this.isFinal = isFinal;
    this.isMutable = isMutable;
    this.isStatic = isStatic;
    this.isVolatile = isVolatile;
    this.isTransient = isTransient;
    this.sinceSpecifier = sinceSpecifier;
    this.name = name;
  }

  /**
   * Creates an array of MemberVariables from the specified FieldAST.
   * @param fieldAST - the FieldAST to base the new MemberVariables on
   * @return the MemberVariables created
   * @throws ConceptualException - if there is a problem converting the AST instance to a Conceptual instance
   */
  public static MemberVariable[] fromAST(FieldAST fieldAST) throws ConceptualException
  {
    AccessSpecifier accessSpecifier = AccessSpecifier.fromAST(fieldAST.getAccessSpecifier());

    boolean isFinal = false;
    boolean isMutable = false;
    boolean isStatic = false;
    boolean isVolatile = false;
    boolean isTransient = false;
    SinceSpecifier sinceSpecifier = null;
    for (ModifierAST modifier : fieldAST.getModifiers())
    {
      switch (modifier.getType())
      {
      case FINAL:
        isFinal = true;
        break;
      case MUTABLE:
        isMutable = true;
        break;
      case SINCE_SPECIFIER:
        sinceSpecifier = SinceSpecifier.fromAST((SinceSpecifierAST) modifier);
        break;
      case STATIC:
        isStatic = true;
        break;
      case TRANSIENT:
        isTransient = true;
        break;
      case VOLATILE:
        isVolatile = true;
        break;
      default:
        throw new ConceptualException("Illegal Modifier for a Field", modifier.getParseInfo());
      }
    }

    DeclarationAssigneeAST[] assignees = fieldAST.getAssignees();
    MemberVariable[] memberVariables = new MemberVariable[assignees.length];
    for (int i = 0; i < assignees.length; i++)
    {
      memberVariables[i] = new MemberVariable(accessSpecifier, isFinal, isMutable, isStatic, isVolatile, isTransient, sinceSpecifier, assignees[i].getName().getName());
    }
    return memberVariables;
  }

  /**
   * Sets the type of this member variable.
   * @param type
   */
  public void setType(Type type)
  {
    this.type = type;
  }

  /**
   * @return the accessSpecifier
   */
  public AccessSpecifier getAccessSpecifier()
  {
    return accessSpecifier;
  }

  /**
   * @return the isFinal
   */
  public boolean isFinal()
  {
    return isFinal;
  }

  /**
   * @return the isMutable
   */
  public boolean isMutable()
  {
    return isMutable;
  }

  /**
   * @return the isStatic
   */
  public boolean isStatic()
  {
    return isStatic;
  }

  /**
   * @return the isVolatile
   */
  public boolean isVolatile()
  {
    return isVolatile;
  }

  /**
   * @return the isTransient
   */
  public boolean isTransient()
  {
    return isTransient;
  }

  /**
   * @return the sinceSpecifier
   */
  public SinceSpecifier getSinceSpecifier()
  {
    return sinceSpecifier;
  }

  /**
   * @return the type
   */
  public Type getType()
  {
    return type;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

}
