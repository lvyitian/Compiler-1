package compiler.language.ast.type;

import compiler.language.ast.ParseInfo;
import compiler.language.ast.misc.QNameAST;
import compiler.language.ast.terminal.NameAST;

/*
 * Created on 30 Jun 2010
 */

/**
 * @author Anthony Bryant
 */
public class PointerTypeAST extends TypeAST
{

  private boolean immutable;

  private NameAST[] names;
  private TypeArgumentAST[][] typeArgumentLists;

  /**
   * Creates a new PointerTypeAST that consists only of the specified QNameAST
   * @param qname - the qualified name of the type
   * @param parseInfo - the parsing information
   */
  public PointerTypeAST(QNameAST qname, ParseInfo parseInfo)
  {
    super(parseInfo);
    immutable = false;
    names = qname.getNames();
    typeArgumentLists = new TypeArgumentAST[names.length][];
    for (int i = 0; i < typeArgumentLists.length; i++)
    {
      typeArgumentLists[i] = null;
    }
  }

  /**
   * Creates a new PointerTypeAST with the specified immutability, qualifying names and type argument lists.
   * @param immutable - true if this type should be immutable, false otherwise
   * @param names - the list of (qualifier) names in this PointerTypeAST, ending in the actual type name
   * @param typeArgumentLists - the type argument list for each name in this pointer type, with empty arrays for names that do not have type arguments
   * @param parseInfo - the parsing information
   */
  public PointerTypeAST(boolean immutable, NameAST[] names, TypeArgumentAST[][] typeArgumentLists, ParseInfo parseInfo)
  {
    super(parseInfo);
    this.immutable = immutable;

    if (names.length != typeArgumentLists.length)
    {
      throw new IllegalArgumentException("A PointerTypeAST must have an equal number of names and lists of type arguments");
    }
    this.names = names;
    this.typeArgumentLists = typeArgumentLists;
  }

  /**
   * Creates a new PointerTypeAST with the specified immutability, qualifying names and type argument lists.
   * @param baseType - the base PointerTypeAST to copy the old qualifying names and type arguments from
   * @param immutable - true if this type should be immutable, false otherwise
   * @param addedNames - the list of added (qualifier) names in this PointerTypeAST, ending in the actual type name
   * @param addedTypeArgumentLists - the type argument list for each added name in this pointer type, with empty arrays for names that do not have type arguments
   * @param parseInfo - the parsing information
   */
  public PointerTypeAST(PointerTypeAST baseType, boolean immutable, NameAST[] addedNames, TypeArgumentAST[][] addedTypeArgumentLists, ParseInfo parseInfo)
  {
    super(parseInfo);
    this.immutable = immutable;
    if (addedNames.length != addedTypeArgumentLists.length)
    {
      throw new IllegalArgumentException("A PointerTypeAST must have an equal number of names and lists of type arguments");
    }
    NameAST[] oldNames = baseType.getNames();
    TypeArgumentAST[][] oldTypeArgumentLists = baseType.getTypeArgumentLists();

    names = new NameAST[oldNames.length + addedNames.length];
    typeArgumentLists = new TypeArgumentAST[oldTypeArgumentLists.length + addedTypeArgumentLists.length][];
    System.arraycopy(oldNames, 0, names, 0, oldNames.length);
    System.arraycopy(addedNames, 0, names, oldNames.length, addedNames.length);
    System.arraycopy(oldTypeArgumentLists, 0, typeArgumentLists, 0, oldTypeArgumentLists.length);
    System.arraycopy(addedTypeArgumentLists, 0, typeArgumentLists, oldTypeArgumentLists.length, addedTypeArgumentLists.length);
  }

  /**
   * @return true if this type is immutable, false otherwise
   */
  public boolean isImmutable()
  {
    return immutable;
  }

  /**
   * @return the names
   */
  public NameAST[] getNames()
  {
    return names;
  }

  /**
   * @return the typeArgumentLists
   */
  public TypeArgumentAST[][] getTypeArgumentLists()
  {
    return typeArgumentLists;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    if (immutable)
    {
      buffer.append("#");
    }
    for (int i = 0; i < names.length; i++)
    {
      buffer.append(names[i]);
      TypeArgumentAST[] typeArgumentList = typeArgumentLists[i];
      if (typeArgumentList != null && typeArgumentList.length > 0)
      {
        buffer.append("<");
        for (int j = 0; j < typeArgumentList.length; j++)
        {
          buffer.append(typeArgumentList[j]);
          if (j != typeArgumentList.length - 1)
          {
            buffer.append(", ");
          }
        }
        buffer.append(">");
      }
      if (i != names.length - 1)
      {
        buffer.append(".");
      }
    }
    return buffer.toString();
  }
}
