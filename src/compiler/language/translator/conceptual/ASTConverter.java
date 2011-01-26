package compiler.language.translator.conceptual;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import compiler.language.ast.member.ConstructorAST;
import compiler.language.ast.member.FieldAST;
import compiler.language.ast.member.MemberAST;
import compiler.language.ast.member.MethodAST;
import compiler.language.ast.member.PropertyAST;
import compiler.language.ast.member.StaticInitializerAST;
import compiler.language.ast.misc.DeclarationAssigneeAST;
import compiler.language.ast.misc.ModifierAST;
import compiler.language.ast.misc.NativeSpecifierAST;
import compiler.language.ast.misc.QNameAST;
import compiler.language.ast.terminal.SinceSpecifierAST;
import compiler.language.ast.topLevel.CompilationUnitAST;
import compiler.language.ast.topLevel.PackageDeclarationAST;
import compiler.language.ast.topLevel.TypeDefinitionAST;
import compiler.language.ast.type.TypeArgumentAST;
import compiler.language.ast.typeDefinition.ClassDefinitionAST;
import compiler.language.ast.typeDefinition.EnumConstantAST;
import compiler.language.ast.typeDefinition.EnumDefinitionAST;
import compiler.language.ast.typeDefinition.InterfaceDefinitionAST;
import compiler.language.conceptual.ConceptualException;
import compiler.language.conceptual.ConceptualProgram;
import compiler.language.conceptual.Scope;
import compiler.language.conceptual.ScopeType;
import compiler.language.conceptual.member.Constructor;
import compiler.language.conceptual.member.MemberVariable;
import compiler.language.conceptual.member.Method;
import compiler.language.conceptual.member.Property;
import compiler.language.conceptual.member.StaticInitializer;
import compiler.language.conceptual.member.VariableInitializers;
import compiler.language.conceptual.misc.AccessSpecifier;
import compiler.language.conceptual.misc.NativeSpecifier;
import compiler.language.conceptual.misc.SinceSpecifier;
import compiler.language.conceptual.type.TypeArgument;
import compiler.language.conceptual.typeDefinition.ConceptualClass;
import compiler.language.conceptual.typeDefinition.ConceptualEnum;
import compiler.language.conceptual.typeDefinition.ConceptualInterface;
import compiler.language.conceptual.typeDefinition.EnumConstant;
import compiler.language.conceptual.typeDefinition.InnerClass;

/*
 * Created on 19 Jan 2011
 */

/**
 * Handles conversion of an AST into a basic conceptual view.
 * This class does not take care of scoping, or building a scope. It just translates the raw data that it can from the AST into the Conceptual hierarchy.
 * @author Anthony Bryant
 */
public class ASTConverter
{

  private ConceptualProgram program;
  private Scope rootScope;

  private Map<Object, Scope> scopes = new HashMap<Object, Scope>();

  // TODO: handle duplicate modifiers properly (instead of just disregarding them)

  /**
   * Creates a new ASTConverter that will add all converted AST nodes to the specified ConceptualProgram
   * @param program - the ConceptualProgram to add all converted AST nodes to
   * @param rootScope - the root scope of the program
   */
  public ASTConverter(ConceptualProgram program, Scope rootScope)
  {
    this.program = program;
    this.rootScope = rootScope;
    scopes.put(program, rootScope);
  }

