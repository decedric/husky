/*
 * This code is made available under the terms of the Eclipse Public License v1.0
 * in the github project https://github.com/project-husky/husky there you also
 * find a list of the contributors and the license information.
 *
 * This project has been developed further and modified by the joined working group Husky
 * on the basis of the eHealth Connector opensource project from June 28, 2021,
 * whereas medshare GmbH is the initial and main contributor/author of the eHealth Connector.
 *
 */
package org.projecthusky.fhir.emed.ch.epr.resource.pml;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.projecthusky.fhir.emed.ch.common.annotation.ExpectsValidResource;
import org.projecthusky.fhir.emed.ch.common.error.InvalidEmedContentException;
import org.projecthusky.fhir.emed.ch.common.resource.ChCorePatientEpr;
import org.projecthusky.fhir.emed.ch.epr.enums.SubstanceAdministrationSubstitutionCode;
import org.projecthusky.fhir.emed.ch.epr.resource.ChEmedEprMedicationStatement;
import org.projecthusky.fhir.emed.ch.epr.resource.ChEmedEprPractitionerRole;
import org.projecthusky.fhir.emed.ch.epr.util.References;

import java.util.UUID;

/**
 * The HAPI custom structure for CH-EMED-EPR MedicationStatement (PML).
 *
 * @author Quentin Ligier
 **/
@ResourceDef(profile = "https://fhir.cara.ch/StructureDefinition/ch-emed-epr-medicationstatement-list")
public class ChEmedEprMedicationStatementPml extends ChEmedEprMedicationStatement {

    /**
     * Whether the dispenser can substitute the prescribed medicine/package by another that is deemed equivalent, for
     * medical or logistical reasons. By default, substitution is authorized.
     */
    @Nullable
    @Child(name = "substitution")
    @Extension(url = "http://fhir.ch/ig/ch-emed/StructureDefinition/ch-emed-ext-substitution", definedLocally = false)
    protected CodeableConcept substitution;

    /**
     * Author of the original document if different from the author of the medical decision
     * (MedicationStatement.informationSource)
     */
    @Nullable
    @Child(name = "auhtorDocument")
    @Extension(url = "http://fhir.ch/ig/ch-core/StructureDefinition/ch-ext-author")
    protected Reference authorDocument;

    /**
     * Empty constructor for the parser.
     */
    public ChEmedEprMedicationStatementPml() {
        super();
    }

    /**
     * Constructor that pre-populates fields
     *
     * @param entryUuid
     */
    public ChEmedEprMedicationStatementPml(final UUID entryUuid) {
        super(entryUuid);
    }

    /**
     * Gets the substitution element in the medication statement.
     *
     * @return the substitution element.
     */
    public CodeableConcept getSubstitutionElement() {
        if (this.substitution == null) {
            this.substitution = new CodeableConcept();
        }
        return this.substitution;
    }

    /**
     * Gets the author document element in the medication statement.
     *
     * @return the author document element.
     */
    public Reference getAuthorDocumentElement() {
        if (this.authorDocument == null) {
            this.authorDocument = new Reference();
        }
        return this.authorDocument;
    }

    /**
     * Gets the substitution code in the medication statement.
     *
     * @return the substitution code.
     * @throws InvalidEmedContentException if the substitution code is invalid.
     */
    @ExpectsValidResource
    public SubstanceAdministrationSubstitutionCode getSubstitution() throws InvalidEmedContentException {
        if (!this.hasSubstitution()) {
            return SubstanceAdministrationSubstitutionCode.EQUIVALENT;
        }
        final var substitutionCode =
                SubstanceAdministrationSubstitutionCode.fromCoding(this.getSubstitution().getCoding());
        if (substitutionCode == null) {
            throw new InvalidEmedContentException("The substitution code is invalid");
        }
        return substitutionCode;
    }

    /**
     * Gets the last author document resource in the medication statement if available.
     *
     * @return the author document resource or {@code null}.
     * @throws InvalidEmedContentException if the author document resource is invalid.
     */
    @Nullable
    @ExpectsValidResource
    public DomainResource getAuthorDocument() throws InvalidEmedContentException {
        final var resource = getAuthorDocumentElement().getResource();
        if (resource == null) return null;

        if (resource instanceof ChCorePatientEpr || resource instanceof ChEmedEprPractitionerRole) {
            return (DomainResource) resource;
        }
        throw new InvalidEmedContentException("The last author of the original document is invalid");
    }

    /**
     * Resolves the last author and her/his organization of the medical decision.
     *
     * @return The last author and her/his organization of the medical decision.
     * @throws InvalidEmedContentException if the last author and her/his organization of the medical decision is
     *                                     missing.
     */
    @ExpectsValidResource
    public ChEmedEprPractitionerRole resolveInformationSource() throws InvalidEmedContentException {
        if (!this.hasInformationSource()) {
            throw new InvalidEmedContentException(
                    "The last author and her/his organization of the medical decision is missing.");
        }
        if (this.getInformationSource().getResource() instanceof ChEmedEprPractitionerRole practitionerRole) {
            return practitionerRole;
        }
        throw new InvalidEmedContentException(
                "The last author and her/his organization of the medical decision isn't of right type.");
    }

    /**
     * Returns whether substitution code exists.
     *
     * @return {@code true} if the substitution code exists, {@code false} otherwise.
     */
    public boolean hasSubstitution() {
        return this.substitution != null && !this.substitution.isEmpty();
    }

    /**
     * Returns whether author document exists.
     *
     * @return {@code true} if the author document exists, {@code false} otherwise.
     */
    public boolean hasAuthorDocument() {
        return this.authorDocument != null && this.authorDocument.getResource() != null;
    }

    /**
     * Sets the substitution element in the medication statement.
     *
     * @param value the substitution element.
     * @return this.
     */
    public ChEmedEprMedicationStatementPml setSubstitutionElement(final CodeableConcept value) {
        this.substitution = value;
        return this;
    }

    /**
     * Sets the substitution code in the medication statement.
     *
     * @param value the substitution code.
     * @return this.
     */
    public ChEmedEprMedicationStatementPml setSubstitution(final SubstanceAdministrationSubstitutionCode value) {
        this.setSubstitutionElement(value.getCodeableConcept());
        return this;
    }

    /**
     * Sets the author of the original document.
     *
     * @param author the author.
     * @return this.
     */
    public ChEmedEprMedicationStatementPml setAuthorDocument(final IBaseResource author) {
        this.authorDocument = References.createReference((Resource) author);
        return this;
    }
}
