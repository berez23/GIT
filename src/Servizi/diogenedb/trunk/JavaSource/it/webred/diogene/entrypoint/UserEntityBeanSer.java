package it.webred.diogene.entrypoint;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class UserEntityBeanSer  implements Serializable {
	
	public static final long serialVersionUID = -1;
	
	private byte[] userEntityBean;

	public byte[] getUserEntityBean() {
		return userEntityBean;
	}

	public void setUserEntityBean(byte[] userEntityBean) {
		this.userEntityBean = userEntityBean;
	}




}
