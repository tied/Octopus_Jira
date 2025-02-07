package com.igsl.configmigration.optionset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.option.Option;
import com.atlassian.jira.issue.fields.option.OptionSet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igsl.configmigration.JiraConfigDTO;
import com.igsl.configmigration.JiraConfigUtil;
import com.igsl.configmigration.fieldconfig.FieldConfigDTO;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class OptionSetDTO extends JiraConfigDTO {

	private List<String> optionIds;
	private List<OptionDTO> options;

	@JsonIgnore
	private FieldConfigDTO fieldConfig;
	
	/**
	 * #0: FieldConfigDTO
	 */
	@Override
	public void fromJiraObject(Object o, Object... params) throws Exception {
		this.fieldConfig = (FieldConfigDTO) params[0];
		OptionSet obj = (OptionSet) o;
		this.optionIds = new ArrayList<String>();
		for (String s : obj.getOptionIds()) {
			this.optionIds.add(s);
		}
		this.options = new ArrayList<>();
		for (Option opt : obj.getOptions()) {
			OptionDTO item = new OptionDTO();
			item.setJiraObject(opt);
			this.options.add(item);
		}
		Collections.sort(this.optionIds);
	}
		
	@Override
	public String getUniqueKey() {
		return optionIds.toString();
	}

	@Override
	public String getInternalId() {
		return optionIds.toString();
	}

	public List<String> getOptionIds() {
		return optionIds;
	}

	public void setOptionIds(List<String> optionIds) {
		this.optionIds = optionIds;
	}

	public FieldConfigDTO getFieldConfig() {
		return fieldConfig;
	}

	public void setFieldConfig(FieldConfigDTO fieldConfig) {
		this.fieldConfig = fieldConfig;
	}

	@Override
	protected List<String> getCompareMethods() {
		return Arrays.asList(
				"getOptionIds",
				"getOptions",
				"getFieldConfig");
	}

	@Override
	public Class<? extends JiraConfigUtil> getUtilClass() {
		return OptionSetUtil.class;
	}

	@Override
	public Class<?> getJiraClass() {
		return OptionSet.class;
	}

}
