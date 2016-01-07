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
			var text = response.replace("<style>", "").replace("</style>", "");
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
			var text = response.replace("<style>", "").replace("</style>", "");
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
			var text = response.replace("<style>", "").replace("</style>", "");
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

// 打開Detail_3畫面之函式
function goDetail_3(argURL, argTitle, argData) {
	$("#div_Detail_3 .content > .header > .title").html(argTitle);
	showLoading();
	$.ajax({
		type : "POST",
		url : argURL,
		data : argData,
		async : true,
		cache : false,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			var response = XMLHttpRequest.responseText;
			var text = response.replace("<style>", "").replace("</style>", "");
			goAlert("結果", text);
			closeLoading();
		},
		success : function(msg) {
			$("#div_Detail_3").show();
			UI_Resize();
			$(window).scrollTop(0);
			$("#div_Detail_3 .content > .contain").html(msg);
			closeLoading();
		}
	});
}

// 關閉Detail_3畫面之函式
function closeDetail_3() {
	$("#div_Detail_3").hide();
	UI_Resize();
}

// 打開ReferenceOwners畫面之函式
function goReferenceOwners(argURL, argTitle) {
	$("#div_ReferenceOwners .content > .header > .title").html(argTitle);
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
			// $("#div_ReferenceOwners").show();
			$(window).scrollTop(0);
			$("#div_ReferenceOwners .content > .contain").html(msg);
			closeLoading();
		}
	});
}

// 關閉ReferenceOwners畫面之函式
function closeReferenceOwners() {
	$("#div_ReferenceOwners").hide();
	UI_Resize();
}

// 打開ResDbs畫面之函式
function goResDbs(argURL, argTitle) {
	$("#div_ResDbs .content > .header > .title").html(argTitle);
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
			// $("#div_ResDbs").show();
			$(window).scrollTop(0);
			$("#div_ResDbs .content > .contain").html(msg);
			closeLoading();
		}
	});
}

// 關閉ResDbs畫面之函式
function closeResDbs() {
	$("#div_ResDbs").hide();
	UI_Resize();
}

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
	$("#div_Detail_3 > .overlay").css("width", $(window).width());
	$("#div_Detail_3 > .overlay").css("height", $(window).height());
	//
	$("#div_ReferenceOwners > .overlay").css("width", $(window).width());
	$("#div_ReferenceOwners > .overlay").css("height", $(window).height());
	//
	$("#div_ResDbs > .overlay").css("width", $(window).width());
	$("#div_ResDbs > .overlay").css("height", $(window).height());
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
	$("#div_Detail_3 > .overlay").css("top", $(window).scrollTop());
	$("#div_Detail_3 > .overlay").css("left", $(window).scrollLeft());
	//
	$("#div_ReferenceOwners > .overlay").css("top", $(window).scrollTop());
	$("#div_ReferenceOwners > .overlay").css("left", $(window).scrollLeft());
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

function goDetail_Sub(argURL, argFormId, argData) {
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
			$("#div_Detail_2 .content > .contain").empty().html(msg);
			closeLoading();
		}
	});
}

function goDetail_Sub_2(argURL, argFormId, argData) {
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
			$("#div_Detail_3 .content > .contain").empty().html(msg);
			closeLoading();
		}
	});
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

function resetCloseDetail_3() {
	$("#div_Detail_3 .content .header .title").html("");
	$("#div_Detail_3 .content .contain")
			.html(
					"<"
							+ "script>"
							+ "function closeDetail_3(){$('#div_Detail_3').hide();UI_Resize();}"
							+ "</" + "script>");
}

function goNumTip(argURL) {
	$.ajax({
		url : argURL,
		async : true,
		cache : false,
		error : function(msq) {
			goAlert("結果", "連結失敗.");
			closeLoading();
		},
		success : function(msg) {
			$("#span-num-tip").html(msg);
			closeLoading();
		}
	});
}

function goTitleNameTip(argURL) {
	$.ajax({
		url : argURL,
		async : true,
		cache : false,
		error : function(msq) {
			goAlert("結果", "連結失敗.");
			closeLoading();
		},
		success : function(msg) {
			$("#span-title-name-tip").html(msg);
			closeLoading();
		}
	});
}