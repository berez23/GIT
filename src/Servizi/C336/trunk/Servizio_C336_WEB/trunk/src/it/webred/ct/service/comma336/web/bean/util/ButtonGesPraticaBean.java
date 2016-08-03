package it.webred.ct.service.comma336.web.bean.util;

public class ButtonGesPraticaBean {
	private Boolean visible = new Boolean(false);
	private Boolean enabled = new Boolean(false);
	
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public void attiva(boolean isAttivo) {
		this.visible = isAttivo;
		this.enabled = isAttivo;
	}
	
}
