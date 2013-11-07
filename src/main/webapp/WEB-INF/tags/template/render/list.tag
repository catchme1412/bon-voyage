<%@tag description="Render a list of items" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/WEB-INF/tlds/c.tld"%>
<%@attribute name="itemList" rtexprvalue="true" %>
<%@attribute name="linkPrefix" rtexprvalue="true"%>
<c:forEach var="i" items="${itemList}" varStatus="loop">
	<a class="csv" href="${linkPrefix}${i}">${i}</a><c:if test="${!loop.last}">, </c:if>
</c:forEach>
