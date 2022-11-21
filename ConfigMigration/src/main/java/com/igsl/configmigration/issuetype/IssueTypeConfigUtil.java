package com.igsl.configmigration.issuetype;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.atlassian.jira.avatar.Avatar;
import com.atlassian.jira.avatar.AvatarManager;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.IssueTypeManager;
import com.atlassian.jira.icon.IconOwningObjectId;
import com.atlassian.jira.icon.IconType;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igsl.configmigration.JiraConfigItem;
import com.igsl.configmigration.JiraConfigUtil;
import com.igsl.configmigration.SessionData.ImportData;
import com.igsl.configmigration.avatar.AvatarConfigUtil;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class IssueTypeConfigUtil extends JiraConfigUtil {

	private static final Logger LOGGER = Logger.getLogger(IssueTypeConfigUtil.class);
	private static IssueTypeManager ISSUE_MANAGER = ComponentAccessor.getComponent(IssueTypeManager.class);
	
	@Override
	public String getName() {
		return "Issue Type";
	}
	
	@Override
	public TypeReference<?> getTypeReference() {
		return new TypeReference<Map<String, IssueTypeConfigItem>>() {};
	}
	
	@Override
	public Map<String, JiraConfigItem> readAllItems(Object... params) throws Exception {
		Map<String, JiraConfigItem> result = new HashMap<>();
		for (IssueType it : ISSUE_MANAGER.getIssueTypes()) {
			IssueTypeConfigItem item = new IssueTypeConfigItem();
			item.setJiraObject(it);
			result.put(item.getUniqueKey(), item);
		}
		return result;
	}

	/**
	 * params[0]: name
	 */
	@Override
	public Object findObject(Object... params) throws Exception {
		String identifier = (String) params[0];
		for (IssueType it : ISSUE_MANAGER.getIssueTypes()) {
			if (it.getName().equals(identifier)) {
				return it;
			}
		}
		return null;
	}
	
	public Object merge(JiraConfigItem oldItem, JiraConfigItem newItem) throws Exception {
		IssueType original = null;
		if (oldItem != null) {
			if (oldItem.getJiraObject() != null) {
				original = (IssueType) oldItem.getJiraObject();
			} else {
				original = (IssueType) findObject(oldItem.getUniqueKey());
			}
		} else {
			original = (IssueType) findObject(newItem.getUniqueKey());
		}
		IssueTypeConfigItem src = (IssueTypeConfigItem) newItem;
		// Avatar
		AvatarConfigUtil avatarUtil = new AvatarConfigUtil();
		Avatar av = (Avatar) avatarUtil.merge(null, src.getAvatarConfigItem());
		if (original != null) {
			// Update
			ISSUE_MANAGER.updateIssueType(original, src.getName(), src.getDescription(), av.getId());
			return original;
		} else {
			// Create
			return ISSUE_MANAGER.createIssueType(src.getName(), src.getDescription(), av.getId());
		}
	}
	
	@Override
	public void merge(Map<String, ImportData> items) throws Exception {
		for (ImportData data : items.values()) {
			try {
				merge(data.getServer(), data.getData());
				data.setImportResult("Updated");
			} catch (Exception ex) {
				data.setImportResult(ex.getClass().getCanonicalName() + ": " + ex.getMessage());
				throw ex;
			}
		}
	}

}