  /**
   * Converts the specified CompilationUnitAST and adds the converted classes etc. to the ConceptualProgram that this object was constructed with.
   * @param compilationUnit - the CompilationUnitAST to convert
   * @throws ConceptualException - if there is a problem converting the AST to the conceptual view
   * @throws ScopeException - if there is a naming conflict in the AST
   */
  public void convert(CompilationUnitAST compilationUnit) throws ConceptualException, ScopeException
  {
    PackageDeclarationAST packageDeclaration = compilationUnit.getPackageDeclaration();
    Scope enclosingScope = null;
    if (packageDeclaration == null)
    {
      enclosingScope = rootScope;
    }
    else
    {
      QNameAST packageName = packageDeclaration.getPackageName();
      enclosingScope = ScopeFactory.getPackageScope(rootScope, packageName.getNameStrings());
    }

    Scope fileScope = ScopeFactory.createFileScope(enclosingScope);

    for (TypeDefinitionAST typeDefinition : compilationUnit.getTypes())
    {
      Scope typeScope = null;
      if (typeDefinition instanceof ClassDefinitionAST)
      {
        typeScope = convert((ClassDefinitionAST) typeDefinition, fileScope);
        ConceptualClass conceptualClass = (ConceptualClass) typeScope.getValue();
        program.addClass(conceptualClass);
      }
      else if (typeDefinition instanceof InterfaceDefinitionAST)
      {
        typeScope = convert((InterfaceDefinitionAST) typeDefinition, fileScope);
        ConceptualInterface conceptualInterface = (ConceptualInterface) typeScope.getValue();
        program.addInterface(conceptualInterface);
      }
      else if (typeDefinition instanceof EnumDefinitionAST)
      {
        typeScope = convert((EnumDefinitionAST) typeDefinition, fileScope);
        ConceptualEnum conceptualEnum = (ConceptualEnum) typeScope.getValue();
        program.addEnum(conceptualEnum);
      }
      else
      {
        throw new UnsupportedOperationException("Cannot translate a type definition that does not represent a class, interface or enum.");
      }

      // TODO: don't throw the exception if there is a conflict, just print it out and fail when we've detected all of the errors (probably not with an exception in fact)
      enclosingScope.addChild(typeDefinition.getName().getName(), typeScope);
    }
  }

