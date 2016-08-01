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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBO;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.ontology.Term;

/**
 * This class processes a given SBML document to data suitable for exporting
 *
 * @author Sofia Mersmann
 * @author Jennifer Boedker
 * @author Lisa Falk
 * @version 1.0
 * @since 1.0
 */
public class Preprocessor {

  private Map<String, List<Species>>  mapCompartmentSpecies   =
      new HashMap<String, List<Species>>();
  private Map<String, List<Reaction>> mapCompartmentReactions =
      new HashMap<String, List<Reaction>>();
  private Set<Term>                   sboTerms                =
      new TreeSet<Term>();


  /**
   * @param sbmlDocument
   */
  public Preprocessor(SBMLDocument sbmlDocument) {
    fillListOfSpecies(sbmlDocument);
    fillListOfReactions(sbmlDocument);
    collectSBOTerms(sbmlDocument);
  }


  /**
   * Extract the list of species from given SBML document and write them into a
   * hash map
   *
   * @param sbmlDocument
   */
  public void fillListOfSpecies(SBMLDocument sbmlDocument) {
    Model model = sbmlDocument.getModel();
    int level = model.getListOfCompartments().getLevel();
    int version = model.getListOfCompartments().getVersion();
    for (Compartment compartment : model.getListOfCompartments()) {
      ListOf<Species> speciesOfCompartment =
          new ListOf<Species>(level, version);
      for (Species species : model.getListOfSpecies()) {
        if (species.getCompartment().equals(compartment.getId())) {
          speciesOfCompartment.append(species);
        }
      }
      mapCompartmentSpecies.put(compartment.getId(), speciesOfCompartment);
    }
  }


  /**
   * Extract the list of reactions from given SBML document and write them into
   * a hash map
   *
   * @param sbmlDocument
   */
  public void fillListOfReactions(SBMLDocument sbmlDocument) {
    Model model = sbmlDocument.getModel();
    int level = model.getListOfCompartments().getLevel();
    int version = model.getListOfCompartments().getVersion();
    for (Compartment compartment : model.getListOfCompartments()) {
      ListOf<Reaction> reactionsOfCompartment =
          new ListOf<Reaction>(level, version);
      for (Reaction reaction : model.getListOfReactions()) {
        if (anySpeciesInCompartment(reaction.getListOfReactants(),
          compartment.getId())
            || anySpeciesInCompartment(reaction.getListOfProducts(),
              compartment.getId())) {
          reactionsOfCompartment.append(reaction);
        }
      }
      mapCompartmentReactions.put(compartment.getId(), reactionsOfCompartment);
    }
  }


  /**
   * This method checks if a given list of species contains a compartment
   * reference with a specific ID
   *
   * @param speciesReferences
   * @param compartmentID
   * @return true if ID matches, false if not
   */
  private boolean anySpeciesInCompartment(
    ListOf<SpeciesReference> speciesReferences, String compartmentID) {
    if (speciesReferences == null || speciesReferences.isEmpty()) {
      return false;
    }
    Model model = speciesReferences.getModel();
    for (SpeciesReference speciesRef : speciesReferences) {
      String speciesID = speciesRef.getSpecies();
      Species currSpecies = model.getSpecies(speciesID);
      if (currSpecies.getCompartment().equals(compartmentID)) {
        return true;
      }
    }
    return false;
  }


  /**
   * Collect all non redundant SBO terms in document (alphabetically ordered)
   *
   * @param sbmlDocument
   */
  private void collectSBOTerms(SBMLDocument sbmlDocument) {
    Model model = sbmlDocument.getModel();
    for (Compartment compartment : model.getListOfCompartments()) {
      addSBOTerm(compartment);
    }
    for (Species species : model.getListOfSpecies()) {
      addSBOTerm(species);
    }
    for (Reaction reaction : model.getListOfReactions()) {
      addSBOTerm(reaction);
    }
  }


  /**
   * Extract SBO terms if found and add them to a set
   *
   * @param sBase
   */
  private void addSBOTerm(AbstractSBase sBase) {
    if (sBase.isSetSBOTerm()) {
      Term sboTerm = SBO.getTerm(sBase.getSBOTerm());
      sboTerms.add(sboTerm);
    }
  }


  /**
   * @return list of species
   */
  public Map<String, List<Species>> getMapCompartmentSpecies() {
    return mapCompartmentSpecies;
  }


  /**
   * @return list of reactions
   */
  public Map<String, List<Reaction>> getMapCompartmentReactions() {
    return mapCompartmentReactions;
  }


  /**
   * @return list of SBO terms
   */
  public Set<Term> getSboTerms() {
    return sboTerms;
  }


  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Preprocessor [mapCompartmentSpecies=" + mapCompartmentSpecies
        + ", mapCompartmentReactions=" + mapCompartmentReactions + "]";
  }
}
