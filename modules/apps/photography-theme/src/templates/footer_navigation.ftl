
<nav id="footer-recursive">
	<div class="container text-center">
		<div class="row">
			<div class="col-xs-12">
			
				<ul class="list-inline" role="menubar">
					<#foreach nav_item in nav_items>
						<li class="link-ctn" role="presentation">
							<a class="link" aria-labelledby="layout_${nav_item.getLayoutId()}" href="${nav_item.getURL()}" ${nav_item.getTarget()} role="menuitem">
								${nav_item.getName()}
							</a>
						</li>
					</#foreach>
				</ul>

				<a class="${logo_css_class}" href="${site_default_url}" title="<@liferay.language_format arguments="${site_name}" key="go-to-x" />">
					<img alt="${logo_description}" height="${site_logo_height}" src="${site_logo}" width="${site_logo_width}" />
				</a>

				<div class="bottom-ctn">

					<#if facebook>
						<a href="${facebookUrl}">
							<i aria-hidden="true" class="icon-facebook icon-2x social-media-icon social-media-icon--facebook facebook"></i>
						</a>
					</#if>

					<#if twitter>
						<a href="${twitterUrl}">
							<i aria-hidden="true" class="icon-twitter icon-2x social-media-icon social-media-icon--twitter"></i>
						</a>
					</#if>	

					<#if instagram>
						<a href="${instagramUrl}">
							<i aria-hidden="true" class="icon-instagram icon-2x social-media-icon social-media-icon--instagram"></i>
						</a>
					</#if>

					<#if github>
						<a href="${githubUrl}">
							<i aria-hidden="true" class="icon-github icon-2x social-media-icon social-media-icon--github"></i>
						</a>
					</#if>

					<#if youtube>
						<a href="${youtubeUrl}">
							<i aria-hidden="true" class="icon-youtube icon-2x social-media-icon social-media-icon--youtube"></i>
						</a>
					</#if>

					<#if linkedin>
						<a href="${linkedinUrl}">
							<i aria-hidden="true" class="icon-linkedin icon-2x social-media-icon social-media-icon--linkedin"></i>
						</a>
					</#if>

					<#if dribbble>
						<a href="${dribbbleUrl}">
							<i aria-hidden="true" class="icon-dribbble icon-2x social-media-icon social-media-icon--dribbble"></i>
						</a>
					</#if>	

				</div>

				<h4 id="contact">${email} | ${phone}</h4>

				<h4>© ${year?string.yyyy} ${site_name} All Rights Reserved</h4>

			</div>
		</div>
	</div>
</nav>