  /**
   * Converts the specified ClassDefinitionAST into a ConceptualClass.
   * @param classDefinition - the ClassDefinitionAST to convert
   * @param enclosingScope - the scope to make the parent of the new conceptual object's scope
   * @return the Scope of the ConceptualClass created, which has the ConceptualClass as its value
   * @throws ConceptualException - if there is a problem with the conversion
   */
  private Scope convert(ClassDefinitionAST classDefinition, Scope enclosingScope) throws ConceptualException
  {
    // convert AccessSpecifier and Modifiers
    AccessSpecifier access = AccessSpecifier.fromAST(classDefinition.getAccess());
    ModifierAST[] modifiers = classDefinition.getModifiers();
    boolean isAbstract = false;
    boolean isSealed = false;
    boolean isImmutable = false;
    SinceSpecifier sinceSpecifier = null;
    for (ModifierAST modifier : modifiers)
    {
      switch (modifier.getType())
      {
      case ABSTRACT:
        isAbstract = true;
        break;
      case SEALED:
        isSealed = true;
        break;
      case IMMUTABLE:
        isImmutable = true;
        break;
      case SINCE_SPECIFIER:
        sinceSpecifier = SinceSpecifier.fromAST((SinceSpecifierAST) modifier);
        break;
      default:
        throw new ConceptualException("Illegal Modifier for a Class Definition", modifier.getParseInfo());
      }
    }

    // Create the ConceptualClass and the scope for it
    ConceptualClass conceptualClass = new ConceptualClass(access, isAbstract, isSealed, isImmutable, sinceSpecifier, classDefinition.getName().getName());
    Scope scope = ScopeFactory.createClassDefinitionScope(conceptualClass, enclosingScope);
    scopes.put(conceptualClass, scope);

    // convert the type arguments
    TypeArgumentAST[] typeArgumentASTs = classDefinition.getTypeArguments();
    TypeArgument[] typeArguments = new TypeArgument[typeArgumentASTs.length];
    for (int i = 0; i < typeArgumentASTs.length; i++)
    {
      Scope typeArgumentScope = convert(typeArgumentASTs[i], scope);
      typeArguments[i] = (TypeArgument) typeArgumentScope.getValue();
      scope.addChild(typeArgumentASTs[i].getName().getName(), typeArgumentScope);
    }


    // convert each of the members in turn, switching on the member type. each member is added to a list of the members of its type
    List<MemberVariable>      variables       = new LinkedList<MemberVariable>();
    List<Property>            properties      = new LinkedList<Property>();
    List<Constructor>         constructors    = new LinkedList<Constructor>();
    List<Method>              methods         = new LinkedList<Method>();
    List<InnerClass>          innerClasses    = new LinkedList<InnerClass>();
    List<ConceptualInterface> innerInterfaces = new LinkedList<ConceptualInterface>();
    List<ConceptualEnum>      innerEnums      = new LinkedList<ConceptualEnum>();

    MemberAST[] memberASTs = classDefinition.getMembers();
    for (MemberAST memberAST : memberASTs)
    {
      if (memberAST instanceof FieldAST)
      {
        Scope[] variableScopes = convert((FieldAST) memberAST, scope);
        for (Scope variableScope : variableScopes)
        {
          MemberVariable variable = (MemberVariable) variableScope.getValue();
          variables.add(variable);
          scope.addChild(variable.getName(), variableScope);
        }
      }
      else if (memberAST instanceof PropertyAST)
      {
        Scope propertyScope = convert((PropertyAST) memberAST, scope);
        Property property = (Property) propertyScope.getValue();
        properties.add(property);
        scope.addChild(property.getName(), propertyScope);
      }
      else if (memberAST instanceof StaticInitializerAST)
      {
        // do nothing, as the static initializer contains no information that we need to convert right now
        // the contained statements will be converted when the scope hierarchy has been built
      }
      else if (memberAST instanceof ConstructorAST)
      {
        Scope constructorScope = convert((ConstructorAST) memberAST, scope);
        Constructor constructor = (Constructor) constructorScope.getValue();
        constructors.add(constructor);
        // no need to add to the scope here, you cannot reference a constructor directly, just via the class name
      }
      else if (memberAST instanceof MethodAST)
      {
        Scope methodScope = convert((MethodAST) memberAST, scope);
        Method method = (Method) methodScope.getValue();
        methods.add(method);
        // combine the scopes for existing methods
        Scope existingScope = scope.getChild(method.getName());
        if (existingScope != null && existingScope.getType() == ScopeType.METHOD)
        {
          @SuppressWarnings("unchecked")
          Set<Method> existingMethods = (Set<Method>) existingScope.getValue();
          existingMethods.add(method);
        }
        else
        {
          // if existingScope is not null here then addChild() will fail, as in the addChild() calls for other members
          scope.addChild(method.getName(), methodScope);
        }
      }
      else if (memberAST instanceof ClassDefinitionAST)
      {
        Scope innerClassScope = convertInnerClass((ClassDefinitionAST) memberAST, scope);
        InnerClass innerClass = (InnerClass) innerClassScope.getValue();
        innerClasses.add(innerClass);
        scope.addChild(innerClass.getName(), innerClassScope);
      }
      else if (memberAST instanceof InterfaceDefinitionAST)
      {
        Scope innerInterfaceScope = convertInnerInterface((InterfaceDefinitionAST) memberAST, scope);
        ConceptualInterface innerInterface = (ConceptualInterface) innerInterfaceScope.getValue();
        innerInterfaces.add(innerInterface);
        scope.addChild(innerInterface.getName(), innerInterfaceScope);
      }
      else if (memberAST instanceof EnumDefinitionAST)
      {
        Scope innerEnumScope = convertInnerEnum((EnumDefinitionAST) memberAST, scope);
        ConceptualEnum innerEnum = (ConceptualEnum) innerEnumScope.getValue();
        innerEnums.add(innerEnum);
        scope.addChild(innerEnum.getName(), innerEnumScope);
      }
      else
      {
        throw new UnsupportedOperationException("Cannot translate a class member that is not a field, property, static initializer, constructor, method, or type definition.");
      }
    }

    conceptualClass.setMembers(new StaticInitializer(), new VariableInitializers(),
                               variables.toArray(new MemberVariable[0]),
                               properties.toArray(new Property[0]),
                               constructors.toArray(new Constructor[0]),
                               methods.toArray(new Method[0]),
                               innerClasses.toArray(new InnerClass[0]),
                               innerInterfaces.toArray(new ConceptualInterface[0]),
                               innerEnums.toArray(new ConceptualEnum[0]));

    return scope;
  }

