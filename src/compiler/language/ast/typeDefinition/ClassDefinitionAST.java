package compiler.language.ast.typeDefinition;

import compiler.language.ast.ParseInfo;
import compiler.language.ast.member.AccessSpecifierAST;
import compiler.language.ast.member.MemberAST;
import compiler.language.ast.misc.ModifierAST;
import compiler.language.ast.terminal.NameAST;
import compiler.language.ast.topLevel.TypeDefinitionAST;
import compiler.language.ast.type.PointerTypeAST;
import compiler.language.ast.type.TypeArgumentAST;

/*
 * Created on 1 Jul 2010
 */

/**
 * @author Anthony Bryant
 */
public class ClassDefinitionAST extends TypeDefinitionAST
{
  private AccessSpecifierAST access;
  private ModifierAST[] modifiers;
  private NameAST name;
  private TypeArgumentAST[] typeArguments;
  private PointerTypeAST baseClass;
  private PointerTypeAST[] interfaces;
  private MemberAST[] members;

  /**
   * Creates a new class definition with all of the specified properties
   * @param access - the access specifier, or null for the default access specifier
   * @param modifiers - the list of modifiers for this class
   * @param name - the name of this class
   * @param typeArguments - the type arguments to this class
   * @param baseClass - the base class of this class
   * @param interfaces - the list of interfaces that this class implements
   * @param members - the list of members of this class
   * @param parseInfo - the parsing information
   */
  public ClassDefinitionAST(AccessSpecifierAST access, ModifierAST[] modifiers, NameAST name, TypeArgumentAST[] typeArguments, PointerTypeAST baseClass, PointerTypeAST[] interfaces, MemberAST[] members, ParseInfo parseInfo)
  {
    super(parseInfo);
    this.access = access;
    this.modifiers = modifiers;
    this.name = name;
    this.typeArguments = typeArguments;
    this.baseClass = baseClass;
    this.interfaces = interfaces;
    this.members = members;
  }

  /**
   * @return the access specifier
   */
  public AccessSpecifierAST getAccess()
  {
    return access;
  }

  /**
   * @return the modifiers
   */
  public ModifierAST[] getModifiers()
  {
    return modifiers;
  }

  /**
   * @return the name
   */
  public NameAST getName()
  {
    return name;
  }

  /**
   * @return the type arguments
   */
  public TypeArgumentAST[] getTypeArguments()
  {
    return typeArguments;
  }

  /**
   * @return the base class
   */
  public PointerTypeAST getBaseClass()
  {
    return baseClass;
  }

  /**
   * @return the interfaces
   */
  public PointerTypeAST[] getInterfaces()
  {
    return interfaces;
  }

  /**
   * @return the members
   */
  public MemberAST[] getMembers()
  {
    return members;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    if (access != null)
    {
      buffer.append(access);
      buffer.append(" ");
    }
    for (int i = 0; i < modifiers.length; i++)
    {
      buffer.append(modifiers[i]);
      buffer.append(" ");
    }
    buffer.append("class ");
    buffer.append(name);
    if (typeArguments.length > 0)
    {
      buffer.append("<");
      for (int i = 0; i < typeArguments.length; i++)
      {
        buffer.append(typeArguments[i]);
        if (i != typeArguments.length - 1)
        {
          buffer.append(", ");
        }
      }
      buffer.append(">");
    }
    buffer.append(" ");
    if (baseClass != null)
    {
      buffer.append("extends ");
      buffer.append(baseClass);
      buffer.append(" ");
    }
    if (interfaces.length > 0)
    {
      buffer.append("implements ");
      for (int i = 0; i < interfaces.length; i++)
      {
        buffer.append(interfaces[i]);
        if (i != interfaces.length - 1)
        {
          buffer.append(", ");
        }
      }
      buffer.append(" ");
    }
    buffer.append("\n{\n");
    for (int i = 0; i < members.length; i++)
    {
      String memberStr = members[i].toString();
      buffer.append(memberStr.replaceAll("(?m)^", "   "));
      buffer.append("\n   \n");
    }
    buffer.append("}");
    return buffer.toString();
  }
}