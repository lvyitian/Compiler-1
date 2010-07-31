package compiler.language.ast;

/*
 * Created on 10 Jul 2010
 */

/**
 * @author Anthony Bryant
 */
public class SingleArgument extends Argument
{

  protected Modifier[] modifiers;
  protected Type type;
  protected Name name;

  /**
   * Creates a new single argument with the specified modifiers, type and name.
   * @param modifiers - the modifiers of this argument
   * @param type - the type of this argument
   * @param name - the name of this argument
   */
  public SingleArgument(Modifier[] modifiers, Type type, Name name)
  {
    this.modifiers = modifiers;
    this.type = type;
    this.name = name;
  }

  /**
   * @return the modifiers
   */
  public Modifier[] getModifiers()
  {
    return modifiers;
  }

  /**
   * @return the type of this argument
   */
  public Type getType()
  {
    return type;
  }

  /**
   * @return the name of this argument
   */
  public Name getName()
  {
    return name;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    for (Modifier modifier : modifiers)
    {
      buffer.append(modifier);
      buffer.append(" ");
    }
    buffer.append(type);
    buffer.append(" ");
    buffer.append(name);
    return buffer.toString();
  }
}