  /**
   * Converts the specified InterfaceDefinitionAST into a ConceptualInterface.
   * @param interfaceDefinition - the InterfaceDefinitionAST to convert
   * @param enclosingScope - the scope to make the parent of the new conceptual object's scope
   * @return the Scope of the ConceptualInterface created, which has the ConceptualInterface as its value
   * @throws ConceptualException - if there is a problem with the conversion
   */
  private Scope convert(InterfaceDefinitionAST interfaceDefinition, Scope enclosingScope) throws ConceptualException
  {
    AccessSpecifier access = AccessSpecifier.fromAST(interfaceDefinition.getAccess());
    ModifierAST[] modifiers = interfaceDefinition.getModifiers();
    boolean isImmutable = false;
    SinceSpecifier sinceSpecifier = null;
    for (ModifierAST modifier : modifiers)
    {
      switch (modifier.getType())
      {
      case IMMUTABLE:
        isImmutable = true;
        break;
      case SINCE_SPECIFIER:
        sinceSpecifier = SinceSpecifier.fromAST((SinceSpecifierAST) modifier); // TODO: move to a convert() method
        break;
      default:
        throw new ConceptualException("Illegal Modifier for an Interface Definition", modifier.getParseInfo());
      }
    }

    ConceptualInterface conceptualInterface = new ConceptualInterface(access, isImmutable, sinceSpecifier, interfaceDefinition.getName().getName());
    Scope scope = ScopeFactory.createInterfaceDefinitionScope(conceptualInterface, enclosingScope);
    scopes.put(conceptualInterface, scope);

    // convert the type arguments
    TypeArgumentAST[] typeArgumentASTs = interfaceDefinition.getTypeArguments();
    TypeArgument[] typeArguments = new TypeArgument[typeArgumentASTs.length];
    for (int i = 0; i < typeArgumentASTs.length; i++)
    {
      Scope typeArgumentScope = convert(typeArgumentASTs[i], scope);
      typeArguments[i] = (TypeArgument) typeArgumentScope.getValue();
      scope.addChild(typeArgumentASTs[i].getName().getName(), typeArgumentScope);
    }


    // convert each of the members in turn, switching on the member type. each member is added to a list of the members of its type.
    List<MemberVariable>      variables       = new LinkedList<MemberVariable>();
    List<Property>            properties      = new LinkedList<Property>();
    List<Method>              methods         = new LinkedList<Method>();
    List<InnerClass>          innerClasses    = new LinkedList<InnerClass>();
    List<ConceptualInterface> innerInterfaces = new LinkedList<ConceptualInterface>();
    List<ConceptualEnum>      innerEnums      = new LinkedList<ConceptualEnum>();

    MemberAST[] memberASTs = interfaceDefinition.getMembers();
    for (MemberAST memberAST : memberASTs)
    {
      if (memberAST instanceof FieldAST)
      {
        Scope[] variableScopes = convert((FieldAST) memberAST, scope);
        for (Scope variableScope : variableScopes)
        {
          MemberVariable variable = (MemberVariable) variableScope.getValue();
          if (!variable.isStatic())
          {
            throw new ConceptualException("An interface cannot contain non-static member variables.", memberAST.getParseInfo());
          }
          variables.add(variable);
          scope.addChild(variable.getName(), variableScope);
        }
      }
      else if (memberAST instanceof PropertyAST)
      {
        Scope propertyScope = convert((PropertyAST) memberAST, scope);
        Property property = (Property) propertyScope.getValue();
        properties.add(property);
        scope.addChild(property.getName(), propertyScope);
      }
      else if (memberAST instanceof StaticInitializerAST)
      {
        // do nothing, as the static initializer contains no information that we need to convert right now
        // the contained statements will be converted when the scope hierarchy has been built
      }
      else if (memberAST instanceof MethodAST)
      {
        Scope methodScope = convert((MethodAST) memberAST, scope);
        Method method = (Method) methodScope.getValue();
        methods.add(method);
        // combine the scopes for existing methods
        Scope existingScope = scope.getChild(method.getName());
        if (existingScope != null && existingScope.getType() == ScopeType.METHOD)
        {
          @SuppressWarnings("unchecked")
          Set<Method> existingMethods = (Set<Method>) existingScope.getValue();
          existingMethods.add(method);
        }
        else
        {
          // if existingScope is not null here then addChild() will fail, as in the addChild() calls for other members
          scope.addChild(method.getName(), methodScope);
        }
      }
      else if (memberAST instanceof ClassDefinitionAST)
      {
        Scope innerClassScope = convertInnerClass((ClassDefinitionAST) memberAST, scope);
        InnerClass innerClass = (InnerClass) innerClassScope.getValue();
        innerClasses.add(innerClass);
        scope.addChild(innerClass.getName(), innerClassScope);
      }
      else if (memberAST instanceof InterfaceDefinitionAST)
      {
        Scope innerInterfaceScope = convertInnerInterface((InterfaceDefinitionAST) memberAST, scope);
        ConceptualInterface innerInterface = (ConceptualInterface) innerInterfaceScope.getValue();
        innerInterfaces.add(innerInterface);
        scope.addChild(innerInterface.getName(), innerInterfaceScope);
      }
      else if (memberAST instanceof EnumDefinitionAST)
      {
        Scope innerEnumScope = convertInnerEnum((EnumDefinitionAST) memberAST, scope);
        ConceptualEnum innerEnum = (ConceptualEnum) innerEnumScope.getValue();
        innerEnums.add(innerEnum);
        scope.addChild(innerEnum.getName(), innerEnumScope);
      }
      else
      {
        throw new UnsupportedOperationException("Cannot translate an interface member that is not a static field, property, static initializer, method, or type definition.");
      }
    }

    conceptualInterface.setMembers(new StaticInitializer(),
                                   variables.toArray(new MemberVariable[0]),
                                   properties.toArray(new Property[0]),
                                   methods.toArray(new Method[0]),
                                   innerClasses.toArray(new InnerClass[0]),
                                   innerInterfaces.toArray(new ConceptualInterface[0]),
                                   innerEnums.toArray(new ConceptualEnum[0]));

    return scope;
  }

