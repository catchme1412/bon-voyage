<%@tag description="Html Select" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/WEB-INF/tlds/c.tld"%>
<%@attribute name="name" rtexprvalue="true" type="java.lang.String"%>
<%@attribute name="options" rtexprvalue="true"%>
<%@attribute name="selectedKey" rtexprvalue="true" type="java.lang.String"%>
<select name="${name}" id="${name}" class="menuStyle" size="1">
	<c:forEach var="i" items="${options}">
		<option id="${i}"
			<c:if test="${i==selectedKey}">selected</c:if> 
		>${i}</option>
	</c:forEach>
</select>