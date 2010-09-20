package compiler.parser.lalr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import compiler.parser.AcceptAction;
import compiler.parser.Action;
import compiler.parser.Production;
import compiler.parser.ReduceAction;
import compiler.parser.Rule;
import compiler.parser.ShiftAction;
import compiler.parser.State;
import compiler.parser.Token;

/*
 * Created on 21 Jun 2010
 */

/**
 * @author Anthony Bryant
 *
 */
public class LALRState implements State
{

  private static final long serialVersionUID = 1L;

  private Map<Object, LALRState> shiftRules = null;
  private Map<Object, ReduceAction> reduceActions = null;
  // this should only ever contain null -> new AcceptAction(), (null means there are no more tokens)
  // but other conditions for accept are allowed by the parser, so this is more general
  private Map<Object, AcceptAction> acceptActions = null;

  private Map<Object, LALRState> gotoRules = null;

  /**
   * Creates a new LALRState with no shift or reduce actions or goto rules.
   */
  public LALRState()
  {
    // do nothing, as the maps are created lazily
  }

  /**
   * Adds a shift action from this state to the specified state via the specified token type
   * @param tokenType - the token type to shift on
   * @param shiftTo - the state to shift to
   */
  public void addShift(Object tokenType, LALRState shiftTo)
  {
    if (shiftRules != null && shiftRules.containsKey(tokenType))
    {
      throw new IllegalStateException("Shift-shift conflict! Via terminal: " + tokenType);
    }
    ReduceAction reduceAction = reduceActions != null ? reduceActions.get(tokenType) : null;
    if (reduceAction != null)
    {
      Rule rule = reduceAction.getRule();
      Production production = rule.getProductions()[reduceAction.getProductionIndex()];
      throw shiftReduceConflict(tokenType, rule.getType(), production);
    }
    AcceptAction acceptAction = acceptActions != null ? acceptActions.get(tokenType) : null;
    if (acceptAction != null)
    {
      Rule rule = acceptAction.getRule();
      throw shiftAcceptConflict(tokenType, rule.getType(), rule.getProductions()[acceptAction.getProductionIndex()]);
    }
    if (shiftRules == null)
    {
      shiftRules = new HashMap<Object, LALRState>();
    }
    shiftRules.put(tokenType, shiftTo);
  }

  /**
   * Adds a reduce action from this state via the specified rule and production with the specified token type
   * @param tokenType - the token type to shift on
   * @param reduceRule - the rule to reduce with
   * @param productionIndex - the production of the rule to use
   */
  public void addReduce(Object tokenType, Rule reduceRule, int productionIndex)
  {
    if (shiftRules != null && shiftRules.containsKey(tokenType))
    {
      Production production = reduceRule.getProductions()[productionIndex];
      throw shiftReduceConflict(tokenType, reduceRule.getType(), production);
    }
    ReduceAction existingReduce = reduceActions != null ? reduceActions.get(tokenType) : null;
    if (existingReduce != null)
    {
      Production newProduction = reduceRule.getProductions()[productionIndex];
      throw reduceReduceConflict(tokenType, existingReduce, reduceRule.getType(), newProduction);
    }
    AcceptAction existingAccept = acceptActions != null ? acceptActions.get(tokenType) : null;
    if (existingAccept != null)
    {
      Production newProduction = reduceRule.getProductions()[productionIndex];
      Rule acceptRule = existingAccept.getRule();
      throw acceptReduceConflict(tokenType, acceptRule.getType(), acceptRule.getProductions()[existingAccept.getProductionIndex()], reduceRule.getType(), newProduction);
    }
    if (reduceActions == null)
    {
      reduceActions = new HashMap<Object, ReduceAction>();
    }
    reduceActions.put(tokenType, new ReduceAction(reduceRule, productionIndex));
  }

