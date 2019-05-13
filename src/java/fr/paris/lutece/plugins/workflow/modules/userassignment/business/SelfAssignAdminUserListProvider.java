package fr.paris.lutece.plugins.workflow.modules.userassignment.business;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;

/**
 * An implementation of {@link IAdminUserListProvider} which returns a list containing only the current user.
 */
public class SelfAssignAdminUserListProvider implements IAdminUserListProvider {

private static final String MESSAGE_LABEL = "module.workflow.userassignment.task.user.assign.config.self";
	
	private static final String NAME = "self";
	
	@Override
	public String getLabelKey()
	{
		 return MESSAGE_LABEL;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<AdminUser> getUserList( HttpServletRequest request, int resourceId, String resourceType )
	{
		List<AdminUser> usersList = new ArrayList<>( );
		usersList.add( AdminUserService.getAdminUser( request ) );
		return usersList;
	}
}
