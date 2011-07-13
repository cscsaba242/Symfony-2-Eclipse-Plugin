package org.eclipse.symfony.twig;

import org.eclipse.php.internal.core.PHPLanguageToolkit;
import org.eclipse.twig.core.documentModel.provisional.contenttype.ContentTypeIdForTwig;

@SuppressWarnings("restriction")
public class TwigLanguageToolkit extends PHPLanguageToolkit {
	
	
	@Override
	public String getLanguageContentType() {
		System.err.println("return twig content type3");

		return ContentTypeIdForTwig.CONTENT_TYPE_ID_TWIG;
	}
	
	
	@Override
	public String[] getLanguageFileExtensions() {
		
		String[] exts = super.getLanguageFileExtensions();		
		String[] twigExt = new String[exts.length+1];		
		twigExt[twigExt.length] = "twig";
		return twigExt;
		
	}

}
