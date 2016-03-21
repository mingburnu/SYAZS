<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>

<c:set var="recordPerPage" value="${pager.recordPerPage}" />
<c:set var="totalRecord" value="${pager.totalRecord}" />
<c:set var="currentPage" value="${pager.currentPage}" />
<c:set var="recordPoint" value="${pager.recordPoint}" />
<c:set var="pageFactor" value="${totalRecord/recordPerPage}" />
<c:set var="totalPage">
	<fmt:formatNumber type="number" pattern="#"
		value="${pageFactor+(1-(pageFactor%1))%1}" />
</c:set>

<c:set var="goToPage">
	<c:url
		value='<%=ESAPI.encoder().encodeForXMLAttribute(
						request.getParameter("namespace"))
						+ "/"
						+ ESAPI.encoder().encodeForXMLAttribute(
								request.getParameter("action")) + ".action"%>' />
</c:set>

<script type="text/javascript">
	//IE press Enter GoPage
	$(document).ready(function() {
		$("input.pp").keyup(function(e) {
			if (e.keyCode == 13) {
				gotoPage($(this).val());
			}
		});
	});

	function gotoPage(page) {
		var isNum = /^\d+$/.test(page);
		var lastPage = "${lastPage}";

		if (!isNum) {
			page = "${currentPage}";
		} else {
			if (parseInt(page) < 1) {
				page = 1;
			} else if (parseInt(page) > parseInt(lastPage)) {
				page = parseInt(lastPage);
			}
		}

		var url = $("form").attr("action") + "?pager.currentPage=" + page;
		var data = $("form:eq(0)").serialize();

		$.ajax({
			url : url,
			data : data,
			success : function(result) {
				$("#container").html(result);
			}
		});
		$("body").scrollTop(0);
	}

	function upperChangeSize(size) {
		var url = "<c:url value = '/'/>page/addCookies.action?pager.recordPerPage="
				+ size;
		$.ajax({
			url : url,
			success : function(result) {
				goMain(0);
			}
		});
	}

	function bottomChangeSize(size) {
		var url = "<c:url value = '/'/>page/addCookies.action?pager.recordPerPage="
				+ size;
		$.ajax({
			url : url,
			success : function(result) {
				goMain(1);
			}
		});
	}

	function goMain(formIndex) {
		var url = $("form").attr("action") + "?pager.recordPoint="
				+ "${recordPoint}";
		var data = $("form:eq(" + formIndex + ")").serialize();

		$.ajax({
			url : url,
			data : data,
			success : function(result) {
				$("#container").html(result);
			}
		});

		$("body").scrollTop(0);
	}
</script>

<pg:pager url="${goToPage}" items="${totalRecord}"
	maxPageItems="${recordPerPage }" maxIndexPages="5">
	<pg:index>
		<pg:first>
			<c:choose>
				<c:when test="${pageNumber eq currentPage}">
					<a class="bb" href="#" onclick="return false;">&nbsp;</a>

				</c:when>
				<c:otherwise>
					<a class="bb" onclick="gotoPage('${pageNumber}')">&nbsp;</a>
				</c:otherwise>
			</c:choose>
		</pg:first>
		<pg:prev ifnull="true">
			<c:choose>
				<c:when test="${1 eq currentPage}">
					<a class="b" href="#" onclick="return false;">&nbsp;</a>

				</c:when>
				<c:otherwise>
					<a class="b" onclick="gotoPage('${currentPage-1}')">&nbsp;</a>

				</c:otherwise>
			</c:choose>
		</pg:prev>
		第 <input class="pp" value="${currentPage }" type="number" min="1"
			max="${totalPage }" onchange="gotoPage(this.value)" size="3"> 頁，共${totalPage }頁 
		<pg:next ifnull="true">
			<c:choose>
				<c:when test="${totalPage eq currentPage}">
					<a class="n" href="#" onclick="return false;">&nbsp;</a>

				</c:when>
				<c:otherwise>
					<a class="n" onclick="gotoPage('${currentPage+1}')">&nbsp;</a>

				</c:otherwise>
			</c:choose>
		</pg:next>
		<pg:last>
			<c:choose>
				<c:when test="${pageNumber eq currentPage}">
					<a class="nn" href="#" onclick="return false;">&nbsp;</a>

				</c:when>
				<c:otherwise>
					<a class="nn" onclick="gotoPage('${pageNumber}')">&nbsp;</a>
				</c:otherwise>
			</c:choose>
		</pg:last>
	</pg:index>
</pg:pager>