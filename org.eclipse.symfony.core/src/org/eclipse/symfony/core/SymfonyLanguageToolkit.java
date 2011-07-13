package org.eclipse.symfony.core;

import org.eclipse.php.internal.core.PHPLanguageToolkit;
import org.eclipse.php.internal.core.documentModel.provisional.contenttype.ContentTypeIdForPHP;

@SuppressWarnings("restriction")
public class SymfonyLanguageToolkit extends PHPLanguageToolkit {
	
	
	public String getLanguageContentType() {

		System.err.println("getting content type");
		return super.getLanguageContentType();
	}
	

}
