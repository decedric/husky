/*
 * This code is made available under the terms of the Eclipse Public License v1.0
 * in the github project https://github.com/project-husky/husky there you also
 * find a list of the contributors and the license information.
 *
 * This project has been developed further and modified by the joined working group Husky
 * on the basis of the eHealth Connector opensource project from June 28, 2021,
 * whereas medshare GmbH is the initial and main contributor/author of the eHealth Connector.
 */
package org.husky.cda.elga.generated.artdecor;

import java.util.List;

import javax.annotation.processing.Generated;

import org.husky.common.hl7cdar2.ObjectFactory;
import org.husky.common.hl7cdar2.POCDMT000040AssignedEntity;
import org.husky.common.hl7cdar2.POCDMT000040Performer2;
import org.husky.common.hl7cdar2.ParticipationPhysicalPerformer;

/**
 * atcdabbr_other_PerformerBodyImpfendePerson
 * 
 * Identifier: 1.2.40.0.34.6.0.11.9.21<br>
 * Effective date: 2021-10-13 12:53:37<br>
 * Version: 1.0.2+20220103<br>
 * Status: active
 */
@Generated(value = "org.husky.codegenerator.cda.ArtDecor2JavaGenerator", date = "2022-02-18")
public class AtcdabbrOtherPerformerBodyImpfendePerson extends POCDMT000040Performer2 {

    public AtcdabbrOtherPerformerBodyImpfendePerson() {
		super.setTypeCode(ParticipationPhysicalPerformer.PRF);
		super.getTemplateId().add(createHl7TemplateIdFixedValue("1.2.40.0.34.6.0.11.9.21"));
		super.setAssignedEntity(createHl7AssignedEntityFixedValue("ASSIGNED"));

    }

    /**
     * Creates fixed contents for CDA Element hl7AssignedEntity
     *
     * @param classCode the desired fixed value for this argument.
     */
	private static org.husky.common.hl7cdar2.POCDMT000040AssignedEntity createHl7AssignedEntityFixedValue(
			String classCode) {
		POCDMT000040AssignedEntity retVal = new POCDMT000040AssignedEntity();
		retVal.setClassCode(classCode);
		return retVal;

    }

    /**
     * Creates fixed contents for CDA Element hl7TemplateId
     *
     * @param root the desired fixed value for this argument.
     */
    private static org.husky.common.hl7cdar2.II createHl7TemplateIdFixedValue(String root) {
        ObjectFactory factory = new ObjectFactory();
        org.husky.common.hl7cdar2.II retVal = factory.createII();
        retVal.setRoot(root);
        return retVal;
    }

    /**
     * Gets the hl7AssignedEntity
     */
	public org.husky.common.hl7cdar2.POCDMT000040AssignedEntity getHl7AssignedEntity() {
		return this.assignedEntity;
    }

    /**
     * Gets the hl7TemplateId
     */
    public List<org.husky.common.hl7cdar2.II> getHl7TemplateId() {
        return templateId;
    }

    /**
     * Gets the hl7Time
     */
    public org.husky.common.hl7cdar2.TS getHl7Time() {
		return this.time;
    }

    /**
     * Sets the hl7AssignedEntity
     */
	public void setHl7AssignedEntity(org.husky.common.hl7cdar2.POCDMT000040AssignedEntity value) {
		this.assignedEntity = value;
    }

    /**
     * Sets the hl7TemplateId
     */
    public void setHl7TemplateId(org.husky.common.hl7cdar2.II value) {
        getTemplateId().clear();
        getTemplateId().add(value);
    }

    /**
     * Sets the hl7Time
     */
	public void setHl7Time(org.husky.common.hl7cdar2.IVLTS value) {
		this.time = value;
    }
}
