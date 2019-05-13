package fr.paris.lutece.plugins.workflow.modules.userassignment.service.task;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformationHome;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;

public class UnassignUserResourceTask extends SimpleTask {

	private static final String MESSAGE_TASK_TITLE = "module.workflow.userassignment.task.user.unassign.title";
	
	private static final String PARAMETER_USER_ID = "user_selection_id";
	
	private final IAssignUserResourceTaskService _assignUserResourceTaskService;
	private final IResourceHistoryService _resourceHistoryService;
	
	@Inject
    public UnassignUserResourceTask( IAssignUserResourceTaskService assignUserResourceTaskService, IResourceHistoryService resourceHistoryService )
    {
        super( );
        _assignUserResourceTaskService = assignUserResourceTaskService;
        _resourceHistoryService = resourceHistoryService;
    }
	
	@Override
	public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
	{
		ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
		 
		 if ( resourceHistory != null )
		 {
			 String strUnitSelectionId = request.getParameter( PARAMETER_USER_ID );
			 
			 AdminUser user = AdminUserHome.findByPrimaryKey( Integer.valueOf( strUnitSelectionId ) );
			 
			 _assignUserResourceTaskService.unassignUserToResource(user, resourceHistory.getIdResource( ), resourceHistory.getResourceType( ));
			 
			 saveUserTaskInformation(nIdResourceHistory, AdminUserService.getAdminUser( request ) );
		 }
	}
	
	private void saveUserTaskInformation(int nIdResourceHistory, AdminUser userUnassigned ) {
		UserTaskInformation taskInformation = new UserTaskInformation( nIdResourceHistory, getId( ) );
        taskInformation.add( UserTaskInformation.TASK_INFORMATION_UNASSIGNOR, 
        		userUnassigned.getFirstName( ) + " " + userUnassigned.getLastName( ) );
        
        UserTaskInformationHome.create(taskInformation);
	}
	
	@Override
	public String getTitle(Locale locale)
	{
		 return I18nService.getLocalizedString( MESSAGE_TASK_TITLE, locale );
	}
}
