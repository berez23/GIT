function isInteger(s) {
	var i;
	s = s.toString();
	for (i = 0; i < s.length; i++) {
		var c = s.charAt(i);
		if (isNaN(c)) {
			alert("Given value is not a number");
			return false;
		}
	}
	return true;
}
