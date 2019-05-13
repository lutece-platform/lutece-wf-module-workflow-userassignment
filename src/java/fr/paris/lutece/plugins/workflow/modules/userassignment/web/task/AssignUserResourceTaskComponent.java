package fr.paris.lutece.plugins.workflow.modules.userassignment.web.task;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.userassignment.business.AssignUserResourceTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformationHome;
import fr.paris.lutece.plugins.workflow.modules.userassignment.service.task.AssignUserResourceTask;
import fr.paris.lutece.plugins.workflow.modules.userassignment.service.task.IAssignUserResourceTaskService;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfig;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.web.task.TaskComponent;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class represents a component for the task {@link AssignUserResourceTask}
 *
 */
public class AssignUserResourceTaskComponent extends TaskComponent {

	// Template
	private static final String TEMPLATE_TASK_FORM = "admin/plugins/workflow/modules/userassignment/task_user_assigment_form.html";
	private static final String TEMPLATE_CONFIG_TASK_FORM = "admin/plugins/workflow/modules/userassignment/task_user_assigment_config_form.html";
	private static final String TEMPLATE_TASK_INFORMATION = "admin/plugins/workflow/modules/userassignment/task_user_assignment_information.html";
	
	// Marks
	private static final String MARK_USER_LIST = "list_user_selection";
	private static final String MARK_PROVIDER_LIST = "provider_list";
	private static final String MARK_SELECTED_PROVIDER = "selected_provider";
	private static final String MARK_TASK_INFORMATION = "taskInformation";
	
	// Services
	private final IAssignUserResourceTaskService _assignUserResourceTaskService;
	
	@Inject
	public AssignUserResourceTaskComponent( IAssignUserResourceTaskService assignUserFormResponseTaskService )
	{
		 super( );
		 _assignUserResourceTaskService = assignUserFormResponseTaskService;
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

	@Override
	public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
	{
		return null;
	}

	@Override
	public String validateConfig( ITaskConfig config, HttpServletRequest request )
	{
		return null;
	}

	@Override
	public String getDisplayTaskForm(int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
			ITask task) {
		List<AdminUser> usersList = _assignUserResourceTaskService.createUserList( request, task, nIdResource, strResourceType );
		
		Map<String, Object> model = new HashMap<>( );
		model.put( MARK_USER_LIST, usersList );
		
		HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FORM, locale, model );
		
		return template.getHtml( );
	}

	@Override
	public String getDisplayConfigForm(HttpServletRequest request, Locale locale, ITask task) {
		Map<String, Object> model = new HashMap<>( );
		model.put( MARK_PROVIDER_LIST, _assignUserResourceTaskService.getProviderList() );
		
		AssignUserResourceTaskConfig config = loadConfig(task);
		model.put( MARK_SELECTED_PROVIDER, config.getProviderName( ) );
		
		HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIG_TASK_FORM, locale, model );
		
		return template.getHtml();
	}

	@Override
	public String doValidateTask(int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
			ITask task) {
		return null;
	}
	
	private AssignUserResourceTaskConfig loadConfig(ITask task) {
		AssignUserResourceTaskConfig config = getTaskConfigService().findByPrimaryKey(task.getId());
		if ( config == null )
		{
			config = new AssignUserResourceTaskConfig();
			config.setIdTask(task.getId());
			getTaskConfigService().create(config);
		}
		return config;
	}
}
