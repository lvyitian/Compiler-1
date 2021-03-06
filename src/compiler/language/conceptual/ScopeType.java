package compiler.language.conceptual;

/*
 * Created on 23 Dec 2010
 */

/**
 * An enum containing all of the possible types of scope in the language. These are returned by Resolvable's getType() method.
 * @author Anthony Bryant
 */
public enum ScopeType
{
  // below are all of the possible scope types, followed by the type of object they represent
  PACKAGE,               // ConceptualPackage
  FILE,                  // ConceptualFile
  OUTER_CLASS,           // OuterClass
  INNER_CLASS,           // InnerClass
  OUTER_INTERFACE,       // OuterInterface
  INNER_INTERFACE,       // InnerInterface
  OUTER_ENUM,            // OuterEnum
  INNER_ENUM,            // InnerEnum
  ENUM_CONSTANT,         // EnumConstant
  TYPE_PARAMETER,        // TypeParameter
  MEMBER_VARIABLE,       // MemberVariable
  PROPERTY,              // Property
  CONSTRUCTOR,           // Constructor
  METHOD,                // Set<Method>
  UNRESOLVABLE_DUMMY,    // null - this is used where a Scope has a parent, but is not the child of any other scopes, and so will never have a fully qualified name
  ANONYMOUS_INNER_CLASS,
  // TODO: add others
  ;
}
