package fr.paris.lutece.plugins.workflow.modules.userassignment.service.task;

import fr.paris.lutece.plugins.workflowcore.business.prerequisite.IPrerequisiteConfig;
import fr.paris.lutece.plugins.workflowcore.service.prerequisite.IAutomaticActionPrerequisiteService;

public class NoUserAssignedPrerequisite extends AbstractUserAssignedPrerequisite
implements IAutomaticActionPrerequisiteService {

	private static final String PREREQUISITE_TITLE_I18N = "module.workflow.userassignment.prerequisite.nouser.title";
	private static final String PREREQUISITE_TYPE = "module.workflow.userassignment.prerequisite.nouser";
	
	public String getTitleI18nKey( )
	{
		return PREREQUISITE_TITLE_I18N;
	}

	public String getPrerequisiteType( )
	{
		return PREREQUISITE_TYPE;
	}
	
	@Override
	public boolean canActionBePerformed(int nIdResource, String strResourceType, IPrerequisiteConfig config,
			int nIdAction) {
		return countUserAssignedToResource( nIdResource, strResourceType ) == 0;
	}
}
