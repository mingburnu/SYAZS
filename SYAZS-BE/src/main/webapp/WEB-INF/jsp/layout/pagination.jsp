<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="recordPerPage" value="${pager.recordPerPage}" />
<c:set var="totalRecord" value="${pager.totalRecord}" />
<c:set var="currentPage" value="${pager.currentPage}" />
<c:set var="factor" value="${totalRecord / recordPerPage}" />
<c:set var="lastPage" value="${factor + (1 - (factor % 1)) % 1}" />

<c:set var="goToPage">
	<c:url value="${param.namespace}/${param.action}.action" />
</c:set>
<c:set var="goToPage">
	<c:out value="${goToPage }"></c:out>
</c:set>

<pg:pager url="${goToPage}" items="${totalRecord}"
	maxPageItems="${recordPerPage}" maxIndexPages="5">
	<pg:index>
		<c:choose>
			<c:when test="${1 eq param.detail }">
				<c:choose>
					<c:when test="${1 eq currentPage}">
						<pg:next ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage_detail(${currentPage+1});">下一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</pg:next>
					</c:when>
					<c:when test="${lastPage eq currentPage}">
						<pg:prev ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage_detail(${currentPage-1});">上一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</pg:prev>
					</c:when>
					<c:otherwise>
						<pg:prev ifnull="true">
							<a class="state-default"
								href="javascript:gotoPage_detail(${currentPage-1});">上一頁</a>
						</pg:prev>
						<pg:next ifnull="true">&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage_detail(${currentPage+1});">下一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</pg:next>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${1 eq currentPage}">
						<pg:next ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage(${currentPage+1});">下一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</pg:next>
					</c:when>
					<c:when test="${lastPage eq currentPage}">
						<pg:prev ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage(${currentPage-1});">上一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</pg:prev>
					</c:when>
					<c:otherwise>
						<pg:prev ifnull="true">
							<a class="state-default"
								href="javascript:gotoPage(${currentPage-1});">上一頁</a>
						</pg:prev>
						<pg:next ifnull="true">&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage(${currentPage+1});">下一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</pg:next>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</pg:index>
</pg:pager>
