
Liferay.on('allPortletsReady', function(){
  // new WOW.WOW().init()
  // ======================================================
  // Variables and state initialization
  // ======================================================
  const hi = 'hello'
  
  console.log(hi)
  const breakPoint = 765
  let currentColor = 'white'
  let navbarModalOpen = false
  const paddingLarge = '45px 42px'
  const paddingSmall = '20px 42px'
  let scrollPosition = 0

  // ======================================================
  // Carousel functions and event handlers
  // ======================================================

  // carousel initialization
  const flkty = new Flickity(document.getElementById('main-carousel'), {
		autoPlay: 5000,
		pauseAutoPlayOnHover: false
	})
/*
  $('#main-carousel').slick({
    arrows: false,
    autoplay: true,
    autoplaySpeed: 10000,
    pauseOnHover: false,
    pauseOnFocus: false,
    waitForAnimate: false,
  })
  

  // carousel navigation
  $('.arrow-ctn.right').click(() => {
    $('#main-carousel').slick('slickNext')
  })
  $('.arrow-ctn.left').click(() => {
    $('#main-carousel').slick('slickPrev')
  })

  $('.fa-angle-down').on('click', () => {
    $('html,body').animate({scrollTop:$('#media-tiles').offset().top}, 500)
  })
*/

  
  // ==========================================================
  // Navbar functionality & helper functions for slide changes
  // ==========================================================

  // Small screen mode menu button click OPEN event listener
  $('.fa-bars').click(() => {
    $('#main-navbar').css({
      'background': 'white',
      'color': 'black',
      'height': '80vh'
    })
    $('.fa-bars').hide()
    $('.fa-times, #main-navbar > ul > li, #menu-icon').show()
    $('#nav-logo, #main-navbar > ul > li').css({'color': 'black'})
    $('#menu-icon').css({'display': 'inline-block'})
    $('.social-ctn').css({
      'align-items': 'center',
      'display': 'flex',
      'flex-direction': 'row',
      'justify-content': 'flex-start',
      'margin-top': '-30vh'
    })
    $('.social-ctn > i').css({'padding': '0 20px'})
    navbarModalOpen = true
  })

  // swaps the menu icons from right to down on click
  let open = false
  $('#menu-icon').click(function() {

    if (open) {
      $(this).css({
        'transform': 'translateX(25px)',
        'transition': 'transform .5s'
      })
      $('#portfolio-dropdown > p').hide()
    } else {
      $(this).css({
        'transform': 'rotate(90deg) translateY(-25px)',
        'transition': 'transform .5s'
      })
      $('#portfolio-dropdown > p').show()
    }

    open = !open
  })

  // Small screen mode menu button click CLOSE event listener
  $('.fa-times').click(() => {
    $('#main-navbar > ul > li, #menu-icon').hide()

    if (scrollPosition < 50) {
      $('#main-navbar').css({
        'background': 'none',
        'height': '80px',
      })
      $('#nav-logo, #main-navbar > ul > li').css({'color': currentColor})
    } else {
      $('#main-navbar').css({
        'background': 'white',
        'height': '80px',
      })
      $('#nav-logo, #main-navbar > ul > li').css({'color': 'black'})
    }
    $('.fa-bars').show()
    $('.fa-times').hide()
    navbarModalOpen = false
  })

  // Navbar helper functions
  const changePadding = () => {
    $('#main-navbar').css({'padding': paddingLarge})
  }

  const changeFontColor = (color) => {
    $('#main-navbar, #nav-logo').css({
      'background': 'none',
      'color': color
    })
    $('#main-navbar > ul > li, #main-navbar > ul > li > a').css({'color': color})
    currentColor = color
  }

  const changeIconColor = (color) => {
    $('.arrow-ctn.left > i').css({'color': color})
    $('.arrow-ctn.right > i').css({'color': color})
    $('.fa-angle-down').css({'color': color})
    currentColor = color
  }

  // ==========================================================
  // adjusts the navbar css based on SCROLL position
  // ==========================================================

  $(window).scroll(() => {

    scrollPosition = $(window).scrollTop()

    if (scrollPosition > 50 && $(window).width() > breakPoint) {
      $('#main-navbar').css({
        'background': 'rgb(24, 24, 24)',
        'color': 'white',
        'padding': paddingSmall
      })
      $('#nav-logo, #main-navbar > ul > li').css({'color': 'white'})

    } else if (scrollPosition > 50 && $(window).width() < breakPoint) {
      $('#main-navbar').css({
        'background': 'white',
        'color': 'black',
        'padding': paddingSmall
      })
      $('#nav-logo').css({'color': 'black'})

    } else if (scrollPosition < 50 && $(window).width() > breakPoint) {
      changePadding()
      changeFontColor(currentColor)

    } else if (scrollPosition < 50 && $(window).width() < breakPoint && !navbarModalOpen) {
      $('#main-navbar, #nav-logo').css({
        'background': 'none',
        'color': currentColor
      })
    }
  })

  // changes font & icon color depending on the image
  /* $('#main-carousel').on('afterChange', function (e, slick, currentSlide, nextSlide) {

    if (scrollPosition < 50 && !navbarModalOpen) {

      if (currentSlide === 0 || currentSlide === 1) {
        changeFontColor('white')
        changeIconColor('white')
      } else if (currentSlide === 2) {
        changeFontColor('black')
        changeIconColor('black')
      } else {
        changeFontColor('white')
        changeIconColor('white')
      }

    } else {

      if (currentSlide === 0 || currentSlide === 1) {
        changeIconColor('white')
      } else if (currentSlide === 2) {
        changeIconColor('black')
      } else {
        changeIconColor('white')
      }
    }
  })
  */

  $(window).resize(function(e) {
    // Resize the mini tiles on bottom of home page into squares on window resize
    const ids = [ '#mini-img1', '#mini-img2', '#mini-img3', '#mini-img4', '#mini-img5', '#mini-img6']

    const height = $('#mini-img1').width()

    for (let i = 0; i < ids.length; i++) {
      $(ids[i]).height(height)
    }

    // adjusts the navbar menu as window width fluctuates past the breakpoint
    if (e.target.innerWidth > breakPoint) {
      $('.fa-bars').hide()
      $('.fa-times').hide()
      $('#portfolio-dropdown > i, #portfolio-dropdown > p').hide()

      navbarModalOpen = false

      if (scrollPosition < 50) {
        $('#main-navbar').css({
          'background': 'none',
          'color': currentColor,
          'height': 'auto',
          'padding': paddingLarge
        })
        $('#nav-logo, #main-navbar > ul > li').css({
          'color': currentColor,
          'display': 'inline-block'
        })
      } else {
        $('#main-navbar').css({
          'background': 'black',
          'color': 'white',
          'height': 'auto'
        })
        $('#nav-logo, #main-navbar > ul > li').css({
          'color': 'white',
          'display': 'inline-block'
        })
      }

    } else if (!navbarModalOpen) {
      $('.fa-bars').show()
      $('#main-navbar > ul > li').css({'display': 'none'})

      if (scrollPosition < 50) {
        $('#main-navbar').css({
            'padding': paddingSmall
          })
        $('#nav-logo').css({'color': currentColor})
      } else {
        $('#main-navbar').css({
            'background': 'white',
            'color': 'black',
            'height': '80px'
          })
        $('#nav-logo').css({'color': 'black'})
      }
    }

  }).resize()
})