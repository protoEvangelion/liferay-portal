
AUI().ready(
  'liferay-sign-in-modal',
  function (A) {
    // if ($('body').hasClass('night')) {
    //   const url = window.location.href
    //   if (!url.includes('?color_scheme=night')) {
    //     window.location = window.location.href + '?color_scheme=night'
    //   }
    // }
    // <#assign
    //   url=request.attributes.CURRENT_URL
    // />
    // <#if url?contains("COLOR_SCHEME")>
    //   <#assign
    //       queryIndex=url?index_of("COLOR_SCHEME")
    //       valIndex=queryIndex + 16
    //   />
    // </#if>

    var signIn = A.one('#sign-in')

    if (signIn && signIn.getData('redirect') !== 'true') {
      signIn.plug(Liferay.SignInModal)
    }
  }
)

AUI().ready(function () {
  // new WOW.WOW().init()

  // ======================================================
  // Variables and state initialization
  // ======================================================

  const status = 'Up and running'
  console.log(status)

  const paddingLarge = '20px 42px'
  const paddingSmall = '5px 35px'
  let scrollPosition = 0
  const black = 'rgb(24, 24, 24)'
  const transparentWhite = 'rgba(0, 0, 0, 0.15)'
  const white = '#fff'
  const transparentBlack = 'rgba(255, 255, 255, .8)'

  // ======================================================
  // Carousel initialization
  // ======================================================

  const carousels = document.querySelectorAll('.carousel')

  for (let i=0, len = carousels.length; i < len; i++) {
    let carousel = carousels[i]
    new Flickity( carousel, {
      autoPlay: 5000,
      pauseAutoPlayOnHover: false,
      pageDots: false,
      contain: true,
      resize: false,
      setGallerySize: false
    })
  }

  // ==========================================================
  // Navbar functionality & helper functions
  // ==========================================================

  const changePadding = (size) => {
    size === 'small'
      ? $('#navigation').css({ 'padding': paddingSmall })
      : $('#navigation').css({ 'padding': paddingLarge })
  }

  const changeBackground = (color) => {
    $('.navbar-default').css({ 'background': color })
  }

  const changeFlagSize = (size) => {
    if (size === 'small') {
      $('.language-wrapper .triangle').css({ 'border-width': '0 50px 50px 0' })
      $('.language-wrapper i').css({ 'top': '10px', 'right': '-44px' })

    } else {
      $('.language-wrapper .triangle').css({ 'border-width': '0 70px 70px 0' })
      $('.language-wrapper i').css({ 'top': '17px', 'right': '-55px' })
    }

  }

  const changeBorder = (color) => {
    $('#sign-in').css({ 'border': '1px solid ' + color })
  }

  // ==========================================================
  // adjusts the navbar css based on SCROLL position
  // ==========================================================

  $(window).scroll(() => {

    scrollPosition = $(window).scrollTop()

    if (scrollPosition > 50) {
      changePadding('small')
      changeBackground('white')
      changeFlagSize('small')
      $('body').hasClass('day') ? changeBackground(white) : changeBackground(black)
      $('body').hasClass('day') ? changeBorder(black) : changeBorder(white)

    } else {
      changePadding('large')
      changeFlagSize()
      $('body').hasClass('day') ? changeBackground(transparentBlack) : changeBackground(transparentWhite)
      $('body').hasClass('day') ? changeBorder(white) : changeBorder(black)
    }
  })

  // ==========================================================
  // Language Bar Functionality
  // ==========================================================

  YUI().use(
    'aui-modal',
    function (Y) {
      var modal = new Y.Modal(
        {
          boundingBox: '#bb',
          centered: true,
          contentBox: '#cb',
          modal: true,
          resizable: {
            handles: 'b, r'
          },
          visible: false,
          width: 450
        }
      ).render()

      Y.one('#showModal').on(
        'click',
        function () {
          $('#bb .form-group, #cb').show()
          modal.show()
        }
      )
    }
  )

  // Close button handler
  $('#bb span').click(() => {
    $('#bb .form-group, #cb').hide()
  })

  const countries = ['ar', 'bg', 'ca', 'cs', 'da', 'de', 'el', 'en', 'es', 'et', 'fa', 'fi', 'fr', 'hi_IN', 'hr', 'hu', 'it', 'ja', 'ko', 'lt', 'nl', 'pl', 'pt_PT', 'ro', 'ru', 'sk', 'sl', 'sr_RS_latin', 'sr_RS', 'sv', 'th', 'tr', 'uk', 'vi', 'zh_CN', 'zh_TW']

  // Save handler for language picker
  $('#save').click(() => {
    const location = window.location
    const protocol = location.protocol
    const port = location.port ? ':' + location.port : ''
    const hostname = location.hostname
    const countryCode = $('#cb').find(':selected').val()
    const path = location.pathname
    const query = location.search

    const pathArr = path.split('/')
    let newPath = countryCode + path

    // Check if there is already a language in the url path
    // If so let's replace it with the user's choice

    if (pathArr[1] !== undefined) {
      countries.forEach(country => {
        if (country === pathArr[1]) {
          pathArr[1] = countryCode
          newPath = pathArr.join('/').slice(1)
        }
      })
    }

    const newUrl = protocol + '//' + hostname + port + '/' + newPath + query
    location.href = newUrl
  })
})
