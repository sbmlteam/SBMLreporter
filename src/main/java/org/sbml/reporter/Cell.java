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

/**
 * This class contains an abstract data container for one cell of table
 *
 * @author Sophia Mersmann
 * @author Jennifer Boedker
 * @version 1.0
 * @since 1.0
 */
public class Cell {

  /**
   * Text to be written in cell
   */
  public String  content;
  /**
   * TRUE if content should be showed in a heading style
   */
  public Boolean isHeading;
  /**
   * ID of link
   * if null, then no linking desired
   */
  public String  id;
  /**
   * TRUE if one should be able to click on corresponding text to jump somewhere
   * FALSE if corresponding text can be reached by clicking
   */
  public Boolean isClickable;


  /**
   * Define a cell (possible heading cell)
   *
   * @param content
   * @param isHeading
   */
  public Cell(String content, boolean isHeading) {
    this.content = content;
    this.isHeading = isHeading;
  }


  /**
   * Create a cell with a ID (possible heading cell, possible clickable)
   *
   * @param content
   * @param isHeading
   * @param id
   * @param isClickable
   */
  public Cell(String content, Boolean isHeading, String id,
    Boolean isClickable) {
    this.content = content;
    this.id = id;
    this.isClickable = isClickable;
    this.isHeading = isHeading;
  }
}
