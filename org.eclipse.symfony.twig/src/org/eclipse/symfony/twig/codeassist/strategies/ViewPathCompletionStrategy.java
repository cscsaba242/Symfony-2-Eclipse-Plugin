package org.eclipse.symfony.twig.codeassist.strategies;

import java.util.List;

import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.index2.search.ISearchEngine.MatchRule;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchEngine;
import org.eclipse.dltk.internal.core.ModelElement;
import org.eclipse.dltk.internal.core.SourceRange;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.internal.core.codeassist.CodeAssistUtils;
import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.strategies.MethodParameterKeywordStrategy;
import org.eclipse.php.internal.core.model.PhpModelAccess;
import org.eclipse.symfony.core.model.Bundle;
import org.eclipse.symfony.core.model.Controller;
import org.eclipse.symfony.core.model.SymfonyModelAccess;
import org.eclipse.symfony.core.model.Template;
import org.eclipse.symfony.core.model.ViewPath;
import org.eclipse.symfony.twig.codeassist.context.ViewPathArgumentContext;
import org.eclipse.symfony.ui.contentassist.SymfonyCompletionProposalCollector;
import org.eclipse.twig.ui.editor.contentassist.TwigCompletionProposalCollector;


/**
 * 
 * A viewpath completion strategy for Twig templates:
 * 
 * 
 * <pre> 
 * 	{% extends '|   <-- shows available Bundles etc. 
 * </pre>
 * 
 * 
 * @author Robert Gruendler <r.gruendler@gmail.com>
 *
 */
@SuppressWarnings({ "restriction", "deprecation" })
public class ViewPathCompletionStrategy extends MethodParameterKeywordStrategy {

	
	public ViewPathCompletionStrategy(ICompletionContext context) {
		super(context);

	}
	
	
	@Override
	public void apply(ICompletionReporter reporter) throws BadLocationException {
	
		ViewPathArgumentContext context = (ViewPathArgumentContext) getContext();
		CompletionRequestor req = context.getCompletionRequestor();
			
		if (req.getClass() != SymfonyCompletionProposalCollector.class) {
			return;			
		}

		
		SymfonyModelAccess model = SymfonyModelAccess.getDefault();
		ISourceModule module = context.getSourceModule();
		ViewPath viewPath = context.getViewPath();
		
		String bundle = viewPath.getBundle();
		String controller = viewPath.getController();
		String template = viewPath.getTemplate();
		SourceRange range = getReplacementRange(context);
		IDLTKSearchScope projectScope = SearchEngine.createSearchScope(context.getSourceModule().getScriptProject());
		
		String prefix = context.getPrefix();
		

		// complete the bundle part
		if (bundle == null && controller == null && template == null) {

			List<String> bundles = model.findBundles(context.getSourceModule().getScriptProject());

			for (String b : bundles) {				
				
				IType[] bundleTypes = PhpModelAccess.getDefault().findTypes(b, MatchRule.EXACT, 0, 0, projectScope, null);
				
				if (bundleTypes.length == 1) {
					
					ModelElement bType = (ModelElement) bundleTypes[0];
					
					if (CodeAssistUtils.startsWithIgnoreCase(bType.getElementName(), prefix)) {
						Bundle bundleType = new Bundle(bType, b);
						reporter.reportType(bundleType, ":", range);						
					}
				}
			}			
			
		// complete the controller part: "Bundle:| 
		} else if (controller == null && template == null) {			
			
			IType[] controllers = model.findBundleControllers(bundle, module.getScriptProject());
			
			if (controllers != null) {
				for (IType ctrl : controllers) {
					
					String name = ctrl.getElementName().replace("Controller", "");
					if (!name.endsWith("\\")) {						
						Controller con = new Controller((ModelElement) ctrl, name);
						reporter.reportType(con, ":", range);
					}
						
				}				
			}

		// complete template path: "Bundle:Controller:|
		} else if (bundle != null && controller != null && template == null) {

			IModelElement[] templates = model.findTemplates(bundle, controller, module.getScriptProject());
			
			if (templates != null) {				
				for (IModelElement tpl : templates) {

					if (CodeAssistUtils.startsWithIgnoreCase(tpl.getElementName(), prefix)) {
						Template t = new Template((ModelElement) tpl, tpl.getElementName());
						reporter.reportType(t, "", range);
					}
					
				}
			}
			
		// project root: "::| 
		} else if (bundle == null && controller == null && template != null) {

			IModelElement[] templates = model.findRootTemplates(module.getScriptProject());
			
			if (templates != null) {				
				for (IModelElement tpl : templates) {
					
					if (CodeAssistUtils.startsWithIgnoreCase(tpl.getElementName(), prefix)) {
						Template t = new Template((ModelElement) tpl, tpl.getElementName());
						reporter.reportType(t, "", range);
					}
					
				}
			}
			
		// bundle root: "AcmeDemoBundle::| 
		} else if (bundle != null && controller == null && template != null) {
			
			
			IModelElement[] templates = model.findBundleRootTemplates(bundle, module.getScriptProject());
			
			if (templates != null) {				
				for (IModelElement tpl : templates) {
					
					if (CodeAssistUtils.startsWithIgnoreCase(tpl.getElementName(), prefix)) {
						Template t = new Template((ModelElement) tpl, tpl.getElementName());
						reporter.reportType(t, "", range);
					}					
				}
			}
			
		}
	}
}