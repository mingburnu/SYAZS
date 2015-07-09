<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="div-menu">
	<div id="div-menu-top"></div>
	<div id="div-menu-contain">
		<c:if test="${login.role.role != '管理員'}">
			<div id="menu-titles_1" class="menu-titles">
				<a onclick="showMenuItems('1');"><span
					class="menu-icon menu-icon-site">客戶管理</span></a>
			</div>
			<div id="menu_items_1" style="">
				<div class="menu-items">
					<a
						onclick="goURL('<%=request.getContextPath()%>/page/customer.action');">基本設定</a>
				</div>
			</div>
		</c:if>


		<div id="menu-titles_2" class="menu-titles">
			<a onclick="showMenuItems('2');"><span class="menu-icon">帳戶管理</span></a>
		</div>
		<div id="menu_items_2" style="display: none;">
			<div class="menu-items">
				<a
					onclick="goURL('<%=request.getContextPath()%>/page/accountNumber.action');">帳戶設定</a>
			</div>
		</div>


		<div id="menu-titles_3" class="menu-titles">
			<a onclick="showMenuItems('3');"><span class="menu-icon">書目資料管理</span></a>
		</div>
		<div id="menu_items_3" style="display: none;">
			<div class="menu-items">
				<a
					onclick="goURL('<%=request.getContextPath()%>/page/journal.action');">期刊</a>
			</div>

			<div class="menu-items">
				<a
					onclick="goURL('<%=request.getContextPath()%>/page/ebook.action');">電子書</a>
			</div>

			<div class="menu-items">
				<a
					onclick="goURL('<%=request.getContextPath()%>/page/database.action');">資料庫</a>
			</div>
		</div>

		<div id="menu-titles_5" class="menu-titles">
			<a onclick="showMenuItems('5');"><span class="menu-icon">統計資訊</span></a>
		</div>
		<div id="menu_items_5" style="display: none;">
			<div class="menu-items">
				<a
					onclick="goURL('<%=request.getContextPath()%>/page/beLogs.action');">登入次數統計</a>
			</div>
			<div class="menu-items">
				<a
					onclick="goURL('<%=request.getContextPath()%>/page/feLogs.action');">關鍵字檢索統計</a>
			</div>
		</div>
	</div>
</div>