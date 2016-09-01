angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

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

	.state('d-sm.pickupOrderPage', {
		url: '/pickupOrderPage',
		views: {
			'd-sm': {
				templateUrl: 'templates/pickupOrderPage.html',
				controller: 'pickupOrderPageCtrl'
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
		controller: 'sidemenuCtrl',
		abstract: true
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

	.state('d-sm.orderConvsPage', {
		url: '/orderConvsPage',
		views: {
			'd-sm': {
				templateUrl: 'templates/orderConvsPage.html',
				controller: 'orderConvsPageCtrl'
			}
		}
	})

	.state('d-sm.updatePasswordPage', {
		url: '/updatePasswordPage',
		views: {
			'd-sm': {
				templateUrl: 'templates/updatePasswordPage.html',
				controller: 'updatePasswordPageCtrl'
			}
		}
	})
	
  .state('login', {
      url: '/login',
      templateUrl: 'templates/loginPage.html',
      controller: 'loginPageCtrl'
  })
	
  .state('signup', {
      url: '/signup',
      templateUrl: 'templates/signupPage.html',
      controller: 'signupPageCtrl'
  })

	$urlRouterProvider.otherwise('/login')

});