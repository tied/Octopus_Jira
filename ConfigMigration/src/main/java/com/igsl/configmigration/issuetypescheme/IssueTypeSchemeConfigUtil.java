package com.igsl.configmigration.issuetypescheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.config.FieldConfigScheme;
import com.atlassian.jira.issue.fields.config.manager.IssueTypeSchemeManager;
import com.atlassian.jira.issue.fields.option.OptionSet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igsl.configmigration.JiraConfigItem;
import com.igsl.configmigration.JiraConfigUtil;
import com.igsl.configmigration.SessionData.ImportData;
import com.igsl.configmigration.optionset.OptionSetConfigUtil;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class IssueTypeSchemeConfigUtil extends JiraConfigUtil {

	private static final Logger LOGGER = Logger.getLogger(IssueTypeSchemeConfigUtil.class);
	private static IssueTypeSchemeManager MANAGER = ComponentAccessor.getComponent(IssueTypeSchemeManager.class);
	private static OptionSetConfigUtil OPTION_SET_UTIL = new OptionSetConfigUtil();
	
	@Override
	public String getName() {
		return "Issue Type Scheme";
	}
	
	@Override
	public TypeReference<?> getTypeReference() {
		return new TypeReference<Map<String, IssueTypeSchemeConfigItem>>() {};
	}
	
	@Override
	public Map<String, JiraConfigItem> readAllItems(Object... params) throws Exception {
		Map<String, JiraConfigItem> result = new TreeMap<>();
		for (FieldConfigScheme scheme : MANAGER.getAllSchemes()) {
			IssueTypeSchemeConfigItem item = new IssueTypeSchemeConfigItem();
			item.setJiraObject(scheme);
			result.put(item.getUniqueKey(), item);
		}
		return result;
	}

	/**
	 * params[0]: identifier
	 */
	@Override
	public Object findObject(Object... params) throws Exception {
		assert params.length == 1 && String.class.isAssignableFrom(params[0].getClass());
		String identifier = (String) params[0];
		for (FieldConfigScheme scheme : MANAGER.getAllSchemes()) {
			if (scheme.getName().equals(identifier)) {
				return scheme;
			}
		}
		return null;
	}
	
	@Override
	public Object merge(JiraConfigItem oldItem, JiraConfigItem newItem) throws Exception {
		IssueTypeSchemeConfigItem original = null;
		if (oldItem != null) {
			if (oldItem.getJiraObject() != null) {
				original = (IssueTypeSchemeConfigItem) oldItem.getJiraObject();
			} else {
				original = (IssueTypeSchemeConfigItem) findObject(oldItem.getUniqueKey());
			}
		} else {
			original = (IssueTypeSchemeConfigItem) findObject(newItem.getUniqueKey());
		}
		IssueTypeSchemeConfigItem src = (IssueTypeSchemeConfigItem) newItem;
		if (original != null) {
			OptionSet optionSet = (OptionSet) OPTION_SET_UTIL.findObject(original.getFieldConfig());
			FieldConfigScheme.Builder b = new FieldConfigScheme.Builder((FieldConfigScheme) src.getJiraObject());
			MANAGER.update(b.toFieldConfigScheme(), optionSet.getOptionIds());
			return null;
		} else {
			OptionSet optionSet = (OptionSet) OPTION_SET_UTIL.findObject(src.getFieldConfig());
			List<String> optionIds = new ArrayList<>();
			optionIds.addAll(optionSet.getOptionIds());
			return MANAGER.create(src.getName(), src.getDescription(), optionIds);
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
