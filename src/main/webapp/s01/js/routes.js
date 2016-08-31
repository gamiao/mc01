angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

	$stateProvider

		.state('s-sm.indexPage', {
		url: '/indexPage',
		views: {
			's-sm': {
				templateUrl: 'templates/indexPage.html',
				controller: 'indexPageCtrl'
			}
		}
	})

	.state('s-sm.patientListPage', {
		url: '/patientListPage',
		views: {
			's-sm': {
				templateUrl: 'templates/patientListPage.html',
				controller: 'patientListPageCtrl'
			}
		}
	})

	.state('s-sm.doctorListPage', {
		url: '/doctorListPage',
		views: {
			's-sm': {
				templateUrl: 'templates/doctorListPage.html',
				controller: 'doctorListPageCtrl'
			}
		}
	})

	.state('s-sm.orderListPage', {
		url: '/orderListPage',
		views: {
			's-sm': {
				templateUrl: 'templates/orderListPage.html',
				controller: 'orderListPageCtrl'
			}
		}
	})

	.state('s-sm', {
		url: '/s-sm',
		templateUrl: 'templates/s-sm.html',
		abstract: true
	})

	.state('s-sm.patientDetailPage', {
		url: '/patientDetailPage',
		views: {
			's-sm': {
				templateUrl: 'templates/patientDetailPage.html',
				controller: 'patientDetailPageCtrl'
			}
		}
	})

	.state('s-sm.doctorDetailPage', {
		url: '/doctorDetailPage',
		views: {
			's-sm': {
				templateUrl: 'templates/doctorDetailPage.html',
				controller: 'doctorDetailPageCtrl'
			}
		}
	})

	.state('s-sm.orderDetailPage', {
		url: '/orderDetailPage',
		views: {
			's-sm': {
				templateUrl: 'templates/orderDetailPage.html',
				controller: 'orderDetailPageCtrl'
			}
		}
	})

	.state('s-sm.orderConvsPage', {
		url: '/orderConvsPage',
		views: {
			's-sm': {
				templateUrl: 'templates/orderConvsPage.html',
				controller: 'orderConvsPageCtrl'
			}
		}
	})
	
  .state('login', {
      url: '/login',
      templateUrl: 'templates/loginPage.html',
      controller: 'loginPageCtrl'
  })

	$urlRouterProvider.otherwise('/s-sm/indexPage')

});