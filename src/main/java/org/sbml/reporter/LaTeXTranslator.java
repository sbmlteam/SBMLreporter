/*
 * ----------------------------------------------------------------------------
 * This file is part of SBMLreporter, a documentation tool for systems biology
 * models. Please visit <https://github.com/sbmlteam/SBMLreporter> for the
 * latest version of SBMLreporter and more information about this program.
 * Copyright (C) 2016 jointly by the following organizations:
 * 1. The University of Tuebingen, Germany
 * 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
 * 3. The California Institute of Technology, Pasadena, CA, USA
 * 4. The University of California, San Diego, La Jolla, CA, USA
 * 5. The Babraham Institute, Cambridge, UK
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation. A copy of the license agreement is provided
 * in the file named "LICENSE" included with this software distribution and also
 * available as <https://github.com/sbmlteam/SBMLreporter/edit/master/LICENSE>.
 * ----------------------------------------------------------------------------
 */
package org.sbml.reporter;

import java.util.List;
import org.sbml.jsbml.ASTNode;

/**
 * This class provides functionality to translate commands in LaTeX format
 *
 * @author Sophia Mersmann
 * @author Jennifer Boedker
 * @version 1.0
 * @since 1.0
 */
public class LaTeXTranslator extends AbstractTranslator {

  private static final String NEW_LINE = " \\\\ ";


  public LaTeXTranslator() {
    super();
    loadProperties("/LaTeXMasking.xml");
    // TODO Auto-generated constructor stub
  }


  /**
   * @param documentClass
   * @return
   */
  protected String setDocumentclass(String documentClass) {
    return command("documentclass", documentClass);
  }


  /**
   * @param packageNames
   * @return
   */
  protected String setUsePackages(String... packageNames) {
    StringBuilder sb = new StringBuilder();
    for (String packageName : packageNames) {
      sb.append(command("usepackage", packageName));
    }
    return sb.toString();
  }


  /**
   * @param packageName
   * @param option
   * @return
   */
  protected String setUsePackageWithOption(String packageName, String option) {
    return command("usepackage", packageName, option);
  }


  /**
   * @param title
   * @return
   */
  protected String setTitle(String title) {
    return command("title", mask(title));
  }


  /**
   * @param author
   * @return
   */
  protected String setAuthor(String author) {
    return command("author", "Version " + mask(author));
  }


  /**
   * @param date
   * @return
   */
  protected String setDate(String date) {
    return command("date", mask(date));
  }


  @Override
  public String initializeDocument() {
    StringBuilder sb = new StringBuilder();
    sb.append(command("begin", "document"));
    sb.append(commandNoOptions("maketitle"));
    sb.append(commandNoOptions("tableofcontents"));
    return sb.toString();
  }


  @Override
  public String createHeading(String headline, int mode) {
    StringBuilder sb = new StringBuilder();
    switch (mode) {
    case 1:
      sb.append(command("chapter", mask(headline)));
      break;
    case 2:
      sb.append(command("section", mask(headline)));
      break;
    case 3:
      sb.append(command("subsection", mask(headline)));
      break;
    default:
      sb.append(command("subsubsection", mask(headline)));
    }
    return sb.toString();
  }


  @Override
  public String createHeading(String headline, int mode, String id) {
    StringBuilder secondArg = new StringBuilder();
    switch (mode) {
    case 1:
      secondArg.append("\\chapter{");
      break;
    case 2:
      secondArg.append("\\section{");
      break;
    case 3:
      secondArg.append("\\subsection{");
      break;
    default:
      secondArg.append("\\subsubsection{");
    }
    secondArg.append(mask(headline));
    secondArg.append("}");
    return commandNoOptions("hypertarget", id, secondArg.toString());
  }


  @Override
  public String createSimpleText(String content) {
    StringBuilder sb = new StringBuilder();
    sb.append(mask(content));
    sb.append(System.lineSeparator());
    return sb.toString();
  }


  @Override
  public String openTable(String caption, int numColumns) {
    StringBuilder secondArg = new StringBuilder();
    // double width = Math.floor(19/numColumns);
    for (int i = 1; i <= numColumns; i++) {
      secondArg.append("p{19mm}");
    }
    StringBuilder sb = new StringBuilder();
    sb.append(commandNoOptions("begin", "longtable", secondArg.toString()));
    sb.append(command("caption", mask(caption)));
    sb.append(NEW_LINE);
    sb.append(commandNoOptions("toprule"));
    return sb.toString();
  }


