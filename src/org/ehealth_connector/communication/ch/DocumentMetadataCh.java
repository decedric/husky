package org.ehealth_connector.communication.ch;

import org.ehealth_connector.communication.DocumentMetadata;
import org.ehealth_connector.communication.ch.enums.ConfidentialityCode;
import org.ehealth_connector.communication.ch.enums.FormatCode;
import org.ehealth_connector.communication.ch.enums.HealthcareFacilityTypeCode;
import org.ehealth_connector.communication.ch.enums.LanguageCode;
import org.ehealth_connector.communication.ch.enums.MimeType;
import org.ehealth_connector.communication.ch.enums.PracticeSettingCode;
import org.ehealth_connector.communication.ch.enums.TypeCode;
import org.openhealthtools.ihe.xds.metadata.DocumentEntryType;

public class DocumentMetadataCh extends org.ehealth_connector.communication.DocumentMetadata {

	public DocumentMetadataCh(DocumentMetadata dm) {
		super.xDoc = dm.getMdhtDocumentEntryType();
	}
	
	/**
	 * Instantiates a new swiss (ch) specific document metadata object.
	 * 
	 * @param documentEntryType
	 *          the document entry type
	 */
	public DocumentMetadataCh(DocumentEntryType documentEntryType) {
		super(documentEntryType);
	}
	
	/**
	 * Adds the (optional) confidentialityCode code (e.g. '30001' for 'administrative data')
	 * 
	 * @param code
	 *          the code
	 */
	@SuppressWarnings("unchecked")
	public void addConfidentialityCode(ConfidentialityCode code) {
		xDoc.getConfidentialityCode().add(code.getCodedMetadataType());
	}

	/**
	 * Sets the (required, but in principle computable) class code, which defines
	 * the class of the document (e.g. 'DCT01' for "Notes on Consultations")
	 * 
	 * @param code
	 *          the new class code
	 */
	public void setClassCode(org.ehealth_connector.communication.ch.enums.ClassCode code) {
		xDoc.setClassCode(code.getCodedMetadataType());
	}

	/**
	 * Sets the (required) coded language (e.g. "de-CH"). This code can be
	 * extracted from CDA Document automatically.
	 * 
	 * @param codedLanguage
	 *          the new language code
	 */
	public void setCodedLanguage(LanguageCode codedLanguage) {
		xDoc.setLanguageCode(codedLanguage.getCodeValue());
	}

	/**
	 * Sets the (required) format code (e.g. 'urn:epd:2015:EPD_Basic_Document' for an 'EDP Document')
	 * 
	 * @param code
	 *          the new format code
	 */
	public void setFormatCode(FormatCode code) {
		xDoc.setFormatCode(code.getCodedMetadataType());
	}

	/**
	 * Sets the (required) healthcare facility type code (e.g. '20001' for
	 * 'Institut für medizinische Diagnostik')
	 * 
	 * @param code
	 *          the new healthcare facility type code
	 */
	public void setHealthcareFacilityTypeCode(HealthcareFacilityTypeCode code) {
		xDoc.setHealthCareFacilityTypeCode(code.getCodedMetadataType());
	}

	/**
	 * Sets the (required) mime type (e.g. "text/xml")
	 * 
	 * @param mimeType
	 *          the new mime type
	 */
	public void setMimeType(MimeType mimeType) {
		xDoc.setMimeType(mimeType.getCodedMetadataType().getCode());
	}

	/**
	 * Sets the (required) practice setting code. This is the medical speciality
	 * of the practice where the document was produced (e.g. '10001' for
	 * 'Allergologie')
	 * 
	 * @param code
	 *          the new practice setting code
	 */
	public void setPracticeSettingCode(PracticeSettingCode code) {
		xDoc.setPracticeSettingCode(code.getCodedMetadataType());
	}

	/**
	 * Sets the (required) type code. Specifies the type of the document (like the
	 * class code, but more specific) (e.g. Code for
	 * 'Patienteneinwilligung')
	 * 
	 * @param code
	 *          the new type code
	 */
	public void setTypeCode(TypeCode code) {
		xDoc.setTypeCode(code.getCodedMetadataType());
	}
}