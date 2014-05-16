package model;

enum CheckerColor {
	WHITE,
	BLACK;
	
	static boolean isWhite(final CheckerColor color) {
		if(color.equals(CheckerColor.WHITE)) {
			return true;
		} else {
			return false;
		}
	}
	
	static boolean isBlack(final CheckerColor color) {
		if(color.equals(CheckerColor.BLACK)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isWhite() {
		return CheckerColor.isWhite(this);
	}
	
	public boolean isBlack() {
		return CheckerColor.isBlack(this);
	}
}
