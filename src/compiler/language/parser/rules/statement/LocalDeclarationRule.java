package compiler.language.parser.rules.statement;

import static compiler.language.parser.ParseType.DECLARATION_ASSIGNEE_LIST;
import static compiler.language.parser.ParseType.EQUALS;
import static compiler.language.parser.ParseType.EXPRESSION;
import static compiler.language.parser.ParseType.LOCAL_DECLARATION;
import static compiler.language.parser.ParseType.MODIFIERS_NOT_SYNCHRONIZED;
import static compiler.language.parser.ParseType.TYPE;
import parser.ParseException;
import parser.Production;
import parser.Rule;

import compiler.language.LexicalPhrase;
import compiler.language.ast.expression.ExpressionAST;
import compiler.language.ast.misc.DeclarationAssigneeAST;
import compiler.language.ast.misc.ModifierAST;
import compiler.language.ast.statement.LocalDeclarationStatementAST;
import compiler.language.ast.type.TypeAST;
import compiler.language.parser.ParseList;
import compiler.language.parser.ParseType;

/*
 * Created on 16 Aug 2010
 */

/**
 * @author Anthony Bryant
 */
public final class LocalDeclarationRule extends Rule<ParseType>
{
  private static final long serialVersionUID = 1L;

  private static final Production<ParseType> PRODUCTION                      = new Production<ParseType>(                            TYPE, DECLARATION_ASSIGNEE_LIST);
  private static final Production<ParseType> ASSIGNMENT_PRODUCTION           = new Production<ParseType>(                            TYPE, DECLARATION_ASSIGNEE_LIST, EQUALS, EXPRESSION);
  private static final Production<ParseType> MODIFIERS_PRODUCTION            = new Production<ParseType>(MODIFIERS_NOT_SYNCHRONIZED, TYPE, DECLARATION_ASSIGNEE_LIST);
  private static final Production<ParseType> MODIFIERS_ASSIGNMENT_PRODUCTION = new Production<ParseType>(MODIFIERS_NOT_SYNCHRONIZED, TYPE, DECLARATION_ASSIGNEE_LIST, EQUALS, EXPRESSION);

  @SuppressWarnings("unchecked")
  public LocalDeclarationRule()
  {
    super(LOCAL_DECLARATION, PRODUCTION, ASSIGNMENT_PRODUCTION, MODIFIERS_PRODUCTION, MODIFIERS_ASSIGNMENT_PRODUCTION);
  }

  /**
   * {@inheritDoc}
   * @see parser.Rule#match(parser.Production, java.lang.Object[])
   */
  @Override
  public Object match(Production<ParseType> production, Object[] args) throws ParseException
  {
    if (PRODUCTION.equals(production))
    {
      TypeAST type = (TypeAST) args[0];
      @SuppressWarnings("unchecked")
      ParseList<DeclarationAssigneeAST> assignees = (ParseList<DeclarationAssigneeAST>) args[1];
      return new LocalDeclarationStatementAST(new ModifierAST[0], type, assignees.toArray(new DeclarationAssigneeAST[0]), null,
                                           LexicalPhrase.combine(type.getLexicalPhrase(), assignees.getLexicalPhrase()));
    }
    if (ASSIGNMENT_PRODUCTION.equals(production))
    {
      TypeAST type = (TypeAST) args[0];
      @SuppressWarnings("unchecked")
      ParseList<DeclarationAssigneeAST> assignees = (ParseList<DeclarationAssigneeAST>) args[1];
      ExpressionAST expression = (ExpressionAST) args[3];
      return new LocalDeclarationStatementAST(new ModifierAST[0], type, assignees.toArray(new DeclarationAssigneeAST[0]), expression,
                                           LexicalPhrase.combine(type.getLexicalPhrase(), assignees.getLexicalPhrase(), (LexicalPhrase) args[2], expression.getLexicalPhrase()));
    }
    if (MODIFIERS_PRODUCTION.equals(production))
    {
      @SuppressWarnings("unchecked")
      ParseList<ModifierAST> modifiers = (ParseList<ModifierAST>) args[0];
      TypeAST type = (TypeAST) args[1];
      @SuppressWarnings("unchecked")
      ParseList<DeclarationAssigneeAST> assignees = (ParseList<DeclarationAssigneeAST>) args[2];
      return new LocalDeclarationStatementAST(modifiers.toArray(new ModifierAST[0]), type, assignees.toArray(new DeclarationAssigneeAST[0]), null,
                                           LexicalPhrase.combine(modifiers.getLexicalPhrase(), type.getLexicalPhrase(), assignees.getLexicalPhrase()));
    }
    if (MODIFIERS_ASSIGNMENT_PRODUCTION.equals(production))
    {
      @SuppressWarnings("unchecked")
      ParseList<ModifierAST> modifiers = (ParseList<ModifierAST>) args[0];
      TypeAST type = (TypeAST) args[1];
      @SuppressWarnings("unchecked")
      ParseList<DeclarationAssigneeAST> assignees = (ParseList<DeclarationAssigneeAST>) args[2];
      ExpressionAST expression = (ExpressionAST) args[4];
      return new LocalDeclarationStatementAST(modifiers.toArray(new ModifierAST[0]), type, assignees.toArray(new DeclarationAssigneeAST[0]), expression,
                                           LexicalPhrase.combine(modifiers.getLexicalPhrase(), type.getLexicalPhrase(), assignees.getLexicalPhrase(), (LexicalPhrase) args[3], expression.getLexicalPhrase()));
    }
    throw badTypeList();
  }

}
