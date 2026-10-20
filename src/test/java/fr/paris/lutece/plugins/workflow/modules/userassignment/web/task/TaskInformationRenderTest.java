/*
 * Copyright (c) 2002-2026, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Renders, with HTML auto-escaping (LUT-33153), the task informations and the task forms of workflow-userassignment from
 * the model the components build. plugin-workflow writes the task informations and forms as HTML (HtmlMarkup): the user
 * names and the email address they hold must be escaped by the module's templates, never concatenated in Java. Also
 * renders a notification message as TaskUserAssignmentNotification does: the HTML of the stored message is kept, the
 * marker values are escaped. The core macros the templates call are replaced by stand-ins that write their arguments
 * and their nested content: only the FreeMarker engine is needed.
 */
public class TaskInformationRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_DIR = "admin/plugins/workflow/modules/userassignment/";
    private static final String TEMPLATE_ASSIGN_INFORMATION = TEMPLATE_DIR + "task_user_assignment_information.html";
    private static final String TEMPLATE_UNASSIGN_INFORMATION = TEMPLATE_DIR + "task_user_unassignment_information.html";
    private static final String TEMPLATE_NOTIFICATION_INFORMATION = TEMPLATE_DIR + "task_user_assignment_notification_information.html";
    private static final String TEMPLATE_ASSIGN_FORM = TEMPLATE_DIR + "task_user_assigment_form.html";
    private static final String TEMPLATE_UNASSIGN_FORM = TEMPLATE_DIR + "task_user_unassigment_form.html";

    /** Stand-ins of the core macros the templates call: they write their arguments (escaped) and their nested content. */
    private static final String MACROS = "macros.ftl";
    private static final String MACROS_SOURCE = "<#macro formGroup rest...><div class=\"form-group\"><#nested></div></#macro>"
            + "<#macro staticText rest...><p><#nested></p></#macro>" + "<#macro p rest...><p><#nested></p></#macro>"
            + "<#macro input value='' rest...><input value=\"${value}\"></#macro>" + "<#macro select rest...><select><#nested></select></#macro>"
            + "<#macro option value='' label='' rest...><option value=\"${value}\">${label}</option></#macro>" + "<#macro icon rest...></#macro>";

    private static final String FIRST_NAME = "Jo <b>x</b>";
    private static final String LAST_NAME = "Doe \"q\" <script>alert(1)</script>";
    private static final String FIRST_NAME_ESCAPED = "Jo &lt;b&gt;x&lt;/b&gt;";
    private static final String LAST_NAME_ESCAPED = "Doe &quot;q&quot; &lt;script&gt;alert(1)&lt;/script&gt;";
    private static final String EMAIL = "jo<img src=x>@example.org";
    private static final String EMAIL_ESCAPED = "jo&lt;img src=x&gt;@example.org";

    /**
     * Builds the configuration of the core: HTML output format with auto-escaping.
     *
     * @return the configuration
     * @throws IOException
     *             if the templates directory cannot be read
     */
    private static Configuration configuration( ) throws IOException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                macros, new FileTemplateLoader( new File( TEMPLATES_ROOT ) )
        } ) );
        cfg.addAutoInclude( MACROS );

        return cfg;
    }

    /**
     * Renders a template of the module.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the HTML
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringWriter writer = new StringWriter( );
        configuration( ).getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * @return an admin user whose names hold markup
     */
    private static AdminUser hostileUser( )
    {
        AdminUser user = new AdminUser( );
        user.setUserId( 12 );
        user.setFirstName( FIRST_NAME );
        user.setLastName( LAST_NAME );

        return user;
    }

    /**
     * The assignment and unassignment informations escape the names of the user.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testAssignmentInformationsEscapeTheUserName( ) throws Exception
    {
        for ( String strTemplate : List.of( TEMPLATE_ASSIGN_INFORMATION, TEMPLATE_UNASSIGN_INFORMATION ) )
        {
            String strHtml = render( strTemplate, Map.of( "user", hostileUser( ) ) );

            assertTrue( strHtml.contains( LAST_NAME_ESCAPED + " " + FIRST_NAME_ESCAPED ), strHtml );
            assertTrue( strHtml.contains( "<strong>" ), strHtml );
            assertFalse( strHtml.contains( "<script>" ), strHtml );
            assertFalse( strHtml.contains( "<b>" ), strHtml );
        }
    }

    /**
     * The notification information, rendered from a template since LUT-33153, escapes the names and the email address,
     * and keeps the text the Java code used to build.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testNotificationInformationEscapesTheUserAndTheEmail( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "user", hostileUser( ) );
        model.put( "information", EMAIL );

        String strHtml = render( TEMPLATE_NOTIFICATION_INFORMATION, model );

        assertEquals( "#i18n{module.workflow.userassignment.task_user_assignment_notification.history.information} " + FIRST_NAME_ESCAPED + " "
                + LAST_NAME_ESCAPED + " " + EMAIL_ESCAPED, strHtml );
    }

    /**
     * A user deleted since the notification is shown as the unknown user.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testNotificationInformationOfAnUnknownUser( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "user", null );
        model.put( "information", "jo@example.org" );

        String strHtml = render( TEMPLATE_NOTIFICATION_INFORMATION, model );

        assertEquals( "#i18n{module.workflow.userassignment.task_user_assignment_notification.history.information} "
                + "#i18n{module.workflow.userassignment.task_user_assignment_notification.history.information.user_unknown} jo@example.org", strHtml );
    }

    /**
     * The assignment and unassignment forms escape the user names, with one user (text) and with several (options).
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testTaskFormsEscapeTheUserNames( ) throws Exception
    {
        AdminUser user = hostileUser( );
        for ( String strTemplate : List.of( TEMPLATE_ASSIGN_FORM, TEMPLATE_UNASSIGN_FORM ) )
        {
            for ( List<AdminUser> listUsers : List.of( List.of( user ), List.of( user, user ) ) )
            {
                String strHtml = render( strTemplate, Map.of( "list_user_selection", listUsers ) );

                assertTrue( strHtml.contains( FIRST_NAME_ESCAPED + " " + LAST_NAME_ESCAPED ), strHtml );
                assertTrue( strHtml.contains( "value=\"12\"" ), strHtml );
                assertFalse( strHtml.contains( "<script>" ), strHtml );
                assertFalse( strHtml.contains( "<b>" ), strHtml );
            }
        }
    }

    /**
     * A notification message, a template stored by an administrator, keeps its HTML and escapes the marker values, as
     * TaskUserAssignmentNotification renders it with AppTemplateService.getTemplateFromStringFtl.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testNotificationMessageEscapesTheMarkers( ) throws Exception
    {
        Template template = new Template( "message", "<p>Hello <strong>${agent_name}</strong>, resource ${resourceId}</p>", configuration( ) );
        StringWriter writer = new StringWriter( );
        template.process( Map.of( "agent_name", FIRST_NAME, "resourceId", "42" ), writer );

        assertEquals( "<p>Hello <strong>" + FIRST_NAME_ESCAPED + "</strong>, resource 42</p>", writer.toString( ) );
    }
}
