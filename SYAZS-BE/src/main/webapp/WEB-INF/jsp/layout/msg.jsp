<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty actionMessages}">
	<script type="text/javascript">
		var msg = "";
		<s:iterator value="actionMessages">msg += '<s:property escape="false"/><br>';
		</s:iterator>;
		goAlert('訊息', msg);
	</script>
</c:if>
<c:if test="${not empty actionErrors}">
	<script type="text/javascript">
		var msg = "";
		<s:iterator value="actionErrors">msg += '<s:property escape="false"/><br>';
		</s:iterator>;
		goAlert('訊息', msg);
	</script>
</c:if>