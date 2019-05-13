package fr.paris.lutece.plugins.workflow.modules.userassignment.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;

/**
 * Interface for the providers of the list of users.
 */
public interface IAdminUserListProvider {

	/**
	 * The label key of the service.
	 * @return
	 */
	String getLabelKey( );
	
	/**
	 * The name of the service.
	 * @return
	 */
	String getName( );
	
	
	/**
	 * The list of {@link AdminUser} that can be assigned by the task.
	 * @param resourceId
	 * @param resourceType
	 * @return
	 */
	List<AdminUser> getUserList( HttpServletRequest request, int resourceId, String resourceType );
}
