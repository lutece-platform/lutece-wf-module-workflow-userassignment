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
package fr.paris.lutece.plugins.workflow.modules.userassignment.web.task;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.workflow.modules.userassignment.business.TaskUserAssignmentNotificationConfig;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformationHome;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.ITaskService;
import fr.paris.lutece.plugins.workflowcore.service.task.TaskService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * UserAssignmentNotificationTaskComponent
 *
 */
public class UserAssignmentNotificationTaskComponent extends NoFormTaskComponent
{

	//TEMPLATE
	private static final String TEMPLATE_CONFIG = "admin/plugins/workflow/modules/userassignment/task_user_assignment_notification_config.html";

	//MARKS
	private static final String MARK_CONFIG = "config";

	//PROPERIES
	private static final String PROPERTY_HISTORY_INFO = "module.workflow.userassignment.task_user_assignment_notification.history.information";
	private static final String PROPERTY_HISTORY_INFO_USER_UNKNOWN = "module.workflow.userassignment.task_user_assignment_notification.history.information.user_unknown";
	
	private static final String BEAN_CONFIG = "workflow-userassignment.taskUserAssignmentNotificationConfigService";
	
    @Inject
    @Named( BEAN_CONFIG )
    private ITaskConfigService _taskConfigService;
    
    @Inject
    @Named ( TaskService.BEAN_SERVICE )
    private ITaskService _taskService;
    
	@Override
	public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
	{
		Map<String, Object> model = new HashMap< >( );
		
		TaskUserAssignmentNotificationConfig config = _taskConfigService.findByPrimaryKey( task.getId( ) );

		model.put ( MARK_CONFIG, config );
		HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_CONFIG, locale, model );
		
		return html.getHtml( );
	}

	@Override
	public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
	{
        UserTaskInformation taskInformation = UserTaskInformationHome.find( nIdHistory, task.getId( ) );
        StringBuilder sbTaskInformation = new StringBuilder( );

        if ( taskInformation != null )
        {
        	AdminUser user = AdminUserHome.findByPrimaryKey( Integer.parseInt( taskInformation.get( UserTaskInformation.TASK_USER_ID ) ) ) ;
        	
        	sbTaskInformation.append( I18nService.getLocalizedString( PROPERTY_HISTORY_INFO, locale ) );
        	
        	if( user != null)
        	{
	        	sbTaskInformation.append( user.getFirstName( ) );
	        	sbTaskInformation.append( StringUtils.SPACE );
	        	sbTaskInformation.append( user.getLastName( ) );
        	}
        	else
        	{
        		sbTaskInformation.append( I18nService.getLocalizedString( PROPERTY_HISTORY_INFO_USER_UNKNOWN, locale ) );
        	}
        	sbTaskInformation.append( StringUtils.SPACE );
        	sbTaskInformation.append( taskInformation.get( UserTaskInformation.TASK_INFORMATION ) );
        }
        else
        {
        	sbTaskInformation.append( I18nService.getLocalizedString( PROPERTY_HISTORY_INFO_USER_UNKNOWN, locale ) );
        }
        
        return sbTaskInformation.toString( );
	}
}
