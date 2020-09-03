package fr.paris.lutece.plugins.workflow.modules.userassignment.service.prerequiste;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.userassignment.business.ResourceUserHome;
import fr.paris.lutece.plugins.workflow.service.prerequisite.IManualActionPrerequisiteService;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.DefaultPrerequisiteConfig;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.IPrerequisiteConfig;
import fr.paris.lutece.portal.business.user.AdminUser;

public class UserAssignedToResourcePrerequisite implements IManualActionPrerequisiteService {

	private static final String PREREQUISITE_TITLE_I18N = "module.workflow.userassignment.prerequisite.user.resource.title";
	private static final String PREREQUISITE_TYPE = "module.workflow.userassignment.prerequisite.user.resource";

	public String getTitleI18nKey( )
	{
		return PREREQUISITE_TITLE_I18N;
	}

	public String getPrerequisiteType( )
	{
		return PREREQUISITE_TYPE;
	}

	@Override
	public boolean hasConfiguration( )
	{
		return false;
	}

	@Override
	public IPrerequisiteConfig getEmptyConfiguration( )
	{
		 return new DefaultPrerequisiteConfig( );
	}

	@Override
	public String getConfigurationDaoBeanName( )
	{
		return null;
	}

	@Override
	public String getConfigHtml( IPrerequisiteConfig config, HttpServletRequest request, Locale locale )
	{
		return null;
	}

	@Override
	public boolean canActionBePerformed( int nIdResource, String strResourceType, IPrerequisiteConfig config,
			int nIdAction )
	{
		return false;
	}
	
	@Override
	public boolean canManualActionBePerformed( User user, int nIdResource, String strResourceType,
			IPrerequisiteConfig config, int nIdAction )
	{
		List<AdminUser> userList = ResourceUserHome.findUserByResource( nIdResource, strResourceType );
		for ( AdminUser assignedUser : userList)
		{
			if ( assignedUser.getUserId( ) == ( (AdminUser) user ).getUserId( ) )
			{
				return true;
			}
		}
		return false;
	}
}
