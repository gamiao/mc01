angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
    
  

      .state('side-menu21.page1', {
    url: '/page1',
    views: {
      'side-menu21': {
        templateUrl: 'templates/page1.html',
        controller: 'page1Ctrl'
      }
    }
  })

  .state('side-menu21.page2', {
    url: '/page2',
    views: {
      'side-menu21': {
        templateUrl: 'templates/page2.html',
        controller: 'page2Ctrl'
      }
    }
  })

  .state('side-menu21.page3', {
    url: '/page3',
    views: {
      'side-menu21': {
        templateUrl: 'templates/page3.html',
        controller: 'page3Ctrl'
      }
    }
  })

  .state('side-menu21', {
    url: '/side-menu21',
    templateUrl: 'templates/side-menu21.html',
    abstract:true
  })

  .state('side-menu21.page4', {
    url: '/page4',
    views: {
      'side-menu21': {
        templateUrl: 'templates/page4.html',
        controller: 'page4Ctrl'
      }
    }
  })

  .state('side-menu21.100345', {
    url: '/page6',
    views: {
      'side-menu21': {
        templateUrl: 'templates/100345.html',
        controller: '100345Ctrl'
      }
    }
  })

  .state('side-menu21.page7', {
    url: '/page7',
    views: {
      'side-menu21': {
        templateUrl: 'templates/page7.html',
        controller: 'page7Ctrl'
      }
    }
  })

  .state('side-menu21.page8', {
    url: '/page8',
    views: {
      'side-menu21': {
        templateUrl: 'templates/page8.html',
        controller: 'page8Ctrl'
      }
    }
  })

  .state('side-menu21.1003452', {
    url: '/page10',
    views: {
      'side-menu21': {
        templateUrl: 'templates/1003452.html',
        controller: '1003452Ctrl'
      }
    }
  })

  .state('side-menu21.1003453', {
    url: '/page9',
    views: {
      'side-menu21': {
        templateUrl: 'templates/1003453.html',
        controller: '1003453Ctrl'
      }
    }
  })

$urlRouterProvider.otherwise('/side-menu21/page1')

  

});