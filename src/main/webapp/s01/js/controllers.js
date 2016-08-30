angular.module('app.controllers', [])

.value('ODATA_SERVICE_URL','/mc01/McService.svc/')

.service('configService', function($q, $http, $odataresource, ODATA_SERVICE_URL) {
    return {
    }
})

.service('orderService', [function() {
	this.currentOrder;
	this.isExistingOrder;
	this.isDoctorFixed;
}])

.service('uploadService', [function() {
	this.imgUploadBase64URL = "/mc01/spring/uploadImgBase64/";
	this.param1;
	this.param2;
	this.param3;
	this.fileContent;
	this.fileName;
}])

.service('accountService', function($q, $http, configService, $state) {
    return {
        checkCurrentUser: function() {
			if(!configService.userID || !configService.currentUser) {
				$state.go("login");
			}
        },
		
        loginUser: function(login, pw) {
            var deferred = $q.defer();
            var promise = deferred.promise;
			
			var requestData = {};
			requestData.login = login;
			requestData.password = pw;
			
			var req = {
				method: 'POST',
				url: "/mc01/spring/login/S",
				data: requestData,
				headers: {'Content-Type': 'application/json'}
			}

			$http(req).
			success(function(data, status, headers, config) {
				if(status === 200 && data.result ==='S' ){
					configService.userID = data.userID;
					deferred.resolve('登录ID为' + data.userID);
				}else{
					deferred.reject('请检查登录名和密码是否正确！');
				}
			}).
			error(function(data, status, headers, config) {
				deferred.reject('请检查登录名和密码是否正确！');
			});

            promise.success = function(fn) {
                promise.then(fn);
                return promise;
            }
            promise.error = function(fn) {
                promise.then(null, fn);
                return promise;
            }
            return promise;
        }
		
    }
})

.controller('loginPageCtrl', function($scope, accountService, $ionicPopup, $state) {
    $scope.data = {};
 
    $scope.login = function() {
        console.log("LOGIN user: " + $scope.data.username + " - PW: " + $scope.data.password);
        accountService.loginUser($scope.data.username, $scope.data.password).success(function(data) {
            $state.go('s-sm.indexPage');
        }).error(function(data) {
            var alertPopup = $ionicPopup.alert({
                title: '登录失败',
                template: '请检查您的用户名和密码！'
            });
        });
    }
})

.controller('indexPageCtrl', function($scope, $state, $ionicActionSheet, orderService, accountService) {

})

.controller('doctorListPageCtrl', function($scope, $state, $odataresource, ODATA_SERVICE_URL, $ionicActionSheet, orderService, accountService, $ionicFilterBar, $timeout) {

    var filterBarInstance;

    function getItems () {
      var items = [];
	  items =
			$odataresource(ODATA_SERVICE_URL + "Doctors")
			.odata()
			.query();
      $scope.items = items;
    }

    getItems();

    $scope.showFilterBar = function () {
      filterBarInstance = $ionicFilterBar.show({
        items: $scope.items,
        update: function (filteredItems, filterText) {
          $scope.items = filteredItems;
          if (filterText) {
            console.log(filterText);
          }
        }
      });
    };

    $scope.refreshItems = function () {
      if (filterBarInstance) {
        filterBarInstance();
        filterBarInstance = null;
      }

      $timeout(function () {
        getItems();
        $scope.$broadcast('scroll.refreshComplete');
      }, 1000);
    };

})

