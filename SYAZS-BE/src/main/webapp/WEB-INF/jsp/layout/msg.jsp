<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="hasActionMessages()">
	<script type="text/javascript">
		var msg = "";
		<s:iterator value="actionMessages">msg += '<s:property escape="false"/><br>';
		</s:iterator>;
		goAlert('訊息', msg);
	</script>
</s:if>
<s:if test="hasActionErrors()">
	<script type="text/javascript">
		var msg = "";
		<s:iterator value="actionErrors">msg += '<s:property escape="true"/><br>';
		</s:iterator>;
		goAlert('訊息', msg);
	</script>
</s:if>