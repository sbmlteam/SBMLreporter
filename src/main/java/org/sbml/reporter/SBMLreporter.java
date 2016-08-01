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

import java.io.File;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

/**
 * Test Class to try if creation of reports work.
 * Is not necessary for the final project and can be deleted later
 */
public class SBMLreporter {

  /**
   * @param args
   *        the input file and a target directory for two reports (LaTeX
   *        and HTML)
   * @throws Exception
   *         if something goes wrong.
   */
  public static void main(String[] args) throws Exception {
    File inputFile = new File(args[0]);
    File outputDir = new File(args[1]);
    SBMLDocument sbml = SBMLReader.read(inputFile);

    // Let's build an HTML report first
    AbstractBuilder abstractBuilder = new HTMLBuilder();
    ReportDirector reportDirector = new ReportDirector(abstractBuilder, sbml, new File(outputDir.getAbsolutePath() + "/output.html").toPath());
    reportDirector.call();

    // Now, we proceed by creating a LaTeX report
    abstractBuilder = new LaTeXBuilder();
    reportDirector = new ReportDirector(abstractBuilder, sbml, new File(outputDir.getAbsolutePath() + "/output.tex").toPath());
    reportDirector.call();
  }
}
