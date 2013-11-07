<%@tag description="Main Page template based on Base Layout" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/WEB-INF/tlds/c.tld"%>
<%@taglib prefix="h" tagdir="/WEB-INF/tags/template/html/"%>
<%@attribute name="main" fragment="true"%>
<%@attribute name="pageTitle" rtexprvalue="true" type="java.lang.String"%>
<%@attribute name="pageSpecificLinks" fragment="true"%>
<%@attribute name="pageSpecificScripts" fragment="true"%>
<c:set var="contextPath">${pageContext.request.contextPath}</c:set>
<!DOCTYPE HTML>
<!--[if IE 8]><html class="no-js lt-ie9" lang="en"><![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en">
<!--<![endif]-->
<head>
<title>${pageTitle}</title>
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<link rel="icon" type="image/png" href="/favicon.ico">
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width" />
<!-- Included CSS Files -->
<link rel="stylesheet" href="/resources/css/normalize.css">
<link rel="stylesheet" href="/resources/css/foundation.min.css">
<jsp:invoke fragment="pageSpecificLinks" />
<script async>
	<jsp:invoke fragment="pageSpecificScripts" />
</script>

</head>
<body>
	<div id="container">
		<jsp:include page="/jsp/nav/headerNavigations.jsp" />
	</div>
	
	<jsp:doBody/>
	<div class="large-3 columns">
      <h5>Map</h5>
      <!-- Clicking this placeholder fires the mapModal Reveal modal -->
      <p>
        <a href="" data-reveal-id="mapModal"><img src="http://placehold.it/400x280"></a><br />
        <a href="" data-reveal-id="mapModal">View Map</a>
      </p>
      <p>
        123 Awesome St.<br />
        Barsoom, MA 95155
      </p>
    </div>
    <!-- End Sidebar -->
  </div>
</body>
</html>