package com.igsl.configmigration;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igsl.configmigration.SessionData.ImportData;

@JsonDeserialize(using=JiraConfigUtilDeserializer.class)
@JsonIgnoreProperties(value={"implementation"}, allowGetters=true)
public abstract class JiraConfigUtil {
	
	protected static final ObjectMapper OM = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	/**
	 * Return true if this JiraConfigUtil is to be included in user interface.
	 * Return false if the associated JiraConfigDTO is only referenced via other JiraConfigDTOs.
	 */
	@JsonIgnore
	public abstract boolean isPublic();
	
	/**
	 * Return implementation name. This is used to identify the JiraConfigUtil to be used.
	 * @return String
	 */
	public final String getImplementation() {
		return this.getClass().getCanonicalName();
	}
	
	/**
	 * Get display name of this JiraConfigUtil.
	 * @return
	 */
	@JsonIgnore
	public abstract String getName();
	
	/**
	 * Return associated DTO class.
	 */
	@JsonIgnore
	public abstract Class<? extends JiraConfigDTO> getDTOClass();
	
	/**
	 * Read all Jira objects in current environment and store them into JiraConfigItem.
	 * @param params Parameters. For most implementations where you can load all items, this is not used. 
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, JiraConfigDTO> readAllItems(Object... params) throws Exception;
	
	/**
	 * Find Jira object
	 * @param params Parameters. For most implementations, #0 is identifier.
	 * @return Object
	 * @throws Exception
	 */
	public abstract Object findObject(Object... params) throws Exception;
	
	/**
	 * Merge items. 
	 * @param oldItem Existing item, can be null.
	 * @param newItem New item.
	 * @return Underlying Jira object.
	 * @throws Exception
	 */
	public abstract Object merge(JiraConfigDTO oldItem, JiraConfigDTO newItem) throws Exception;
	
	/**
	 * Merge items. Result will be stored in ImportData.
	 * @param items
	 * @throws Exception
	 */
	public abstract void merge(Map<String, ImportData> items) throws Exception;
	
}
