package fr.paris.lutece.plugins.workflow.modules.userassignment.service.task;

import java.util.List;
import java.util.function.Predicate;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.userassignment.business.IResourceUserDAO;
import fr.paris.lutece.plugins.userassignment.business.ResourceUser;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.AssignUserResourceTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.IAdminUserListProvider;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.SelfAssignAdminUserListProvider;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class AssignUserResourceTaskService implements IAssignUserResourceTaskService {

	private static List<IAdminUserListProvider> _providerList;
	
	private final Plugin workflowPlugin;
	private final IResourceUserDAO resourceUserDAO; 
	private final ITaskConfigDAO<AssignUserResourceTaskConfig> taskConfigDAO;
	
	/**
     * Constructor
     * 
     */
    @Inject
    public AssignUserResourceTaskService( IResourceUserDAO resourceUserDAO, 
    		ITaskConfigDAO<AssignUserResourceTaskConfig> taskConfigDAO )
    {
    	this.workflowPlugin = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );
    	this.resourceUserDAO = resourceUserDAO;
    	this.taskConfigDAO = taskConfigDAO;
    }
    
	@Override
	public void assignUserToResource( AdminUser user, int resourceId, String resourceType )
	{
		List<AdminUser> assignedUsers = resourceUserDAO.selectUserListByResource( resourceId, resourceType, workflowPlugin );
		
		boolean alreadyAssigned = null != assignedUsers.stream( )
				.map( AdminUser::getUserId )
				.filter( Predicate.isEqual( user.getUserId( ) ) )
				.findFirst( )
				.orElse( null );
		
		if ( !alreadyAssigned )
		{
			ResourceUser resourceUser = new ResourceUser( );
			resourceUser.setIdResource(resourceId);
			resourceUser.setResourceType(resourceType);
			resourceUser.setAdminUser(user);
			resourceUser.setActive( true );
			
			resourceUserDAO.insert( resourceUser, workflowPlugin );
		}

	}

	@Override
	public List<IAdminUserListProvider> getProviderList( )
	{
		if ( _providerList == null )
		{
			_providerList = initProviderList( );
		}
		return _providerList;
	}

	@Override
	public List<AdminUser> createUserList( HttpServletRequest request, ITask task, int resourceKey, String resourceType )
	{
		AssignUserResourceTaskConfig config = taskConfigDAO.load( task.getId( ) );
		IAdminUserListProvider provider = null;
		if (config != null)
		{
			for ( IAdminUserListProvider p : getProviderList( ) )
			{
				if ( p.getName( ).equals( config.getProviderName( ) ) )
				{
					provider = p;
					break;
				}
			}
		}
		if ( provider == null )
		{
			provider = new SelfAssignAdminUserListProvider( );
		}
		return provider.getUserList( request, resourceKey, resourceType );
	}

	private static List<IAdminUserListProvider> initProviderList( )
	{
		return SpringContextService.getBeansOfType( IAdminUserListProvider.class );
	}
	
	@Override
	public void unassignUserToResource( AdminUser user, int resourceId, String resourceType )
	{
		resourceUserDAO.deactivateByUserResource( user.getUserId( ), resourceId, resourceType, workflowPlugin );
	}
	
	@Override
	public List<AdminUser> listActiveUserByResource( int resourceId, String resourceType )
	{
		return resourceUserDAO.selectUserListByResource( resourceId, resourceType, workflowPlugin );
	}
}
