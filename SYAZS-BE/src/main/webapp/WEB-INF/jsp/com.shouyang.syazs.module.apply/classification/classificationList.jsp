<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<c:if test="${entity.dataStatus != 'done'}">
	<script type="text/javascript">
		$(document).ready(function() {
			goURL("<c:url value = '/'/>crud/apply.classification.list.action");
		});
	</script>
</c:if>
<script type="text/javascript">
	//新增
	function goAdd() {
		goDetail("<c:url value = '/'/>crud/apply.classification.add.action",
				'分類法-新增');
	}

	//更新資料
	function goUpdate(serNo) {
		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			goDetail(
					"<c:url value = '/'/>crud/apply.classification.edit.action?"
							+ 'entity.serNo=' + serNo, '分類法-修改');
		}
	}

	//單筆刪除
	function goDel(serNo) {
		var f = {
			trueText : '是',
			trueFunc : function() {
				var url = '<c:url value = "/crud/apply.classification.delete.action"/>';
				var data = 'entity.serNo=' + serNo;
				goMain(url, '', data);
			},
			falseText : '否',
			falseFunc : function() {
				//不進行刪除...
			}
		};

		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			goAlert('提醒', '確定要刪除此筆資料嗎?', f);
		} else {
			goAlert('提醒', '錯誤', '');
		}
	}
</script>
</head>
<style type="text/css">
.tabs-box .tabs-items-hover-span {
	display: none;
}

.tabs-box .tabs-items-hover {
	background: none;
	background-color: transparent;
}
</style>
<body>
	<div class="tabs-box">
		<div>
			<a id="tabs-items_A" class="tabs-items-hover"><span
				class="tabs-items-hover-span">查詢</span></a>
		</div>
	</div>
	<div id="div_nav">
		目前位置：<span>書目資料管理</span> &gt; <span>分類法</span>
	</div>
	<div class="list-box">
		<div class="list-buttons">
			<a class="state-default" onclick="goAdd();">新增</a>
		</div>
		<table cellspacing="1" class="list-table">
			<tbody>
				<tr>
					<td colspan="5" class="topic">分類法</td>
				</tr>
				<tr>
					<th>ID</th>
					<th>名稱</th>
					<th></th>
				</tr>
				<c:forEach var="item" items="${ds.results}" varStatus="status">
					<tr>
						<td>${item.serNo}</td>
						<td><esapi:encodeForHTML>${item.classname}</esapi:encodeForHTML></td>
						<td align="center"><a class="state-default2"
							onclick="goUpdate('${item.serNo}')"><span
								class="icon-default icon-edit"></span>修改</a> <a
							class="state-default2" onclick="goDel('${item.serNo}')"><span
								class="icon-default icon-delete"></span>刪除</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>