<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>

<c:set var="recordPerPage" value="${pager.recordPerPage}" />
<c:set var="totalRecord" value="${pager.totalRecord}" />
<c:set var="currentPage" value="${pager.currentPage}" />
<c:set var="recordPoint" value="${pager.recordPoint}" />

<c:set var="goToPage">
	<c:url
		value="<esapi:encodeForXMLAttribute>${param.namespace}</esapi:encodeForXMLAttribute>/<esapi:encodeForXMLAttribute>${param.action}</esapi:encodeForXMLAttribute>.action" />
</c:set>

<script type="text/javascript">
function gotoPage(page){
	var isNum = /^\d+$/.test(page);
	var lastPage = "${lastPage}";

	if (!isNum){
		page="${currentPage}";
	} else {
		if (parseInt(page) < 1){
			page=1;
		} else if (parseInt(page)>parseInt(lastPage)){
			page=parseInt(lastPage);
		}
	}

	var offset=parseInt('${recordPerPage}')*(parseInt(page)-1);
    var url = $("form").attr("action")+"?pager.currentPage="+ page + "&pager.offset="+ offset;
	var data = $("form:eq(0)").serialize(); 

	$.ajax({
		url: url, 
		data: data,
		success: function(result){
            $("#container").html(result);
        }});
	$("body").scrollTop(0);
}

function upperChangeSize(recordPerPage) {
	var page = Math.floor(parseInt('${recordPoint}')/parseInt(recordPerPage))+1;
	var offset=parseInt(recordPerPage)*(page-1);
	var url= $("form").attr("action")+"?pager.recordPoint="+"${recordPoint}"+"&pager.offset="+offset;
	var data = $("form:eq(0)").serialize();
	
	$.ajax({
		url: url, 
		data:data,
		success: function(result){
        $("#container").html(result);
    }});
	
	$("body").scrollTop(0);
}

function bottomChangeSize() {
	var page = Math.floor(parseInt('${recordPoint}')/parseInt(recordPerPage))+1;
	var offset=parseInt(recordPerPage)*(parseInt(page)-1);
	var url= $("form").attr("action")+"?pager.recordPoint="+"${recordPoint}"+"&pager.offset="+offset;
	var data = $("form:eq(1)").serialize();
	
	$.ajax({
		url: url, 
		data:data,
		success: function(result){
        $("#container").html(result);
    }});
	
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
					<a class="bb" onclick="gotoPage(${pageNumber})">&nbsp;</a>
				</c:otherwise>
			</c:choose>
		</pg:first>
		<pg:prev ifnull="true">
			<c:choose>
				<c:when test="${empty pageNumber}">
					<a class="b" href="#" onclick="return false;">&nbsp;</a>

				</c:when>
				<c:otherwise>
					<a class="b" onclick="gotoPage(${pageNumber})">&nbsp;</a>

				</c:otherwise>
			</c:choose>
		</pg:prev>
		<pg:pages>
			<c:choose>
				<c:when test="${pageNumber eq currentPage}">
					<a class="p" href="#" onclick="return false;">${pageNumber}</a>

				</c:when>
				<c:otherwise>
					<a class="p" onclick="gotoPage(${pageNumber})">${pageNumber}</a>

				</c:otherwise>
			</c:choose>
		</pg:pages>
		<pg:next ifnull="true">
			<c:choose>
				<c:when test="${empty pageNumber}">
					<a class="n" href="#" onclick="return false;">&nbsp;</a>

				</c:when>
				<c:otherwise>
					<a class="n" onclick="gotoPage(${pageNumber})">&nbsp;</a>

				</c:otherwise>
			</c:choose>
		</pg:next>
		<pg:last>
			<c:choose>
				<c:when test="${pageNumber eq currentPage}">
					<a class="nn" href="#" onclick="return false;">&nbsp;</a>

				</c:when>
				<c:otherwise>
					<a class="nn" onclick='gotoPage(${pageNumber})'>&nbsp;</a>
				</c:otherwise>
			</c:choose>
		</pg:last>
	</pg:index>
</pg:pager>