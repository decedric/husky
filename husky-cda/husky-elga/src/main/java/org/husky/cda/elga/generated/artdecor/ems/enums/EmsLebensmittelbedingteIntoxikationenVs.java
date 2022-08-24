/*
 * This code is made available under the terms of the Eclipse Public License v1.0
 * in the github project https://github.com/project-husky/husky there you also
 * find a list of the contributors and the license information.
 *
 * This project has been developed further and modified by the joined working group Husky
 * on the basis of the eHealth Connector opensource project from June 28, 2021,
 * whereas medshare GmbH is the initial and main contributor/author of the eHealth Connector.
 */
package org.husky.cda.elga.generated.artdecor.ems.enums;

import java.util.Objects;
import javax.annotation.processing.Generated;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.husky.common.enums.CodeSystems;
import org.husky.common.enums.LanguageCode;
import org.husky.common.enums.ValueSetEnumInterface;

/**
 * Enumeration of ems_lebensmittelbedingte_intoxikationen_vs values
 * <p>
 * EN: No designation found.<br>
 * DE: No designation found.<br>
 * FR: No designation found.<br>
 * IT: No designation found.<br>
 * <p>
 * Identifier: 1.2.40.0.34.6.0.10.29<br>
 * Effective date: 2020-07-23 09:57<br>
 * Version: 2020<br>
 * Status: DRAFT
 */
@Generated(value = "org.husky.codegenerator.ch.valuesets.UpdateValueSets", date = "2022-02-01")
public enum EmsLebensmittelbedingteIntoxikationenVs implements ValueSetEnumInterface {

    /**
     * EN: Food poisoning caused by Bacillus cereus (disorder).<br>
     */
    FOOD_POISONING_CAUSED_BY_BACILLUS_CEREUS_DISORDER("19894004",
                                                      "2.16.840.1.113883.6.96",
                                                      "Food poisoning caused by Bacillus cereus (disorder)",
                                                      "Food poisoning caused by Bacillus cereus (disorder)",
                                                      "TOTRANSLATE",
                                                      "TOTRANSLATE",
                                                      "TOTRANSLATE"),
    /**
     * EN: Food poisoning caused by Clostridium perfringens (disorder).<br>
     */
    FOOD_POISONING_CAUSED_BY_CLOSTRIDIUM_PERFRINGENS_DISORDER("70014009",
                                                              "2.16.840.1.113883.6.96",
                                                              "Food poisoning caused by Clostridium perfringens (disorder)",
                                                              "Food poisoning caused by Clostridium perfringens (disorder)",
                                                              "TOTRANSLATE",
                                                              "TOTRANSLATE",
                                                              "TOTRANSLATE"),
    /**
     * EN: Staphylococcus aureus food poisoning (disorder).<br>
     */
    STAPHYLOCOCCUS_AUREUS_FOOD_POISONING_DISORDER("398384001",
                                                  "2.16.840.1.113883.6.96",
                                                  "Staphylococcus aureus food poisoning (disorder)",
                                                  "Staphylococcus aureus food poisoning (disorder)",
                                                  "TOTRANSLATE",
                                                  "TOTRANSLATE",
                                                  "TOTRANSLATE");

    /**
     * EN: Code for Food poisoning caused by Bacillus cereus (disorder).<br>
     */
    public static final String FOOD_POISONING_CAUSED_BY_BACILLUS_CEREUS_DISORDER_CODE = "19894004";

    /**
     * EN: Code for Food poisoning caused by Clostridium perfringens (disorder).<br>
     */
    public static final String FOOD_POISONING_CAUSED_BY_CLOSTRIDIUM_PERFRINGENS_DISORDER_CODE = "70014009";

    /**
     * EN: Code for Staphylococcus aureus food poisoning (disorder).<br>
     */
    public static final String STAPHYLOCOCCUS_AUREUS_FOOD_POISONING_DISORDER_CODE = "398384001";

    /**
     * Identifier of the value set.
     */
    public static final String VALUE_SET_ID = "1.2.40.0.34.6.0.10.29";

    /**
     * Name of the value set.
     */
    public static final String VALUE_SET_NAME = "ems_lebensmittelbedingte_intoxikationen_vs";

    /**
     * Identifier of the code system (all values share the same).
     */
    public static final String CODE_SYSTEM_ID = "2.16.840.1.113883.6.96";

