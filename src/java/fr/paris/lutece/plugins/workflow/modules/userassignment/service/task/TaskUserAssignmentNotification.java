/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.userassignment.service.task;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;


import fr.paris.lutece.plugins.workflow.modules.userassignment.business.TaskUserAssignmentNotificationConfig;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformationHome;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.resource.ResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITaskService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.plugins.workflowcore.service.task.TaskService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * TaskUserAssignmentNotification
 *
 */
@Dependent
@Named( "workflow-userassignment.taskUserAssignmentNotification" )
public class TaskUserAssignmentNotification extends SimpleTask
{
    // Message
    private static final String MESSAGE_TASK_TITLE 	= "module.workflow.userassignment.task_user_assignment_notification.title";

    //MARKS
    private static final String MARK_AGENT_NAME = "agent_name";
    private static final String MARK_RESOURCE_ID   = "resourceId";
    private static final String MARK_RESOURCE_TYPE = "resourceType";
    
	private static final String BEAN_CONFIG 		= "workflow-userassignment.taskUserAssignmentNotificationConfigService";
	
    @Inject
    @Named( BEAN_CONFIG )
    private ITaskConfigService _taskConfigService;
    
    @Inject
    @Named( ResourceHistoryService.BEAN_SERVICE )
    private IResourceHistoryService _resourceHistoryService;
    
    @Inject
    @Named ( TaskService.BEAN_SERVICE )
    private ITaskService _taskService;
    
    @Inject
    private IAssignUserResourceTaskService _userResourceService;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_TASK_TITLE, locale );
    }


	@Override
	public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
	{
		ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );		
		TaskUserAssignmentNotificationConfig config = _taskConfigService.findByPrimaryKey( getId( ) );
		notifyUser( resourceHistory, config );		
	}

	
	/**
	 * Notify user
	 * @param locale
	 * @param resourceHistory
	 * @param config
	 * @param userConnected
	 * @param adminUser
	 */
	private void notifyUser( ResourceHistory resourceHistory, TaskUserAssignmentNotificationConfig config )
	{	
		List<AdminUser> listNotifyUser =  _userResourceService.listActiveUserByResource( resourceHistory.getIdResource( ), resourceHistory.getResourceType( ) );
		
		if ( !listNotifyUser.isEmpty( ) )
		{
			for( AdminUser adminUser :  listNotifyUser )
			{	
			    Map<String, Object> model = getAvailableMarkersValues( resourceHistory, adminUser );
				HtmlTemplate html = AppTemplateService.getTemplateFromStringFtl( config.getMessage( ), null, model );
				
				//Send notification
				MailService.sendMailHtml( adminUser.getEmail( ), config.getRecipientsCc( ), config.getRecipientsBcc( ), config.getSenderName( ),
						config.getSenderEmail( ), config.getSubject( ), html.getHtml());
				
				//Save information task
				saveUserTaskInformation( resourceHistory.getId( ), adminUser );
			}
		}
		else 			
		{
			AppLogService.error( "User to notify not found", new Exception( ) );
		}
	}
	
    private void saveUserTaskInformation( int nIdResourceHistory, AdminUser userNotified )
    {
        if ( userNotified != null )
        {
            UserTaskInformation taskInformation = new UserTaskInformation( nIdResourceHistory, getId( ) );
            taskInformation.add( UserTaskInformation.TASK_USER_ID, String.valueOf( userNotified.getUserId( ) ) );
            taskInformation.add( UserTaskInformation.TASK_INFORMATION, userNotified.getEmail( ) );
            UserTaskInformationHome.create( taskInformation );
        }
    }

	private Map<String, Object> getAvailableMarkersValues( ResourceHistory resourceHistory, AdminUser user )
    {
        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_AGENT_NAME, user.getFirstName( ) );
        model.put( MARK_RESOURCE_ID, String.valueOf( resourceHistory.getIdResource( ) ) );
        model.put( MARK_RESOURCE_TYPE, String.valueOf( resourceHistory.getResourceType( ) ) );
        
        return model;
    }
}
