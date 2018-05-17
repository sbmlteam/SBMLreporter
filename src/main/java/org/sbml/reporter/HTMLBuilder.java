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
import java.util.Map;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.ontology.Term;

/**
 * This class extends the methods of the AbstractBuilder class to generate a
 * HTMLBuilder
 *
 * @author Tobias Nietsch
 * @author Sophia Mersmann
 * @author Jennifer Boedker
 * @author Christoph Blessing
 * @author Tobias Ladenburger
 * @version 1.0
 * @since 1.0
 */
public class HTMLBuilder extends AbstractBuilder {

  private static final String         idTop                 = "top";
  private static final String         idContentTable        = "contentTable";
  private static final String         idSectionCompartments =
      "sectionCompartments";
  private static final String         idGlossary            = "glossary";
  private static final String         idReactions           = "reactions";
  private static int                  sectionNumbering      = 0;
  private static int                  subsectionNumbering   = 0;
  private static final HTMLTranslator htmlTranslator        =
      new HTMLTranslator();


  @Override
  public String createDocumentHead(SBMLDocument sbmlDocument,
    Preprocessor preprocessor) {
    StringBuilder sb = new StringBuilder();
    sb.append(htmlTranslator.initializeDocument());
    // print headline of document
    if (sbmlDocument.getModel().isSetName()) {
      sb.append(htmlTranslator.createHeading(sbmlDocument.getModel().getName(),
        1, idTop));
    }
    // print creator(s)
    if (sbmlDocument.getHistory().isSetListOfCreators()) {
      sb.append(htmlTranslator.createSimpleText(
        "Author: " + AbstractBuilder.getCreator(sbmlDocument).toString()));
    }
    // print creation date
    if (sbmlDocument.getHistory().isSetCreatedDate()) {
      sb.append(htmlTranslator.createSimpleText(
        "Date: " + AbstractBuilder.getDate(sbmlDocument)));
    }
    // show contents of sbml file at beginning of document
    sb.append(
      createContentOverview(sbmlDocument.getModel().getListOfCompartments()));
    return sb.toString();
  }


  /**
   * Creates a ordered nested list of a given list of compartments
   *
   * @param compartments
   * @return String
   */
  /**
   * @param compartments
   * @return
   */
  private static String createContentOverview(
    ListOf<Compartment> compartments) {
    StringBuilder sb = new StringBuilder();
    sb.append(htmlTranslator.createHeading("Contents", 2, idContentTable));
    sb.append(htmlTranslator.openList(true));
    sb.append(
      htmlTranslator.createListEntry("Compartments", idSectionCompartments));
    sb.append(htmlTranslator.openList(true));
    for (Compartment compartment : compartments) {
      sb.append(htmlTranslator.createListEntry(getDescription(compartment),
        compartment.getId()));
    }
    sb.append(htmlTranslator.closeList(true));
    sb.append(htmlTranslator.closeList(true));
    sb.append(htmlTranslator.openList(true));
    sb.append(htmlTranslator.createListEntryNoNr("Reactions", idReactions));
    sb.append(htmlTranslator.closeList(true));
    sb.append(htmlTranslator.openList(true));
    sb.append(htmlTranslator.createListEntryNoNr("Glossary", idGlossary));
    sb.append(htmlTranslator.closeList(true));
    return sb.toString();
  }


  @Override
  public String createCompartmentOverview(SBMLDocument sbmlDocument) {
    StringBuilder sb = new StringBuilder();
    String numberedHeading = ++sectionNumbering + " Compartments";
    sb.append(
      htmlTranslator.createHeading(numberedHeading, 2, idSectionCompartments));
    int compartmentCount = sbmlDocument.getModel().getCompartmentCount();
    sb.append(
      htmlTranslator.createSimpleText("This model contains " + compartmentCount
        + " compartments that are summarized in the table below."));
    sb.append(createTableOfCompartments(
      sbmlDocument.getModel().getListOfCompartments(), htmlTranslator));
    return sb.toString();
  }


