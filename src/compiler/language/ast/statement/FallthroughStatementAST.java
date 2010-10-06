package compiler.language.ast.statement;

import compiler.language.ast.ParseInfo;
import compiler.language.ast.terminal.IntegerLiteralAST;

/*
 * Created on 13 Aug 2010
 */

/**
 * @author Anthony Bryant
 */
public class FallthroughStatementAST extends StatementAST
{

  private IntegerLiteralAST fallthroughLevels;

  /**
   * Creates a new FallthroughStatementAST to fall through the specified number of levels of switch statements.
   * @param fallthroughLevels - the number of levels of switch statements to fallthrough, or null if it was not specified
   * @param parseInfo - the parsing information
   */
  public FallthroughStatementAST(IntegerLiteralAST fallthroughLevels, ParseInfo parseInfo)
  {
    super(parseInfo);
    this.fallthroughLevels = fallthroughLevels;
  }

  /**
   * @return the fallthroughLevels
   */
  public IntegerLiteralAST getFallthroughLevels()
  {
    return fallthroughLevels;
  }

  /**
   * {@inheritDoc}
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    if (fallthroughLevels == null)
    {
      return "fallthrough;";
    }
    return "fallthrough(" + fallthroughLevels + ");";
  }
}