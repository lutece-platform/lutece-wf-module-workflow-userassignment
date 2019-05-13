package fr.paris.lutece.plugins.workflow.modules.userassignment.service.task;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.userassignment.business.IAdminUserListProvider;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.business.user.AdminUser;

public interface IAssignUserResourceTaskService {

	/**
	 * Assign the user to the FormResponse
	 * @param user
	 * @param formResponse
	 */
	void assignUserToResource(AdminUser user, int resourceId, String resourceType);
	
	/**
	 * get the list of loaded providers.
	 * @return
	 */
	List<IAdminUserListProvider> getProviderList();
	
	/**
	 * Creates the user list for the task.
	 * @param request
	 * @param task
	 * @return
	 */
	List<AdminUser> createUserList(HttpServletRequest request, ITask task, int resourceKey, String resourceType);
	
	/**
	 * Unassign the user.
	 * @param user
	 * @param formResponse
	 */
	void unassignUserToResource( AdminUser user, int resourceId, String resourceType );
	
	/**
	 * List active users by resource.
	 * @param resourceId
	 * @param resourceType
	 * @return
	 */
	List<AdminUser> listActiveUserByResource( int resourceId, String resourceType );
}
