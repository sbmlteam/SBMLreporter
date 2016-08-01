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
import java.util.Set;

import org.sbml.jsbml.AbstractNamedSBase;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Creator;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBO;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ontology.Term;

/**
 * This Class represents the content of a report
 * Methods for the specific type of report (HTML / LATEX) are implemented in
 * extra classes
 *
 * @author Christoph Blessing
 * @author Tobias Ladenburger
 * @author Sohpia Mersmann
 * @author Jonas Wember
 * @author Jennifer Boedker
 * @author Tobias Nietsch
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractBuilder {

  private final static int ROUND_PRECISION = 3;


  /**
   * Create beginning of the report
   *
   * @param sbmlDocument
   * @return String
   */
  public abstract String createDocumentHead(SBMLDocument sbmlDocument,
    Preprocessor preprocessor);


  /**
   * Create end of the report
   *
   * @return String
   */
  public abstract String createDocumentFoot(Preprocessor preprocessor);


  /**
   * Create an overview of document by listing all compartments
   *
   * @param sbmlDocument
   * @return String
   */
  public abstract String createCompartmentOverview(SBMLDocument sbmlDocument);


  /**
   * Create section of specific compartment
   *
   * @param preprocessedSBMLdata
   * @param compartment
   * @return String
   */
  public abstract String createSingleCompartmentSection(
    Preprocessor preprocessedSBMLdata, Compartment compartment);


  /**
   * Create subsection of species in section of specific compartment
   *
   * @param mapCompartmentSpecies
   * @param compartment
   * @return String
   */
  public abstract String createSpeciesSectionOfCompartment(
    Map<String, List<Species>> mapCompartmentSpecies, Compartment compartment);


  /**
   * Create subsection of reactions in section of specific compartment
   *
   * @param mapCompartmentReactions
   * @param compartment
   * @return String
   */
  public abstract String createReactionSectionOfCompartment(
    Map<String, List<Reaction>> mapCompartmentReactions,
    Compartment compartment);


  /**
   * Create section of reactions
   *
   * @param mapCompartmentReactions
   * @param compartment
   * @return String
   */
  public abstract String createSectionOfReactions(
    Map<String, List<Reaction>> mapCompartmentReactions,
    Compartment compartment);


  /**
   * Extract the name of given SBML document
   *
   * @param sbmlDocument
   * @return String
   */
  public static String getName(SBMLDocument sbmlDocument) {
    if (sbmlDocument.getModel().isSetName()) {
      return sbmlDocument.getModel().getName();
    }
    return sbmlDocument.getModel().getId();
  }


  /**
   * Extract version of the SBML Document
   *
   * @param sbmlDocument
   * @return Integer
   */
  public static Integer getVersion(SBMLDocument sbmlDocument) {
    return sbmlDocument.getModel().getVersion();
  }


  /**
   * Extract list with all creators of SBML document
   *
   * @param sbmlDocument
   * @return List
   */
  public static List<Creator> getCreator(SBMLDocument sbmlDocument) {
    return sbmlDocument.getHistory().getListOfCreators();
  }


  /**
   * Extract creation date of SBML document
   *
   * @param sbmlDocument
   * @return String
   */
  public static String getDate(SBMLDocument sbmlDocument) {
    return sbmlDocument.getHistory().getCreatedDate().toString();
  }


  /**
   * This function returns a list of all species with given compartmentID
   *
   * @param Preprocessor
   *        Object, String of CompartmentID
   * @return List
   */
  public static List<Species> getSpeciesOfCompartment(Preprocessor preprocessor,
    String compartmentID) {
    return preprocessor.getMapCompartmentSpecies().get(compartmentID);
  }


  /**
   * This function returns a list of all reactions with given compartmentID
   *
   * @param Preprocessor
   *        Object, String of CompartmentID
   * @return List
   */
  public static List<Reaction> getReactionsOfCompartment(
    Preprocessor preprocessor, String compartmentID) {
    return preprocessor.getMapCompartmentReactions().get(compartmentID);
  }


  /**
   * This function returns a set of SBO terms
   *
   * @param preprocessor
   * @return Set
   */
  public static Set<Term> getSBOTerms(Preprocessor preprocessor) {
    return preprocessor.getSboTerms();
  }


  /**
   * Get reactants of specific reaction as string
   *
   * @param Reaction
   * @return String
   */
  public static String getReactants(Reaction r) {
    String reac = "";
    if (r.getReactantCount() > 0) {
      for (int j = 0; j < r.getListOfReactants().getNumChildren(); j++) {
        if (r.getListOfReactants().getNumChildren() - 1 == j) {
          reac = reac + r.getListOfReactants().get(j).getSpecies() + "\n";
        } else {
          reac = reac + r.getListOfReactants().get(j).getSpecies() + "," + "\n";
        }
      }
      return reac;
    }
    return "-";
  }


  /**
   * Get products of specific reaction as string
   *
   * @param Reaction
   * @return String
   */
  public static String getProducts(Reaction r) {
    String prod = "";
    if (r.getProductCount() > 0) {
      for (int j = 0; j < r.getListOfProducts().getNumChildren(); j++) {
        if (r.getListOfProducts().getNumChildren() - 1 == j) {
          prod = prod + r.getListOfProducts().get(j).getSpecies() + "\n";
        } else {
          prod = prod + r.getListOfProducts().get(j).getSpecies() + "," + "\n";
        }
      }
      return prod;
    }
    return "-";
  }


  /**
   * Get modifiers of specific reaction as string
   *
   * @param Reaction
   * @return String
   */
  public static String getModifiers(Reaction r) {
    String mods = "";
    if (r.getModifierCount() > 0) {
      for (int j = 0; j < r.getListOfModifiers().getNumChildren(); j++) {
        if (r.getListOfModifiers().getNumChildren() - 1 == j) {
          mods = mods + r.getListOfModifiers().get(j).getSpecies() + "\n";
        } else {
          mods = mods + r.getListOfModifiers().get(j).getSpecies() + "," + "\n";
        }
      }
      return mods;
    }
    return "-";
  }


  /**
   * Get text description or ID (if text missing) of given sBase
   *
   * @param sBase
   * @return String
   */
  public static String getDescription(AbstractNamedSBase sBase) {
    String description;
    if (sBase.isSetName()) {
      description = sBase.getName();
    } else {
      description = sBase.getId();
    }
    return description;
  }


  /**
   * Create table with information about specific compartment
   *
   * @param Compartment
   * @param AbstractTranslator
   * @return String
   */
  public static String createCompartmentInformationTable(
    Compartment compartment, AbstractTranslator translator) {
    StringBuilder sb = new StringBuilder();
    List<Cell> cells = new ArrayList<Cell>();
    Cell defaultCell = new Cell("-", false);
    String compartmentDescription = getDescription(compartment);
    sb.append(translator.openTable(
      "Information about Compartment " + compartmentDescription, 2));
    cells.add(new Cell("Name", true));
    cells.add(new Cell(compartmentDescription, false));
    sb.append(translator.createTableRow(cells));
    cells.clear();
    String content;
    // set units of the size of the compartment
    UnitDefinition unitDefinition = compartment.getDerivedUnitDefinition();
    if (unitDefinition != null
        && !unitDefinition.getName().equals("dimensionless")) {
      cells.add(new Cell("Size [" + unitDefinition.getName() + "]", true));
    } else {
      cells.add(new Cell("Size", true));
    }
    if (compartment.isSetSize()) {
      content = translator.round(Double.toString(compartment.getSize()),
        ROUND_PRECISION);
      content = appendUnit(content, unitDefinition);
      cells.add(new Cell(content, false));
    } else {
      cells.add(defaultCell);
    }
    sb.append(translator.createTableRow(cells));
    cells.clear();
    cells.add(new Cell("Constant", true));
    cells.add(
      new Cell(translator.trueFalseMask(compartment.getConstant()), false));
    sb.append(translator.createTableRow(cells));
    cells.clear();
    sb.append(translator.closeTable());
    return sb.toString();
  }


  /**
   * Adds units to a given string
   *
   * @param content
   * @param unitDefinition
   * @return String
   */
  private static String appendUnit(String content,
    UnitDefinition unitDefinition) {
    StringBuilder sb = new StringBuilder(content);
    if (unitDefinition != null
        && !unitDefinition.getName().equals("dimensionless")) {
      sb.append(UnitDefinition.printUnits(unitDefinition, true));
    }
    return sb.toString();
  }


  /**
   * Create table of all compartments (only names printed)
   *
   * @param compartments
   * @param translator
   * @return String
   */
  public static String createTableOfCompartments(List<Compartment> compartments,
    AbstractTranslator translator) {
    StringBuilder sb = new StringBuilder();
    sb.append(translator.openTable("Summary of compartments in model", 1));
    sb.append(translator.createTableHeading("Compartment"));
    List<Cell> cells = new ArrayList<Cell>();
    for (Compartment compartment : compartments) {
      cells.add(new Cell(getDescription(compartment), false,
        compartment.getId(), true));
      sb.append(translator.createTableRow(cells));
      cells.clear();
    }
    sb.append(translator.closeTable());
    return sb.toString();
  }


  /**
   * Create table of species belonging to certain compartment
   *
   * @param compartment
   * @param species
   * @param translator
   * @return String
   */
  public static String createTableOfSpecies(Compartment compartment,
    List<Species> species, AbstractTranslator translator) {
    StringBuilder sb = new StringBuilder();
    sb.append(translator.openTable("Table of Species", 8));
    sb.append(translator.createTableHeading("Name", "Initial Amount",
      "Initial Concentration", "Conversion Factor", "SBO Term", "Compartment"));
    List<Cell> cells = new ArrayList<Cell>();
    Cell defaultCell = new Cell("-", false);
    for (Species s : species) {
      cells.add(new Cell(getDescription(s), false, s.getId(), false));
      String content;
      // TODO hasOnlySubstanceUnits? correct units term for concentration?
      // set units of initial amount or concentration
      UnitDefinition unitDefinition = s.getDerivedUnitDefinition();
      if (s.isSetInitialAmount()) {
        content = translator.round(Double.toString(s.getInitialAmount()),
          ROUND_PRECISION);
        content = appendUnit(content, unitDefinition);
        cells.add(new Cell(content, false));
      } else {
        cells.add(defaultCell);
      }
      if (s.isSetInitialConcentration()) {
        content = translator.round(Double.toString(s.getInitialConcentration()),
          ROUND_PRECISION);
        content = appendUnit(content, unitDefinition);
        cells.add(new Cell(content, false));
      } else {
        cells.add(defaultCell);
      }
      // cells.add(new Cell(Boolean.toString(s.getHasOnlySubstanceUnits()),
      // false));
      // cells.add(new Cell(Boolean.toString(s.getBoundaryCondition()), false));
      // cells.add(new Cell(Boolean.toString(s.getConstant()), false));
      if (s.isSetConversionFactor()) {
        cells.add(new Cell(s.getConversionFactor(), false));
      } else {
        cells.add(defaultCell);
      }
      if (s.isSetSBOTerm()) {
        Term sboTerm = SBO.getTerm(s.getSBOTerm());
        cells.add(new Cell(
          translator.setGlossaryLink(sboTerm.toString(), sboTerm.getId()),
          false));
      } else {
        cells.add(defaultCell);
      }
      cells.add(new Cell(getDescription(compartment), false,
        compartment.getId(), true));
      sb.append(translator.createTableRow(cells));
      cells.clear();
    }
    sb.append(translator.closeTable());
    return sb.toString();
  }


  /**
   * Create table of reactions belonging to certain compartment
   *
   * @param compartment
   * @param reactions
   * @param translator
   * @return String
   */
  public static String createTableOfReactions(Compartment compartment,
    List<Reaction> reactions, AbstractTranslator translator) {
    StringBuilder sb = new StringBuilder();
    sb.append(translator.openTable("Information about Reactions", 6));
    sb.append(translator.createTableHeading("Name", "Reversible", "Reactants",
      "Products", "SBO Term", "Compartment"));
    List<Cell> cells = new ArrayList<Cell>();
    Cell defaultCell = new Cell("-", false);
    for (Reaction r : reactions) {
      cells.add(new Cell(getDescription(r), false, r.getId(), false));
      if (r.isSetReversible()) {
        cells.add(new Cell(translator.trueFalseMask(r.getReversible()), false));
      } else {
        cells.add(defaultCell);
      }
      cells.add(new Cell(getReactants(r), false));
      cells.add(new Cell(getProducts(r), false));
      if (r.isSetSBOTerm()) {
        Term sboTerm = SBO.getTerm(r.getSBOTerm());
        cells.add(new Cell(
          translator.setGlossaryLink(sboTerm.toString(), sboTerm.getId()),
          false));
      } else {
        cells.add(defaultCell);
      }
      cells.add(new Cell(getDescription(compartment), false,
        compartment.getId(), true));
      sb.append(translator.createTableRow(cells));
      cells.clear();
    }
    sb.append(translator.closeTable());
    return sb.toString();
  }


  /**
   * Create the section for the reactions
   *
   * @param compartment
   * @param reactions
   * @param translator
   * @return String
   */
  public static String createSectionOfReactions(Compartment compartment,
    List<Reaction> reactions, AbstractTranslator translator) {
    StringBuilder sb = new StringBuilder();
    for (Reaction r : reactions) {
      sb.append(translator.createHeading(r.getId(), 2));
      List<Cell> cells = new ArrayList<Cell>();
      Cell defaultCell = new Cell("-", false);
      sb.append(translator.createHeading("Basic Information", 3));
      sb.append(translator.listingBegin());
      cells.add(new Cell(getDescription(r), false, r.getId(), false));
      sb.append(
        translator.newEntry("Name: " + translator.createTableRowList(cells)));
      cells.clear();
      if (r.isSetReversible()) {
        cells.add(new Cell(translator.trueFalseMask(r.getReversible()), false));
        sb.append(translator.newEntry(
          "Reversible: " + translator.createTableRowList(cells)));
        cells.clear();
      }
      if (r.isSetListOfModifiers()) {
        cells.add(new Cell(getModifiers(r), false));
        sb.append(translator.newEntry(
          "Modifiers: " + translator.createTableRowList(cells)));
        cells.clear();
      }
      if (r.isSetListOfReactants()) {
        cells.add(new Cell(getReactants(r), false));
        sb.append(translator.newEntry(
          "Reactants: " + translator.createTableRowList(cells)));
        cells.clear();
      }
      if (r.isSetListOfProducts()) {
        cells.add(new Cell(getProducts(r), false));
        sb.append(translator.newEntry(
          "Products: " + translator.createTableRowList(cells)));
        cells.clear();
      }
      if (r.isSetSBOTerm()) {
        Term sboTerm = SBO.getTerm(r.getSBOTerm());
        cells.add(new Cell(
          translator.setGlossaryLink(sboTerm.toString(), sboTerm.getId()),
          false));
        sb.append(translator.newEntry(
          "SBO Term: " + translator.createTableRowList(cells)));
        cells.clear();
      }
      if (r.isSetCompartment()) {
        cells.add(new Cell(getDescription(compartment), false,
          compartment.getId(), true));
        sb.append(translator.newEntry(
          "Compartment: " + translator.createTableRowList(cells)));
        cells.clear();
      }
      sb.append(translator.listingEnd());
      if (r.isSetKineticLaw()) {
        sb.append(translator.createHeading("Kinetic Law", 3));
        sb.append(translator.listingBegin());
        cells.add(new Cell(translator.newEntry(
          translator.kineticLaw(r.getKineticLaw().getMath())), false));
        sb.append(translator.createTableRowList(cells));
        sb.append(translator.listingEnd());
        cells.clear();
      }
    }
    return sb.toString();
  }
}
