package compiler.language.ast.type;

import compiler.language.LexicalPhrase;


/*
 * Created on 12 Jul 2010
 */

/**
 * @author Anthony Bryant
 */
public class WildcardTypeArgumentAST extends TypeArgumentAST
{

  private PointerTypeAST[] superTypes;
  private PointerTypeAST[] subTypes;

  /**
   * Creates a new wildcard type argument that bounds its type to the specified range of values.
   * @param superTypes - the super types of this type argument
   * @param subTypes - the sub types of this type argument
   * @param lexicalPhrase - the lexical phrase associated with this AST node
   */
  public WildcardTypeArgumentAST(PointerTypeAST[] superTypes, PointerTypeAST[] subTypes, LexicalPhrase lexicalPhrase)
  {
    super(lexicalPhrase);
    this.superTypes = superTypes;
    this.subTypes = subTypes;
  }

  /**
   * @return the super types
   */
  public PointerTypeAST[] getSuperTypes()
  {
    return superTypes;
  }

  /**
   * @return the sub types
   */
  public PointerTypeAST[] getSubTypes()
  {
    return subTypes;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("?");
    if (superTypes.length > 0)
    {
      buffer.append(" extends ");
      for (int i = 0; i < superTypes.length; i++)
      {
        buffer.append(superTypes[i]);
        if (i != superTypes.length - 1)
        {
          buffer.append(" & ");
        }
      }
    }
    if (subTypes.length > 0)
    {
      buffer.append(" super ");
      for (int i = 0; i < subTypes.length; i++)
      {
        buffer.append(subTypes[i]);
        if (i != subTypes.length - 1)
        {
          buffer.append(" & ");
        }
      }
    }
    return buffer.toString();
  }

}