  /**
   * Adds an accept action from this state via the specified token type.
   * @param tokenType - the token type to accept on
   * @param rule - the rule to reduce with before accepting
   * @param productionIndex - the index of the production of the rule to use
   */
  public void addAccept(Object tokenType, Rule rule, int productionIndex)
  {
    if (shiftRules != null && shiftRules.containsKey(tokenType))
    {
      throw shiftAcceptConflict(tokenType, rule.getType(), rule.getProductions()[productionIndex]);
    }
    ReduceAction existingReduce = reduceActions != null ? reduceActions.get(tokenType) : null;
    if (existingReduce != null)
    {
      Rule existingRule = existingReduce.getRule();
      throw acceptReduceConflict(tokenType, rule.getType(), rule.getProductions()[productionIndex], existingRule.getType(), existingRule.getProductions()[existingReduce.getProductionIndex()]);
    }
    AcceptAction existingAccept = acceptActions != null ? acceptActions.get(tokenType) : null;
    if (existingAccept != null)
    {
      Rule existingRule = existingAccept.getRule();
      throw acceptAcceptConflict(tokenType, existingAccept, existingRule.getType(), existingRule.getProductions()[existingAccept.getProductionIndex()]);
    }
    if (acceptActions == null)
    {
      acceptActions = new HashMap<Object, AcceptAction>();
    }
    acceptActions.put(tokenType, new AcceptAction(rule, productionIndex));
  }

  /**
   * Adds a goto from this state with the specified token to the specified other state.
   * @param tokenType - the token type for the goto condition
   * @param state - the state to go to on the specified token
   */
  public void addGoto(Object tokenType, LALRState state)
  {
    if (gotoRules != null && gotoRules.containsKey(tokenType))
    {
      throw new IllegalStateException("Goto-goto conflict! Type:" + tokenType);
    }
    if (gotoRules == null)
    {
      gotoRules = new HashMap<Object, LALRState>();
    }
    gotoRules.put(tokenType, state);
  }

  /**
   * @see compiler.parser.State#getAction(compiler.parser.Token)
   */
  @Override
  public Action getAction(Token terminal)
  {
    Object type = terminal == null ? null : terminal.getType();
    // try to return a shift rule first
    LALRState state = shiftRules != null ? shiftRules.get(type) : null;
    if (state != null)
    {
      return new ShiftAction(state);
    }
    // there was no shift rule, so try a reduce rule
    ReduceAction reduceAction = reduceActions != null ? reduceActions.get(type) : null;
    if (reduceAction != null)
    {
      return reduceAction;
    }
    // there were no shift or reduce rules, so try an accept rule
    AcceptAction acceptAction = acceptActions != null ? acceptActions.get(type) : null;
    if (acceptAction != null)
    {
      return acceptAction;
    }
    // there is nothing in the table for this terminal from this state
    return null;
  }

  /**
   * @see compiler.parser.State#getExpectedTerminalTypes()
   */
  @Override
  public Object[] getExpectedTerminalTypes()
  {
    Set<Object> types = new HashSet<Object>();
    if (shiftRules != null)
    {
      types.addAll(shiftRules.keySet());
    }
    if (reduceActions != null)
    {
      types.addAll(reduceActions.keySet());
    }
    if (acceptActions != null)
    {
      types.addAll(acceptActions.keySet());
    }
    return types.toArray(new Object[types.size()]);
  }

  /**
   * @see compiler.parser.State#getGoto(compiler.parser.Token)
   */
  @Override
  public State getGoto(Token nonTerminal)
  {
    return gotoRules != null ? gotoRules.get(nonTerminal.getType()) : null;
  }

  /**
   * Creates an exception representing a shift-reduce conflict.
   * This exception contains details of the input token type,
   * the type that the reduce action would produce, and
   * the production it would take to get there.
   * @param tokenType - the input token that leads to this conflict
   * @param reduceRuleType - the token type that reducing would produce
   * @param reduceProduction - the production that the reduce rule uses
   * @return an exception representing the shift-reduce conflict
   */
  private static IllegalStateException shiftReduceConflict(Object tokenType, Object reduceRuleType, Production reduceProduction)
  {
    String production = Rule.getProductionString(reduceRuleType, reduceProduction);
    return new IllegalStateException("Shift-reduce conflict! On input: " + tokenType + ", can either shift or reduce via " + production);
  }

