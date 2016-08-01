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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.sbml.jsbml.ASTNode;

/**
 * Overhead for translator classes of certain formats
 *
 * @author Tobias Nietsch
 * @author Jennifer Boedker
 * @author Sophia Mersmann
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractTranslator {

  protected final Properties properties = new Properties();


  /**
   * This function initializes the document
   *
   * @return String
   */
  public abstract String initializeDocument();


  /**
   * This function creates a heading with given mode (rank of heading with 1
   * being the highest)
   *
   * @param headline
   * @param mode
   * @return String
   */
  public abstract String createHeading(String headline, int mode);


  /**
   * Create linked heading with ID
   *
   * @param headline
   * @param mode
   * @param id
   * @return String
   */
  public abstract String createHeading(String headline, int mode, String id);


  /**
   * Create a simple text section
   *
   * @param content
   * @return String
   */
  public abstract String createSimpleText(String content);


  /**
   * This function initializes a table
   *
   * @param caption
   * @param numColumns
   * @return String
   */
  public abstract String openTable(String caption, int numColumns);


  /**
   * Create first row of table containing headings
   *
   * @param content
   * @return String
   */
  public abstract String createTableHeading(String... content);


  /**
   * Create one row of a table from a list of cells
   *
   * @param cells
   * @return String
   */
  public abstract String createTableRow(List<Cell> cells);


  /**
   * Create entry "row" entry for the reactions section
   *
   * @param cells
   * @return String
   */
  public abstract String createTableRowList(List<Cell> cells);


  /**
   * Function to end a table
   *
   * @return String
   */
  public abstract String closeTable();


  /**
   * This function initializes a list (ordered list when param is true)
   *
   * @param ordered
   * @return String
   */
  public abstract String openList(boolean ordered);


  /**
   * Create one item of list
   *
   * @param content
   * @return String
   */
  public abstract String createListEntry(String content);


  /**
   * Create linked item of list with given ID
   *
   * @param content
   * @param id
   * @return String
   */
  public abstract String createListEntry(String content, String id);


  /**
   * Create a list entry without a number (with given ID)
   *
   * @param content
   * @param id
   * @return String
   */
  public abstract String createListEntryNoNr(String content, String id);


  /**
   * Function to end a list (params true when closing an ordered list)
   *
   * @param ordered
   * @return String
   */
  public abstract String closeList(boolean ordered);


  /**
   * Function to end a report
   *
   * @return String
   */
  public abstract String terminateDocument();


  /**
   * Create single cell of row of table
   *
   * @param cell
   * @return String
   */
  public abstract String createCell(Cell cell);


  /**
   * Function to round a given number
   * precision param defines the number of digits after decimal point
   *
   * @param str
   * @param precision
   * @return String
   */
  public abstract String round(String str, int precision);


  /**
   * Method to unmask special characters of given string
   *
   * @param str
   * @return String
   */
  public abstract String mask(String str);


  /**
   * Method to replace a boolean (TRUE or FALSE) with symbols
   *
   * @param boolean
   * @return String
   */
  public abstract String trueFalseMask(Boolean bool);


  /**
   * Draw the kinetic law of given abstract syntax tree representation of a
   * mathematical expression
   *
   * @param law
   * @return String
   */
  public abstract String kineticLaw(ASTNode law);


  /**
   * Create new list entry
   *
   * @param str
   * @return String
   */
  public abstract String newEntry(String str);


  /**
   * Set begin of list constructor
   *
   * @return String
   */
  public abstract String listingBegin();


  /**
   * Set end of list constructor
   *
   * @return String
   */
  public abstract String listingEnd();


  /**
   * Create glossary link
   *
   * @param content
   * @param id
   * @return String
   */
  public abstract String setGlossaryLink(String content, String id);


  /**
   * Loads properties from xml file
   *
   * @param filename
   * @return properties
   */
  protected void loadProperties(String filename) {
    // Path path = Paths.get(filename);
    // try (InputStream stream = Files.newInputStream(path)) {
    try (InputStream stream = getClass().getResourceAsStream(filename)) {
      // try (InputStream stream =
      // Thread.currentThread().getContextClassLoader().getResourceAsStream(filename+".properties")){
      properties.loadFromXML(stream);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
