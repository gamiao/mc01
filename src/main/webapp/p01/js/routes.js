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

  .state('mc-sidemenu.openOrderPage', {
    url: '/openOrderPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/openOrderPage.html',
        controller: 'openOrderPageCtrl'
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

  .state('mc-sidemenu.patientInfoPage', {
    url: '/patientInfoPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/patientInfoPage.html',
        controller: 'patientInfoPageCtrl'
      }
    }
  })

  .state('mc-sidemenu.orderDetailPage', {
    url: '/orderDetailPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/orderDetailPage.html',
        controller: 'orderDetailPageCtrl'
      }
    }
  })

  .state('mc-sidemenu.createOrderPage', {
    url: '/createOrderPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/createOrderPage.html',
        controller: 'createOrderPageCtrl'
      }
    }
  })

  .state('mc-sidemenu.patientUpdatePage', {
    url: '/patientUpdatePage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/patientUpdatePage.html',
        controller: 'patientUpdatePageCtrl'
      }
    }
  })

  .state('mc-sidemenu.imageUploadPage', {
    url: '/imageUploadPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/imageUploadPage.html',
        controller: 'imageUploadPageCtrl'
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

  .state('mc-sidemenu.orderConvsPage', {
    url: '/orderConvsPage',
    views: {
      'mc-sidemenu': {
        templateUrl: 'templates/orderConvsPage.html',
        controller: 'orderConvsPageCtrl'
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