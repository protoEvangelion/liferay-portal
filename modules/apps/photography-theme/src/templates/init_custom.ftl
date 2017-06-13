<#--
This file allows you to override and define new FreeMarker variables.
-->
<#assign
  year = .now
  phone = getterUtil.getString(themeDisplay.getThemeSetting("phone"))
  email = getterUtil.getString(themeDisplay.getThemeSetting("email"))
  showLanguagePicker = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-language-picker"))
  facebook = getterUtil.getBoolean(themeDisplay.getThemeSetting("facebook"))
  facebookUrl = getterUtil.getString(themeDisplay.getThemeSetting("facebook-url"))
  twitter = getterUtil.getBoolean(themeDisplay.getThemeSetting("twitter"))
  twitterUrl = getterUtil.getString(themeDisplay.getThemeSetting("twitter-url"))
  instagram = getterUtil.getBoolean(themeDisplay.getThemeSetting("instagram"))
  instagramUrl = getterUtil.getString(themeDisplay.getThemeSetting("instagram-url"))
  youtube = getterUtil.getBoolean(themeDisplay.getThemeSetting("youtube"))
  youtubeUrl = getterUtil.getString(themeDisplay.getThemeSetting("youtube-url"))
  linkedin = getterUtil.getBoolean(themeDisplay.getThemeSetting("linkedin"))
  linkedinUrl = getterUtil.getString(themeDisplay.getThemeSetting("linkedin-url"))
  github = getterUtil.getBoolean(themeDisplay.getThemeSetting("github"))
  githubUrl = getterUtil.getString(themeDisplay.getThemeSetting("github-url"))
  dribbble = getterUtil.getBoolean(themeDisplay.getThemeSetting("dribbble"))
  dribbbleUrl = getterUtil.getString(themeDisplay.getThemeSetting("dribbble-url"))
  showLogoImage = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-logo-image"))
  logoWidth = getterUtil.getString(themeDisplay.getThemeSetting("logo-width"))
  logoHeight = getterUtil.getString(themeDisplay.getThemeSetting("logo-height"))
  showPortletBorders = getterUtil.getBoolean(themeDisplay.getThemeSetting("portlet-setup-show-borders-default"))
  backgroundColor1 = getterUtil.getString(themeDisplay.getThemeSetting("background-color-1"))
  backgroundColor2 = getterUtil.getString(themeDisplay.getThemeSetting("background-color-2"))
  backgroundGradientDirection = getterUtil.getString(themeDisplay.getThemeSetting("background-gradient-direction"))
/>







<#--
Web content templates are used to lay out the fields defined in a web
content structure.

Please use the left panel to quickly add commonly used variables.
Autocomplete is also available and can be invoked by typing "${".
-->


