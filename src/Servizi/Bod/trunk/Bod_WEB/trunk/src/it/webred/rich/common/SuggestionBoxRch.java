package it.webred.rich.common;

import java.util.ArrayList;

import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

public class SuggestionBoxRch {
	
    private ArrayList<String> itemsNames = new ArrayList<String>();
    private List<SelectItem> itemsOptions = new ArrayList<SelectItem>();
    private String item = ""; 

    private String property = "";
	private String property2 = "";
		
	private SelectItem[] comboSelectItems = { new SelectItem("Test", "Test"), new SelectItem("Test2", "Test2"), new SelectItem("Test3", "Test3") };

	public SuggestionBoxRch() {
		// TODO Auto-generated constructor stub
	}//-------------------------------------------------------------------------

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

	public SelectItem[] getComboSelectItems() {
		return comboSelectItems;
	}

	public void setComboSelectItems(SelectItem[] comboSelectItems) {
		this.comboSelectItems = comboSelectItems;
	}

	public void setItemsNames(ArrayList<String> itemsNames) {
		this.itemsNames = itemsNames;
	}

	public void setItemsOptions(List<SelectItem> itemsOptions) {
		this.itemsOptions = itemsOptions;
	}

    public String getItem() {
        return item;
    }

    public void setItem(String i) {
        this.item = i;
    }

    public List<SelectItem> getItemsOptions() {
        return itemsOptions;
    }

    public ArrayList<String> getItemsNames() {
        return itemsNames;
    }
    
    public void selectionChanged(ValueChangeEvent evt) {
		 String selectedValue = (String) evt.getNewValue();

		 if (selectedValue.equals("")) {
			 this.setProperty2("No Selected");
		 } else {
			 this.setProperty2(selectedValue);
		 }
	}
	
}
