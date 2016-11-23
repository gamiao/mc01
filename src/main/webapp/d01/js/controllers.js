angular.module('app.controllers', [])

.value('ODATA_SERVICE_URL','/mc01/McService.svc/')

.service('configService', function($q, $http, $odataresource, ODATA_SERVICE_URL) {
    return {
        getUser: function() {
            var deferred = $q.defer();
            var promise = deferred.promise;
			
			if (this.userID){
				$odataresource(ODATA_SERVICE_URL + "Doctors(" + this.userID + ")")
					.odata().single(
						function(user) {
							deferred.resolve(user);
						},
						function(user) {
							deferred.reject('不能获得用户信息');
						});
			} else {
				deferred.reject('无法获得登录ID');
			}

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
				url: "/mc01/spring/login/D",
				data: requestData,
				headers: {'Content-Type': 'application/json'}
			}

			$http(req).
			success(function(data, status, headers, config) {
				if(status === 200 && data.result ==='S' ){
					configService.userID = data.userID;
					configService.getUser()
					.success(function(data) {
						configService.currentUser = data;
						deferred.resolve('登录ID为' + data.userID);
					}).error(function(data) {
						deferred.reject('获取用户信息失败');
					});
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
        },
		
        createUser: function(user) {
            var deferred = $q.defer();
            var promise = deferred.promise;

			user.$save(
				function(result) {
					configService.currentUser = result;
					deferred.resolve('注册用户成功');
				},
				function(result) {
					deferred.reject('注册用户失败');
				}
			);
			
            promise.success = function(fn) {
                promise.then(fn);
                return promise;
            }
            promise.error = function(fn) {
                promise.then(null, fn);
                return promise;
            }
            return promise;
        },
		
        checkLogin: function(login) {
            var deferred = $q.defer();
            var promise = deferred.promise;
			
			var requestData = {};
			requestData.login = login;
			
			var req = {
				method: 'POST',
				url: "/mc01/spring/checkLogin/D",
				data: requestData,
				headers: {'Content-Type': 'application/json'}
			}

			$http(req).
			success(function(data, status, headers, config) {
				if(status === 200 && data.result ==='S' ){
					deferred.resolve('检查正常！');
				}else{
					deferred.reject('用户名已存在！');
				}
			}).
			error(function(data, status, headers, config) {
				deferred.reject('服务器没响应！');
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
        },
		
        mailPassword: function(mail) {
            var deferred = $q.defer();
            var promise = deferred.promise;
 
            var requestData = {};
			requestData.mail = mail;
			
			var req = {
				method: 'POST',
				url: "/mc01/spring/mailPassword/D",
				data: requestData,
				headers: {'Content-Type': 'application/json'}
			}

			$http(req).
			success(function(data, status, headers, config) 
			{
				if(status === 200 && data.result ==='S' ){
					deferred.resolve('找回账户密码成功');
				}else{
					deferred.reject('请检查邮箱是否正确！');
				}
			}).
			error(function(data, status, headers, config) 
			{
				deferred.reject('服务器暂时无法响应，请稍后再试');
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
        },
		
        updatePassword: function(id, oldPassword, newPassword) {
            var deferred = $q.defer();
            var promise = deferred.promise;
 
            var requestData = {};
			requestData.id = id;
			requestData.oldPassword = oldPassword;
			requestData.newPassword = newPassword;
			
			var req = {
				method: 'POST',
				url: "/mc01/spring/updatePassword/D",
				data: requestData,
				headers: {'Content-Type': 'application/json'}
			}

			$http(req).
			success(function(data, status, headers, config) 
			{
				if(status === 200 && data.result ==='S' ){
					deferred.resolve('登录ID为' + data.userID);
				}else{
					deferred.reject('请检查原密码是否正确！');
				}
			}).
			error(function(data, status, headers, config) 
			{
				deferred.reject('请检查原密码是否正确！');
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

.controller('mailPasswordPageCtrl', function($scope, accountService, $ionicPopup, $state) {
    $scope.data = {};
 
    $scope.mailPassword = function() {
        console.log("mail: " + $scope.data.mail );
        accountService.mailPassword($scope.data.mail).success(function(data) {
            var alertPopup = $ionicPopup.alert({
                title: '找回密码成功',
                template: '已将您的登录名和密码发到您的邮箱中，请查看后重新登录！'
            }).then(function(res) {
				$state.go('login');
            });
        }).error(function(data) {
            var alertPopup = $ionicPopup.alert({
                title: '找回密码成功',
                template: data
            });
        });
    }
})

.controller('sidemenuCtrl', function($scope, configService) {
	$scope.user = configService.currentUser;
})

.controller('indexPageCtrl', function($scope, $state, accountService, configService) {
	accountService.checkCurrentUser();
	$scope.user = configService.currentUser;
})

.controller('loginPageCtrl', function($scope, accountService, $ionicPopup, $state) {
    $scope.data = {};
 
    $scope.login = function() {
        console.log("LOGIN user: " + $scope.data.username + " - PW: " + $scope.data.password);
        accountService.loginUser($scope.data.username, $scope.data.password).success(function(data) {
            $state.go('d-sm.indexPage');
        }).error(function(data) {
            var alertPopup = $ionicPopup.alert({
                title: '登录失败',
                template: '请检查您的用户名和密码！'
            });
        });
    }
})

.controller('signupPageCtrl', function($scope, $state, $ionicPopup, accountService, ODATA_SERVICE_URL, $odataresource) {
	Doctor = $odataresource(ODATA_SERVICE_URL + 'Doctors', 'ID');
	$scope.user = new Doctor();
	$scope.temp = {};

	$scope.signup = function() {
		if(!$scope.user.Password || !$scope.user.Login){
			var alertPopup = $ionicPopup.alert({
				title: '输入有误',
				template: '登录名和密码不能为空！'
			});
		} else if ($scope.user.Password !== $scope.temp.passwordRepeat) {
			var alertPopup = $ionicPopup.alert({
				title: '输入有误',
				template: '两次密码输入不一致！'
			});
		} else {
			accountService.checkLogin($scope.user.Login)
			.success(function(checkLoginResult) {
				accountService.createUser($scope.user)
				.success(function(data) {
					$state.go('login');
				}).error(function(data) {
					var alertPopup = $ionicPopup.alert({
						title: '注册用户失败',
						template: '网络问题，请稍后再试！'
					});
				});
			}).error(function(checkLoginResult) {
				var alertPopup = $ionicPopup.alert({
					title: '账户检查失败',
					template: checkLoginResult
				});
			});
		}
	}
})

.controller('updatePasswordPageCtrl', function($scope, accountService, $ionicPopup, $state, configService) {
	
	accountService.checkCurrentUser();
	
    $scope.data = {};
 
    $scope.updatePassword = function() {
		oldPassword = $scope.data.oldPassword;
		newPassword = $scope.data.newPassword;
		newPasswordRepeat = $scope.data.newPasswordRepeat;
		
		if(newPassword !== newPasswordRepeat) {
            var alertPopup = $ionicPopup.alert({
                title: '输入有误',
                template: '请检查两次输入的新密码！'
            });
		} else if ( !newPassword ) {
            var alertPopup = $ionicPopup.alert({
                title: '输入有误',
                template: '新密码不能为空！'
            });
		} else if ( !oldPassword ) {
            var alertPopup = $ionicPopup.alert({
                title: '输入有误',
                template: '旧密码不能为空！'
            });
		} else {
			console.log("userID: " + configService.userID + " - oldPW: " + oldPassword + " - newPW: " + newPassword);
			accountService.updatePassword(configService.userID, oldPassword, newPassword).success(function(data) {
				$state.go('d-sm.doctorInfoPage');
			}).error(function(data) {
				var alertPopup = $ionicPopup.alert({
					title: '密码更新失败',
					template: '请检查您的旧密码是否正确！'
				});
			});
		}
    }
})

.controller('pickupOrderPageCtrl', function($scope, $state, $odataresource, ODATA_SERVICE_URL, orderService, configService, $ionicActionSheet, accountService, $ionicFilterBar, $timeout) {
	
	accountService.checkCurrentUser();
	Order = $odataresource(ODATA_SERVICE_URL + 'Orders', 'ID');
    var filterBarInstance;

    function getItems () {
	  results = Order.odata()
		.filter("CTDoctor/ID", configService.userID)
		.filter("Status", "pickup")
		.query();
      $scope.items = results;
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

	$scope.getDetail = function(ObjectData) {
		orderService.currentOrder = ObjectData;
		$state.go('d-sm.orderDetailPage');
	}
})

.controller('historyOrderPageCtrl', function($scope, $state, $odataresource, ODATA_SERVICE_URL, orderService, configService, accountService, $ionicFilterBar, $timeout) {
	accountService.checkCurrentUser();
	Order = $odataresource(ODATA_SERVICE_URL + 'Orders', 'ID');
	var filterBarInstance;

    function getItems () {
	  results = Order.odata()
		.filter("CTDoctor/ID", configService.userID)
		.filter("IsArchived", "Y")
		.query();
      $scope.items = results;
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

	$scope.getDetail = function(ObjectData) {
		orderService.currentOrder = ObjectData;
		$state.go('d-sm.orderDetailPage');
	}
})

.controller('openOrderPageCtrl', function($scope, $state, $odataresource, ODATA_SERVICE_URL, orderService, configService, accountService, $ionicFilterBar, $timeout) {
	accountService.checkCurrentUser();

	Order = $odataresource(ODATA_SERVICE_URL + 'Orders', 'ID');
	var filterBarInstance;

    function getItems () {
	  results = Order.odata()
		.filter("CTDoctor/ID", configService.userID)
		.filter("IsArchived", "N")
		.query();
      $scope.items = results;
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

	$scope.getDetail = function(ObjectData) {
		orderService.currentOrder = ObjectData;
		$state.go('d-sm.orderDetailPage');
	}
})

.controller('orderDetailPageCtrl', function($scope, $ionicModal, $state, $odataresource, orderService, configService, $ionicActionSheet, accountService) {
	accountService.checkCurrentUser();
	
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
		page.title = '等待确认';
		page.orderType = 'new';
	} else if (orderService.currentOrder && orderService.currentOrder.Status === 'complete') {
		page.title = '已结束';
		page.orderType = 'complete';
	}
	$scope.page = page;
	$scope.getConvs = function(ObjectData) {
		orderService.currentOrder = ObjectData;
		$state.go('d-sm.orderConvsPage');
	}
	
	$scope.complete = function(order) {
		actionSheetTiyle = "请确认";
		closeButtonLabel = "确定关闭";
		$scope.hideSheet = $ionicActionSheet.show({
				buttons: [{
					text: closeButtonLabel
				}],
				titleText: actionSheetTiyle,
				cancelText: '先不关闭',
				buttonClicked: function(index) {
					order.Status = 'complete';
					order.IsArchived = 'Y';
					order.$update(
						function(order) {
							orderService.currentOrder = order;
							$scope.currentOrder = orderService.currentOrder;
							$scope.page.title = '已结束';
							$scope.page.orderType = 'complete';
						},
						function(myOrderConv) {}
					);

					return true;
				}
			});


	}
	

	$scope.pickup = function(order) {
		actionSheetTiyle = "请确认";
		pickupButtonLabel = "确定接单";
		rejectButtonLabel = "拒绝接单";
		if (order.CTDoctor === null) {
			
			$scope.hideSheet = $ionicActionSheet.show({
				buttons: [{
					text: pickupButtonLabel
				}],
				titleText: actionSheetTiyle,
				cancelText: '先不接单',
				buttonClicked: function(index) {
					order.CTDoctor = {};
					order.CTDoctor.ID = configService.userID;
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

		} else {
			
			$scope.hideSheet = $ionicActionSheet.show({
				buttons: [{
					text: pickupButtonLabel
				}, {
					text: rejectButtonLabel
				}],
				titleText: actionSheetTiyle,
				cancelText: '先不接单',
				buttonClicked: function(index) {
					order.CTDoctor = {};
					order.CTDoctor.ID = configService.userID;
					if(index === 0) {
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
					} else {
						order.Status = 'reject';
						order.IsArchived = 'Y';
						order.$update(
							function(order) {
								orderService.currentOrder = order;
								$scope.currentOrder = orderService.currentOrder;
								$scope.page.title = '已结束';
								$scope.page.orderType = 'complete';
							},
							function(myOrderConv) {}
						);
						
					}

					return true;
				}
			});
			
		}
	}
})

.controller('doctorInfoPageCtrl', function($scope, $state, uploadService, configService, accountService) {
	accountService.checkCurrentUser();
	$scope.doctor = configService.currentUser;
	$scope.updateImage = function() {
		uploadService.param1 = "Doctor";
		uploadService.param2 = configService.userID;
		uploadService.param3 = "Avatar";
		$state.go('d-sm.imageUploadPage');
	}

	$scope.updatePassword = function() {
		$state.go('d-sm.updatePasswordPage');
	}
})


.controller('doctorUpdatePageCtrl', function($scope, $state, configService, accountService) {
	accountService.checkCurrentUser();
	$scope.doctor = configService.currentUser;

	$scope.updateDoctor = function() {
		configService.currentUser.$update();
		$state.go('d-sm.doctorInfoPage');
	}
})

.controller('imageUploadPageCtrl', function($rootScope, $state, uploadService, configService, Upload, $scope, accountService, $ionicPopup) {
	accountService.checkCurrentUser();

	$scope.progressval = 0;
	$scope.browseFile = function() {
		document.getElementById('browseBtn').click();
	}

	angular.element(document.getElementById('browseBtn')).on('change', function(e) {
		var file = e.target.files[0];
		angular.element(document.getElementById('browseBtn')).val('');
		
		if(file && file.size && file.size < 2100000) {
			var fileReader = new FileReader();
			fileReader.onload = function(event) {
				$rootScope.$broadcast('event:file:selected', {
					fileData: event.target.result,
					fileName: file.name
				})
			}
			fileReader.readAsDataURL(file);
		} else {
			var alertPopup = $ionicPopup.alert({
				title: '文件有误',
				template: '请确保图片文件存在，并且小于2M！'
			});
		}
		
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
			if(uploadService.param1 === 'Doctor'){
				configService.getUser()
				.success(function(data) {
					configService.currentUser = data;
					$state.go('d-sm.doctorInfoPage');
				}).error(function(data) {
					$state.go('d-sm.doctorInfoPage');
				});
			} else {
				$state.go('d-sm.orderConvsPage');
			}
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

.controller('orderConvsPageCtrl', function($scope, uploadService, $ionicModal, $rootScope, $state, $stateParams, $ionicActionSheet,	$ionicPopup, $ionicScrollDelegate, $timeout, $interval, orderService, ODATA_SERVICE_URL, $odataresource, accountService) {
	accountService.checkCurrentUser();
	
	OrderConv = $odataresource(ODATA_SERVICE_URL + 'Orders(' + orderService.currentOrder.ID + ')/OrderConvs', 'id');
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
	} else if (orderService.currentOrder && orderService.currentOrder.Status === 'complete') {
		page.title = '互动已结束';
		page.enableNewMessage = false;
		page.newMessageHolder = '已停止互动';
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
		m: "%d分",
		mm: "%d分",
		h: "%d小时",
		hh: "%d小时",
		d: "%d天",
		dd: "%d天",
		M: "%d月",
		MM: "%d月",
		y: "%d年",
		yy: "%d年"
	}
})