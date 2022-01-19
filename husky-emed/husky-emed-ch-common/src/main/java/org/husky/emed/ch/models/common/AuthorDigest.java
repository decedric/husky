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
package org.husky.emed.ch.models.common;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.husky.emed.ch.enums.ParticipationFunction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the author of an eMed document, section or item entry.
 *
 * @author Quentin Ligier
 */
public class AuthorDigest {

    /**
     * This code is used to specify the exact function an actor has or {@code null}.
     */
    @Nullable
    private ParticipationFunction participationFunction;

    /**
     * The authorship timestamp.
     */
    private Instant authorshipTimestamp;

    /**
     * The author given name or {@code null}.
     */
    @Nullable
    private String givenName;

    /**
     * The author family name or {@code null}.
     */
    @Nullable
    private String familyName;

    /**
     * The author GS1 GLN. For persons, their personal GLN must be declared. For devices or software modules, the
     * GLN of their organization must be declared. If it is not (yet) known, it can be {@code null}.
     */
    @Nullable
    private String authorGln;

    /**
     * The list of author Ids appart from the GLN as HL7 CX. The list may be empty.
     */
    private final List<String> otherIds = new ArrayList<>();

    /**
     * The list of author addresses. The list may be empty.
     */
    private final List<AddressDigest> addresses = new ArrayList<>();

    /**
     * The authoring device manufacturer model name or {@code null}.
     */
    @Nullable
    private String deviceManufacturerModelName;

    /**
     * The authoring device software name or {@code null}.
     */
    @Nullable
    private String deviceSoftwareName;

    /**
     * The author's organization or {@code null}.
     */
    @Nullable
    private OrganizationDigest organization;

    /**
     * The author telecoms or {@code null}.
     */
    @Nullable
    private TelecomDigest telecoms;

    public AuthorDigest() {
    }

    public AuthorDigest(@Nullable final ParticipationFunction participationFunction,
                        final Instant authorshipTimestamp,
                        @Nullable final String givenName,
                        @Nullable final String familyName,
                        @Nullable final String authorGln,
                        final List<String> otherIds,
                        final List<AddressDigest> addresses,
                        @Nullable final String deviceManufacturerModelName,
                        @Nullable final String deviceSoftwareName,
                        @Nullable final OrganizationDigest organization,
                        @Nullable final TelecomDigest telecoms) {
        this.participationFunction = participationFunction;
        this.authorshipTimestamp = Objects.requireNonNull(authorshipTimestamp);
        this.givenName = givenName;
        this.familyName = familyName;
        this.authorGln = authorGln;
        this.otherIds.addAll(otherIds);
        this.addresses.addAll(addresses);
        this.deviceManufacturerModelName = deviceManufacturerModelName;
        this.deviceSoftwareName = deviceSoftwareName;
        this.organization = organization;
        this.telecoms = telecoms;
    }

    @Nullable
    public ParticipationFunction getParticipationFunction() {
        return participationFunction;
    }

    public void setParticipationFunction(@Nullable final ParticipationFunction participationFunction) {
        this.participationFunction = participationFunction;
    }

    public Instant getAuthorshipTimestamp() {
        return authorshipTimestamp;
    }

    public void setAuthorshipTimestamp(final Instant authorshipTimestamp) {
        this.authorshipTimestamp = Objects.requireNonNull(authorshipTimestamp);
    }

    @Nullable
    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(@Nullable final String givenName) {
        this.givenName = givenName;
    }

    @Nullable
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(@Nullable final String familyName) {
        this.familyName = familyName;
    }

    @Nullable
    public String getAuthorGln() {
        return authorGln;
    }

    public void setAuthorGln(@Nullable final String authorGln) {
        this.authorGln = authorGln;
    }

    public List<String> getOtherIds() {
        return otherIds;
    }

    public List<AddressDigest> getAddresses() {
        return addresses;
    }

    @Nullable
    public String getDeviceManufacturerModelName() {
        return deviceManufacturerModelName;
    }

    public void setDeviceManufacturerModelName(@Nullable final String deviceManufacturerModelName) {
        this.deviceManufacturerModelName = deviceManufacturerModelName;
    }

    @Nullable
    public String getDeviceSoftwareName() {
        return deviceSoftwareName;
    }

    public void setDeviceSoftwareName(@Nullable final String deviceSoftwareName) {
        this.deviceSoftwareName = deviceSoftwareName;
    }

    @Nullable
    public OrganizationDigest getOrganization() {
        return organization;
    }

    public void setOrganization(@Nullable final OrganizationDigest organization) {
        this.organization = organization;
    }

    @Nullable
    public TelecomDigest getTelecoms() {
        return telecoms;
    }

    public void setTelecoms(@Nullable final TelecomDigest telecoms) {
        this.telecoms = telecoms;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final AuthorDigest that)) return false;
        return participationFunction == that.participationFunction
                && Objects.equals(authorshipTimestamp, that.authorshipTimestamp)
                && Objects.equals(givenName, that.givenName)
                && Objects.equals(familyName, that.familyName)
                && Objects.equals(authorGln, that.authorGln)
                && Objects.equals(otherIds, that.otherIds)
                && Objects.equals(addresses, that.addresses)
                && Objects.equals(deviceManufacturerModelName, that.deviceManufacturerModelName)
                && Objects.equals(deviceSoftwareName, that.deviceSoftwareName)
                && Objects.equals(organization, that.organization)
                && Objects.equals(telecoms, that.telecoms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participationFunction, authorshipTimestamp, givenName, familyName, authorGln, otherIds,
                addresses, deviceManufacturerModelName, deviceSoftwareName, organization, telecoms);
    }

    @Override
    public String toString() {
        return "AuthorDigest{" +
                "participationFunction=" + this.participationFunction +
                ", authorshipTimestamp=" + this.authorshipTimestamp +
                ", givenName='" + this.givenName + '\'' +
                ", familyName='" + this.familyName + '\'' +
                ", authorGln='" + this.authorGln + '\'' +
                ", otherIds=" + this.otherIds +
                ", addresses=" + this.addresses +
                ", deviceManufacturerModelName='" + this.deviceManufacturerModelName + '\'' +
                ", deviceSoftwareName='" + this.deviceSoftwareName + '\'' +
                ", organization=" + this.organization +
                ", telecoms=" + this.telecoms +
                '}';
    }
}
