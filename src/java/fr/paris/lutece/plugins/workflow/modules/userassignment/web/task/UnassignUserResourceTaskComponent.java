/*
 * Copyright (c) 2002-2020, City of Paris
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
public class UnassignUserResourceTaskComponent extends NoConfigTaskComponent
{

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
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        List<AdminUser> usersList = _assignUserResourceTaskService.listActiveUserByResource( nIdResource, strResourceType );

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
