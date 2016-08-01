angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
    
  

      .state('mc-sidemenu.indexPage', {
    url: '/indexPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/indexPage.html',
        controller: 'indexPageCtrl'
      }
    }
  })

  .state('mc-sidemenu.page2', {
    url: '/page2',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/page2.html',
        controller: 'page2Ctrl'
      }
    }
  })

  .state('mc-sidemenu.historyOrderPage', {
    url: '/historyOrderPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/historyOrderPage.html',
        controller: 'historyOrderPageCtrl'
      }
    }
  })

  .state('mc-sidemenu', {
    url: '/mc-sidemenu',
    templateUrl: 'templates/mc-sidemenu.html',
    abstract:true
  })

  .state('mc-sidemenu.page4', {
    url: '/page4',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/page4.html',
        controller: 'page4Ctrl'
      }
    }
  })

  .state('mc-sidemenu.historyDetailPage', {
    url: '/historyDetailPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/historyDetailPage.html',
        controller: 'historyDetailPageCtrl'
      }
    }
  })

  .state('mc-sidemenu.page7', {
    url: '/page7',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/page7.html',
        controller: 'page7Ctrl'
      }
    }
  })

  .state('mc-sidemenu.page8', {
    url: '/page8',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/page8.html',
        controller: 'page8Ctrl'
      }
    }
  })

  .state('mc-sidemenu.1003452', {
    url: '/indexPage0',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/1003452.html',
        controller: '1003452Ctrl'
      }
    }
  })

  .state('mc-sidemenu.historyConvsPage', {
    url: '/historyConvsPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/historyConvsPage.html',
        controller: 'historyConvsPageCtrl'
      }
    }
  })

  .state('mc-sidemenu.1003453', {
    url: '/page9',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/1003453.html',
        controller: '1003453Ctrl'
      }
    }
  })

$urlRouterProvider.otherwise('/mc-sidemenu/indexPage')

  

});