package fr.paris.lutece.plugins.workflow.modules.userassignment.service.task;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflowcore.business.prerequisite.DefaultPrerequisiteConfig;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.IPrerequisiteConfig;
import fr.paris.lutece.portal.business.user.AdminUser;

public abstract class AbstractUserAssignedPrerequisite {

	@Inject
	private IAssignUserResourceTaskService _assignUserResourceTaskService;
	
	public boolean hasConfiguration( )
    {
        return false;
    }
	
	public String getConfigHtml( IPrerequisiteConfig config, HttpServletRequest request, Locale locale )
    {
		return null;
    }
	
	public IPrerequisiteConfig getEmptyConfiguration( )
    {
        return new DefaultPrerequisiteConfig( );
    }
	
	public String getConfigurationDaoBeanName( )
    {
        return null;
    }
	
	protected int countUserAssignedToResource( int nIdResource, String strResourceType )
	{
		List<AdminUser> listActiveUserByResource = _assignUserResourceTaskService.listActiveUserByResource( nIdResource, strResourceType );
		return listActiveUserByResource.size();
	}
}
