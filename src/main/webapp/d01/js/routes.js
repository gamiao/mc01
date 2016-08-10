angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
    
  

      .state('d-sm.indexPage', {
    url: '/indexPage',
    views: {
      'd-sm': {
        templateUrl: 'templates/indexPage.html',
        controller: 'indexPageCtrl'
      }
    }
  })

  .state('d-sm.openOrderPage', {
    url: '/openOrderPage',
    views: {
      'd-sm': {
        templateUrl: 'templates/openOrderPage.html',
        controller: 'openOrderPageCtrl'
      }
    }
  })

  .state('d-sm.historyOrderPage', {
    url: '/historyOrderPage',
    views: {
      'd-sm': {
        templateUrl: 'templates/historyOrderPage.html',
        controller: 'historyOrderPageCtrl'
      }
    }
  })

  .state('d-sm', {
    url: '/d-sm',
    templateUrl: 'templates/d-sm.html',
    abstract:true
  })

  .state('d-sm.doctorInfoPage', {
    url: '/doctorInfoPage',
    views: {
      'd-sm': {
        templateUrl: 'templates/doctorInfoPage.html',
        controller: 'doctorInfoPageCtrl'
      }
    }
  })

  .state('d-sm.orderDetailPage', {
    url: '/orderDetailPage',
    views: {
      'd-sm': {
        templateUrl: 'templates/orderDetailPage.html',
        controller: 'orderDetailPageCtrl'
      }
    }
  })

  .state('d-sm.createOrderPage', {
    url: '/createOrderPage',
    views: {
      'd-sm': {
        templateUrl: 'templates/createOrderPage.html',
        controller: 'createOrderPageCtrl'
      }
    }
  })

  .state('d-sm.doctorUpdatePage', {
    url: '/doctorUpdatePage',
    views: {
      'd-sm': {
        templateUrl: 'templates/doctorUpdatePage.html',
        controller: 'doctorUpdatePageCtrl'
      }
    }
  })

  .state('d-sm.imageUploadPage', {
    url: '/imageUploadPage',
    views: {
      'd-sm': {
        templateUrl: 'templates/imageUploadPage.html',
        controller: 'imageUploadPageCtrl'
      }
    }
  })

  .state('d-sm.1003452', {
    url: '/indexPage0',
    views: {
      'd-sm': {
        templateUrl: 'templates/1003452.html',
        controller: '1003452Ctrl'
      }
    }
  })

  .state('d-sm.orderConvsPage', {
    url: '/orderConvsPage',
    views: {
      'd-sm': {
        templateUrl: 'templates/orderConvsPage.html',
        controller: 'orderConvsPageCtrl'
      }
    }
  })

  .state('d-sm.createOrderConvPage', {
    url: '/createOrderConvPage',
    views: {
      'd-sm': {
        templateUrl: 'templates/createOrderConvPage.html',
        controller: 'createOrderConvPageCtrl'
      }
    }
  })

$urlRouterProvider.otherwise('/d-sm/indexPage')

  

});