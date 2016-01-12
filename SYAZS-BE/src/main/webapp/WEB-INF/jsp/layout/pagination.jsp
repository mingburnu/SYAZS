<%@ page import="org.owasp.esapi.ESAPI"%>
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
		value='<%=ESAPI.encoder().encodeForXMLAttribute(
						request.getParameter("namespace"))
						+ "/"
						+ ESAPI.encoder().encodeForXMLAttribute(
								request.getParameter("action")) + ".action"%>' />
</c:set>

<c:set var="lastPage">
	<pg:pager url="${goToPage}" items="${totalRecord}"
		maxPageItems="${recordPerPage}" maxIndexPages="0">
		<pg:index>
			<pg:last>${pageNumber }</pg:last>
		</pg:index>
	</pg:pager>
</c:set>

<c:choose>
	<c:when test="${1 eq param.detail }">
		<script type="text/javascript">
			//IE press Enter GoPage
			$(document).ready(function() {
				$("input#listForm_currentPageHeader:(1)").keyup(function(e) {
					if (e.keyCode == 13) {
						gotoPage_detail($(this).val());
					}
				});
			});

			//GoPage
			function gotoPage_detail(page) {
				var isNum = /^\d+$/.test(page);
				var totalPage = '${lastPage}';

				if (!isNum) {
					page = '${currentPage}';
				} else {
					if (parseInt(page) < 1) {
						page = 1;
					}

					if (parseInt(page) > parseInt(totalPage)) {
						page = totalPage;
					}
				}

				goDetail_Main($('#' + $("form:eq(1)").attr("id"))
						.attr("action"), '#' + $("form:eq(1)").attr("id"),
						'&pager.currentPage=' + page);
			}

			//變更顯示筆數
			function changePageSize_detail() {
				goDetail_Main($('#' + $("form:eq(1)").attr("id"))
						.attr("action"), '#' + $("form:eq(1)").attr("id"),
						'&pager.recordPoint=' + '${recordPoint }');
			}
		</script>
	</c:when>
	<c:when test="${2 eq param.detail }">
		<script type="text/javascript">
			//IE press Enter GoPage
			$(document).ready(function() {
				$("input#listForm_currentPageHeader:(2)").keyup(function(e) {
					if (e.keyCode == 13) {
						gotoPage_detail_2($(this).val());
					}
				});
			});

			//GoPage
			function gotoPage_detail_2(page) {
				var isNum = /^\d+$/.test(page);
				var totalPage = '${lastPage}';

				if (!isNum) {
					page = '${currentPage}';
				} else {
					if (parseInt(page) < 1) {
						page = 1;
					}

					if (parseInt(page) > parseInt(totalPage)) {
						page = totalPage;
					}
				}

				goDetail_Sub(
						$('#' + $("form:eq(2)").attr("id")).attr("action"), '#'
								+ $("form:eq(2)").attr("id"),
						'&pager.currentPage=' + page);
			}

			//變更顯示筆數
			function changePageSize_detail_2() {
				goDetail_Sub(
						$('#' + $("form:eq(2)").attr("id")).attr("action"), '#'
								+ $("form:eq(2)").attr("id"),
						'&pager.recordPoint=' + '${recordPoint }');
			}
		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			//IE press Enter GoPage
			$(document).ready(function() {
				$("input#listForm_currentPageHeader").keyup(function(e) {
					if (e.keyCode == 13) {
						gotoPage($(this).val());
					}
				});
			});

			//GoPage
			function gotoPage(page) {
				var isNum = /^\d+$/.test(page);
				var totalPage = '${lastPage}';

				if (!isNum) {
					page = '${currentPage}';
				} else {
					if (parseInt(page) < 1) {
						page = 1;
					}

					if (parseInt(page) > parseInt(totalPage)) {
						page = totalPage;
					}
				}

				goMain($('#' + $("form:eq(0)").attr("id")).attr("action"), '#'
						+ $("form:eq(0)").attr("id"), '&pager.currentPage='
						+ page);
			}

			//變更顯示筆數
			function changePageSize() {
				goMain($('#' + $("form:eq(0)").attr("id")).attr("action"), '#'
						+ $("form:eq(0)").attr("id"), '&pager.recordPoint='
						+ '${recordPoint }');
			}
		</script>
	</c:otherwise>
</c:choose>
<pg:pager url="${goToPage}" items="${totalRecord}"
	maxPageItems="${recordPerPage}" maxIndexPages="0">
	<pg:index>

		<c:choose>
			<c:when test="${1 eq param.detail }">
				<c:choose>
					<c:when test="${1 eq currentPage}">
						<pg:next ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage_detail(${currentPage+1});">下一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;</pg:next>
					</c:when>

					<c:when test="${lastPage eq currentPage}">
						<pg:prev ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage_detail(${currentPage-1});">上一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;</pg:prev>
					</c:when>
					<c:otherwise>
						<pg:prev ifnull="true">
							<a class="state-default"
								href="javascript:gotoPage_detail(${currentPage-1});">上一頁</a>
						</pg:prev>
						<pg:next ifnull="true">&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage_detail(${currentPage+1});">下一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;</pg:next>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${2 eq param.detail }">
				<c:choose>
					<c:when test="${1 eq currentPage}">
						<pg:next ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage_detail_2(${currentPage+1});">下一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</pg:next>
					</c:when>

					<c:when test="${lastPage eq currentPage}">
						<pg:prev ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage_detail_2(${currentPage-1});">上一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;</pg:prev>
					</c:when>
					<c:otherwise>
						<pg:prev ifnull="true">
							<a class="state-default"
								href="javascript:gotoPage_detail_2(${currentPage-1});">上一頁</a>
						</pg:prev>
						<pg:next ifnull="true">&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage_detail_2(${currentPage+1});">下一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;</pg:next>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${1 eq currentPage}">
						<pg:next ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage(${currentPage+1});">下一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;</pg:next>
					</c:when>
					<c:when test="${lastPage eq currentPage}">
						<pg:prev ifnull="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
								class="state-default"
								href="javascript:gotoPage(${currentPage-1});">上一頁</a>&nbsp;&nbsp;&nbsp;&nbsp;</pg:prev>
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