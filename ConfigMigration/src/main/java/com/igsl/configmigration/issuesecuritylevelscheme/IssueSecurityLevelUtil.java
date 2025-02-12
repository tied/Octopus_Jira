package com.igsl.configmigration.issuesecuritylevelscheme;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.security.IssueSecurityLevel;
import com.atlassian.jira.issue.security.IssueSecurityLevelImpl;
import com.atlassian.jira.issue.security.IssueSecurityLevelManager;
import com.atlassian.jira.issue.security.IssueSecuritySchemeManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igsl.configmigration.JiraConfigDTO;
import com.igsl.configmigration.JiraConfigUtil;
import com.igsl.configmigration.SessionData.ImportData;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class IssueSecurityLevelUtil extends JiraConfigUtil {

	private static final Logger LOGGER = Logger.getLogger(IssueSecurityLevelUtil.class);
	private static final IssueSecurityLevelManager LEVEL_MANAGER = 
			ComponentAccessor.getIssueSecurityLevelManager();
	private static final IssueSecuritySchemeManager SCHEME_MANAGER = 
			ComponentAccessor.getComponent(IssueSecuritySchemeManager.class);
	
	@Override
	public String getName() {
		return "Issue Security Level";
	}
	
	@Override
	public Map<String, JiraConfigDTO> findAll(Object... params) throws Exception {
		Map<String, JiraConfigDTO> result = new TreeMap<>();
		for (IssueSecurityLevel s : LEVEL_MANAGER.getAllIssueSecurityLevels()) {
			IssueSecurityLevelDTO item = new IssueSecurityLevelDTO();
			item.setJiraObject(s);
			result.put(item.getUniqueKey(), item);
		}
		return result;
	}

	@Override
	public JiraConfigDTO findByInternalId(String id, Object... params) throws Exception {
		Long idAsLong = Long.parseLong(id);
		IssueSecurityLevel s = LEVEL_MANAGER.getSecurityLevel(idAsLong);
		if (s != null) {
			IssueSecurityLevelDTO item = new IssueSecurityLevelDTO();
			item.setJiraObject(s);
			return item;
		}
		return null;
	}

	@Override
	public JiraConfigDTO findByUniqueKey(String uniqueKey, Object... params) throws Exception {
		for (IssueSecurityLevel s : LEVEL_MANAGER.getAllIssueSecurityLevels()) {
			if (s.getName().equals(uniqueKey)) {
	 			IssueSecurityLevelDTO item = new IssueSecurityLevelDTO();
				item.setJiraObject(s);
				return item;
			}
		}
		return null;
	}

	public JiraConfigDTO merge(JiraConfigDTO oldItem, JiraConfigDTO newItem) throws Exception {
		IssueSecurityLevelDTO original = null;
		if (oldItem != null) {
			original = (IssueSecurityLevelDTO) oldItem;
		} else {
			original = (IssueSecurityLevelDTO) findByDTO(newItem);
		}
		IssueSecurityLevelDTO src = (IssueSecurityLevelDTO) newItem;
		if (original != null) {
			// Update
			IssueSecurityLevelImpl item = new IssueSecurityLevelImpl(
					original.getId(), src.getName(), src.getDescription(), src.getSchemeId());
			LEVEL_MANAGER.updateIssueSecurityLevel(item);
			return findByInternalId(Long.toString(original.getId()));
		} else {
			// Create
			IssueSecurityLevel createdJira = LEVEL_MANAGER.createIssueSecurityLevel(
					src.getSchemeId(), src.getName(), src.getDescription());
			IssueSecurityLevelDTO created = new IssueSecurityLevelDTO();
			created.setJiraObject(createdJira);
			return created;
		}
	}

	@Override
	public Class<? extends JiraConfigDTO> getDTOClass() {
		return IssueSecurityLevelDTO.class;
	}

	@Override
	public boolean isPublic() {
		// Referenced by IssueSecurityLevelSchemeDTO only
		return false;
	}

}
