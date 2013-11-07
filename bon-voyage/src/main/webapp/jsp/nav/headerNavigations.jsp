<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<nav class="top-bar">
<ul class="title-area">
	<!-- Title Area -->
	<li class="name">
		<h1>
			<a href="${pageContext.request.contextPath}/home" >Bon Voyage</a>
		</h1>
	</li>
	<li class="toggle-topbar menu-icon"><a href="${pageContext.request.contextPath}/nav/headerNavigations.jsp"><span></span></a>
	</li>
</ul>
<section class="top-bar-section">
	<!-- Right Nav Section -->
	<ul class="right">
		<li class="divider"></li>
		<li><a href="${pageContext.request.contextPath}/home">Home</a></li>
		<li class="divider"></li>
		<li><a href="${pageContext.request.contextPath}/reportBug.jsp">Contact Us</a></li>
		<li class="divider"></li>
		<li class="has-dropdown">
			<a href="#">Help</a>
			<ul class="dropdown">
				<li><a target="_blank" href="#">FAQ</a></li>
			</ul>
		</li>
	</ul>
</section>
</nav>