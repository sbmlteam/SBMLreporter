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

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.SBMLDocument;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * This class creates a report file from given preprocessed SBML document data
 *
 * @author Tobias Nietsch
 * @author Sophia Mersmann
 * @author Jennifer Boedker
 * @author Christoph Blessing
 * @author Tobias Ladenburger
 * @version 1.0
 * @since 1.0
 */
public class ReportDirector implements Callable<Path> {

  private AbstractBuilder abstractBuilder;
  private SBMLDocument    sbml;
  private Path            file;


  /**
   * Report constructor from given builder, SBML document and destined file
   *
   * @param abstractBuilder
   * @param sbml
   * @param file
   */
  public ReportDirector(AbstractBuilder abstractBuilder, SBMLDocument sbml,
    Path file) {
    this.abstractBuilder = abstractBuilder;
    this.sbml = sbml;
    this.file = file;
  }


  /**
   * This function writes the specific report to new file
   *
   * @param sbmlDocument
   * @throws IOException
   */
  public void createReport(SBMLDocument sbmlDocument) throws IOException {
    // TODO somehow the report must be written to a file...?
    // TODO check if the sbml file is empty
    // TODO check if content to write is really in the file with if clauses
    Preprocessor preprocessedSBMLdata = new Preprocessor(sbmlDocument);
    BufferedWriter bw = new BufferedWriter(new FileWriter(file.toFile()));
    bw.write(
      abstractBuilder.createDocumentHead(sbmlDocument, preprocessedSBMLdata));
    bw.write(abstractBuilder.createCompartmentOverview(sbmlDocument));
    for (Compartment compartment : sbmlDocument.getModel()
        .getListOfCompartments()) {
      bw.write(abstractBuilder.createSingleCompartmentSection(
        preprocessedSBMLdata, compartment));
      bw.write(abstractBuilder.createSpeciesSectionOfCompartment(
        preprocessedSBMLdata.getMapCompartmentSpecies(), compartment));
      bw.write(abstractBuilder.createReactionSectionOfCompartment(
        preprocessedSBMLdata.getMapCompartmentReactions(), compartment));
      bw.write(abstractBuilder.createSectionOfReactions(
        preprocessedSBMLdata.getMapCompartmentReactions(), compartment));
    }
    bw.write(abstractBuilder.createDocumentFoot(preprocessedSBMLdata));
    bw.flush();
    bw.close();
  }


  @Override
  public Path call() throws Exception {
    createReport(sbml);
    return file;
  }
}
