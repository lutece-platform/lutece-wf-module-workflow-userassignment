package fr.paris.lutece.plugins.workflow.modules.userassignment.service.task;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.i18n.I18nService;

public class AssignUserResourceTask extends SimpleTask {

private static final String MESSAGE_TASK_TITLE = "module.workflow.userassignment.task.user.assign.title";
	
	private static final String PARAMETER_USER_ID = "user_selection_id";
	
	private final IAssignUserResourceTaskService _assignUserResourceTaskService;
	private final IResourceHistoryService _resourceHistoryService;
	
	@Inject
    public AssignUserResourceTask( IAssignUserResourceTaskService assignUserResourceTaskService, IResourceHistoryService resourceHistoryService)
    {
        super( );
        _assignUserResourceTaskService = assignUserResourceTaskService;
        _resourceHistoryService = resourceHistoryService;
    }
	
	@Override
	public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
	{
		 ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
		 
		 String strUnitSelectionId = request.getParameter( PARAMETER_USER_ID );
		 
		 AdminUser user = AdminUserHome.findByPrimaryKey( Integer.valueOf( strUnitSelectionId ) );
		 
			 _assignUserResourceTaskService.assignUserToResource( user, resourceHistory.getIdResource( ), resourceHistory.getResourceType( ) );
	}
	
	@Override
	public String getTitle(Locale locale)
	{
		 return I18nService.getLocalizedString( MESSAGE_TASK_TITLE, locale );
	}
}
