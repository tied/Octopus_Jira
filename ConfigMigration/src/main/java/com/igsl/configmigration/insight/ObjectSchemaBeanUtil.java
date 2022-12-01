package com.igsl.configmigration.insight;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.atlassian.jira.component.ComponentAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igsl.configmigration.JiraConfigDTO;
import com.igsl.configmigration.JiraConfigUtil;
import com.igsl.configmigration.SessionData.ImportData;
import com.riadalabs.jira.plugins.insight.channel.external.api.facade.ObjectSchemaFacade;
import com.riadalabs.jira.plugins.insight.services.model.ObjectSchemaBean;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class ObjectSchemaBeanUtil extends JiraConfigUtil {

	private static final Logger LOGGER = Logger.getLogger(ObjectSchemaBeanUtil.class);
	private static ObjectSchemaFacade OBJECT_SCHEMA_FACADE;
			
	static {
		Logger logger = Logger.getLogger("com.igsl.configmigration.insight.InsightUtil");
		try {
			OBJECT_SCHEMA_FACADE = 
					(ObjectSchemaFacade) ComponentAccessor.getOSGiComponentInstanceOfType(
					ComponentAccessor.getPluginAccessor().getClassLoader()
					.loadClass("com.riadalabs.jira.plugins.insight.channel.external.api.facade.ObjectSchemaFacade"));
			logger.debug("OBJECT_FACADE: " + OBJECT_SCHEMA_FACADE);
		} catch (Exception ex) {
			logger.error("Failed to initialize InsightUtil", ex);
		}
	}
	
	@Override
	public String getName() {
		return "Insight Schema";
	}
	
	@Override
	public Map<String, JiraConfigDTO> findAll(Object... params) throws Exception {
		Map<String, JiraConfigDTO> result = new TreeMap<>();
		List<ObjectSchemaBean> objects = OBJECT_SCHEMA_FACADE.findObjectSchemaBeans();
		for (ObjectSchemaBean ob : objects) {
			ObjectSchemaBeanDTO item = new ObjectSchemaBeanDTO();
			item.setJiraObject(ob);
			result.put(item.getUniqueKey(), item);
		}
		return result;
	}

	@Override
	public JiraConfigDTO findByInternalId(String id, Object... params) throws Exception {
		List<ObjectSchemaBean> objects = OBJECT_SCHEMA_FACADE.findObjectSchemaBeans();
		Integer idAsInt = Integer.parseInt(id);
		for (ObjectSchemaBean ob : objects) {
			if (idAsInt.equals(ob.getId())) {
				ObjectSchemaBeanDTO dto = new ObjectSchemaBeanDTO();
				dto.setJiraObject(ob);
				return dto;
			}
		}
		return null;
	}

	@Override
	public JiraConfigDTO findByUniqueKey(String uniqueKey, Object... params) throws Exception {
		List<ObjectSchemaBean> objects = OBJECT_SCHEMA_FACADE.findObjectSchemaBeans();
		for (ObjectSchemaBean ob : objects) {
			if (uniqueKey.equals(ob.getName())) {
				ObjectSchemaBeanDTO dto = new ObjectSchemaBeanDTO();
				dto.setJiraObject(ob);
				return dto;
			}
		}
		return null;
	}

	public JiraConfigDTO merge(JiraConfigDTO oldItem, JiraConfigDTO newItem) throws Exception {
		ObjectSchemaBeanDTO original = null;
		if (oldItem != null) {
			original = (ObjectSchemaBeanDTO) oldItem;
		} else {
			original = (ObjectSchemaBeanDTO) findByDTO(newItem);
		}
		// Schema?
		return null;
	}
	
	@Override
	public Class<? extends JiraConfigDTO> getDTOClass() {
		return ObjectSchemaBeanDTO.class;
	}

	@Override
	public boolean isPublic() {
		// Referenced by other DTOs
		return false;
	}

}
