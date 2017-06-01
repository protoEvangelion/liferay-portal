<#--
This file allows you to override and define new FreeMarker variables.
-->
<#assign 
  show_header_search = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-header-search"))
  year = .now
  phone = getterUtil.getString(themeDisplay.getThemeSetting("phone"))
  email = getterUtil.getString(themeDisplay.getThemeSetting("email"))
  facebook = getterUtil.getBoolean(themeDisplay.getThemeSetting("Facebook"))
  facebookUrl = getterUtil.getString(themeDisplay.getThemeSetting("Facebook Url"))
  twitter = getterUtil.getBoolean(themeDisplay.getThemeSetting("Twitter"))
  twitterUrl = getterUtil.getString(themeDisplay.getThemeSetting("Twitter Url"))
  instagram = getterUtil.getBoolean(themeDisplay.getThemeSetting("Instagram"))
  instagramUrl = getterUtil.getString(themeDisplay.getThemeSetting("Instagram Url"))
  youtube = getterUtil.getBoolean(themeDisplay.getThemeSetting("Youtube"))
  youtubeUrl = getterUtil.getString(themeDisplay.getThemeSetting("Youtube Url"))
  linkedin = getterUtil.getBoolean(themeDisplay.getThemeSetting("Linkedin"))
  linkedinUrl = getterUtil.getString(themeDisplay.getThemeSetting("Linkedin Url"))
  github = getterUtil.getBoolean(themeDisplay.getThemeSetting("Github"))
  githubUrl = getterUtil.getString(themeDisplay.getThemeSetting("Github Url"))
  dribbble = getterUtil.getBoolean(themeDisplay.getThemeSetting("Dribbble"))
  dribbbleUrl = getterUtil.getString(themeDisplay.getThemeSetting("Dribbble Url"))
/>