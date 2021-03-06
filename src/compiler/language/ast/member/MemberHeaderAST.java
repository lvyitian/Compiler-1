package compiler.language.ast.member;

import compiler.language.LexicalPhrase;
import compiler.language.ast.misc.ModifierAST;



/*
 * Created on 13 Jul 2010
 */

/**
 * @author Anthony Bryant
 */
public class MemberHeaderAST
{

  private LexicalPhrase lexicalPhrase;

  private AccessSpecifierAST accessSpecifier;
  private ModifierAST[] modifiers;

  /**
   * Creates a new MemberHeaderAST with the specified access specifier and modifiers
   * @param accessSpecifier - the access specifier of this member header
   * @param modifiers - the modifiers of this member header
   * @param lexicalPhrase - the lexical phrase associated with this AST node
   */
  public MemberHeaderAST(AccessSpecifierAST accessSpecifier, ModifierAST[] modifiers, LexicalPhrase lexicalPhrase)
  {
    this.lexicalPhrase = lexicalPhrase;
    this.accessSpecifier = accessSpecifier;
    this.modifiers = modifiers;
  }

  /**
   * @return the access specifier
   */
  public AccessSpecifierAST getAccessSpecifier()
  {
    return accessSpecifier;
  }

  /**
   * @return the modifiers
   */
  public ModifierAST[] getModifiers()
  {
    return modifiers;
  }

  /**
   * @return the lexicalPhrase
   */
  public LexicalPhrase getLexicalPhrase()
  {
    return lexicalPhrase;
  }

}
