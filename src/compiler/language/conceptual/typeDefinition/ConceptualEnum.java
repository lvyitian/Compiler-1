package compiler.language.conceptual.typeDefinition;

import compiler.language.conceptual.Resolvable;
import compiler.language.conceptual.member.Constructor;
import compiler.language.conceptual.member.MemberVariable;
import compiler.language.conceptual.member.Method;
import compiler.language.conceptual.member.Property;
import compiler.language.conceptual.member.StaticInitializer;
import compiler.language.conceptual.member.VariableInitializers;
import compiler.language.conceptual.misc.AccessSpecifier;
import compiler.language.conceptual.misc.SinceSpecifier;
import compiler.language.conceptual.type.AutomaticBaseTypeInstance;
import compiler.language.conceptual.type.ClassTypeInstance;
import compiler.language.conceptual.type.InterfaceTypeInstance;
import compiler.language.conceptual.type.PointerType;
import compiler.language.conceptual.type.TypeInstance;

/*
 * Created on 21 Oct 2010
 */

/**
 * @author Anthony Bryant
 */
public abstract class ConceptualEnum extends TypeDefinition
{
  private AccessSpecifier accessSpecifier;
  private SinceSpecifier sinceSpecifier;
  private String name;
  private PointerType baseClass;
  private PointerType[] interfaces;

  private EnumConstant[] constants;

  // members
  private StaticInitializer staticInitializer;
  private VariableInitializers variableInitializers;
  private MemberVariable[] variables;
  private Property[] properties;
  private Constructor[] constructors;
  private Method[] methods;

  private InnerClass[] innerClasses;
  private ConceptualInterface[] innerInterfaces;
  private ConceptualEnum[] innerEnums;

  /**
   * Creates a new Conceptual enum with the specified properties.
   * The members of the new enum and other data must be set later on.
   * @param accessSpecifier
   * @param sinceSpecifier
   * @param name
   */
  public ConceptualEnum(AccessSpecifier accessSpecifier, SinceSpecifier sinceSpecifier, String name)
  {
    this.accessSpecifier = accessSpecifier;
    this.sinceSpecifier = sinceSpecifier;
    this.name = name;
  }

  /**
   * Sets the base class of this conceptual enum
   * @param baseClass - the new base class of this conceptual enum
   */
  public void setBaseClass(PointerType baseClass)
  {
    this.baseClass = baseClass;
  }

  /**
   * Sets the interfaces of this conceptual enum
   * @param interfaces - the implemented interfaces of this conceptual enum
   */
  public void setInterfaces(PointerType[] interfaces)
  {
    this.interfaces = interfaces;
  }

  /**
   * Sets the enum constants for this conceptual enum
   * @param constants
   */
  public void setConstants(EnumConstant[] constants)
  {
    this.constants = constants;
  }

  /**
   * Sets the members of this conceptual enum.
   * @param staticInitializer
   * @param variableInitializers
   * @param variables
   * @param properties
   * @param constructors
   * @param methods
   * @param innerClasses
   * @param innerInterfaces
   * @param innerEnums
   */
  public void setMembers(StaticInitializer staticInitializer, VariableInitializers variableInitializers,
                         MemberVariable[] variables, Property[] properties, Constructor[] constructors, Method[] methods,
                         InnerClass[] innerClasses, ConceptualInterface[] innerInterfaces, ConceptualEnum[] innerEnums)
  {
    this.staticInitializer = staticInitializer;
    this.variableInitializers = variableInitializers;
    this.variables = variables;
    this.properties = properties;
    this.constructors = constructors;
    this.methods = methods;
    this.innerClasses = innerClasses;
    this.innerInterfaces = innerInterfaces;
    this.innerEnums = innerEnums;
  }

  /**
   * @return the accessSpecifier
   */
  public AccessSpecifier getAccessSpecifier()
  {
    return accessSpecifier;
  }
  /**
   * @return the sinceSpecifier
   */
  public SinceSpecifier getSinceSpecifier()
  {
    return sinceSpecifier;
  }
  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }
  /**
   * @return the baseClass
   */
  public PointerType getBaseClass()
  {
    return baseClass;
  }
  /**
   * @return the interfaces
   */
  public PointerType[] getInterfaces()
  {
    return interfaces;
  }
  /**
   * @return the constants
   */
  public EnumConstant[] getConstants()
  {
    return constants;
  }
  /**
   * @return the staticInitializer
   */
  public StaticInitializer getStaticInitializer()
  {
    return staticInitializer;
  }
  /**
   * @return the constructors
   */
  public Constructor[] getConstructors()
  {
    return constructors;
  }
  /**
   * @return the properties
   */
  public Property[] getProperties()
  {
    return properties;
  }
  /**
   * @return the variables
   */
  public MemberVariable[] getVariables()
  {
    return variables;
  }
  /**
   * @return the variableInitializers
   */
  public VariableInitializers getVariableInitializers()
  {
    return variableInitializers;
  }
  /**
   * @return the methods
   */
  public Method[] getMethods()
  {
    return methods;
  }
  /**
   * @return the innerClasses
   */
  public InnerClass[] getInnerClasses()
  {
    return innerClasses;
  }
  /**
   * @return the innerInterfaces
   */
  public ConceptualInterface[] getInnerInterfaces()
  {
    return innerInterfaces;
  }
  /**
   * @return the innerEnums
   */
  public ConceptualEnum[] getInnerEnums()
  {
    return innerEnums;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Resolvable resolve(String name)
  {
    // TODO: create static member variables out of the enum constants and return these instead of the actual
    //       enum constants when resolving, so that an enum constant's members cannot be referenced explicitly
    /*
    for (EnumConstant constant : constants)
    {
      if (constant.getName().equals(name))
      {
      }
    }
    */
    for (MemberVariable variable : variables)
    {
      if (variable.getName().equals(name))
      {
        return variable;
      }
    }
    for (Property property : properties)
    {
      if (property.getName().equals(name))
      {
        return property;
      }
    }
    // TODO: how will methods work here? there can be multiple methods with one name
    for (InnerClass innerClass : innerClasses)
    {
      if (innerClass.getName().equals(name))
      {
        return innerClass;
      }
    }
    for (ConceptualInterface innerInterface : innerInterfaces)
    {
      if (innerInterface.getName().equals(name))
      {
        return innerInterface;
      }
    }
    for (ConceptualEnum innerEnum : innerEnums)
    {
      if (innerEnum.getName().equals(name))
      {
        return innerEnum;
      }
    }
    if (baseClass != null)
    {
      TypeInstance typeInstance = baseClass.getTypeInstance();
      if (typeInstance instanceof ClassTypeInstance)
      {
        Resolvable result = ((ClassTypeInstance) typeInstance).getClassType().resolve(name);
        if (result != null)
        {
          return result;
        }
      }
      else if (!(typeInstance instanceof AutomaticBaseTypeInstance))
      {
        throw new IllegalStateException("A base class' type instance must be either a ClassTypeInstance or an AutomaticBaseTypeInstance");
      }
    }
    if (interfaces != null)
    {
      for (PointerType type : interfaces)
      {
        if (type == null)
        {
          // the type has not yet been populated, so go on to the next one
          continue;
        }
        TypeInstance typeInstance = type.getTypeInstance();
        if (!(typeInstance instanceof InterfaceTypeInstance))
        {
          throw new IllegalStateException("An implemented interface's type instance must be an InterfaceTypeInstance");
        }
        Resolvable result = ((InterfaceTypeInstance) typeInstance).getInterfaceType().resolve(name);
        if (result != null)
        {
          return result;
        }
      }
    }
    return null;
  }

}
