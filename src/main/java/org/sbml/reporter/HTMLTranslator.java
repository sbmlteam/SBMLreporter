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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import org.sbml.jsbml.ASTNode;

/**
 * This class provides the functionality to translate commands in HTML format
 *
 * @author Sophia Mersmann
 * @author Jennifer Boedker
 * @author Tobias Nietsch
 * @version 1.0
 * @since 1.0
 */
public class HTMLTranslator extends AbstractTranslator {

  public HTMLTranslator() {
    super();
    loadProperties("/HTMLMasking.xml");
    // TODO Auto-generated constructor stub
  }


  @Override
  public String initializeDocument() {
    StringBuilder sb = new StringBuilder();
    sb.append("<!DOCTYPE html>");
    sb.append(System.lineSeparator());
    sb.append(openTag("html", "lang=\"en\""));
    sb.append(openTag("head"));
    sb.append(openTag("meta", "charset='utf-8'"));
    // sb.append("<title>" + title + "</title>\r\n"); //TODO
    sb.append(createStyleTag());
    sb.append(closeTag("head"));
    sb.append(closeTag("body"));
    return sb.toString();
  }


  /**
   * Creates the embedded css style
   *
   * @return String
   */
  private static String createStyleTag() {
    StringBuilder sb = new StringBuilder();
    sb.append(openTag("style"));
    sb.append("body { background-color: lightgrey; }");
    sb.append("h1 {color: blue;}");
    sb.append("p {color:green;}");
    sb.append("table, th, td {border: 1px solid black;}");
    sb.append("table{width: 50%;}");
    sb.append("tr:nth-child(even) {background-color: #f2f2f2}");
    sb.append("OL { counter-reset: item; padding-left: 10px; }\r\n");
    sb.append("OL LI { display: block }\r\n");
    sb.append(
        "OL LI:before { content: counters(item, \".\") \" \"; counter-increment: item }\r\n");
    sb.append(closeTag("style"));
    return sb.toString();
  }


  @Override
  public String createHeading(String headline, int mode) {
    StringBuilder tag = new StringBuilder();
    tag.append("h");
    tag.append(mode);
    return command(tag.toString(), mask(headline));
  }


  @Override
  public String createHeading(String headline, int mode, String id) {
    StringBuilder tag = new StringBuilder();
    tag.append("h");
    tag.append(mode);
    StringBuilder attr = new StringBuilder();
    attr.append("id=\"");
    attr.append(id);
    attr.append("\"");
    return command(tag.toString(), mask(headline), attr.toString());
  }


  @Override
  public String createSimpleText(String content) {
    return command("p", mask(content));
  }


  @Override
  public String openTable(String caption, int numColumns) {
    StringBuilder sb = new StringBuilder();
    sb.append(openTag("table"));
    sb.append(command("caption", mask(caption)));
    return sb.toString();
  }


  @Override
  public String createTableHeading(String... content) {
    StringBuilder sb = new StringBuilder();
    sb.append(openTag("tr"));
    for (String headline : content) {
      sb.append(command("th", mask(headline)));
    }
    sb.append(closeTag("tr"));
    return sb.toString();
  }


  @Override
  public String createTableRow(List<Cell> cells) {
    StringBuilder sb = new StringBuilder();
    sb.append(openTag("tr"));
    for (Cell cell : cells) {
      sb.append(createCell(cell));
    }
    sb.append(closeTag("tr"));
    return sb.toString();
  }


  @Override
  public String createTableRowList(List<Cell> cells) {
    return createTableRow(cells).toString();
  }


  @Override
  public String closeTable() {
    return closeTag("table");
  }


  @Override
  public String openList(boolean ordered) {
    if (ordered) {
      return openTag("ol");
    } else {
      return openTag("ul");
    }
  }


  @Override
  public String createListEntry(String content) {
    return command("li", mask(content));
  }


  @Override
  public String createListEntry(String content, String id) {
    StringBuilder attr = new StringBuilder();
    attr.append("href=\"#");
    attr.append(id);
    attr.append("\"");
    return command("li", command("a", mask(content), attr.toString()));
  }


  @Override
  public String createListEntryNoNr(String content, String id) {
    StringBuilder attr = new StringBuilder();
    attr.append("href=\"#");
    attr.append(id);
    attr.append("\"");
    return command("a", mask(content), attr.toString());
  }


