package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool;

public  class SaveNode {
	private int dbIndex;
	private int tableIndex;

	public SaveNode(int dbIndex, int tableIndex) {
		this.setDbIndex(dbIndex);
		this.setTableIndex(tableIndex);
	}

	public int getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(int dbIndex) {
		this.dbIndex = dbIndex;
	}

	public int getTableIndex() {
		return tableIndex;
	}

	public void setTableIndex(int tableIndex) {
		this.tableIndex = tableIndex;
	}
}