    /**
     * Gets the Enum with a given code.
     *
     * @param code The code value.
     * @return the enum value found or {@code null}.
     */
    @Nullable
    public static EmsLebensmittelbedingteIntoxikationenVs getEnum(@Nullable final String code) {
        for (final EmsLebensmittelbedingteIntoxikationenVs x : values()) {
            if (x.getCodeValue().equals(code)) {
                return x;
            }
        }
        return null;
    }

    /**
     * Checks if a given enum is part of this value set.
     *
     * @param enumName The name of the enum.
     * @return {@code true} if the name is found in this value set, {@code false} otherwise.
     */
    public static boolean isEnumOfValueSet(@Nullable final String enumName) {
        if (enumName == null) {
            return false;
        }
        try {
            Enum.valueOf(EmsLebensmittelbedingteIntoxikationenVs.class,
                         enumName);
            return true;
        } catch (final IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Checks if a given code value is in this value set.
     *
     * @param codeValue The code value.
     * @return {@code true} if the value is found in this value set, {@code false} otherwise.
     */
    public static boolean isInValueSet(@Nullable final String codeValue) {
        for (final EmsLebensmittelbedingteIntoxikationenVs x : values()) {
            if (x.getCodeValue().equals(codeValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Machine interpretable and (inside this class) unique code.
     */
    @NonNull
    private final String code;

    /**
     * Identifier of the referencing code system.
     */
    @NonNull
    private final String codeSystem;

    /**
     * The display names per language. It's always stored in the given order: default display name (0), in English (1),
     * in German (2), in French (3) and in Italian (4).
     */
    @NonNull
    private final String[] displayNames;

    /**
     * Instantiates this enum with a given code and display names.
     *
     * @param code          The code value.
     * @param codeSystem    The code system (OID).
     * @param displayName   The default display name.
     * @param displayNameEn The display name in English.
     * @param displayNameDe The display name in German.
     * @param displayNameFr The display name in French.
     * @param displayNameIt The display name in Italian.
     */
    EmsLebensmittelbedingteIntoxikationenVs(@NonNull final String code, @NonNull final String codeSystem, @NonNull final String displayName, @NonNull final String displayNameEn, @NonNull final String displayNameDe, @NonNull final String displayNameFr, @NonNull final String displayNameIt) {
        this.code = Objects.requireNonNull(code);
        this.codeSystem = Objects.requireNonNull(codeSystem);
        this.displayNames = new String[5];
        this.displayNames[0] = Objects.requireNonNull(displayName);
        this.displayNames[1] = Objects.requireNonNull(displayNameEn);
        this.displayNames[2] = Objects.requireNonNull(displayNameDe);
        this.displayNames[3] = Objects.requireNonNull(displayNameFr);
        this.displayNames[4] = Objects.requireNonNull(displayNameIt);
    }

    /**
     * Gets the code system identifier.
     *
     * @return the code system identifier.
     */
    @Override
    @NonNull
    public String getCodeSystemId() {
        return this.codeSystem;
    }

    /**
     * Gets the code system name.
     *
     * @return the code system name.
     */
    @Override
    @NonNull
    public String getCodeSystemName() {
        final var codeSystem = CodeSystems.getEnum(this.codeSystem);
        if (codeSystem != null) {
            return codeSystem.getCodeSystemName();
        }
        return "";
    }

    /**
     * Gets the code value as a string.
     *
     * @return the code value.
     */
    @Override
    @NonNull
    public String getCodeValue() {
        return this.code;
    }

    /**
     * Gets the display name defined by the language param.
     *
     * @param languageCode The language code to get the display name for, {@code null} to get the default display name.
     * @return the display name in the desired language.
     */
    @Override
    @NonNull
    public String getDisplayName(@Nullable final LanguageCode languageCode) {
        if (languageCode == null) {
            return this.displayNames[0];
        }
        return switch(languageCode) {
            case ENGLISH ->
                this.displayNames[1];
            case GERMAN ->
                this.displayNames[2];
            case FRENCH ->
                this.displayNames[3];
            case ITALIAN ->
                this.displayNames[4];
            default ->
                "TOTRANSLATE";
        };
    }

    /**
     * Gets the value set identifier.
     *
     * @return the value set identifier.
     */
    @Override
    @NonNull
    public String getValueSetId() {
        return VALUE_SET_ID;
    }

    /**
     * Gets the value set name.
     *
     * @return the value set name.
     */
    @Override
    @NonNull
    public String getValueSetName() {
        return VALUE_SET_NAME;
    }
}