  /**
   * Creates an exception representing a shift-accept conflict.
   * This exception contains details of the input token type,
   * the type that the accept action would produce, and
   * the production it would take to get there.
   * @param tokenType - the input token that leads to this conflict
   * @param acceptRuleType - the token type that accepting would produce
   * @param acceptProduction - the production that the accept rule uses
   * @return an exception representing the shift-accept conflict
   */
  private static IllegalStateException shiftAcceptConflict(Object tokenType, Object acceptRuleType, Production acceptProduction)
  {
    String production = Rule.getProductionString(acceptRuleType, acceptProduction);
    return new IllegalStateException("Shift-accept conflict! On input: " + tokenType + ", can either shift or accept via " + production);
  }

  /**
   * Creates an exception representing a reduce-reduce conflict.
   * This exception contains details of the input token type,
   * the type that each reduce action would produce, and
   * the production it would take to get there in each case.
   * @param tokenType - the input token that leads to this conflict
   * @param existingReduce - the existing reduce action
   * @param newRuleType - the token type that the new reduce rule would produce
   * @param newProduction - the production that the new reduce rule uses
   * @return an exception representing the reduce-reduce conflict
   */
  private static IllegalStateException reduceReduceConflict(Object tokenType, ReduceAction existingReduce, Object newRuleType, Production newProduction)
  {
    Rule existingRule = existingReduce.getRule();
    String existingProductionStr = Rule.getProductionString(existingRule.getType(), existingRule.getProductions()[existingReduce.getProductionIndex()]);
    String newProductionStr = Rule.getProductionString(newRuleType, newProduction);
    return new IllegalStateException("Reduce-reduce conflict! On input: " + tokenType + ", can reduce via " + existingProductionStr + " or " + newProductionStr);
  }

  /**
   * Creates an exception representing an accept-reduce conflict.
   * This exception contains details of the input token type,
   * the type that each action would produce, and
   * the production that it would take to get there in each case.
   * @param tokenType - the input token that leads to this conflict
   * @param acceptRuleType - the token type that the accept rule would produce
   * @param acceptProduction - the production that the accept rule uses
   * @param reduceRuleType - the token type that reducing would produce
   * @param reduceProduction - the production that the reduce rule uses
   * @return an exception representing the accept-reduce conflict
   */
  private static IllegalStateException acceptReduceConflict(Object tokenType, Object acceptRuleType, Production acceptProduction, Object reduceRuleType, Production reduceProduction)
  {
    String acceptProductionStr = Rule.getProductionString(acceptRuleType, acceptProduction);
    String productionStr = Rule.getProductionString(reduceRuleType, reduceProduction);
    return new IllegalStateException("Accept-reduce conflict! On input: " + tokenType + ", can accept via " + acceptProductionStr + " or reduce via " + productionStr);
  }

  /**
   * Creates an exception representing an accept-accept conflict.
   * This exception contains details of the input token type,
   * the type that each action would produce, and
   * the production that it would take to get there in each case.
   * @param tokenType - the input token that leads to this conflict
   * @param existingAccept - the existing accept action
   * @param acceptRuleType - the token type that the accept rule would produce
   * @param acceptProduction - the production that the accept rule uses
   * @return an exception representing the accept-accept conflict
   */
  private static IllegalStateException acceptAcceptConflict(Object tokenType, AcceptAction existingAccept, Object acceptRuleType, Production acceptProduction)
  {
    Rule existingRule = existingAccept.getRule();
    String existingProductionStr = Rule.getProductionString(existingRule.getType(), existingRule.getProductions()[existingAccept.getProductionIndex()]);
    String acceptProductionStr = Rule.getProductionString(acceptRuleType, acceptProduction);
    return new IllegalStateException("Accept-accept conflict! On input: " + tokenType + ", can accept via " + existingProductionStr + " or via " + acceptProductionStr);
  }
}
