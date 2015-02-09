package org.ehc.cda;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.ehc.common.Util;
import org.openhealthtools.mdht.uml.cda.ihe.IHEFactory;

public class Comment {
	org.openhealthtools.mdht.uml.cda.ihe.Comment mComment;

	public Comment() {
		mComment = IHEFactory.eINSTANCE.createComment().init();
	}

	public Comment(org.openhealthtools.mdht.uml.cda.ihe.Comment comment) {
		mComment = comment;
	}

	public Comment (String text) {
		mComment = IHEFactory.eINSTANCE.createComment().init();
		setText(text);
	}

	public org.openhealthtools.mdht.uml.cda.ihe.Comment copyMdhtComment() {
		return EcoreUtil.copy(mComment);
	}

	public org.openhealthtools.mdht.uml.cda.ihe.Comment getMdhtComment() {
		return mComment;
	}

	public String getText() {
		return mComment.getText().getText();
	}

	public void setText(String text) {
		mComment.setText(Util.createEd(text));
	}
}
