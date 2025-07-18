/*
 * Copyright (c) 2002-2025, City of Paris
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

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.paris.lutece.plugins.workflowcore.business.task.ITaskType;
import fr.paris.lutece.plugins.workflowcore.business.task.TaskType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class TaskTypeProducer 
{
	@Produces
    @ApplicationScoped
    @Named( "workflow-userassignment.assignUserResourceTypeTask" )
    public ITaskType produceAssignUserResourceTypeTask( 
    		@ConfigProperty( name = "workflow-userassignment.assignUserResourceTypeTask.key" ) String key,
            @ConfigProperty( name = "workflow-userassignment.assignUserResourceTypeTask.titleI18nKey" ) String titleI18nKey,
            @ConfigProperty( name = "workflow-userassignment.assignUserResourceTypeTask.beanName" ) String beanName,
            @ConfigProperty( name = "workflow-userassignment.assignUserResourceTypeTask.configBeanName" ) String configBeanName,
            @ConfigProperty( name = "workflow-userassignment.assignUserResourceTypeTask.configRequired", defaultValue = "false" ) boolean configRequired,
            @ConfigProperty( name = "workflow-userassignment.assignUserResourceTypeTask.formTaskRequired", defaultValue = "false" ) boolean formTaskRequired,
            @ConfigProperty( name = "workflow-userassignment.assignUserResourceTypeTask.taskForAutomaticAction", defaultValue = "false" ) boolean taskForAutomaticAction )
    {
        return buildTaskType( key, titleI18nKey, beanName, configBeanName, configRequired, formTaskRequired, taskForAutomaticAction );
    }

	@Produces
	@ApplicationScoped
	@Named( "workflow-userassignment.assignToCurrentUserResourceTypeTask" )
	public ITaskType produceAssignToCurrentUserResourceTypeTask(
	    @ConfigProperty( name = "workflow-userassignment.assignToCurrentUserResourceTypeTask.key" ) String key,
	    @ConfigProperty( name = "workflow-userassignment.assignToCurrentUserResourceTypeTask.titleI18nKey" ) String titleI18nKey,
	    @ConfigProperty( name = "workflow-userassignment.assignToCurrentUserResourceTypeTask.beanName" ) String beanName,
	    @ConfigProperty( name = "workflow-userassignment.assignToCurrentUserResourceTypeTask.configRequired", defaultValue = "false" ) boolean configRequired,
	    @ConfigProperty( name = "workflow-userassignment.assignToCurrentUserResourceTypeTask.formTaskRequired", defaultValue = "false" ) boolean formTaskRequired,
	    @ConfigProperty( name = "workflow-userassignment.assignToCurrentUserResourceTypeTask.taskForAutomaticAction", defaultValue = "false" ) boolean taskForAutomaticAction )
	{
	    return buildTaskType( key, titleI18nKey, beanName, null, configRequired, formTaskRequired, taskForAutomaticAction );
	}

	@Produces
	@ApplicationScoped
	@Named( "workflow-userassignment.unassignUserResourceTypeTask" )
	public ITaskType produceUnassignUserResourceTypeTask(
	    @ConfigProperty( name = "workflow-userassignment.unassignUserResourceTypeTask.key" ) String key,
	    @ConfigProperty( name = "workflow-userassignment.unassignUserResourceTypeTask.titleI18nKey" ) String titleI18nKey,
	    @ConfigProperty( name = "workflow-userassignment.unassignUserResourceTypeTask.beanName" ) String beanName,
	    @ConfigProperty( name = "workflow-userassignment.unassignUserResourceTypeTask.configRequired", defaultValue = "false" ) boolean configRequired,
	    @ConfigProperty( name = "workflow-userassignment.unassignUserResourceTypeTask.formTaskRequired", defaultValue = "true" ) boolean formTaskRequired,
	    @ConfigProperty( name = "workflow-userassignment.unassignUserResourceTypeTask.taskForAutomaticAction", defaultValue = "false" ) boolean taskForAutomaticAction )
	{
	    return buildTaskType( key, titleI18nKey, beanName, null, configRequired, formTaskRequired, taskForAutomaticAction );
	}

	@Produces
	@ApplicationScoped
	@Named( "workflow-userassignment.taskTypeUserAssignmentNotification" )
	public ITaskType produceTaskTypeUserAssignmentNotification(
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUserAssignmentNotification.key" ) String key,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUserAssignmentNotification.titleI18nKey" ) String titleI18nKey,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUserAssignmentNotification.beanName" ) String beanName,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUserAssignmentNotification.configBeanName" ) String configBeanName,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUserAssignmentNotification.configRequired", defaultValue = "true" ) boolean configRequired,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUserAssignmentNotification.formTaskRequired", defaultValue = "false" ) boolean formTaskRequired,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUserAssignmentNotification.taskForAutomaticAction", defaultValue = "true" ) boolean taskForAutomaticAction )
	{
	    return buildTaskType( key, titleI18nKey, beanName, configBeanName, configRequired, formTaskRequired, taskForAutomaticAction );
	}

	@Produces
	@ApplicationScoped
	@Named( "workflow-userassignment.taskTypeUnassignUserResourceAutomatic" )
	public ITaskType produceTaskTypeUnassignUserResourceAutomatic(
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUnassignUserResourceAutomatic.key" ) String key,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUnassignUserResourceAutomatic.titleI18nKey" ) String titleI18nKey,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUnassignUserResourceAutomatic.beanName" ) String beanName,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUnassignUserResourceAutomatic.configRequired", defaultValue = "false" ) boolean configRequired,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUnassignUserResourceAutomatic.formTaskRequired", defaultValue = "false" ) boolean formTaskRequired,
	    @ConfigProperty( name = "workflow-userassignment.taskTypeUnassignUserResourceAutomatic.taskForAutomaticAction", defaultValue = "true" ) boolean taskForAutomaticAction )
	{
	    return buildTaskType( key, titleI18nKey, beanName, null, configRequired, formTaskRequired, taskForAutomaticAction );
	}

	private ITaskType buildTaskType( String strKey, String strTitleI18nKey, String strBeanName, String strConfigBeanName, 
			boolean bIsConfigRequired, boolean bIsFormTaskRequired, boolean bIsTaskForAutomaticAction )
    {
        TaskType taskType = new TaskType( );
        taskType.setKey( strKey );
        taskType.setTitleI18nKey( strTitleI18nKey );
        taskType.setBeanName( strBeanName );
        taskType.setConfigBeanName( strConfigBeanName );
        taskType.setConfigRequired( bIsConfigRequired );
        taskType.setFormTaskRequired( bIsFormTaskRequired );
        taskType.setTaskForAutomaticAction( bIsTaskForAutomaticAction );
        return taskType;
    }
}
