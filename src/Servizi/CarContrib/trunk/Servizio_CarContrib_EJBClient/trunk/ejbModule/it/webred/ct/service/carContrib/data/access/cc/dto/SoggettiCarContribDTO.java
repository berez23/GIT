package it.webred.ct.service.carContrib.data.access.cc.dto;

import it.webred.ct.service.carContrib.data.model.SoggettiCarContrib;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class SoggettiCarContribDTO extends CeTBaseObject implements Serializable{
		
		private static final long serialVersionUID = 1L;
		private SoggettiCarContrib sogg;
		
		public SoggettiCarContribDTO() {
			super();
		}
		
		public SoggettiCarContribDTO(SoggettiCarContrib sogg) {
			this.sogg=sogg;
		}
		
		public SoggettiCarContrib getSogg() {
			return sogg;
		}
		public void setSogg(SoggettiCarContrib sogg) {
			this.sogg = sogg;
		}
		

}