  /**
   * Converts the specified EnumDefinitionAST into a ConceptualEnum.
   * @param enumDefinition - the EnumDefinitionAST to convert
   * @param enclosingScope - the scope to make the parent of the new conceptual object's scope
   * @return the Scope of the ConceptualEnum created, which has the ConceptualEnum as its value
   * @throws ConceptualException - if there is a problem with the conversion
   */
  private Scope convert(EnumDefinitionAST enumDefinition, Scope enclosingScope) throws ConceptualException
  {
    AccessSpecifier accessSpecifier = AccessSpecifier.fromAST(enumDefinition.getAccessSpecifier());
    ModifierAST[] modifiers = enumDefinition.getModifiers();
    SinceSpecifier sinceSpecifier = null;
    for (ModifierAST modifier : modifiers)
    {
      switch (modifier.getType())
      {
      case SINCE_SPECIFIER:
        sinceSpecifier = SinceSpecifier.fromAST((SinceSpecifierAST) modifier); // TODO: move to a convert() method
        break;
      default:
        throw new ConceptualException("Illegal Modifier for an Enum Definition", modifier.getParseInfo());
      }
    }

    ConceptualEnum conceptualEnum = new ConceptualEnum(accessSpecifier, sinceSpecifier, enumDefinition.getName().getName());
    Scope scope = ScopeFactory.createEnumDefinitionScope(conceptualEnum, enclosingScope);
    scopes.put(conceptualEnum, scope);

    // convert each of the members in turn, switching on the member type. each member is added to a list of the members of its type.

    // convert the enum constants
    EnumConstantAST[] constantASTs = enumDefinition.getConstants();
    EnumConstant[] constants = new EnumConstant[constantASTs.length];
    for (int i = 0; i < constantASTs.length; i++)
    {
      Scope constantScope = convert(constantASTs[i], scope);
      constants[i] = (EnumConstant) constantScope.getValue();
      scope.addChild(constants[i].getName(), constantScope);
    }
    conceptualEnum.setConstants(constants);

    // convert the other members
    List<MemberVariable>      variables       = new LinkedList<MemberVariable>();
    List<Property>            properties      = new LinkedList<Property>();
    List<Constructor>         constructors    = new LinkedList<Constructor>();
    List<Method>              methods         = new LinkedList<Method>();
    List<InnerClass>          innerClasses    = new LinkedList<InnerClass>();
    List<ConceptualInterface> innerInterfaces = new LinkedList<ConceptualInterface>();
    List<ConceptualEnum>      innerEnums      = new LinkedList<ConceptualEnum>();

    MemberAST[] memberASTs = enumDefinition.getMembers();
    for (MemberAST memberAST : memberASTs)
    {
      if (memberAST instanceof FieldAST)
      {
        Scope[] variableScopes = convert((FieldAST) memberAST, scope);
        for (Scope variableScope : variableScopes)
        {
          MemberVariable variable = (MemberVariable) variableScope.getValue();
          variables.add(variable);
          scope.addChild(variable.getName(), variableScope);
        }
      }
      else if (memberAST instanceof PropertyAST)
      {
        Scope propertyScope = convert((PropertyAST) memberAST, scope);
        Property property = (Property) propertyScope.getValue();
        properties.add(property);
        scope.addChild(property.getName(), propertyScope);
      }
      else if (memberAST instanceof StaticInitializerAST)
      {
        // do nothing, as the static initializer contains no information that we need to convert right now
        // the contained statements will be converted when the scope hierarchy has been built
      }
      else if (memberAST instanceof ConstructorAST)
      {
        Scope constructorScope = convert((ConstructorAST) memberAST, scope);
        Constructor constructor = (Constructor) constructorScope.getValue();
        constructors.add(constructor);
        // no need to add to the scope here, you cannot reference a constructor directly, just via the enum constant definitions
      }
      else if (memberAST instanceof MethodAST)
      {
        Scope methodScope = convert((MethodAST) memberAST, scope);
        Method method = (Method) methodScope.getValue();
        methods.add(method);
        // combine the scopes for existing methods
        Scope existingScope = scope.getChild(method.getName());
        if (existingScope != null && existingScope.getType() == ScopeType.METHOD)
        {
          @SuppressWarnings("unchecked")
          Set<Method> existingMethods = (Set<Method>) existingScope.getValue();
          existingMethods.add(method);
        }
        else
        {
          // if existingScope is not null here then addChild() will fail, as in the addChild() calls for other members
          scope.addChild(method.getName(), methodScope);
        }
      }
      else if (memberAST instanceof ClassDefinitionAST)
      {
        Scope innerClassScope = convertInnerClass((ClassDefinitionAST) memberAST, scope);
        InnerClass innerClass = (InnerClass) innerClassScope.getValue();
        innerClasses.add(innerClass);
        scope.addChild(innerClass.getName(), innerClassScope);
      }
      else if (memberAST instanceof InterfaceDefinitionAST)
      {
        Scope innerInterfaceScope = convertInnerInterface((InterfaceDefinitionAST) memberAST, scope);
        ConceptualInterface innerInterface = (ConceptualInterface) innerInterfaceScope.getValue();
        innerInterfaces.add(innerInterface);
        scope.addChild(innerInterface.getName(), innerInterfaceScope);
      }
      else if (memberAST instanceof EnumDefinitionAST)
      {
        Scope innerEnumScope = convertInnerEnum((EnumDefinitionAST) memberAST, scope);
        ConceptualEnum innerEnum = (ConceptualEnum) innerEnumScope.getValue();
        innerEnums.add(innerEnum);
        scope.addChild(innerEnum.getName(), innerEnumScope);
      }
      else
      {
        throw new UnsupportedOperationException("Cannot translate a class member that is not a field, property, static initializer, constructor, method, or type definition.");
      }
    }

    conceptualEnum.setMembers(new StaticInitializer(), new VariableInitializers(),
                               variables.toArray(new MemberVariable[0]),
                               properties.toArray(new Property[0]),
                               constructors.toArray(new Constructor[0]),
                               methods.toArray(new Method[0]),
                               innerClasses.toArray(new InnerClass[0]),
                               innerInterfaces.toArray(new ConceptualInterface[0]),
                               innerEnums.toArray(new ConceptualEnum[0]));

    return scope;
  }

