<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="hasActionErrors()">
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							var msg = "";
							<s:iterator value="actionErrors">msg += '<s:property escape="true"/>\r\n';
							</s:iterator>;
							alert(msg);
						});
	</script>
</s:if>