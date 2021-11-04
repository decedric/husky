package org.husky.emed.cda.models.entry;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.husky.emed.cda.enums.EmedEntryType;
import org.husky.emed.cda.generated.artdecor.enums.ActSubstanceAdminSubstitutionCode;
import org.husky.emed.cda.generated.artdecor.enums.RouteOfAdministrationEdqm;
import org.husky.emed.cda.models.common.AuthorDigest;
import org.husky.emed.cda.models.common.EmedReference;
import org.husky.emed.cda.models.common.MedicationDosageInstructions;
import org.husky.emed.cda.models.treatment.MedicationProduct;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the digest of an eMed PRE document item entry.
 *
 * @author Quentin Ligier
 */
@Getter
@Setter
public class EmedPreEntryDigest extends EmedEntryDigest {

    /**
     * The list of substance substitution permissions.
     */
    private final List<@NonNull ActSubstanceAdminSubstitutionCode> substitutionPermissions = new ArrayList<>();

    /**
     * The dosage instructions.
     */
    private MedicationDosageInstructions dosageInstructions;

    /**
     * The prescribed medication product.
     */
    private MedicationProduct product;

    /**
     * Number of repeats/refills (excluding the initial dispense). It's a non-zero integer if it's limited, it's zero if
     * no repeat/refill is authorized and {@code null} if unlimited repeats/refills are authorized.
     */
    @Nullable
    private Integer repeatNumber;

    /**
     * The medication route of administration, or {@code null} if it's not specified.
     */
    @Nullable
    private RouteOfAdministrationEdqm routeOfAdministration;

    /**
     * The inclusive instant at which the item shall start.
     */
    private Instant serviceStartTime;

    /**
     * The exclusive instant at which the item shall stop.
     */
    private Instant serviceStopTime;

    /**
     * The reference to the MTP entry, if any.
     */
    @Nullable
    private EmedReference mtpReference;

    /**
     * Whether this prescription item is provisional or not.
     */
    private boolean provisional;

    /**
     * The treatment reason or {@code null} if it isn't provided.
     */
    @Nullable
    private String treatmentReason;

    /**
     * The patient medication instructions or {@code null} if it isn't provided.
     */
    @Nullable
    private String patientMedicationInstructions;

    /**
     * The fulfilment instructions or {@code null} if it isn't provided.
     */
    @Nullable
    private String fulfilmentInstructions;

    /**
     * Constructor.
     *
     * @param creationTime                  The instant at which the item entry was created.
     * @param documentId                    The parent document unique ID.
     * @param documentAuthor                The author of the original parent document.
     * @param sectionAuthor                 The author of the original parent section.
     * @param entryId                       The item entry ID.
     * @param medicationTreatmentId         The ID of the medication treatment this item entry belongs to.
     * @param patientId                     The patient ID.
     * @param sequence                      The sequence of addition.
     * @param annotationComment             The annotation comment or {@code null} if it isn't provided.
     * @param dosageInstructions            The dosage instructions.
     * @param product                       The medication product.
     * @param repeatNumber                  Number of repeats/refills (excluding the initial dispense). It's a non-zero
     *                                      integer if it's limited, it's zero if no repeat/refill is authorized and
     *                                      {@code null} if unlimited repeats/refills are authorized.
     * @param routeOfAdministration         The medication route of administration or {@code null} if it's not
     *                                      specified.
     * @param serviceStartTime              The inclusive instant at which the item shall start.
     * @param serviceStopTime               The exclusive instant at which the item shall stop or {@code null} if it's
     *                                      unknown.
     * @param mtpReference                  The reference to the MTP entry, if any.
     * @param provisional                   Whether this prescription item is provisional or not.
     * @param substitutionPermissions       The list of substance substitution permissions or {@code null} if it's not
     *                                      specified.
     * @param treatmentReason               The treatment reason or {@code null} if it isn't provided.
     * @param patientMedicationInstructions The patient medication instructions or {@code null} if it isn't provided.
     * @param fulfilmentInstructions        The fulfilment instructions or {@code null} if it isn't provided.
     */
    public EmedPreEntryDigest(final Instant creationTime,
                              final String documentId,
                              final AuthorDigest documentAuthor,
                              final AuthorDigest sectionAuthor,
                              final String entryId,
                              final String medicationTreatmentId,
                              final String patientId,
                              final int sequence,
                              @Nullable final String annotationComment,
                              final MedicationDosageInstructions dosageInstructions,
                              final MedicationProduct product,
                              @Nullable final Integer repeatNumber,
                              @Nullable final RouteOfAdministrationEdqm routeOfAdministration,
                              final Instant serviceStartTime,
                              final Instant serviceStopTime,
                              @Nullable final EmedReference mtpReference,
                              final boolean provisional,
                              @Nullable final List<@NonNull ActSubstanceAdminSubstitutionCode> substitutionPermissions,
                              @Nullable final String treatmentReason,
                              @Nullable final String patientMedicationInstructions,
                              @Nullable final String fulfilmentInstructions) {
        super(creationTime, documentId, documentAuthor, sectionAuthor, entryId, medicationTreatmentId, patientId,
                sequence, annotationComment);
        this.dosageInstructions = Objects.requireNonNull(dosageInstructions);
        this.product = Objects.requireNonNull(product);
        this.routeOfAdministration = routeOfAdministration;
        this.repeatNumber = repeatNumber;
        this.serviceStartTime = Objects.requireNonNull(serviceStartTime);
        this.serviceStopTime = Objects.requireNonNull(serviceStopTime);
        this.mtpReference = mtpReference;
        this.provisional = provisional;
        if (substitutionPermissions != null) {
            this.substitutionPermissions.addAll(substitutionPermissions);
        }
        this.treatmentReason = treatmentReason;
        this.patientMedicationInstructions = patientMedicationInstructions;
        this.fulfilmentInstructions = fulfilmentInstructions;
    }

    /*
     * Returns the dosage type.
     *
    @NonNull public DosageType getDosageType() {
        if (!this.dosageInstructions.isEmpty()) {
            return DosageType.Split;
        } else {
            return DosageType.Normal;
        } // tapered
    }*/

    /**
     * Returns the non-null type of the item entry digest.
     */
    public EmedEntryType getEmedEntryType() {
        return EmedEntryType.PRE;
    }

    @Override
    public String toString() {
        return "EmedPreEntryDigest{" +
                "creationTime=" + creationTime +
                ", documentId='" + documentId + '\'' +
                ", sectionAuthor=" + sectionAuthor +
                ", documentAuthor=" + documentAuthor +
                ", entryId='" + entryId + '\'' +
                ", medicationTreatmentId='" + medicationTreatmentId + '\'' +
                ", sequence=" + sequence +
                ", patientId='" + patientId + '\'' +
                ", annotationComment='" + annotationComment + '\'' +
                ", substitutionPermissions=" + substitutionPermissions +
                ", dosageInstructions=" + dosageInstructions +
                ", product=" + product +
                ", repeatNumber=" + repeatNumber +
                ", routeOfAdministration=" + routeOfAdministration +
                ", serviceStartTime=" + serviceStartTime +
                ", serviceStopTime=" + serviceStopTime +
                ", mtpReference=" + mtpReference +
                ", provisional=" + provisional +
                ", treatmentReason='" + treatmentReason + '\'' +
                ", patientMedicationInstructions='" + patientMedicationInstructions + '\'' +
                ", fulfilmentInstructions='" + fulfilmentInstructions + '\'' +
                '}';
    }
}