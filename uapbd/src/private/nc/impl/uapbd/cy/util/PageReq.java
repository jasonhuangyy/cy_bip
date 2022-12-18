package nc.impl.uapbd.cy.util;

import java.io.Serializable;

public class PageReq implements Serializable {
	private static final long serialVersionUID = 8759922858974451761L;
	public int page = 0;
	public int size = 100;

	public PageReq() {
	}

	public PageReq(int page, int size) {
		this.page = Math.max(page, 0);
		this.size = size;
	}
}
