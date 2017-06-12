AUI().ready(
  'liferay-sign-in-modal',
  function (A) {
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
  const transparentBlack = 'rgba(0, 0, 0, 0.15)'
  const white = '#fff'
  const transparentWhite = 'rgba(255, 255, 255, .8)'

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
      ? $('#navigation').css({'padding': paddingSmall})
      : $('#navigation').css({'padding': paddingLarge})
  }

  const changeBackground = (color) => {
    $('.navbar-default').css({'background': color})
  }

  const changeFontColor = (color) => {
    $('.logo > span, .navbar-nav span, #sign-in, .icon-bar').css({'color': color})
  }

  const changeBorder = (color) => {
    $('#sign-in').css({'border': '1px solid ' + color})
  }

  // ==========================================================
  // adjusts the navbar css based on SCROLL position
  // ==========================================================

  $(window).scroll(() => {

    scrollPosition = $(window).scrollTop()

    if (scrollPosition > 50) {
      changePadding('small')
      changeBackground('white')
      $('body').hasClass('day') ? changeBackground(white) : changeBackground(black)
      $('body').hasClass('day') ? changeFontColor(black) : changeFontColor(white)
      $('body').hasClass('day') ? changeBorder(black) : changeBorder(white)

    } else {
      changePadding('large')
      $('body').hasClass('day') ? changeBackground(transparentBlack) : changeBackground(transparentWhite)
      $('body').hasClass('day') ? changeFontColor(white) : changeFontColor(black)
      $('body').hasClass('day') ? changeBorder(white) : changeBorder(black)
    }
  })

  // ==========================================================
  // Opens language bar
  // ==========================================================

  let languageBarOpen = false

  $('#show-language > .icon-flag').click(() => {
    languageBarOpen
      ? $('#show-language > .language-bar').hide()
      : $('#show-language > .language-bar').show()

    languageBarOpen = !languageBarOpen
  })
})