.controller('orderListPageCtrl', function($scope, $state, $odataresource, ODATA_SERVICE_URL, $ionicActionSheet, orderService, accountService, $ionicFilterBar, $timeout) {

    var filterBarInstance;

    function getItems () {
      var items = [];
	  items =
			$odataresource(ODATA_SERVICE_URL + "Orders")
			.odata()
			.query();
      $scope.items = items;
    }

    getItems();

    $scope.showFilterBar = function () {
      filterBarInstance = $ionicFilterBar.show({
        items: $scope.items,
        update: function (filteredItems, filterText) {
          $scope.items = filteredItems;
          if (filterText) {
            console.log(filterText);
          }
        }
      });
    };

    $scope.refreshItems = function () {
      if (filterBarInstance) {
        filterBarInstance();
        filterBarInstance = null;
      }

      $timeout(function () {
        getItems();
        $scope.$broadcast('scroll.refreshComplete');
      }, 1000);
    };
})



  .controller('patientListPageCtrl', function($scope, $timeout, $ionicFilterBar) {

    var filterBarInstance;

    function getItems () {
      var items = [];
      for (var x = 1; x < 2000; x++) {
        items.push({text: 'This is item number ' + x + ' which is an ' + (x % 2 === 0 ? 'EVEN' : 'ODD') + ' number.'});
      }
      $scope.items = items;
    }

    getItems();

    $scope.showFilterBar = function () {
      filterBarInstance = $ionicFilterBar.show({
        items: $scope.items,
        update: function (filteredItems, filterText) {
          $scope.items = filteredItems;
          if (filterText) {
            console.log(filterText);
          }
        }
      });
    };

    $scope.refreshItems = function () {
      if (filterBarInstance) {
        filterBarInstance();
        filterBarInstance = null;
      }

      $timeout(function () {
        getItems();
        $scope.$broadcast('scroll.refreshComplete');
      }, 1000);
    };
  })


.controller('orderDetailPageCtrl', function($scope, $ionicModal, $ionicActionSheet, $state, orderService, accountService) {
	accountService.checkCurrentUser();

	var image = {};
	image.src1 = 'jiaohuai1.jpg';
	image.src2 = 'jiaohuai2.jpg';
	image.src3 = 'add_img.png';
	$scope.image = image;

	$scope.currentOrder = orderService.currentOrder;
	page = {};
	page.title = '进行中咨询';
	page.orderType = 'ongoing';
	if (orderService.currentOrder && orderService.currentOrder.IsArchived === 'Y') {
		page.title = '历史咨询';
		page.orderType = 'archived';
	} else if (orderService.currentOrder && orderService.currentOrder.Status === 'new') {
		page.title = '待接单';
		page.orderType = 'new';
	} else if (orderService.currentOrder && orderService.currentOrder.Status === 'unpaid') {
		page.title = '待付款';
		page.orderType = 'unpaid';
	}

	$scope.page = page;
	$scope.getConvs = function(ObjectData) {
		orderService.currentOrder = ObjectData;
		$state.go('s-sm.orderConvsPage');
	}

	$ionicModal.fromTemplateUrl('templates/fullScreenImage.html', {
		scope: $scope,
		animation: 'slide-in-up'
	}).then(function(modal) {
		$scope.modal = modal;
		$scope.modal.scope.closeImgModal = function() {
			$scope.modal.hide();
		}
		$scope.openModal = function() {
			$scope.modal.show();
		};
		$scope.closeModal = function() {
			$scope.modal.hide();
		};
		// Cleanup the modal when we're done with it!
		$scope.$on('$destroy', function() {
			$scope.modal.remove();
		});
		// Execute action on hide modal
		$scope.$on('modal.hidden', function() {
			// Execute action
		});
		// Execute action on remove modal
		$scope.$on('modal.removed', function() {
			// Execute action
		});

	});

	$scope.openFullScreenImage = function(imgSrc) {
		$scope.modal.scope.imageSrc = imgSrc;
		$scope.modal.show();
	}

	$scope.pay = function(order) {
		actionSheetTiyle = "请确认";
		pickupButtonLabel = "确认付款(" + $scope.currentOrder.CTDoctor.Price + "元)";
		$scope.hideSheet = $ionicActionSheet.show({
			buttons: [{
				text: pickupButtonLabel
			}],
			titleText: actionSheetTiyle,
			cancelText: '我再想想',
			buttonClicked: function(index) {
				order.Status = 'ongoing';
				order.$update(
					function(order) {
						orderService.currentOrder = order;
						$scope.currentOrder = orderService.currentOrder;
						$scope.page.title = '进行中咨询';
						$scope.page.orderType = 'ongoing';
					},
					function(myOrderConv) {}
				);
				return true;
			}
		});
	}

})

.controller('patientInfoPageCtrl', function($scope, $state, uploadService, configService, accountService) {
	accountService.checkCurrentUser();
	$scope.patient = configService.currentUser;

	$scope.updateImage = function() {
		uploadService.param1 = "Patient";
		uploadService.param2 = configService.userID;
		uploadService.param3 = "Avatar";
		$state.go('s-sm.imageUploadPage');
	}

	$scope.updatePassword = function() {
		$state.go('s-sm.updatePasswordPage');
	}
})

