package fr.paris.lutece.plugins.workflow.modules.userassignment.business;

import java.util.List;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * This interface provides Data Access methods for {@link ResourceUser} objects
 */
public interface IResourceUserDAO {

	/**
	 * Insert new record in base.
	 * @param resource
	 */
	void insert( ResourceUser resource, Plugin plugin );
	
	/**
	 * Delete record from base.
	 * @param resource
	 */
	void delete( ResourceUser resource, Plugin plugin );
	
	
	/**
	 * Select all ResourceUser By User.
	 * @param userId
	 * @return
	 */
	List<ResourceUser> selectResourcesByUser( int userId, String resourceType, Plugin plugin );
	
	/**
	 * Select all Users By resource.
	 * @param userId
	 * @return
	 */
	List<AdminUser> selectUserListByFormResponse( int resourceID, String resourceType, Plugin plugin );
	
	/**
	 * Deactivate by User and resource
	 * @param userId
	 * @param resourceID
	 * @param resourceType
	 * @param plugin
	 */
	void deactivateByUserResource( int userId, int resourceID, String resourceType, Plugin plugin );
}
