package compiler.language.parser;

import parser.Rule;
import parser.lalr.LALRRuleSet;

import compiler.language.parser.rules.expression.AdditiveExpressionRule;
import compiler.language.parser.rules.expression.ArrayAccessExpressionRule;
import compiler.language.parser.rules.expression.ArrayInitializerRule;
import compiler.language.parser.rules.expression.ArrayInstanciationExpressionNoInitializerRule;
import compiler.language.parser.rules.expression.ArrayInstanciationExpressionWithInitializerRule;
import compiler.language.parser.rules.expression.BasicPrimaryRule;
import compiler.language.parser.rules.expression.BitwiseAndExpressionRule;
import compiler.language.parser.rules.expression.BitwiseOrExpressionRule;
import compiler.language.parser.rules.expression.BitwiseXorExpressionRule;
import compiler.language.parser.rules.expression.BooleanAndExpressionRule;
import compiler.language.parser.rules.expression.BooleanLiteralExpressionRule;
import compiler.language.parser.rules.expression.BooleanOrExpressionRule;
import compiler.language.parser.rules.expression.BooleanXorExpressionRule;
import compiler.language.parser.rules.expression.CastExpressionRule;
import compiler.language.parser.rules.expression.ClosureCreationExpressionRule;
import compiler.language.parser.rules.expression.DimensionExpressionRule;
import compiler.language.parser.rules.expression.DimensionExpressionsRule;
import compiler.language.parser.rules.expression.EqualityExpressionRule;
import compiler.language.parser.rules.expression.ExpressionListRule;
import compiler.language.parser.rules.expression.ExpressionNoTupleRule;
import compiler.language.parser.rules.expression.ExpressionRule;
import compiler.language.parser.rules.expression.FieldAccessExpressionNotQNameRule;
import compiler.language.parser.rules.expression.FieldAccessExpressionRule;
import compiler.language.parser.rules.expression.InlineIfExpressionRule;
import compiler.language.parser.rules.expression.InstanciationExpressionRule;
import compiler.language.parser.rules.expression.MethodCallExpressionRule;
import compiler.language.parser.rules.expression.MultiplicativeExpressionRule;
import compiler.language.parser.rules.expression.PrimaryNoTrailingDimensionsNotQNameRule;
import compiler.language.parser.rules.expression.PrimaryRule;
import compiler.language.parser.rules.expression.QNameExpressionRule;
import compiler.language.parser.rules.expression.QNameOrLessThanExpressionRule;
import compiler.language.parser.rules.expression.RelationalExpressionLessThanQNameRule;
import compiler.language.parser.rules.expression.RelationalExpressionNotLessThanQNameRule;
import compiler.language.parser.rules.expression.ShiftExpressionRule;
import compiler.language.parser.rules.expression.StatementExpressionRule;
import compiler.language.parser.rules.expression.SuperAccessExpressionRule;
import compiler.language.parser.rules.expression.ThisAccessExpressionRule;
import compiler.language.parser.rules.expression.TupleExpressionRule;
import compiler.language.parser.rules.expression.TupleIndexExpressionRule;
import compiler.language.parser.rules.expression.UnaryExpressionRule;
import compiler.language.parser.rules.member.AccessSpecifierRule;
import compiler.language.parser.rules.member.ConstructorRule;
import compiler.language.parser.rules.member.FieldRule;
import compiler.language.parser.rules.member.MemberHeaderRule;
import compiler.language.parser.rules.member.MemberListRule;
import compiler.language.parser.rules.member.MemberRule;
import compiler.language.parser.rules.member.MethodRule;
import compiler.language.parser.rules.member.PropertyRule;
import compiler.language.parser.rules.member.StaticInitializerRule;
import compiler.language.parser.rules.misc.ArgumentListRule;
import compiler.language.parser.rules.misc.ArgumentRule;
import compiler.language.parser.rules.misc.ArgumentsRule;
import compiler.language.parser.rules.misc.AssigneeListRule;
import compiler.language.parser.rules.misc.AssigneeRule;
import compiler.language.parser.rules.misc.AssignmentOperatorRule;
import compiler.language.parser.rules.misc.DeclarationAssigneeListRule;
import compiler.language.parser.rules.misc.DeclarationAssigneeRule;
import compiler.language.parser.rules.misc.DimensionsRule;
import compiler.language.parser.rules.misc.ModifierNotSynchronizedRule;
import compiler.language.parser.rules.misc.ModifierRule;
import compiler.language.parser.rules.misc.ModifiersNotSynchronizedRule;
import compiler.language.parser.rules.misc.ModifiersRule;
import compiler.language.parser.rules.misc.NativeSpecifierRule;
import compiler.language.parser.rules.misc.NestedQNameListRule;
import compiler.language.parser.rules.misc.ParameterListRule;
import compiler.language.parser.rules.misc.ParameterRule;
import compiler.language.parser.rules.misc.ParametersRule;
import compiler.language.parser.rules.misc.QNameListRule;
import compiler.language.parser.rules.misc.QNameRule;
import compiler.language.parser.rules.misc.ThrowsClauseRule;
import compiler.language.parser.rules.misc.ThrowsListRule;
import compiler.language.parser.rules.statement.AssignmentRule;
import compiler.language.parser.rules.statement.BlockRule;
import compiler.language.parser.rules.statement.BreakStatementRule;
import compiler.language.parser.rules.statement.CatchClauseRule;
import compiler.language.parser.rules.statement.CatchClausesRule;
import compiler.language.parser.rules.statement.ContinueStatementRule;
import compiler.language.parser.rules.statement.DecrementRule;
import compiler.language.parser.rules.statement.DoStatementRule;
import compiler.language.parser.rules.statement.ElseClauseRule;
import compiler.language.parser.rules.statement.ElseIfClauseRule;
import compiler.language.parser.rules.statement.ElseIfClausesRule;
import compiler.language.parser.rules.statement.EmptyStatementRule;
import compiler.language.parser.rules.statement.FallthroughStatementRule;
import compiler.language.parser.rules.statement.FinallyClauseRule;
import compiler.language.parser.rules.statement.ForEachStatementRule;
import compiler.language.parser.rules.statement.ForInitRule;
import compiler.language.parser.rules.statement.ForStatementRule;
import compiler.language.parser.rules.statement.ForUpdateRule;
import compiler.language.parser.rules.statement.IfStatementRule;
import compiler.language.parser.rules.statement.IncrementRule;
import compiler.language.parser.rules.statement.LocalDeclarationRule;
import compiler.language.parser.rules.statement.OptionalBlockRule;
import compiler.language.parser.rules.statement.ReturnStatementRule;
import compiler.language.parser.rules.statement.StatementRule;
import compiler.language.parser.rules.statement.StatementsRule;
import compiler.language.parser.rules.statement.SwitchCaseRule;
import compiler.language.parser.rules.statement.SwitchCasesRule;
import compiler.language.parser.rules.statement.SwitchStatementRule;
import compiler.language.parser.rules.statement.SynchronizedStatementRule;
import compiler.language.parser.rules.statement.ThrowStatementRule;
import compiler.language.parser.rules.statement.TryStatementRule;
import compiler.language.parser.rules.statement.WhileStatementRule;
import compiler.language.parser.rules.topLevel.CompilationUnitRule;
import compiler.language.parser.rules.topLevel.ImportDeclarationRule;
import compiler.language.parser.rules.topLevel.PackageDeclarationRule;
import compiler.language.parser.rules.topLevel.TypeDefinitionRule;
import compiler.language.parser.rules.type.ArrayTypeRule;
import compiler.language.parser.rules.type.BooleanTypeRule;
import compiler.language.parser.rules.type.CharacterTypeRule;
import compiler.language.parser.rules.type.ClosureTypeRule;
import compiler.language.parser.rules.type.FloatingTypeRule;
import compiler.language.parser.rules.type.IntegerTypeRule;
import compiler.language.parser.rules.type.PointerTypeDoubleRAngleRule;
import compiler.language.parser.rules.type.PointerTypeNoTrailingArgsNotQNameRule;
import compiler.language.parser.rules.type.PointerTypeNotQNameRule;
import compiler.language.parser.rules.type.PointerTypeRAngleRule;
import compiler.language.parser.rules.type.PointerTypeRule;
import compiler.language.parser.rules.type.PointerTypeTrailingArgsDoubleRAngleRule;
import compiler.language.parser.rules.type.PointerTypeTrailingArgsRAngleRule;
import compiler.language.parser.rules.type.PointerTypeTrailingArgsRule;
import compiler.language.parser.rules.type.PointerTypeTrailingArgsTripleRAngleRule;
import compiler.language.parser.rules.type.PointerTypeTripleRAngleRule;
import compiler.language.parser.rules.type.PrimitiveTypeRule;
import compiler.language.parser.rules.type.TupleTypeNotQNameListRule;
import compiler.language.parser.rules.type.TupleTypeRule;
import compiler.language.parser.rules.type.TypeArgumentDoubleRAngleRule;
import compiler.language.parser.rules.type.TypeArgumentListDoubleRAngleRule;
import compiler.language.parser.rules.type.TypeArgumentListRAngleRule;
import compiler.language.parser.rules.type.TypeArgumentListRule;
import compiler.language.parser.rules.type.TypeArgumentListTripleRAngleRule;
import compiler.language.parser.rules.type.TypeArgumentNotQNameListRule;
import compiler.language.parser.rules.type.TypeArgumentRAngleRule;
import compiler.language.parser.rules.type.TypeArgumentTripleRAngleRule;
import compiler.language.parser.rules.type.TypeArgumentsRule;
import compiler.language.parser.rules.type.TypeBoundListDoubleRAngleRule;
import compiler.language.parser.rules.type.TypeBoundListRAngleRule;
import compiler.language.parser.rules.type.TypeBoundListRule;
import compiler.language.parser.rules.type.TypeBoundListTripleRAngleRule;
import compiler.language.parser.rules.type.TypeDoubleRAngleRule;
import compiler.language.parser.rules.type.TypeListNotQNameListRule;
import compiler.language.parser.rules.type.TypeListRule;
import compiler.language.parser.rules.type.TypeNotArrayTypeRule;
import compiler.language.parser.rules.type.TypeNotPointerTypeNotTupleTypeRule;
import compiler.language.parser.rules.type.TypeNotQNameListRule;
import compiler.language.parser.rules.type.TypeNotQNameRule;
import compiler.language.parser.rules.type.TypeParameterListRAngleRule;
import compiler.language.parser.rules.type.TypeParameterListRule;
import compiler.language.parser.rules.type.TypeParameterRAngleRule;
import compiler.language.parser.rules.type.TypeParameterRule;
import compiler.language.parser.rules.type.TypeParametersRule;
import compiler.language.parser.rules.type.TypeRAngleRule;
import compiler.language.parser.rules.type.TypeRule;
import compiler.language.parser.rules.type.TypeTripleRAngleRule;
import compiler.language.parser.rules.type.VoidTypeRule;
import compiler.language.parser.rules.type.WildcardTypeArgumentDoubleRAngleRule;
import compiler.language.parser.rules.type.WildcardTypeArgumentRAngleRule;
import compiler.language.parser.rules.type.WildcardTypeArgumentRule;
import compiler.language.parser.rules.type.WildcardTypeArgumentTripleRAngleRule;
import compiler.language.parser.rules.typeDefinition.ClassDefinitionRule;
import compiler.language.parser.rules.typeDefinition.ClassExtendsClauseRule;
import compiler.language.parser.rules.typeDefinition.EnumConstantListRule;
import compiler.language.parser.rules.typeDefinition.EnumConstantRule;
import compiler.language.parser.rules.typeDefinition.EnumConstantsRule;
import compiler.language.parser.rules.typeDefinition.EnumDefinitionRule;
import compiler.language.parser.rules.typeDefinition.ImplementsClauseRule;
import compiler.language.parser.rules.typeDefinition.InterfaceDefinitionRule;
import compiler.language.parser.rules.typeDefinition.InterfaceExtendsClauseRule;
import compiler.language.parser.rules.typeDefinition.InterfaceListRule;

