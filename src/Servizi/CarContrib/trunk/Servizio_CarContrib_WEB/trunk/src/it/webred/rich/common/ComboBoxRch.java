package it.webred.rich.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

public class ComboBoxRch {
	
	//String suggestions = "Alabama,Alaska,Arizona,Arkansas,California,Colorado,Connecticut,Delaware,Florida,Massachusetts,Michigan,Georgia,Hawaii,Idaho,Indiana,Iowa,Kansas,Kentucky,Louisiana,Maine,Minnesota,Mississippi,Missouri,Montana,Nebraska";
	String suggestions = "";
	List selectItems = new ArrayList();
	private boolean disabled = false;
	private boolean enableManualInput = true;
	private boolean selectFirstOnUpdate = true;
	private boolean filterNewValues = true;
	private boolean directInputSuggestions = true;
	private String defaultMessage = "edit ..."; 
	private String width;
	private String listWidth;
	private String listHeight;
	private String inputSize;
	private String onchangeScript;
	private String onlistcallScript;
	private String onitemselectedScript;
	
	private String state = "No Selected";
	
    private String property = "";
	private String property2 = "";
	
	public ComboBoxRch() {
		/*
		selectItems.add(new SelectItem("District of Columbia"));
		selectItems.add(new SelectItem("Illinois"));
		selectItems.add(new SelectItem("Maryland"));
		selectItems.add(new SelectItem("Nevada"));
		selectItems.add(new SelectItem("New Hampshire"));
		selectItems.add(new SelectItem("New Jersey"));
		*/
	}
	
	public void selectionChanged(ValueChangeEvent evt) {
		 String selectedValue = (String) evt.getNewValue();

		 if (selectedValue.equals("")) {
			 state = "No Selected";
		 } else {
			 state = selectedValue;
		 }
	}
	 
	 public String getState() {
			return state;
 	 }

	 public void setState(String state) {
		this.state = state;
	 }
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getProperty2() {
		return property2;
	}

	public void setProperty2(String property2) {
		this.property2 = property2;
	}

	public List getSuggestions() {
		List result = Arrays.asList(suggestions.split(","));
		/*
		 * Per mescolare gli elementi: Collections.shuffle(result);
		 */
		return result;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}//-------------------------------------------------------------------------

	public List getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(List selectItems) {
		this.selectItems = selectItems;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isEnableManualInput() {
		return enableManualInput;
	}

	public void setEnableManualInput(boolean enableManualInput) {
		this.enableManualInput = enableManualInput;
	}

	public boolean isSelectFirstOnUpdate() {
		return selectFirstOnUpdate;
	}

	public void setSelectFirstOnUpdate(boolean selectFirstOnUpdate) {
		this.selectFirstOnUpdate = selectFirstOnUpdate;
	}

	public boolean isFilterNewValues() {
		return filterNewValues;
	}

	public void setFilterNewValues(boolean filterNewValues) {
		this.filterNewValues = filterNewValues;
	}

	public boolean isDirectInputSuggestions() {
		return directInputSuggestions;
	}

	public void setDirectInputSuggestions(boolean directInputSuggestions) {
		this.directInputSuggestions = directInputSuggestions;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getListWidth() {
		return listWidth;
	}

	public void setListWidth(String listWidth) {
		this.listWidth = listWidth;
	}

	public String getListHeight() {
		return listHeight;
	}

	public void setListHeight(String listHeight) {
		this.listHeight = listHeight;
	}

	public String getInputSize() {
		return inputSize;
	}

	public void setInputSize(String inputSize) {
		this.inputSize = inputSize;
	}

	public String getOnchangeScript() {
		return onchangeScript;
	}

	public void setOnchangeScript(String onchangeScript) {
		this.onchangeScript = onchangeScript;
	}

	public String getOnlistcallScript() {
		return onlistcallScript;
	}

	public void setOnlistcallScript(String onlistcallScript) {
		this.onlistcallScript = onlistcallScript;
	}

	public String getOnitemselectedScript() {
		return onitemselectedScript;
	}

	public void setOnitemselectedScript(String onitemselectedScript) {
		this.onitemselectedScript = onitemselectedScript;
	}
	
}