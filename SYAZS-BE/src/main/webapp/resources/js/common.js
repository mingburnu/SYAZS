$(document).ready(function() {
	formSetCSS();
	state_hover();
	menu_hover();

	showMenuItems('1');

});
// css
function formSetCSS() {
	$('input[type="text"]').addClass("input_text");
	$('input[type="password"]').addClass("input_text");
	$('input[type="file"]').addClass("input_file");
}
// state
function state_hover() {
	$(".state-default").hover(function() {
		$(this).addClass("state-hover");
	}, function() {
		$(this).removeClass("state-hover");
	});
}
// menu
function menu_hover() {
	$(".menu-titles").hover(function() {
		$(this).addClass("menu-titles-hover");
	}, function() {
		$(this).removeClass("menu-titles-hover");
	});
	//
	$(".menu-items").hover(function() {
		$(this).addClass("menu-items-hover");
	}, function() {
		$(this).removeClass("menu-items-hover");
	});
}
//
// 打開主要畫面之函式
function goURL(argURL) {
	showLoading();
	$.ajax({
		url : argURL,
		async : true,
		cache : false,
		error : function(msq) {
			$("#div-contain").html('<div class="message">連結失敗!</div>');
			goAlert("結果", "連結失敗.");
			closeLoading();
		},
		success : function(msg) {
			$("#div-contain").html(msg);
			closeLoading();
		}
	});
}
// 打開Detail畫面之函式
function goDetail(argURL, argTitle, argData) {
	$("#div_Detail .content > .header > .title").html(argTitle);
	showLoading();
	$.ajax({
		type : "POST",
		url : argURL,
		data : argData,
		async : true,
		cache : false,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			var response = XMLHttpRequest.responseText;
			var text = response.replace("<style>", "").replace("</style>","");
			$("div#div_Detail").hide();
			goAlert("結果", text);
			closeLoading();
		},
		success : function(msg) {
			$("#div_Detail").show();
			UI_Resize();
			$(window).scrollTop(0);
			$("#div_Detail .content > .contain").empty().html(msg);
			closeLoading();
		}
	});
}

function goDetail_import(argURL, argTitle, argData, argFormId) {
	$("#div_Detail .content > .header > .title").html(argTitle);
	showLoading();
	$.post(argURL, argData, function() {
		$("form#" + argFormId).submit();
	});

	$.post({
		type : "POST",
		url : argURL,
		data : argData,
		async : true,
		cache : false,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			var response = XMLHttpRequest.responseText;
			var text = response.replace("<style>", "").replace("</style>","");
			$("div#div_Detail").hide();
			goAlert("結果", text);
			closeLoading();
		},
		success : function(msg) {
			$("#div_Detail").show();
			UI_Resize();
			$(window).scrollTop(0);
			$("#div_Detail .content > .contain").empty().html(msg);
			closeLoading();
		}
	});
}
// 打開Detail畫面之函式
function goDetailNloading(argURL, argTitle, argData) {
	$("#div_Detail .content > .header > .title").html(argTitle);
	$.ajax({
		type : "POST",
		url : argURL,
		data : argData,
		async : true,
		cache : false,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			// $("#div_Detail .content > .contain").html('<div
			// class="message">連結失敗!</div>');
			goAlert("結果", XMLHttpRequest.responseText);
			closeLoading();
		},
		success : function(msg) {
			$("#div_Detail").show();
			UI_Resize();
			$(window).scrollTop(0);
			$("#div_Detail .content > .contain").empty().html(msg);
		}
	});
}
// 關閉Detail畫面之函式
function closeDetail() {
	$("#div_Detail").hide();
	UI_Resize();
}
// 打開Detail_2畫面之函式
function goDetail_2(argURL, argTitle, argData) {
	$("#div_Detail_2 .content > .header > .title").html(argTitle);
	showLoading();
	$.ajax({
		type : "POST",
		url : argURL,
		data : argData,
		async : true,
		cache : false,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			var response = XMLHttpRequest.responseText;
			var text = response.replace("<style>", "").replace("</style>","");
			goAlert("結果", text);
			closeLoading();
		},
		success : function(msg) {
			$("#div_Detail_2").show();
			UI_Resize();
			$(window).scrollTop(0);
			$("#div_Detail_2 .content > .contain").html(msg);
			closeLoading();
		}
	});
}
// 關閉Detail_2畫面之函式
function closeDetail_2() {
	$("#div_Detail_2").hide();
	UI_Resize();
}
//
// 打開Customers畫面之函式
function goCustomers(argURL, argTitle) {
	$("#div_Customers .content > .header > .title").html(argTitle);
	showLoading();
	$.ajax({
		url : argURL,
		async : true,
		cache : false,
		error : function(msq) {
			goAlert("結果", "連結失敗.");
			closeLoading();
		},
		success : function(msg) {
			//$("#div_Customers").show();
			$(window).scrollTop(0);
			$("#div_Customers .content > .contain").html(msg);
			closeLoading();
		}
	});
}
// 關閉Customers畫面之函式
function closeCustomers() {
	$("#div_Customers").hide();
	UI_Resize();
}
//