  @Override
  public String createSingleCompartmentSection(
    Preprocessor preprocessedSBMLdata, Compartment compartment) {
    StringBuilder sb = new StringBuilder();
    String compartmentDescription = getDescription(compartment);
    String numbering = sectionNumbering + "." + ++subsectionNumbering;
    String numberedHeading =
        numbering + " Compartment " + compartmentDescription;
    sb.append(
      htmlTranslator.createHeading(numberedHeading, 3, compartment.getId()));
    sb.append(createCompartmentInformationTable(compartment, htmlTranslator));
    // print all species belonging to current compartment
    sb.append(htmlTranslator.createHeading(
      numbering + ".1 List of Species in " + compartment.getName(), 4));
    sb.append(htmlTranslator.openList(false));
    for (Species s : AbstractBuilder.getSpeciesOfCompartment(
      preprocessedSBMLdata, compartment.getId())) {
      sb.append(htmlTranslator.createListEntry(getDescription(s), s.getId()));
    }
    sb.append(htmlTranslator.closeList(false));
    // print all reaction belonging to current compartment
    sb.append(htmlTranslator.createHeading(
      numbering + ".2 List of Reactions in " + compartment.getName(), 4));
    sb.append(htmlTranslator.openList(false));
    for (Reaction r : AbstractBuilder.getReactionsOfCompartment(
      preprocessedSBMLdata, compartment.getId())) {
      sb.append(htmlTranslator.createListEntry(getDescription(r), r.getId()));
    }
    sb.append(htmlTranslator.closeList(false));
    return sb.toString();
  }


  @Override
  public String createSpeciesSectionOfCompartment(
    Map<String, List<Species>> mapCompartmentSpecies, Compartment compartment) {
    StringBuilder sb = new StringBuilder();
    String numbering = sectionNumbering + "." + subsectionNumbering + ".";
    sb.append(
      htmlTranslator.createHeading(numbering + "3 Species Definitions", 4));
    sb.append(createTableOfSpecies(compartment,
      mapCompartmentSpecies.get(compartment.getId()), htmlTranslator));
    return sb.toString();
  }


  @Override
  public String createReactionSectionOfCompartment(
    Map<String, List<Reaction>> mapCompartmentReactions,
    Compartment compartment) {
    StringBuilder sb = new StringBuilder();
    String numbering = sectionNumbering + "." + subsectionNumbering + ".";
    sb.append(
      htmlTranslator.createHeading(numbering + "4 Reactions Definitions", 4));
    sb.append(createTableOfReactions(compartment,
      mapCompartmentReactions.get(compartment.getId()), htmlTranslator));
    return sb.toString();
  }


  @Override
  public String createSectionOfReactions(
    Map<String, List<Reaction>> mapCompartmentReactions,
    Compartment compartment) {
    StringBuilder sb = new StringBuilder();
    sb.append(htmlTranslator.createHeading("Reactions", 1, idReactions));
    sb.append(createSectionOfReactions(compartment,
      mapCompartmentReactions.get(compartment.getId()), htmlTranslator));
    return sb.toString();
  }


  @Override
  public String createDocumentFoot(Preprocessor preprocessor) {
    StringBuilder sb = new StringBuilder();
    sb.append(setGlossary(preprocessor));
    sb.append(htmlTranslator.terminateDocument());
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
	  if(preprocessor.getSboTerms().size()>0)
	  {


		  sb.append(htmlTranslator.createHeading("Glossary", 1, idGlossary));
		  for (Term sboTerm : preprocessor.getSboTerms()) {
			  StringBuilder id = new StringBuilder();
			  id.append("id=\"");
			  id.append(sboTerm.getId());
			  id.append("\"");
			  StringBuilder content = new StringBuilder();
			  content.append(sboTerm.toString());
			  content.append(". ");
			  content.append(sboTerm.getName());
			  content.append(". ");
			  content.append(sboTerm.getDefinition());
			  sb.append(HTMLTranslator.command("p", content.toString(), id.toString()));
		  }
	  }

	  return sb.toString();
  }
}