  @Override
  public String closeList(boolean ordered) {
    if (ordered) {
      return closeTag("ol");
    } else {
      return closeTag("ul");
    }
  }


  @Override
  public String terminateDocument() {
    StringBuilder sb = new StringBuilder();
    sb.append(closeTag("body"));
    sb.append(closeTag("html"));
    return sb.toString();
  }


  @Override
  public String createCell(Cell cell) {
    String mode;
    if (cell.isHeading) {
      mode = "h";
    } else {
      mode = "d";
    }
    StringBuilder tag = new StringBuilder();
    tag.append("t");
    tag.append(mode);
    if (cell.id != null) {
      StringBuilder attr = new StringBuilder();
      if (cell.isClickable) {
        attr.append("href=\"#");
        attr.append(cell.id);
        attr.append("\"");
        return command(tag.toString(),
          command("a", mask(cell.content), attr.toString()));
      } else {
        return "<t" + mode + " id=\"" + cell.id + "\">" + mask(cell.content)
        + "</a>" + "</t" + mode + ">\r\n";
      }
    } else {
      return command(tag.toString(), mask(cell.content));
    }
  }


  /**
   * Helper class that constructs a HTML command from tag, argument and
   * attributes
   *
   * @param tag
   * @param arg
   * @param attributes
   * @return String
   */
  public static String command(String tag, String arg, String... attributes) {
    StringBuilder sb = new StringBuilder();
    sb.append('<');
    sb.append(tag);
    if (attributes != null && attributes.length > 0) {
      for (int i = 0; i < attributes.length; i++) {
        sb.append(" ");
        sb.append(attributes[i]);
      }
    }
    sb.append('>');
    sb.append(arg);
    sb.append(closeTag(tag));
    return sb.toString();
  }


  /**
   * Initializes command with certain tag and arbitrary number of attributes
   *
   * @param tag
   * @param attributes
   * @return String
   */
  public static String openTag(String tag, String... attributes) {
    StringBuilder sb = new StringBuilder();
    sb.append("<");
    sb.append(tag);
    if (attributes != null && attributes.length > 0) {
      for (int i = 0; i < attributes.length; i++) {
        sb.append(" ");
        sb.append(attributes[i]);
      }
    }
    sb.append(">");
    sb.append(System.lineSeparator());
    return sb.toString();
  }


  /**
   * Terminates a given tag
   *
   * @param tag
   * @return String
   */
  public static String closeTag(String tag) {
    StringBuilder sb = new StringBuilder();
    sb.append("</");
    sb.append(tag);
    sb.append(">");
    sb.append(System.lineSeparator());
    return sb.toString();
  }


  @Override
  public String round(String str, int precision) {
    Double number = Double.parseDouble(str);
    StringBuilder pattern = new StringBuilder();
    pattern.append("#.");
    for (int i = 1; i <= precision; i++) {
      pattern.append("#");
    }
    DecimalFormat df = new DecimalFormat(pattern.toString());
    df.setRoundingMode(RoundingMode.HALF_UP);
    return df.format(number);
  }


  @Override
  public String mask(String str) {
    // loadProperties(maskingFilename);
    for (Object keyObj : properties.keySet()) {
      // hier noch: mit if? true/false abfangen das nicht maskiert wenn name...
      String key = (String) keyObj;
      str = str.replaceAll(key, properties.getProperty(key));
    }
    return str;
  }


  @Override
  public String setGlossaryLink(String content, String id) {
    StringBuilder attr = new StringBuilder();
    attr.append("href=\"#");
    attr.append(id);
    attr.append("\"");
    return command("a", content, attr.toString());
  }


  @Override
  public String trueFalseMask(Boolean bool) {
    String shape;
    if (bool) {
      shape = "\u2611";
    } else {
      shape = "\u25A1";
    }
    return shape;
  }


  @Override
  public String kineticLaw(ASTNode law) {
    String lawHTML;
    lawHTML = law.toFormula();
    return lawHTML;
  }


  @Override
  public String newEntry(String str) {
    str = str + "<br>";
    return str;
  }


  @Override
  public String listingBegin() {
    return "<ul>";
  }


  @Override
  public String listingEnd() {
    return "</ul>";
  }
}
