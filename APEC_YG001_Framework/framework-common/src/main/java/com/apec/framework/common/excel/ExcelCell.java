package com.apec.framework.common.excel;

/**
 * @author xx
 */
public class ExcelCell {

	private Integer row;

	private Integer col;

	public ExcelCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return this.row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return this.col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.col;
		result = prime * result + this.row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		final ExcelCell other = (ExcelCell) obj;
		if (this.col.equals(other.col)){
			return false;
		}
		if (this.row.equals(other.row)){
			return false;
		}
		return true;
	}

}
