package it.webred.ct.service.tsSoggiorno.web.dto;

public class TreeItem {

	private String id;
	private String value;
	private String imgurl = "/images/tab_active.png";
	private String tooltip;

	public TreeItem(String id, String value, String tooltip, String imgurl) {
		this.id = id;
		this.value = value;
		this.tooltip = tooltip;
		this.imgurl = imgurl;
	}

	public TreeItem() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
}
