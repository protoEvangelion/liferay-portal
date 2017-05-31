<!DOCTYPE html>

<#include init />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
	<title>${the_title} - ${company_name}</title>

	<meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="initial-scale=1.0, width=device-width" name="viewport" />

  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css">

	<link rel="stylesheet" type="text/css" href="https://unpkg.com/flickity@2/dist/flickity.min.css">
	<script src="https://unpkg.com/flickity@2/dist/flickity.pkgd.js"></script>

	<@liferay_util["include"] page=top_head_include />
</head>

<body class="${css_class}">

<@liferay_ui["quick-access"] contentId="#main-content" />

<@liferay_util["include"] page=body_top_include />

<@liferay.control_menu />

	<div id="wrapper">
		<div id="photography-theme-ctn">
			<header id="banner" role="banner">

				<#if !is_signed_in>
					<a data-redirect="${is_login_redirect_required?string}" href="${sign_in_url}" id="sign-in" rel="nofollow">${sign_in_text}</a>
				</#if>
						<#if has_navigation && is_setup_complete>
					<#include "${full_templates_path}/navigation.ftl" />
				</#if>

				<div class="carousel">
					<div id="carousel1" class="carousel-cell"></div>
					<div id="carousel2" class="carousel-cell"></div>
					<div id="carousel3" class="carousel-cell"></div>
					<div id="carousel4" class="carousel-cell"></div>
				</div>

			</header>
		</div>
	</div>


	<section id="content">
		<h1 class="hide-accessible">${the_title}</h1>

		<#if selectable>
			<@liferay_util["include"] page=content_include />
		<#else>
			${portletDisplay.recycle()}

			${portletDisplay.setTitle(the_title)}

			<@liferay_theme["wrap-portlet"] page="portlet.ftl">
				<@liferay_util["include"] page=content_include />
			</@>
		</#if>
	</section>



		<footer id="footer" role="contentinfo">
			<div class="container-fluid-1280" id="company-info">
				<div class="text-center" id="footer-brand">
					<img alt="${logo_description}" height="${company_logo_height}" src="${site_logo}" width="${company_logo_width}" />
				</div>

				<#if has_navigation>
					<#include "${full_templates_path}/footer_navigation.ftl" />
				</#if>

				<#-- <#include "${full_templates_path}/social_media.ftl" /> -->
			</div>

			<div class="container-fluid-1280">
				<p id="copyright">
					<small><@liferay.language key="powered-by" /> <a href="http://www.liferay.com" rel="external">Liferay</a></small>
				</p>
			</div>
		</footer>

		<footer class="wow fadeInUp">
			<div id="footer-desktop">
				<div id="left-ctn">
					<div class="top-ctn">
						<div class="logo" src="" alt="">Lance</div>
						<div class="nav-ctn">
							<h3>Home</h3>
							<h3>About</h3>
						</div>
						<div class="nav-ctn">
							<h3>Portfolio</h3>
							<h3>Contact</h3>
						</div>
					</div>
					<div id="copyright" class="bottom-ctn">
						<h4>© 2017 Lance Photography, Inc. | Images from pixabay</h4>
					</div>
				</div>

				<div id="right-ctn">
					<div>
						<div>
							<h2 class="italic footer-co">Lance Photography</h2>
						</div>
						<div class="text-ctn">
							<p class="italic">info@lancephotography.com &nbsp;&nbsp; |</p>
							<p class="italic">&nbsp;&nbsp;&nbsp;(661) 222-0000</p>
						</div>
					</div>
					<div class="bottom-ctn">
						<i class="icon-facebook" aria-hidden="true"></i>
						<i class="icon-twitter" aria-hidden="true"></i>
						<i class="icon-instagram" aria-hidden="true"></i>
					</div>
				</div>
			</div>

			<div id="footer-mobile">
				<div class="logo" src="" alt="">Lance</div>
				<h3><a href="/">Home</a></h3>
				<h3><a href="/about">About</a></h3>
				<h3><a href="/portfolio">Portfolio</a></h3>
				<h3><a href="/contact">Contact</a></h3>
				<h2 class="italic footer-co">Lance Photography</h2>
				<div class="contact-info">
					<p class="italic">info@lancephotography.com &nbsp;&nbsp; |</p>
					<p class="italic">&nbsp;&nbsp;&nbsp;(661) 222-0000</p>
				</div>
				<div class="bottom-ctn">
					<i class="icon-facebook" aria-hidden="true"></i>
					<i class="icon-twitter" aria-hidden="true"></i>
					<i class="icon-instagram" aria-hidden="true"></i>
				</div>
				<h4>© 2017 Lance Photography, Inc. | Images from pixabay</h4>
			</div>
		</footer>	

<@liferay_util["include"] page=body_bottom_include />

<@liferay_util["include"] page=bottom_include />

<!-- inject:js -->
<!-- endinject -->

<script type="text/javascript">

</script>


</body>

</html>