.controller('orderConvsPageCtrl', function($scope, $ionicModal, uploadService, $rootScope, $state, $stateParams, $ionicActionSheet,	$ionicPopup, $ionicScrollDelegate, $timeout, $interval, orderService, ODATA_SERVICE_URL, $odataresource, accountService) {
	accountService.checkCurrentUser();

	OrderConv = $odataresource(ODATA_SERVICE_URL + 'Orders(' + orderService.currentOrder.ID + ')/OrderConvs', 'id');
	orderConvs = OrderConv.odata().query();
	currentOrder = orderService.currentOrder;

	page = {};
	page.title = '咨询互动';
	page.enableNewMessage = true;
	page.newMessageHolder = '发消息给' + currentOrder.CTDoctor.Name + '：';
	if (orderService.currentOrder && orderService.currentOrder.IsArchived === 'Y') {
		page.title = '互动历史';
		page.enableNewMessage = false;
		page.newMessageHolder = '已停止互动';
	} else if (orderService.currentOrder && orderService.currentOrder.Status === 'new') {
		page.enableNewMessage = false;
		page.newMessageHolder = '待病人付款后互动';
	}
	$scope.page = page;

	$scope.imageSrc = "jiaohuai1.jpg";
	$scope.currentOrder = currentOrder;

	// this could be on $rootScope rather than in $stateParams
	$scope.user = currentOrder.CTPatient;
	$scope.user._id = 'P-' + currentOrder.CTPatient.ID;

	$scope.toUser = currentOrder.CTDoctor;
	$scope.toUser._id = 'D-' + currentOrder.CTDoctor.ID;

	$scope.input = {
		message: localStorage['userMessage-' + $scope.toUser._id] || ''
	};

	var messageCheckTimer;

	var viewScroll = $ionicScrollDelegate.$getByHandle('userMessageScroll');
	var footerBar; // gets set in $ionicView.enter
	var scroller;
	var txtInput; // ^^^

	$scope.$on('$ionicView.enter', function() {
		console.log('UserMessages $ionicView.enter');

		getMessages();

		$timeout(function() {
			footerBar = document.body.querySelector('#userMessagesView .bar-footer');
			scroller = document.body.querySelector('#userMessagesView .scroll-content');
			txtInput = angular.element(footerBar.querySelector('textarea'));
		}, 0);

		messageCheckTimer = $interval(function() {
			// here you could check for new messages if your app doesn't use push notifications or user disabled them
		}, 20000);
	});

	$scope.$on('$ionicView.leave', function() {
		console.log('leaving UserMessages view, destroying interval');
		// Make sure that the interval is destroyed
		if (angular.isDefined(messageCheckTimer)) {
			$interval.cancel(messageCheckTimer);
			messageCheckTimer = undefined;
		}
	});

	$scope.$on('$ionicView.beforeLeave', function() {
		if (!$scope.input.message || $scope.input.message === '') {
			localStorage.removeItem('userMessage-' + $scope.toUser._id);
		}
	});

	function getMessages() {
		// the service is mock but you would probably pass the toUser's GUID here
		$scope.doneLoading = true;
		$scope.messages = orderConvs;

		$timeout(function() {
			viewScroll.scrollBottom();
		}, 0);
	}

	$scope.$watch('input.message', function(newValue, oldValue) {
		console.log('input.message $watch, newValue ' + newValue);
		if (!newValue) newValue = '';
		localStorage['userMessage-' + $scope.toUser._id] = newValue;
	});

	$scope.addImage = function() {
		uploadService.param1 = "OrderConv";
		uploadService.param2 = orderService.currentOrder.ID;
		uploadService.param3 = "P";
		$state.go('s-sm.imageUploadPage');
	}

	$ionicModal.fromTemplateUrl('templates/fullScreenImage.html', {
		scope: $scope,
		animation: 'slide-in-up'
	}).then(function(modal) {
		$scope.modal = modal;
		$scope.modal.scope.closeImgModal = function() {
			$scope.modal.hide();
		}
		$scope.openModal = function() {
			$scope.modal.show();
		};
		$scope.closeModal = function() {
			$scope.modal.hide();
		};
		// Cleanup the modal when we're done with it!
		$scope.$on('$destroy', function() {
			$scope.modal.remove();
		});
		// Execute action on hide modal
		$scope.$on('modal.hidden', function() {
			// Execute action
		});
		// Execute action on remove modal
		$scope.$on('modal.removed', function() {
			// Execute action
		});

	});

	$scope.openFullScreenImage = function(imgSrc) {
		$scope.modal.scope.imageSrc = imgSrc;
		$scope.modal.show();
	}

	$scope.sendMessage = function(sendMessageForm) {

		var myOrderConv = new OrderConv();
		myOrderConv.Type = 'TEXT';
		myOrderConv.Owner = 'P';
		myOrderConv.Description = $scope.input.message;

		myOrderConv.$save(
			function(myOrderConv) {},
			function(myOrderConv) {}
		);

		// if you do a web service call this will be needed as well as before the viewScroll calls
		// you can't see the effect of this in the browser it needs to be used on a real device
		// for some reason the one time blur event is not firing in the browser but does on devices
		keepKeyboardOpen();
		$scope.input.message = '';

		$scope.messages.push(myOrderConv);

		$timeout(function() {
			keepKeyboardOpen();
			viewScroll.scrollBottom(true);
		}, 0);

		$timeout(function() {
			//$scope.messages.push(MockService.getMockMessage());
			keepKeyboardOpen();
			viewScroll.scrollBottom(true);
		}, 2000);

		//});
	};

	// this keeps the keyboard open on a device only after sending a message, it is non obtrusive
	function keepKeyboardOpen() {
		console.log('keepKeyboardOpen');
		txtInput.one('blur', function() {
			console.log('textarea blur, focus back on it');
			txtInput[0].focus();
		});
	}

	$scope.onMessageHold = function(e, itemIndex, message) {
		console.log('onMessageHold');
		console.log('message: ' + JSON.stringify(message, null, 2));
		$ionicActionSheet.show({
			buttons: [{
				text: 'Copy Text'
			}, {
				text: 'Delete Message'
			}],
			buttonClicked: function(index) {
				switch (index) {
					case 0: // Copy Text
						//cordova.plugins.clipboard.copy(message.text);

						break;
					case 1: // Delete
						// no server side secrets here :~)
						$scope.messages.splice(itemIndex, 1);
						$timeout(function() {
							viewScroll.resize();
						}, 0);

						break;
				}

				return true;
			}
		});
	};

	// this prob seems weird here but I have reasons for this in my app, secret!
	$scope.viewProfile = function(msg) {
		if (msg.userId === $scope.user._id) {
			// go to your profile
		} else {
			// go to other users profile
		}
	};

	// I emit this event from the monospaced.elastic directive, read line 480
	$scope.$on('taResize', function(e, ta) {
		console.log('taResize');
		if (!ta) return;

		var taHeight = ta[0].offsetHeight;
		console.log('taHeight: ' + taHeight);

		if (!footerBar) return;

		var newFooterHeight = taHeight + 10;
		newFooterHeight = (newFooterHeight > 44) ? newFooterHeight : 44;

		footerBar.style.height = newFooterHeight + 'px';
		scroller.style.bottom = newFooterHeight + 'px';
	});

})

function onProfilePicError(ele) {
	this.ele.src = ''; // set a fallback
}

// configure moment relative time
moment.locale('en', {
	relativeTime: {
		future: "in %s",
		past: "%s ago",
		s: "%d sec",
		m: "a minute",
		mm: "%d minutes",
		h: "an hour",
		hh: "%d hours",
		d: "a day",
		dd: "%d days",
		M: "a month",
		MM: "%d months",
		y: "a year",
		yy: "%d years"
	}
})

moment.locale('cn', {
	relativeTime: {
		future: "%s以后",
		past: "%s前",
		s: "%d秒",
		m: "a分",
		mm: "%d分",
		h: "小时",
		hh: "%d小时",
		d: "天",
		dd: "%d天",
		M: "月",
		MM: "%d月",
		y: "年",
		yy: "%d年"
	}
})