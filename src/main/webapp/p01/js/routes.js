angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider

  .state('p-sm.indexPage', {
    url: '/indexPage',
    views: {
      'p-sm': {
        templateUrl: 'templates/indexPage.html',
        controller: 'indexPageCtrl'
      }
    }
  })

  .state('p-sm.openOrderPage', {
    url: '/openOrderPage',
    views: {
      'p-sm': {
        templateUrl: 'templates/openOrderPage.html',
        controller: 'openOrderPageCtrl'
      }
    }
  })

  .state('p-sm.historyOrderPage', {
    url: '/historyOrderPage',
    views: {
      'p-sm': {
        templateUrl: 'templates/historyOrderPage.html',
        controller: 'historyOrderPageCtrl'
      }
    }
  })

  .state('p-sm', {
    url: '/p-sm',
    templateUrl: 'templates/p-sm.html',
    abstract:true
  })

  .state('p-sm.patientInfoPage', {
    url: '/patientInfoPage',
    views: {
      'p-sm': {
        templateUrl: 'templates/patientInfoPage.html',
        controller: 'patientInfoPageCtrl'
      }
    }
  })

  .state('p-sm.orderDetailPage', {
    url: '/orderDetailPage',
    views: {
      'p-sm': {
        templateUrl: 'templates/orderDetailPage.html',
        controller: 'orderDetailPageCtrl'
      }
    }
  })

  .state('p-sm.createOrderPage', {
    url: '/createOrderPage',
    views: {
      'p-sm': {
        templateUrl: 'templates/createOrderPage.html',
        controller: 'createOrderPageCtrl'
      }
    }
  })

  .state('p-sm.orderConvsPage', {
    url: '/orderConvsPage',
    views: {
      'p-sm': {
        templateUrl: 'templates/orderConvsPage.html',
        controller: 'orderConvsPageCtrl'
      }
    }
  })

  .state('p-sm.patientUpdatePage', {
    url: '/patientUpdatePage',
    views: {
      'p-sm': {
        templateUrl: 'templates/patientUpdatePage.html',
        controller: 'patientUpdatePageCtrl'
      }
    }
  })

  .state('p-sm.imageUploadPage', {
    url: '/imageUploadPage',
    views: {
      'p-sm': {
        templateUrl: 'templates/imageUploadPage.html',
        controller: 'imageUploadPageCtrl'
      }
    }
  })

  .state('p-sm.createOrderConvPage', {
    url: '/createOrderConvPage',
    views: {
      'p-sm': {
        templateUrl: 'templates/createOrderConvPage.html',
        controller: 'createOrderConvPageCtrl'
      }
    }
  })

$urlRouterProvider.otherwise('/p-sm/indexPage')

});