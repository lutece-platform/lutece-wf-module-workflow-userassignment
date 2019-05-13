package fr.paris.lutece.plugins.workflow.modules.userassignment.web.task;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformationHome;
import fr.paris.lutece.plugins.workflow.modules.userassignment.service.task.IAssignUserResourceTaskService;
import fr.paris.lutece.plugins.workflow.modules.userassignment.service.task.UnassignUserResourceTask;
import fr.paris.lutece.plugins.workflow.web.task.NoConfigTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class represents a component for the task {@link UnassignUserResourceTask}
 *
 */
public class UnassignUserResourceTaskComponent extends NoConfigTaskComponent {

	// Template
	private static final String TEMPLATE_TASK_FORM = "admin/plugins/workflow/modules/userassignment/task_user_unassigment_form.html";
	private static final String TEMPLATE_TASK_INFORMATION = "admin/plugins/workflow/modules/userassignment/task_user_unassignment_information.html";
		
	// Marks
	private static final String MARK_USER_LIST = "list_user_selection";
	private static final String MARK_TASK_INFORMATION = "taskInformation";
		
	// Services
	private final IAssignUserResourceTaskService _assignUserResourceTaskService;
	
	@Inject
	public UnassignUserResourceTaskComponent( IAssignUserResourceTaskService assignUserFormResponseTaskService )
	{
		 super( );
		 _assignUserResourceTaskService = assignUserFormResponseTaskService;
	}
	
	@Override
	public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
	{
		return null;
	}

	@Override
	public String doValidateTask(int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
			ITask task) {
		return null;
	}
	
	@Override
	public String getDisplayTaskForm(int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
			ITask task) {
		List<AdminUser> usersList = _assignUserResourceTaskService.listActiveUserByResource(nIdResource, strResourceType);
		
		Map<String, Object> model = new HashMap<>( );
		model.put( MARK_USER_LIST, usersList );
		
		HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FORM, locale, model );
		
		return template.getHtml( );
	}
	
	@Override
	public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
	{
		UserTaskInformation taskInformation = UserTaskInformationHome.find( nIdHistory, task.getId( ) );
		String strTaskInformationHtml = null;
		
	    if ( taskInformation != null )
	    {
	    	Map<String, Object> model = new HashMap<String, Object>( );
            model.put( MARK_TASK_INFORMATION, taskInformation );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_INFORMATION, locale, model );

            strTaskInformationHtml = template.getHtml( );
	    }
		return strTaskInformationHtml;
	}
}
