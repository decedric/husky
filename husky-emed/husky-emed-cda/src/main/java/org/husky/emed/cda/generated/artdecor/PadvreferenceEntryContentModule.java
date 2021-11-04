package org.husky.emed.cda.generated.artdecor;

import java.util.List;
import javax.annotation.processing.Generated;

import org.husky.emed.cda.generated.hl7cdar2.ObjectFactory;
import org.husky.emed.cda.generated.hl7cdar2.POCDMT000040Observation;

/**
 * PADVReferenceEntryContentModule
 * <p>
 * Template description: Reference to Pharmaceutical Advice<br>
 * Element description: Reference to Pharmaceutical Advice Item General Specification<br>
 * <p>
 * Identifier: 2.16.756.5.30.1.1.10.4.53<br>
 * Effective date: 2018-01-11 21:10:57<br>
 * Version: 2017<br>
 * Status: pending
 */
@Generated(value = "org.ehealth_connector.codegenerator.cda.ArtDecor2JavaGenerator", date = "2021-09-08")
public class PadvreferenceEntryContentModule extends POCDMT000040Observation {

    public PadvreferenceEntryContentModule() {
        super.getClassCode().add("OBS");
        super.setMoodCode(org.husky.emed.cda.generated.hl7cdar2.XActMoodDocumentObservation.EVN);
        super.getTemplateId().add(createHl7TemplateIdFixedValue("1.3.6.1.4.1.19376.1.9.1.3.13"));
        super.getTemplateId().add(createHl7TemplateIdFixedValue("2.16.756.5.30.1.1.10.4.53"));
        super.setCode(createHl7CodeFixedValue("PADVItem",
                                              "1.3.6.1.4.1.19376.1.9.2.2",
                                              null,
                                              null));
    }

    /**
     * Creates fixed contents for CDA Element hl7Code
     *
     * @param code the desired fixed value for this argument.
     */
    private static org.husky.emed.cda.generated.hl7cdar2.CD createHl7CodeFixedValue(String code, String codeSystem, String codeSystemName, String displayName) {
        ObjectFactory factory = new ObjectFactory();
        org.husky.emed.cda.generated.hl7cdar2.CD retVal = factory.createCD();
        retVal.setCode(code);
        retVal.setCodeSystem(codeSystem);
        retVal.setCodeSystemName(codeSystemName);
        retVal.setDisplayName(displayName);
        return retVal;
    }

    /**
     * Creates fixed contents for CDA Element hl7Reference
     *
     * @param typeCode the desired fixed value for this argument.
     */
    private static org.husky.emed.cda.generated.hl7cdar2.POCDMT000040Reference createHl7ReferenceFixedValue(String typeCode) {
        ObjectFactory factory = new ObjectFactory();
        org.husky.emed.cda.generated.hl7cdar2.POCDMT000040Reference retVal = factory.createPOCDMT000040Reference();
        retVal.setTypeCode(org.husky.emed.cda.generated.hl7cdar2.XActRelationshipExternalReference.fromValue(typeCode));
        return retVal;
    }

    /**
     * Creates fixed contents for CDA Element hl7TemplateId
     *
     * @param root the desired fixed value for this argument.
     */
    private static org.husky.emed.cda.generated.hl7cdar2.II createHl7TemplateIdFixedValue(String root) {
        ObjectFactory factory = new ObjectFactory();
        org.husky.emed.cda.generated.hl7cdar2.II retVal = factory.createII();
        retVal.setRoot(root);
        return retVal;
    }

    /**
     * Gets the hl7Author
     * Author of the referenced item
     */
    public List<org.husky.emed.cda.generated.hl7cdar2.POCDMT000040Author> getHl7Author() {
        return author;
    }

    /**
     * Gets the hl7Code
     * Reference to Pharmaceutical Advice Item code
     */
    public org.husky.emed.cda.generated.hl7cdar2.CD getHl7Code() {
        return code;
    }

    /**
     * Gets the hl7Id
     * Reference to Pharmaceutical Advice Item ID
     */
    public List<org.husky.emed.cda.generated.hl7cdar2.II> getHl7Id() {
        return id;
    }

    /**
     * Gets the hl7Reference
     * ID of parent container of referenced item
     */
    public List<org.husky.emed.cda.generated.hl7cdar2.POCDMT000040Reference> getHl7Reference() {
        return reference;
    }

    /**
     * Gets the hl7TemplateId
     * Reference to Pharmaceutical Advice Item Template ID
     */
    public List<org.husky.emed.cda.generated.hl7cdar2.II> getHl7TemplateId() {
        return templateId;
    }

    /**
     * Adds a predefined org.ehealth_connector.emed.cda.generated.hl7cdar2.POCDMT000040Reference, filled by: "XCRPT"
     * @return the predefined element.
     */
    public static org.husky.emed.cda.generated.hl7cdar2.POCDMT000040Reference getPredefinedReferenceXcrpt() {
        return createHl7ReferenceFixedValue("XCRPT");
    }

    /**
     * Sets the hl7Author
     * Author of the referenced item
     */
    public void setHl7Author(org.husky.emed.cda.generated.hl7cdar2.POCDMT000040Author value) {
        getAuthor().clear();
        getAuthor().add(value);
    }

    /**
     * Sets the hl7Code
     * Reference to Pharmaceutical Advice Item code
     */
    public void setHl7Code(org.husky.emed.cda.generated.hl7cdar2.CD value) {
        this.code = value;
    }

    /**
     * Sets the hl7Id
     * Reference to Pharmaceutical Advice Item ID
     */
    public void setHl7Id(org.husky.emed.cda.generated.hl7cdar2.II value) {
        getId().clear();
        getId().add(value);
    }

    /**
     * Sets the hl7Reference
     * ID of parent container of referenced item
     */
    public void setHl7Reference(org.husky.emed.cda.generated.hl7cdar2.POCDMT000040Reference value) {
        getReference().clear();
        getReference().add(value);
    }

    /**
     * Sets the hl7TemplateId
     * Reference to Pharmaceutical Advice Item Template ID
     */
    public void setHl7TemplateId(org.husky.emed.cda.generated.hl7cdar2.II value) {
        getTemplateId().clear();
        getTemplateId().add(value);
    }
}