  @Override
  public String createTableHeading(String... content) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < content.length - 1; i++) {
      sb.append(mask(content[i]));
      sb.append(" & ");
    }
    sb.append(content[content.length - 1]);
    sb.append(NEW_LINE);
    sb.append(commandNoOptions("midrule"));
    return sb.toString();
  }


  @Override
  public String createTableRow(List<Cell> cells) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < cells.size() - 1; i++) {
      sb.append(createCell(cells.get(i)));
      sb.append(" & ");
    }
    sb.append(createCell(cells.get(cells.size() - 1)));
    sb.append(NEW_LINE);
    sb.append(System.lineSeparator());
    // sb.append(commandNoOptions("hline"));
    return sb.toString();
  }


  @Override
  public String createTableRowList(List<Cell> cells) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < cells.size() - 1; i++) {
      sb.append(createCell(cells.get(i)));
      sb.append(" & ");
    }
    sb.append(createCell(cells.get(cells.size() - 1)));
    sb.append(System.lineSeparator());
    return sb.toString();
  }


  @Override
  public String closeTable() {
    StringBuilder sb = new StringBuilder();
    sb.append(commandNoOptions("bottomrule"));
    sb.append(command("end", "longtable"));
    return sb.toString();
  }


  @Override
  public String openList(boolean ordered) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public String createListEntry(String content) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public String createListEntry(String content, String id) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public String createListEntryNoNr(String content, String id) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public String closeList(boolean ordered) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public String terminateDocument() {
    StringBuilder sb = new StringBuilder();
    sb.append(commandNoOptions("clearpage"));
    sb.append(commandNoOptions("printglossary"));
    sb.append(command("end", "document"));
    return sb.toString();
  }


  @Override
  public String round(String str, int precision) {
    return command("num", str, "round-mode=places",
      "round-precision=" + precision);
  }


  /**
   * Helper method that constructs a LaTeX command.
   *
   * @param name
   *        the name of the command, e.g., 'begin'
   * @param arg
   *        the argument of the command, e.g., 'document'
   * @param options
   *        an array of options for the LaTeX command, can be {@code null}.
   * @return A LaTeX command in the form <code>\name[options]{arg}\n</code>
   */
  public static String command(String name, String arg, String... options) {
    StringBuilder sb = new StringBuilder();
    sb.append('\\');
    sb.append(name);
    if ((options != null) && (options.length > 0)) {
      sb.append('[');
      sb.append(options[0]);
      for (int i = 1; i < options.length; i++) {
        sb.append(',');
        sb.append(options[i]);
      }
      sb.append(']');
    }
    sb.append('{');
    sb.append(arg);
    sb.append('}');
    sb.append(System.lineSeparator());
    return sb.toString();
  }


  /**
   * helper method that constructs LaTeX command without arguments
   *
   * @param name
   *        the name of the command
   * @param args
   *        arbitrary number of arguments
   * @return LaTeX command in the form <code>\name{arg1}...{argn}\n</code>
   */
  public static String commandNoOptions(String name, String... args) {
    StringBuilder sb = new StringBuilder();
    sb.append('\\');
    sb.append(name);
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        sb.append('{');
        sb.append(args[i]);
        sb.append('}');
      }
    }
    sb.append(System.lineSeparator());
    return sb.toString();
  }


  @Override
  public String createCell(Cell cell) {
    StringBuilder sb = new StringBuilder();
    if (cell.id != null) {
      if (cell.isClickable) {
        sb.append("\\hyperlink{");
      } else {
        sb.append("\\hypertarget{");
      }
      sb.append(cell.id);
      sb.append("}{");
      sb.append(mask(cell.content));
      sb.append("}");
    } else {
      sb.append(mask(cell.content));
    }
    return sb.toString();
  }


  @Override
  public String mask(String str) {
    // loadProperties(maskingFilename);
    for (Object keyObj : properties.keySet()) {
      String key = (String) keyObj;
      str = str.replaceAll(key, properties.getProperty(key));
    }
    return str;
  }


  @Override
  public String setGlossaryLink(String content, String id) {
    return commandNoOptions("gls", id);
  }


  @Override
  public String trueFalseMask(Boolean bool) {
    if (bool) {
      return "\\makebox[0pt][l]{" + "$" + "\\square" + "$"
          + "}\\raisebox{.15ex}{\\hspace{0.1em}" + "$" + "\\checkmark" + "$}";
    }
    return "$" + "\\square" + "$";
  }


  @Override
  public String kineticLaw(ASTNode law) {
    String lawTeX;
    lawTeX = "$" + law.toLaTeX() + "$";
    return lawTeX;
  }


  @Override
  public String newEntry(String str) {
    str = "\\item " + str;
    return str;
  }


  @Override
  public String listingBegin() {
    return "\\begin{description}";
  }


  @Override
  public String listingEnd() {
    return "\\end{description}";
  }
}