  /**
   * Converts the specified TypeArgumentAST into a TypeArgument.
   * @param typeArgumentAST - the TypeArgumentAST to convert
   * @param enclosingScope - the scope to make the parent of the new conceptual object's scope
   * @return the Scope of the TypeArgument created, which has the TypeArgument as its value
   */
  private Scope convert(TypeArgumentAST typeArgumentAST, Scope enclosingScope)
  {
    TypeArgument typeArgument = new TypeArgument(typeArgumentAST.getName().getName());
    Scope scope = ScopeFactory.createTypeArgumentScope(typeArgument, enclosingScope);
    scopes.put(typeArgument, scope);
    return scope;
  }

  /**
   * Converts the specified FieldAST into an array of MemberVariables.
   * @param fieldAST - the FieldAST to convert
   * @param enclosingScope - the scope to make the parent of the new conceptual objects' scopes
   * @return the Scopes of the MemberVariables created, which have the MemberVariables as their values
   * @throws ConceptualException - if there is a problem with the conversion
   */
  private Scope[] convert(FieldAST fieldAST, Scope enclosingScope) throws ConceptualException
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
    Scope[] memberScopes = new Scope[assignees.length];
    for (int i = 0; i < assignees.length; i++)
    {
      memberVariables[i] = new MemberVariable(accessSpecifier, isFinal, isMutable, isStatic, isVolatile, isTransient, sinceSpecifier, assignees[i].getName().getName());
      memberScopes[i] = ScopeFactory.createMemberVariableScope(memberVariables[i], enclosingScope);
      scopes.put(memberVariables[i], memberScopes[i]);
    }
    return memberScopes;
  }

  /**
   * Converts the specified PropertyAST into a Property
   * @param propertyAST - the PropertyAST to convert
   * @param enclosingScope - the scope to make the parent of the new conceptual object's scope
   * @return the Scope of the Property created, which has the Property as its value
   * @throws ConceptualException - if there is a problem with the conversion
   */
  private Scope convert(PropertyAST propertyAST, Scope enclosingScope) throws ConceptualException
  {
    boolean isFinal = false;
    boolean isMutable = false;
    boolean isSealed = false;
    boolean isStatic = false;
    boolean isSynchronized = false;
    boolean isTransient = false;
    boolean isVolatile = false;
    SinceSpecifier sinceSpecifier = null;
    for (ModifierAST modifier : propertyAST.getModifiers())
    {
      switch (modifier.getType())
      {
      case FINAL:
        isFinal = true;
        break;
      case MUTABLE:
        isMutable = true;
        break;
      case SEALED:
        isSealed = true;
        break;
      case SINCE_SPECIFIER:
        sinceSpecifier = SinceSpecifier.fromAST((SinceSpecifierAST) modifier); // TODO: change this to a convert() method
        break;
      case STATIC:
        isStatic = true;
        break;
      case SYNCHRONIZED:
        isSynchronized = true;
        break;
      case TRANSIENT:
        isTransient = true;
        break;
      case VOLATILE:
        isVolatile = true;
        break;
      default:
        throw new ConceptualException("Illegal Modifier for a Property", modifier.getParseInfo());
      }
    }

    AccessSpecifier retrieveAccessSpecifier = AccessSpecifier.fromAST(propertyAST.getRetrieveAccess()); // TODO: change these to use a convert() method
    AccessSpecifier assignAccessSpecifier = AccessSpecifier.fromAST(propertyAST.getAssignAccess());

    Property property = new Property(isSealed, isMutable, isFinal, isStatic, isSynchronized, isTransient, isVolatile, sinceSpecifier,
                                     propertyAST.getName().getName(), retrieveAccessSpecifier, assignAccessSpecifier);
    Scope scope = ScopeFactory.createPropertyScope(property, enclosingScope);
    scopes.put(property, scope);

    return scope;
  }

  /**
   * Converts the specified MethodAST into a Method
   * @param methodAST - the MethodAST to convert
   * @param enclosingScope - the scope to make the parent of the new conceptual object's scope
   * @return the Scope of the Method created, which has the Method as its value
   * @throws ConceptualException - if there is a problem with the conversion
   */
  private Scope convert(MethodAST methodAST, Scope enclosingScope) throws ConceptualException
  {
    boolean isStatic = false;
    boolean isSealed = false;
    boolean isAbstract = false;
    boolean isImmutable = false;
    boolean isSynchronized = false;
    SinceSpecifier sinceSpecifier = null;
    NativeSpecifier nativeSpecifier = null;
    for (ModifierAST modifier : methodAST.getModifiers())
    {
      switch (modifier.getType())
      {
      case ABSTRACT:
        isAbstract = true;
        break;
      case IMMUTABLE:
        isImmutable = true;
        break;
      case NATIVE_SPECIFIER:
        nativeSpecifier = convert((NativeSpecifierAST) modifier);
        break;
      case SEALED:
        isSealed = true;
        break;
      case SINCE_SPECIFIER:
        sinceSpecifier = convert((SinceSpecifierAST) modifier);
        break;
      case STATIC:
        isStatic = true;
        break;
      case SYNCHRONIZED:
        isSynchronized = true;
        break;
      default:
        throw new ConceptualException("Illegal Modifier for a Method", modifier.getParseInfo());
      }
    }

    AccessSpecifier accessSpecifier = convert(methodAST.getAccessSpecifier());

    Method method = new Method(accessSpecifier, isAbstract, isSealed, isStatic, isSynchronized, isImmutable,
                               sinceSpecifier, nativeSpecifier, methodAST.getName().getName());

    return ScopeFactory.createMethodScope(method, enclosingScope);
  }

  private Scope convert(ConstructorAST constructorAST, Scope enclosingScope)
  {

  }

}
