
<%@taglib prefix="t" tagdir="/WEB-INF/tags/template/layout"%>
<t:baselayout>
	<!-- Main Page Content and Sidebar -->
	<div class="row">
		<!-- Contact Details -->
		<div class="large-9 columns">
			<a href="${pageContext.request.contextPath}/home">click</a>
			<a href="${pageContext.request.contextPath}/init">init</a> 
			<a	href="${pageContext.request.contextPath}/map.jsp">Map</a>
			<h3>Find the best option for your travel!</h3>
			<div class="section-container auto" data-section>
				<section class="section">
					<h5 class="title">
						<a href="#panel1">Search</a>
					</h5>
					<div class="content" data-slug="panel1">
						<form>
							<div class="row collapse">
								<div class="large-2 columns">
									<label class="inline">From</label>
								</div>
								<div class="large-10 columns">
									<input type="text" id="from" placeholder="">
								</div>
							</div>
							<div class="row collapse">
								<div class="large-2 columns">
									<label class="inline">To</label>
								</div>
								<div class="large-10 columns">
									<input type="text" id="to" placeholder="">
								</div>
							</div>
							<div class="row collapse">
								<div class="large-2 columns">
									<label class="inline">Date of Travel</label>
								</div>
								<div class="large-10 columns">
									<input type="date" id="startDate" placeholder="mm/dd/yyyy" >
								</div>
							</div>
							<div class="row collapse">
								<div class="large-2 columns">
									<label class="inline">Preference</label>
								</div>
								<div class="large-10 columns">
									<select>
										<option>No preference</option>
										<option>Fastest</option>
										<option>Cheapest</option>
										<option>Luxury</option>
										<option>Best Value</option>
									</select>
								</div>
							</div>
							<button type="submit" class="radius button">Search</button>
						</form>
					</div>
				</section>
			</div>
		</div>
		<!-- End Contact Details -->
		<!-- Sidebar -->
		<!-- End Main Content and Sidebar -->
</t:baselayout>
