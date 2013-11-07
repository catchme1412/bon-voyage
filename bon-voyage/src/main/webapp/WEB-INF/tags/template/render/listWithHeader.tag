<%@tag description="Render a list of items" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/WEB-INF/tlds/c.tld"%>
<%@attribute name="itemList" rtexprvalue="true"%>
<%@attribute name="linkPrefix" rtexprvalue="true"%>
<%@attribute name="linkSuffix" rtexprvalue="true"%>
<%@attribute name="heading" rtexprvalue="true"%>
<dl>
	<dt>${heading}</dt>
	<dd><ul class="no-bullet">
	<c:forEach var="i" items="${itemList}" varStatus="loop">
		<li><a href="${linkPrefix}${i}${linkSuffix}">${i}</a></li>
	</c:forEach>
	</ul>
	</dd>
</dl>