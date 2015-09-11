// 檢查英數字
function validateNumberAndAlphabet(theVar) {
	var regExpression = /^[a-zA-Z0-9]*$/;
	return regExpression.test(theVar);
}

// 檢查IP Address
function validateIpAddress(theVar) {
	var regExpression = /^(\d|[01]?\d\d|2[0-4]\d|25[0-5])\.(\d|[01]?\d\d|2[0-4]\d|25[0-5])\.(\d|[01]?\d\d|2[0-4]\d|25[0-5])\.(\d|[01]?\d\d|2[0-4]\d|25[0-5])$/;
	return regExpression.test(theVar);
}

// 檢查數字
function validateNunber(theVar) {
	var regExpression = /^[0-9]*$/;
	return regExpression.test(theVar);
}

// 檢查電話 (數字須出現一次以上, " ( - ) " 可以任意出現)
function validateTel(theVar) {
	// var regExpression = /^[0-9\(\)-]*[0-9]+[0-9\(\)-]*$/;
	var regExpression = /^[0-9\(\)-]*[0-9]+[0-9\(\)-]*[\#0-9]*$/;
	return regExpression.test(theVar);
}

// 檢查email
function validateEmail(theVar) {
	var regExpression = /^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4})*$/;
	return regExpression.test(theVar);
}

// 去除頭尾空白
function trim(theVar) {
	return theVar.replace(/(^\s*)|(\s*$)/g, "");
}

// 取檔案副檔名
function file_subName(fileName) {
	var fileName_array = fileName.split(".");
	return fileName_array[1];
}

// 日期檢查 yyyy/MM/dd
function validateSlashDate(theVar) {
	var regExpression = /^\d{1,4}[\/](0[1-9]|1[012])[\/](0[1-9]|[12][0-9]|3[01])$/;
	return regExpression.test(theVar);
}

//檢查 ISSN
function isValidISSN(issn) {
	var patt = /\d\d\d\d\-?\d\d\d+[0-9Xx]/;
	if (!patt.test(issn.trim())) {
		return false;
	}

	issn = issn.replace("-", "");
	if (issn.length != 8) {
		return false;
	}

	var chars = issn.split('');
	if (chars[7].toUpperCase() == 'X') {
		chars[7] = 10;
	}

	var sum = 0;
	for (var i = 0; i < chars.length; i++) {
		sum += ((8 - i) * parseInt(chars[i]));
	}

	return ((sum % 11) == 0);
}

//檢查 ISBN
function isValidISBN(isbn) {
	var patt1 = /^(978|979)\-{1}\d{1}\-{1}\d{2}\-{1}\d{6}\-{1}\d{1}$/;
	var patt2 = /^(978|979)\d{10}$/;
	if (!patt1.test(isbn.trim()) && !patt2.test(isbn.trim())) {
		return false;
	}

	isbn = isbn.replace(/[^\d]/g, "");
	var chars = isbn.split('');

	var sum = 0;
	for (var i = 0; i < chars.length - 1; i++) {
		if (i % 2 == 0) {
			sum = sum + parseInt(chars[i]) * 1;
		} else {
			sum = sum + parseInt(chars[i]) * 3;
		}
	}

	var remainder = sum % 10;
	var num = 10 - remainder;

	if (num == 10) {
		if (parseInt(chars[12]) != 0) {
			return false;
		}
	} else {
		if (parseInt(chars[12]) != num) {
			return false;
		}
	}

	return true;
}