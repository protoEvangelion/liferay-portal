
AUI().ready(function(){
  // new WOW.WOW().init()
  // ======================================================
  // Variables and state initialization
  // ======================================================
  const hi = 'goodmorning'
  
  console.log(hi)
  const breakPoint = 765
  let currentColor = 'white'
  let navbarModalOpen = false
  const standardHeight = '80px'
  const paddingLarge = '45px 42px'
  const paddingSmall = '20px 42px'
  let scrollPosition = 0

  // ======================================================
  // Carousel functions and event handlers
  // ======================================================

  // carousel initialization

  const flkty = new Flickity('.carousel', {
    autoPlay: 5000,
    pauseAutoPlayOnHover: false,
    pageDots: false, 
    contain: true
  })  

  // carousel navigation

  $('.icon-angle-down').on('click', () => {
    $('html,body').animate({scrollTop:$('#media-tiles').offset().top}, 500)
  })


  
  // ==========================================================
  // Navbar functionality & helper functions for slide changes
  // ==========================================================

  const changeHeight = (height) => {
    $('#main-navbar').css({'height': height})
  }

  const changePadding = (size) => {
    size === 'small'
      ? $('#main-navbar').css({'padding': paddingSmall})
      : $('#main-navbar').css({'padding': paddingLarge})
  }

  const changeBackground = (color) => {
    $('#main-navbar').css({'background': color})
  }

  const changeFontColor = (color, setCurrentColor = true) => {
    $('#nav-logo, #nav-ctn > li, #nav-ctn > li > a').css({'color': color})

    if (setCurrentColor) {
      currentColor = color
    }
  }

  const changeIconColor = (color) => {
    $('.flickity-prev-next-button .arrow').css({'fill': color})
    $('.icon-angle-down').css({'color': color})
    currentColor = color
  }

  // ==========================================================
  // Menu functionality
  // ==========================================================

  // Small screen mode menu button click OPEN event listener
  $('#icon-bars').click(() => {
    changeBackground('white')
    changeFontColor('black', false)
    changeHeight('80vh')

    $('#icon-bars').hide()
    $('#icon-times, #main-navbar > ul > li, #dropdown-icon').show()
    $('#main-navbar > .social-ctn').css({'display': 'flex'})

    navbarModalOpen = true
  })

  // swaps the menu icons from right to down on click
  let open = false

  $('#dropdown-icon').click(function() {
    console.log('clicked', open)
    if (open) {
      $('#dropdown-icon').removeClass('icon-rotate-90')
       $('#portfolio-dropdown > p').hide()

    } else {
      $(this).addClass('icon-rotate-90')
      $('#portfolio-dropdown > p').show()
    }
    open = !open
  })

  // Small screen mode menu button click CLOSE event listener
  $('#icon-times').click(() => {

    $('#icon-bars').show()
    $('#icon-times').hide()
    $('.social-ctn').hide()
    $('#main-navbar > ul > li, #menu-icon').hide()

    if (scrollPosition < 50) {
      changeBackground('none')
      changeFontColor(currentColor)
      changeHeight(standardHeight)
      
    } else {
      changeBackground('white')
      changeFontColor('black', false)
      changeHeight(standardHeight)
    }

    navbarModalOpen = false
  })

  // ==========================================================
  // adjusts the navbar css based on SCROLL position
  // ==========================================================

  $(window).scroll(() => {

    scrollPosition = $(window).scrollTop()

    if (scrollPosition > 50 && $(window).width() > breakPoint) {
      changePadding('small')
      changeBackground('black')
      changeFontColor('white', false) // false prevents setting currentColo

    } else if (scrollPosition < 50 && $(window).width() > breakPoint) {
      changePadding('large')
      changeBackground('none')
      changeFontColor(currentColor)

    } else if (scrollPosition > 50 && $(window).width() < breakPoint) {
      changePadding('small')
      changeBackground('white')
      changeFontColor('black', false)

    } else if (scrollPosition < 50 && $(window).width() < breakPoint && !navbarModalOpen) {
      changePadding('small')
      changeBackground('none')
      changeFontColor(currentColor)
      changeHeight(standardHeight)
    }
  })

  // ==========================================================
  // changes font & icon color depending on the image
  // ==========================================================
  
  flkty.on('select', function() {
    const slide = flkty.selectedIndex
    
    if (scrollPosition < 50 && !navbarModalOpen) {

      if (slide === 0 || slide === 1) {
        changeFontColor('white')
        changeIconColor('white')
      } else if (slide === 2) {
        changeFontColor('black')
        changeIconColor('black')
      } else {
        changeFontColor('white')
        changeIconColor('white')
      }

    } else {

      if (slide === 0 || slide === 1) {
        changeIconColor('white')
      } else if (slide === 2) {
        changeIconColor('black')
      } else {
        changeIconColor('white')
      }
    }
  })
  
  // ==========================================================
  // adjusts the navbar css based on WINDOW position
  // ==========================================================

  $(window).resize(function(e) {
    // Resize the mini tiles on bottom of home page into squares on window resize
    const ids = [ '#mini-img1', '#mini-img2', '#mini-img3', '#mini-img4', '#mini-img5', '#mini-img6']

    const height = $('#mini-img1').width()

    for (let i = 0; i < ids.length; i++) {
      $(ids[i]).height(height)
    }

    // adjusts the navbar menu as window width fluctuates past the breakpoint
    if (e.target.innerWidth > breakPoint) {

      $('#portfolio-dropdown > i, #portfolio-dropdown > p, #main-navbar > .social-ctn, #icon-bars, #icon-times').hide()

      navbarModalOpen = false
      changeHeight(standardHeight)
      $('#nav-logo, #main-navbar > ul > li').css({'display': 'inline-block'})

      if (scrollPosition < 50) {
        changeBackground('none')
        changeFontColor(currentColor)
        changePadding(paddingLarge)

      } else {
        changeBackground('black')
        changeFontColor('white')
      }

    } else if (!navbarModalOpen) {
      $('#icon-bars').show()
      $('#main-navbar > ul > li').hide()

      if (scrollPosition < 50) {
        changePadding(paddingSmall)
        changeFontColor(currentColor)

      } else {
        changeBackground('white')
        changeFontColor('black')
        changeHeight(standardHeight)
      }
    }

  }).resize()
})