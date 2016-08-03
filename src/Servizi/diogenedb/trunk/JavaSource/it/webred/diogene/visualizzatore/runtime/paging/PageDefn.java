package it.webred.diogene.visualizzatore.runtime.paging;



public class PageDefn {
	public interface PageAction {
		public static final int FIRST = 0;
		public static final int NEXT = 1;
		public static final int PREVIOUS = 2;
		public static final int CURRENT = 3;
	}
	
	protected int pageSize;
	
	public PageDefn(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	


	
}
