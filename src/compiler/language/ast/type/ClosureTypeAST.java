package compiler.language.ast.type;

import compiler.language.ast.ParseInfo;


/*
 * Created on 13 Jul 2010
 */

/**
 * @author Anthony Bryant
 */
public class ClosureTypeAST extends TypeAST
{

  private TypeArgumentAST[] typeArguments;
  private TypeAST[] parameterTypes;
  private TypeAST[] resultTypes;
  private PointerTypeAST[] exceptionTypes;

  /**
   * Creates a new Closure type with the specified type arguments, parameter types, result types, and exception types
   * @param typeArguments - the type arguments of this closure type
   * @param parameterTypes - the types of the parameters to this closure
   * @param resultTypes - the types of the results of this closure
   * @param exceptionTypes - the types of the exceptions thrown by this closure
   * @param parseInfo - the parsing information
   */
  public ClosureTypeAST(TypeArgumentAST[] typeArguments, TypeAST[] parameterTypes, TypeAST[] resultTypes, PointerTypeAST[] exceptionTypes, ParseInfo parseInfo)
  {
    super(parseInfo);
    this.typeArguments = typeArguments;
    this.parameterTypes = parameterTypes;
    this.resultTypes = resultTypes;
    this.exceptionTypes = exceptionTypes;
  }

  /**
   * @return the typeArguments
   */
  public TypeArgumentAST[] getTypeArguments()
  {
    return typeArguments;
  }

  /**
   * @return the parameter types
   */
  public TypeAST[] getParameterTypes()
  {
    return parameterTypes;
  }

  /**
   * @return the result types
   */
  public TypeAST[] getResultTypes()
  {
    return resultTypes;
  }

  /**
   * @return the exception types
   */
  public PointerTypeAST[] getExceptionTypes()
  {
    return exceptionTypes;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("{");
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
      buffer.append("> ");
    }
    for (int i = 0; i < parameterTypes.length; i++)
    {
      buffer.append(parameterTypes[i]);
      if (i != parameterTypes.length - 1)
      {
        buffer.append(", ");
      }
    }
    buffer.append(" -> ");
    for (int i = 0; i < resultTypes.length; i++)
    {
      buffer.append(resultTypes[i]);
      if (i != resultTypes.length - 1)
      {
        buffer.append(", ");
      }
    }
    if (exceptionTypes.length > 0)
    {
      buffer.append(" throws ");
      for (int i = 0; i < exceptionTypes.length; i++)
      {
        buffer.append(exceptionTypes[i]);
        if (i != exceptionTypes.length - 1)
        {
          buffer.append(", ");
        }
      }
    }
    buffer.append("}");
    return buffer.toString();
  }

}
