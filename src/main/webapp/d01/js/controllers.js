angular.module('app.controllers', [])

.service('urlService', [function() {
	this.baseURL = "/mc01/McService.svc/";
}])

.service('configService', [function($odataresource) {
	this.currentDoctorID = 5;
	this.currentDoctor;
}])

.service('orderService', [function() {
	this.currentOrder;
	this.isExistingOrder;
}])

.service('uploadService', [function() {
	this.imgUploadBase64URL = "/mc01/spring/uploadImgBase64/";
	this.param1;
	this.param2;
	this.param3;
	this.fileContent;
	this.fileName;
}])

.controller('indexPageCtrl', function($scope, $state, $ionicActionSheet, orderService) {

})

.controller('pickupOrderPageCtrl', function($scope, $state, $odataresource, urlService, orderService, configService, $ionicActionSheet) {

	Order = $odataresource(urlService.baseURL + 'Orders', 'ID');
	$scope.results = Order.odata()
		.filter("CTDoctor/ID", configService.currentDoctorID)
		.filter("Status", "pickup")
		.query();

	$scope.getDetail = function(ObjectData) {
		orderService.currentOrder = ObjectData;
		$state.go('d-sm.orderDetailPage');
	}
})

.controller('historyOrderPageCtrl', function($scope, $state, $odataresource, urlService, orderService, configService) {
	Order = $odataresource(urlService.baseURL + 'Orders', 'ID');
	$scope.results = Order.odata()
		.filter("CTDoctor/ID", configService.currentDoctorID)
		.filter("IsArchived", "Y")
		.query();

	$scope.getDetail = function(ObjectData) {
		orderService.currentOrder = ObjectData;
		$state.go('d-sm.orderDetailPage');
	}
})

.controller('orderDetailPageCtrl', function($scope, $ionicModal, $state, $odataresource, $stateParams, urlService, orderService, configService, $ionicActionSheet) {
	
	var image = {};
	image.src1 = 'jiaohuai1.jpg';
	image.src2 = 'jiaohuai2.jpg';
	$scope.image = image;

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
	}
	$scope.page = page;
	$scope.getConvs = function(ObjectData) {
		orderService.currentOrder = ObjectData;
		$state.go('d-sm.orderConvsPage');
	}

	$scope.pickup = function(order) {
		actionSheetTiyle = "请确认";
		pickupButtonLabel = "确定接单";
		if (order.Doctor === null) {

		}
		$scope.hideSheet = $ionicActionSheet.show({
			buttons: [{
				text: pickupButtonLabel
			}],
			titleText: actionSheetTiyle,
			cancelText: '先不接单',
			buttonClicked: function(index) {
				order.CTDoctor = {};
				order.CTDoctor.ID = configService.currentDoctorID;
				order.Status = 'unpaid';
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

.controller('doctorInfoPageCtrl', function($scope, $odataresource, $stateParams, urlService, configService) {
	configService.currentDoctor = $odataresource(urlService.baseURL + "Doctors(" + configService.currentDoctorID + ")")
		.odata()
		.single();
	$scope.doctor = configService.currentDoctor;
})

.controller('doctorUpdatePageCtrl', function($scope, uploadService, $state, $odataresource, $stateParams, urlService, configService) {
	test = $odataresource(urlService.baseURL + "Doctors(" + configService.currentDoctorID + ")")
		.odata()
		.single();
	$scope.doctor = configService.currentDoctor;

	$scope.updateDoctor = function() {
		configService.currentDoctor.$update();
	}
	$scope.updateImage = function() {
		uploadService.param1 = "Doctor";
		uploadService.param2 = configService.currentDoctorID;
		uploadService.param3 = "Avatar";
		$state.go('d-sm.imageUploadPage');
	}
})

.controller('imageUploadPageCtrl', function($rootScope, $ionicHistory, uploadService, Upload, $scope, $odataresource, $stateParams,	urlService, $ionicActionSheet) {

	$scope.progressval = 0;
	$scope.browseFile = function() {
		document.getElementById('browseBtn').click();
	}

	angular.element(document.getElementById('browseBtn')).on('change', function(e) {
		var file = e.target.files[0];
		angular.element(document.getElementById('browseBtn')).val('');
		var fileReader = new FileReader();
		fileReader.onload = function(event) {
			$rootScope.$broadcast('event:file:selected', {
				fileData: event.target.result,
				fileName: file.name
			})
		}
		fileReader.readAsDataURL(file);
	});

	$rootScope.$on('event:file:selected', function(event, data) {
		$scope.imgSrc = data.fileData;
		$scope.$apply();
		uploadService.fileData = data.fileData;
		uploadService.fileName = data.fileName;
	});

	$scope.uploadFile = function() {

		uploadUrl = uploadService.imgUploadBase64URL + uploadService.param1 +
			"/" + uploadService.param2 + "/" + uploadService.param3;
		Upload.upload({
			url: uploadUrl,
			data: {
				fileData: uploadService.fileData,
				fileName: uploadService.fileName
			}
		}).then(function(resp) {
			$ionicHistory.goBack();
			console.log('Success ');
		}, function(resp) {
			console.log('Error status: ' + resp.status);
		}, function(evt) {
			var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
			$scope.progressval = progressPercentage;
			console.log('progress: ');
		});
	}
})

.controller('openOrderPageCtrl', function($scope, $state, $odataresource, urlService, orderService, configService) {

	Order = $odataresource(urlService.baseURL + 'Orders', 'ID');
	$scope.results = Order.odata()
		.filter("CTDoctor/ID", configService.currentDoctorID)
		.filter("IsArchived", "N")
		.query();

	$scope.getDetail = function(ObjectData) {
		orderService.currentOrder = ObjectData;
		$state.go('d-sm.orderDetailPage');
	}
})

.controller('orderConvsPageCtrl', function($scope, uploadService, $ionicModal, $rootScope, $state, $stateParams, $ionicActionSheet,	$ionicPopup, $ionicScrollDelegate, $timeout, $interval, orderService, urlService, $odataresource) {
	
	OrderConv = $odataresource(urlService.baseURL + 'Orders(' + orderService.currentOrder.ID + ')/OrderConvs', 'id');
	orderConvs = OrderConv.odata().query();
	currentOrder = orderService.currentOrder;

	page = {};
	page.title = '咨询互动';
	page.enableNewMessage = true;
	page.newMessageHolder = '发消息给' + currentOrder.CTPatient.Name + '：';
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
	$scope.toUser = currentOrder.CTPatient;
	$scope.toUser._id = 'P-' + currentOrder.CTPatient.ID;

	$scope.user = currentOrder.CTDoctor;
	$scope.user._id = 'D-' + currentOrder.CTDoctor.ID;

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
		$scope.doneLoading = true;
		$scope.messages = orderConvs;

		$timeout(function() {
			viewScroll.scrollBottom();
		}, 0);
	}

	$scope.addImage = function() {
		uploadService.param1 = "OrderConv";
		uploadService.param2 = orderService.currentOrder.ID;
		uploadService.param3 = "D";
		$state.go('d-sm.imageUploadPage');
	}

	$scope.$watch('input.message', function(newValue, oldValue) {
		console.log('input.message $watch, newValue ' + newValue);
		if (!newValue) newValue = '';
		localStorage['userMessage-' + $scope.toUser._id] = newValue;
	});

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
		myOrderConv.Owner = 'D';
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