// 關閉Alert畫面之函式
function closeAlert() {
	$("#div_Alert").hide();
	UI_Resize();
}
// 打開div_Loading
function showLoading() {
	$("#div_Loading").show();
	UI_Resize();
	$(window).scrollTop(0);
}
// 關閉div_Loading
function closeLoading() {
	$("#div_Loading").hide();
	UI_Resize();
}

// UI_Resize
function UI_Resize() {
	$("#div_Detail > .overlay").css("width", $(window).width());
	$("#div_Detail > .overlay").css("height", $(window).height());
	$("#div_Detail_2 > .overlay").css("width", $(window).width());
	$("#div_Detail_2 > .overlay").css("height", $(window).height());
	//
	$("#div_Customers > .overlay").css("width", $(window).width());
	$("#div_Customers > .overlay").css("height", $(window).height());
	//
	$("#div_Alert > .overlay").css("width", $(window).width());
	$("#div_Alert > .overlay").css("height", $(window).height());
	//
	$("#div_Loading > .overlay").css("width", $(window).width());
	$("#div_Loading > .overlay").css("height", $(window).height());
}
$(window).resize(function() {
	UI_Resize();
});
// UI_Scroll
function UI_Scroll() {
	$("#div_Detail > .overlay").css("top", $(window).scrollTop());
	$("#div_Detail > .overlay").css("left", $(window).scrollLeft());
	$("#div_Detail_2 > .overlay").css("top", $(window).scrollTop());
	$("#div_Detail_2 > .overlay").css("left", $(window).scrollLeft());
	//
	$("#div_Customers > .overlay").css("top", $(window).scrollTop());
	$("#div_Customers > .overlay").css("left", $(window).scrollLeft());
	//
	$("#div_Alert > .overlay").css("top", $(window).scrollTop());
	$("#div_Alert > .overlay").css("left", $(window).scrollLeft());
	//
	$("#div_Loading > .overlay").css("top", $(window).scrollTop());
	$("#div_Loading > .overlay").css("left", $(window).scrollLeft());
}
$(window).scroll(function() {
	UI_Scroll();
});
// Menu函式
function showMenuItems(argTarget) {

	var t = $("#menu_items_" + argTarget);
	var t2 = $("#menu-titles_" + argTarget + " span");
	if (t.css("display") == "none") {
		t.show();
		t2.addClass("menu-icon-site");
	} else {
		t.hide();
		t2.removeClass("menu-icon-site");
	}
}

function goMain(argURL, argFormId, argData) {
	showLoading();
	$.ajax({
		type : "POST",
		url : argURL,
		async : true,
		cache : false,
		data : $(argFormId).serialize() + argData,
		error : function(msq) {
			// goAlert("結果","連結失敗.");
			goAlert("結果", XMLHttpRequest.responseText);
			closeLoading();
		},
		success : function(msg) {
			$("#div-contain").empty().html(msg);
			closeLoading();

		}
	});
}

function goDetail_Main(argURL, argFormId, argData) {
	showLoading();
	$.ajax({
		type : "POST",
		url : argURL,
		async : true,
		cache : false,
		data : $(argFormId).serialize() + argData,
		error : function(msq) {
			// goAlert("結果","連結失敗.");
			goAlert("結果", XMLHttpRequest.responseText);
			closeLoading();
		},
		success : function(msg) {
			$("#div_Detail .content > .contain").empty().html(msg);
			closeLoading();
		}
	});
}

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

function initAutoComplete(url, serNoId, nameId) {
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		data : '',
		async : false,
		success : function(message) {
			$(nameId).autocomplete(message, {
				minChars : 0,
				max : 20,
				mustMatch : false,
				dataType : "json",
				autoFill : false,
				scrollHeight : 220,
				formatItem : function(data, i, total) {
					return data.name;
				},
				formatMatch : function(data, i, total) {
					return data.name;
				},
				formatResult : function(data) {
					return data.name;
				}
			}).result(function(event, data) {
				$(serNoId).val(data.value);
			}).clear(function(event, data) {
				$(serNoId).val("");
			});
		}
	});
}

function resetCloseDetail() {
	$("#div_Detail .content .header .title").html("");
	$("#div_Detail .content .contain")
			.html(
					"<"
							+ "script>"
							+ "function closeDetail(){$('#div_Detail').hide();UI_Resize();}"
							+ "</" + "script>");
}

function resetCloseDetail_2() {
	$("#div_Detail_2 .content .header .title").html("");
	$("#div_Detail_2 .content .contain")
			.html(
					"<"
							+ "script>"
							+ "function closeDetail_2(){$('#div_Detail_2').hide();UI_Resize();}"
							+ "</" + "script>");
}