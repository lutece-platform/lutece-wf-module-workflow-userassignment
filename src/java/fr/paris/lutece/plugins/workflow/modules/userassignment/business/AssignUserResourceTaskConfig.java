package fr.paris.lutece.plugins.workflow.modules.userassignment.business;

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;

/**
 * Configuration form task AssignUserResourceTask
 *
 */
public class AssignUserResourceTaskConfig extends TaskConfig {

	private String providerName;

	/**
	 * @return the providerName
	 */
	public String getProviderName( )
	{
		return providerName;
	}

	/**
	 * @param providerName the providerName to set
	 */
	public void setProviderName( String providerName )
	{
		this.providerName = providerName;
	}
	
}
