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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.ontology.Term;

/**
 * This class extends the methods of the AbstractBuilder class to generate a
 * LatexBuilder
 *
 * @author Christoph Blessing
 * @author Tobias Ladenburger
 * @author Sohpia Mersmann
 * @author Jennifer Boedker
 * @author Tobias Nietsch
 * @version 1.0
 * @since 1.0
 */
public class LaTeXBuilder extends AbstractBuilder {

  private final static LaTeXTranslator laTeXTranslator = new LaTeXTranslator();


  @Override
  public String createDocumentHead(SBMLDocument sbmlDocument,
    Preprocessor preprocessor) {
    StringBuilder sb = new StringBuilder();
    sb.append(laTeXTranslator.setDocumentclass("scrreprt"));
    // set packages
    sb.append(laTeXTranslator.setUsePackageWithOption("inputenc", "utf8"));
    sb.append(laTeXTranslator.setUsePackageWithOption("babel", "english"));
    sb.append(laTeXTranslator.setUsePackageWithOption("helvet", "scaled=0.9"));
    sb.append(laTeXTranslator.setUsePackages("amsmath", "tabu", "float",
      "mathptmx", "cleveref", "booktabs", "longtable", "hyperref", "siunitx",
        "amsfonts"));
    sb.append(laTeXTranslator.setUsePackageWithOption("glossaries", "toc"));
    // make glossary
    sb.append(setGlossary(preprocessor));
    // set title
    sb.append(laTeXTranslator.setTitle(AbstractBuilder.getName(sbmlDocument)));
    // set list of creators
    if (sbmlDocument.getHistory().isSetListOfCreators()) {
      sb.append(laTeXTranslator.setAuthor(
        AbstractBuilder.getCreator(sbmlDocument).toString()));
    }
    // set date of creation
    if (sbmlDocument.getHistory().isSetCreatedDate()) {
      sb.append(laTeXTranslator.setDate(AbstractBuilder.getDate(sbmlDocument)));
    }
    sb.append(laTeXTranslator.initializeDocument());
    return sb.toString();
  }


  /**
   * Creates a glossary
   *
   * @param preprocessor
   * @return String
   */
  private static String setGlossary(Preprocessor preprocessor) {
    StringBuilder sb = new StringBuilder();
    sb.append(LaTeXTranslator.commandNoOptions("makeglossaries"));
    for (Term sboTerm : preprocessor.getSboTerms()) {
      StringBuilder description = new StringBuilder();
      description.append(sboTerm.getName());
      description.append(". ");
      description.append(sboTerm.getDefinition());
      sb.append(setGlossaryEntry(sboTerm.getId(), sboTerm.toString(),
        laTeXTranslator.mask(description.toString())));
    }
    return sb.toString();
  }


  /**
   * Set a glossary entry with given ID, name and description
   *
   * @param id
   * @param name
   * @param description
   * @return String
   */
  private static String setGlossaryEntry(String id, String name,
    String description) {
    StringBuilder content = new StringBuilder();
    content.append("name=");
    content.append(name);
    content.append(", description={");
    content.append(description);
    content.append("}");
    return LaTeXTranslator.commandNoOptions("newglossaryentry", id,
      content.toString());
  }


  @Override
  public String createCompartmentOverview(SBMLDocument sbmlDocument) {
    StringBuilder sb = new StringBuilder();
    sb.append(laTeXTranslator.createHeading("Compartments", 1));
    String text = "This model contains "
        + sbmlDocument.getModel().getListOfCompartments().size()
        + " compartment(s) that is/are summarized in the table below. \n";
    sb.append(laTeXTranslator.createSimpleText(text));
    sb.append(createTableOfCompartments(
      sbmlDocument.getModel().getListOfCompartments(), laTeXTranslator));
    return sb.toString();
  }


  @Override
  public String createSingleCompartmentSection(
    Preprocessor preprocessedSBMLdata, Compartment compartment) {
    StringBuilder sb = new StringBuilder();
    String compartmentDescription = getDescription(compartment);
    sb.append(laTeXTranslator.createHeading(
      "Compartment " + compartmentDescription, 2, compartment.getId()));
    sb.append(createCompartmentInformationTable(compartment, laTeXTranslator));
    sb.append(laTeXTranslator.createHeading("List of Species", 3));
    // print all species in current compartment
    sb.append(laTeXTranslator.openTable(
      "Summary of all species in this compartment", 1));
    sb.append(laTeXTranslator.createTableHeading("Species"));
    List<Cell> cells = new ArrayList<Cell>();
    for (Species s : AbstractBuilder.getSpeciesOfCompartment(
      preprocessedSBMLdata, compartment.getId())) {
      cells.add(new Cell(getDescription(s), false, s.getId(), true));
      sb.append(laTeXTranslator.createTableRow(cells));
      cells.clear();
    }
    sb.append(laTeXTranslator.closeTable());
    // print all reactions of compartment
    sb.append(laTeXTranslator.createHeading("List of Reactions", 3));
    sb.append(laTeXTranslator.openTable(
      "Summary of all reactions in this compartment", 1));
    sb.append(laTeXTranslator.createTableHeading("Reaction"));
    for (Reaction r : AbstractBuilder.getReactionsOfCompartment(
      preprocessedSBMLdata, compartment.getId())) {
      cells.add(new Cell(getDescription(r), false, r.getId(), true));
      sb.append(laTeXTranslator.createTableRow(cells));
      cells.clear();
    }
    sb.append(laTeXTranslator.closeTable());
    return sb.toString();
  }


  @Override
  public String createSpeciesSectionOfCompartment(
    Map<String, List<Species>> mapCompartmentSpecies, Compartment compartment) {
    StringBuilder sb = new StringBuilder();
    sb.append(laTeXTranslator.createHeading("Species Definition", 3));
    sb.append(createTableOfSpecies(compartment,
      mapCompartmentSpecies.get(compartment.getId()), laTeXTranslator));
    return sb.toString();
  }


  @Override
  public String createReactionSectionOfCompartment(
    Map<String, List<Reaction>> mapCompartmentReactions,
    Compartment compartment) {
    StringBuilder sb = new StringBuilder();
    sb.append(laTeXTranslator.createHeading("Reactions Definition", 3));
    sb.append(createTableOfReactions(compartment,
      mapCompartmentReactions.get(compartment.getId()), laTeXTranslator));
    return sb.toString();
  }


  @Override
  public String createSectionOfReactions(
    Map<String, List<Reaction>> mapCompartmentReactions,
    Compartment compartment) {
    StringBuilder sb = new StringBuilder();
    sb.append(laTeXTranslator.createHeading("Reactions", 1));
    sb.append(createSectionOfReactions(compartment,
      mapCompartmentReactions.get(compartment.getId()), laTeXTranslator));
    return sb.toString();
  }


  @Override
  public String createDocumentFoot(Preprocessor preprocessor) {
    return laTeXTranslator.terminateDocument();
  }
}