/*
 * Created on 30 Jun 2010
 */

/**
 * Manages the set of rules used by the language parser. This is really just a list of all rules used in the language, and is used in parser generation.
 * @author Anthony Bryant
 */
public class LanguageRules
{

  private static final Rule<ParseType> startRule = new CompilationUnitRule();

  // these are all of type Rule<ParseType>, but an array of Rule<ParseType>
  // cannot be created, so we use the raw type here
  @SuppressWarnings("rawtypes")
  private static final Rule[] rules = new Rule[]
  {
    // top level
    // startRule (does not need to be included here): new CompilationUnitRule(),
    new ImportDeclarationRule(),
    new PackageDeclarationRule(),
    new TypeDefinitionRule(),

    // type definitions
    new ClassDefinitionRule(),
    new ClassExtendsClauseRule(),
    new EnumConstantListRule(),
    new EnumConstantRule(),
    new EnumConstantsRule(),
    new EnumDefinitionRule(),
    new ImplementsClauseRule(),
    new InterfaceDefinitionRule(),
    new InterfaceExtendsClauseRule(),
    new InterfaceListRule(),

    // members
    new AccessSpecifierRule(),
    new ConstructorRule(),
    new FieldRule(),
    new MemberHeaderRule(),
    new MemberListRule(),
    new MemberRule(),
    new MethodRule(),
    new PropertyRule(),
    new StaticInitializerRule(),

    // statements
    new AssignmentRule(),
    new BlockRule(),
    new BreakStatementRule(),
    new CatchClauseRule(),
    new CatchClausesRule(),
    new ContinueStatementRule(),
    new DecrementRule(),
    new DoStatementRule(),
    new ElseClauseRule(),
    new ElseIfClauseRule(),
    new ElseIfClausesRule(),
    new EmptyStatementRule(),
    new FallthroughStatementRule(),
    new FinallyClauseRule(),
    new ForEachStatementRule(),
    new ForInitRule(),
    new ForStatementRule(),
    new ForUpdateRule(),
    new IfStatementRule(),
    new IncrementRule(),
    new LocalDeclarationRule(),
    new OptionalBlockRule(),
    new ReturnStatementRule(),
    new StatementRule(),
    new StatementsRule(),
    new SwitchCaseRule(),
    new SwitchCasesRule(),
    new SwitchStatementRule(),
    new SynchronizedStatementRule(),
    new ThrowStatementRule(),
    new TryStatementRule(),
    new WhileStatementRule(),

    // expressions
    new AdditiveExpressionRule(),
    new ArrayAccessExpressionRule(),
    new ArrayInitializerRule(),
    new ArrayInstanciationExpressionNoInitializerRule(),
    new ArrayInstanciationExpressionWithInitializerRule(),
    new BasicPrimaryRule(),
    new BitwiseAndExpressionRule(),
    new BitwiseOrExpressionRule(),
    new BitwiseXorExpressionRule(),
    new BooleanAndExpressionRule(),
    new BooleanLiteralExpressionRule(),
    new BooleanOrExpressionRule(),
    new BooleanXorExpressionRule(),
    new CastExpressionRule(),
    new ClosureCreationExpressionRule(),
    new DimensionExpressionRule(),
    new DimensionExpressionsRule(),
    new EqualityExpressionRule(),
    new ExpressionListRule(),
    new ExpressionNoTupleRule(),
    new ExpressionRule(),
    new FieldAccessExpressionNotQNameRule(),
    new FieldAccessExpressionRule(),
    new InlineIfExpressionRule(),
    new InstanciationExpressionRule(),
    new MethodCallExpressionRule(),
    new MultiplicativeExpressionRule(),
    new PrimaryNoTrailingDimensionsNotQNameRule(),
    new PrimaryRule(),
    new QNameExpressionRule(),
    new QNameOrLessThanExpressionRule(),
    new RelationalExpressionLessThanQNameRule(),
    new RelationalExpressionNotLessThanQNameRule(),
    new ShiftExpressionRule(),
    new StatementExpressionRule(),
    new SuperAccessExpressionRule(),
    new ThisAccessExpressionRule(),
    new TupleExpressionRule(),
    new TupleIndexExpressionRule(),
    new UnaryExpressionRule(),

    // types
    new ArrayTypeRule(),
    new BooleanTypeRule(),
    new CharacterTypeRule(),
    new ClosureTypeRule(),
    new FloatingTypeRule(),
    new IntegerTypeRule(),
    new PointerTypeDoubleRAngleRule(),
    new PointerTypeNotQNameRule(),
    new PointerTypeNoTrailingArgsNotQNameRule(),
    new PointerTypeRAngleRule(),
    new PointerTypeRule(),
    new PointerTypeTrailingArgsDoubleRAngleRule(),
    new PointerTypeTrailingArgsRAngleRule(),
    new PointerTypeTrailingArgsRule(),
    new PointerTypeTrailingArgsTripleRAngleRule(),
    new PointerTypeTripleRAngleRule(),
    new PrimitiveTypeRule(),
    new TupleTypeNotQNameListRule(),
    new TupleTypeRule(),
    new TypeArgumentDoubleRAngleRule(),
    new TypeArgumentListDoubleRAngleRule(),
    new TypeArgumentListRAngleRule(),
    new TypeArgumentListRule(),
    new TypeArgumentListTripleRAngleRule(),
    new TypeArgumentNotQNameListRule(),
    new TypeArgumentRAngleRule(),
    new TypeArgumentsRule(),
    new TypeArgumentTripleRAngleRule(),
    new TypeBoundListDoubleRAngleRule(),
    new TypeBoundListRAngleRule(),
    new TypeBoundListRule(),
    new TypeBoundListTripleRAngleRule(),
    new TypeDoubleRAngleRule(),
    new TypeListNotQNameListRule(),
    new TypeListRule(),
    new TypeNotArrayTypeRule(),
    new TypeNotPointerTypeNotTupleTypeRule(),
    new TypeNotQNameListRule(),
    new TypeNotQNameRule(),
    new TypeParameterListRAngleRule(),
    new TypeParameterListRule(),
    new TypeParameterRAngleRule(),
    new TypeParameterRule(),
    new TypeParametersRule(),
    new TypeRAngleRule(),
    new TypeRule(),
    new TypeTripleRAngleRule(),
    new VoidTypeRule(),
    new WildcardTypeArgumentDoubleRAngleRule(),
    new WildcardTypeArgumentRAngleRule(),
    new WildcardTypeArgumentRule(),
    new WildcardTypeArgumentTripleRAngleRule(),

    // miscellaneous
    new ArgumentListRule(),
    new ArgumentRule(),
    new ArgumentsRule(),
    new AssigneeListRule(),
    new AssigneeRule(),
    new AssignmentOperatorRule(),
    new DeclarationAssigneeListRule(),
    new DeclarationAssigneeRule(),
    new DimensionsRule(),
    new ModifierNotSynchronizedRule(),
    new ModifierRule(),
    new ModifiersNotSynchronizedRule(),
    new ModifiersRule(),
    new NativeSpecifierRule(),
    new NestedQNameListRule(),
    new ParameterListRule(),
    new ParameterRule(),
    new ParametersRule(),
    new QNameListRule(),
    new QNameRule(),
    new ThrowsClauseRule(),
    new ThrowsListRule(),
  };

  /**
   * @return an LALRRuleSet containing all of the rules needed to parse this language
   */
  @SuppressWarnings("unchecked")
  public static LALRRuleSet<ParseType> getRuleSet()
  {
    LALRRuleSet<ParseType> ruleSet = new LALRRuleSet<ParseType>();
    ruleSet.addStartRule(startRule);
    for (Rule<ParseType> rule : rules)
    {
      ruleSet.addRule(rule);
    }
    return ruleSet;
  }
}
