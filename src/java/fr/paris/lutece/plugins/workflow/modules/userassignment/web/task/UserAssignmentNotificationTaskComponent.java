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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.workflow.modules.userassignment.business.TaskUserAssignmentNotificationConfig;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformationHome;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.business.task.ITaskType;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * UserAssignmentNotificationTaskComponent
 *
 */
@ApplicationScoped
public class UserAssignmentNotificationTaskComponent extends NoFormTaskComponent
{

	//TEMPLATE
	private static final String TEMPLATE_CONFIG = "admin/plugins/workflow/modules/userassignment/task_user_assignment_notification_config.html";
	private static final String TEMPLATE_TASK_INFORMATION = "admin/plugins/workflow/modules/userassignment/task_user_assignment_notification_information.html";

	//MARKS
	private static final String MARK_CONFIG = "config";
    private static final String MARK_EMAIL_MARKERS = "email_markers";
    private static final String MARK_USER = "user";
    private static final String MARK_INFORMATION = "information";

	//PROPERIES
    private static final String PROPERTY_AGENT_NAME = "module.workflow.userassignment.markers.agent_name";
    private static final String PROPERTY_RESOURCE_ID = "module.workflow.userassignment.markers.resource_id";
    private static final String PROPERTY_RESOURCE_TYPE = "module.workflow.userassignment.markers.resource_type";
    
    @Inject
    public UserAssignmentNotificationTaskComponent( @Named( "workflow-userassignment.taskTypeUserAssignmentNotification" ) ITaskType taskType,
            @Named( "workflow-userassignment.taskUserAssignmentNotificationConfigService" ) ITaskConfigService taskConfigService )
    {
        setTaskType( taskType );
        setTaskConfigService( taskConfigService );
    }
    
	@Override
	public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
	{
		Map<String, Object> model = new HashMap< >( );
		
		TaskUserAssignmentNotificationConfig config = getTaskConfigService( ).findByPrimaryKey( task.getId( ) );

		model.put ( MARK_CONFIG, config );
        model.put( MARK_EMAIL_MARKERS, getAvailableMarkers( locale ) );
		HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_CONFIG, locale, model );
		
		return html.getHtml( );
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Rendered from a template, never concatenated in Java: plugin-workflow writes the task information as HTML
	 * (LUT-33153), so the user name and the email address are escaped by the template.
	 * </p>
	 */
	@Override
	public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
	{
        UserTaskInformation taskInformation = UserTaskInformationHome.find( nIdHistory, task.getId( ) );

        if ( taskInformation == null )
        {
            return StringUtils.EMPTY;
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( Integer.parseInt( taskInformation.get( UserTaskInformation.TASK_USER_ID ) ) ) );
        model.put( MARK_INFORMATION, taskInformation.get( UserTaskInformation.TASK_INFORMATION ) );

        return AppTemplateService.getTemplate( TEMPLATE_TASK_INFORMATION, locale, model ).getHtml( );
	}
	
	private ReferenceList getAvailableMarkers( Locale locale )
    {
    	ReferenceList referenceList = new ReferenceList( );
		
		referenceList.addItem( "agent_name", I18nService.getLocalizedString( PROPERTY_AGENT_NAME, locale ) );
		referenceList.addItem( "resourceId", I18nService.getLocalizedString( PROPERTY_RESOURCE_ID, locale ) );
		referenceList.addItem( "resourceType", I18nService.getLocalizedString( PROPERTY_RESOURCE_TYPE, locale ) );
		
		return referenceList;
    }
}
