angular.module('app.controllers', [])

.service('urlService', [function(){
	this.baseURL = "/mc01/McService.svc/";
}])

.service('doctorService', [function($odataresource){
	this.currentDoctorID = 5;

	this.currentDoctor;
}])

.service('orderService', [function(){
	this.currentOrder;
	
	this.isExistingOrder;
	
	
}])
.controller('indexPageCtrl', function($scope, $state, $ionicActionSheet, orderService) {

})
   
.controller('pickupOrderPageCtrl', function($scope,$state,$odataresource,urlService,orderService,doctorService,$ionicActionSheet) {
	
	Order = $odataresource(urlService.baseURL  + 'Orders', 'ID');
    $scope.results = Order.odata()
		.filter("CTDoctor/ID",doctorService.currentDoctorID)
		.filter("Status", "pickup")
        .query();


  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
	$state.go('d-sm.orderDetailPage');
  }
})
   
.controller('historyOrderPageCtrl', function($scope,$state,$odataresource,urlService,orderService,doctorService) {
    Order = $odataresource(urlService.baseURL  + 'Orders', 'ID');
    $scope.results = Order.odata()
		.filter("CTDoctor/ID",doctorService.currentDoctorID)
		.filter("IsArchived", "Y")
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
	$state.go('d-sm.orderDetailPage');
  }
})
   
.controller('orderDetailPageCtrl', function($scope,$state,$odataresource, $stateParams, urlService,orderService, doctorService,$ionicActionSheet) {		
  $scope.currentOrder=orderService.currentOrder;
  page = {};
  page.title = '进行中咨询';
  page.orderType = 'ongoing';
  if(orderService.currentOrder && orderService.currentOrder.IsArchived === 'Y'){
	page.title = '历史咨询';
	page.orderType = 'archived';
  } else if(orderService.currentOrder && orderService.currentOrder.Status === 'new'){
	page.title = '待接单';
	page.orderType = 'new';
  }
  $scope.page = page;
  $scope.getConvs=function(ObjectData){
	orderService.currentOrder=ObjectData;
	$state.go('d-sm.chat');
  }
  
  $scope.pickup=function(order){
	actionSheetTiyle = "请确认";
	pickupButtonLabel = "确定接单";
	if(order.Doctor === null){
		
	}
	$scope.hideSheet = $ionicActionSheet.show({
      buttons: [
        { text: pickupButtonLabel }
      ],
      titleText: actionSheetTiyle,
      cancelText: '先不接单',
      buttonClicked: function(index) {
		  order.CTDoctor = {};
		  order.CTDoctor.ID = doctorService.currentDoctorID;
		  order.Status = 'unpaid';
		  order.$update(
				function(order){
					orderService.currentOrder=order;
					$scope.currentOrder=orderService.currentOrder;
					$scope.page.title = '进行中咨询';
					$scope.page.orderType='ongoing';
				},function(myOrderConv){
				}
			);
			
			
					return true;
      }
    });
  }
  
})
   
.controller('orderConvsPageCtrl', function($scope,$odataresource, $stateParams, urlService,orderService) {
	if(orderService.currentOrder !== null && orderService.currentOrder.ID !== null){
    $scope.orderConvs = 
        $odataresource(urlService.baseURL  + "Orders("+ orderService.currentOrder.ID+")/OrderConvs")
        .odata()
        .query();
	}
	$scope.currentOrder=orderService.currentOrder;

})
      
.controller('doctorInfoPageCtrl', function($scope,$odataresource, $stateParams, urlService,doctorService) {
		doctorService.currentDoctor = $odataresource(urlService.baseURL  + "Doctors("+ doctorService.currentDoctorID +")")
        .odata()
        .single();
    $scope.doctor = doctorService.currentDoctor;
})
   
.controller('doctorUpdatePageCtrl', function($scope,$odataresource, $stateParams, urlService,doctorService) {
	test = $odataresource(urlService.baseURL  + "Doctors("+ doctorService.currentDoctorID +")")
        .odata()
        .single();
    $scope.doctor = doctorService.currentDoctor;
	
	$scope.updateDoctor=function(){
	   doctorService.currentDoctor.$update();
  }
})
   
.controller('imageUploadPageCtrl', function($scope,$odataresource, $stateParams, urlService, $ionicActionSheet) {

 
  $scope.addMedia = function() {
    $scope.hideSheet = $ionicActionSheet.show({
      buttons: [
        { text: 'Take photo' },
        { text: 'Photo from library' }
      ],
      titleText: 'Add images',
      cancelText: 'Cancel',
      buttonClicked: function(index) {
		  $scope.takePic();
      }
    });
  }
 
  $scope.takePic = function() {
        var options =   {
            quality: 50,
            destinationType: Camera.DestinationType.FILE_URI,
            sourceType: 1,      // 0:Photo Library, 1=Camera, 2=Saved Photo Album
            encodingType: 0     // 0=JPG 1=PNG
        }
        navigator.camera.getPicture(onSuccess,onFail,options);
    }
    var onSuccess = function(FILE_URI) {
        console.log(FILE_URI);
        $scope.picData = FILE_URI;
        $scope.$apply();
    };
    var onFail = function(e) {
        console.log("On fail " + e);
    }
    $scope.send = function() {   
        var myImg = $scope.picData;
        var options = new FileUploadOptions();
        options.fileKey="post";
        options.chunkedMode = false;
        var params = {};
        params.user_token = localStorage.getItem('auth_token');
        params.user_email = localStorage.getItem('email');
        options.params = params;
        var ft = new FileTransfer();
        ft.upload(myImg, encodeURI("https://example.com/posts/"), onUploadSuccess, onUploadFail, options);
    }
  
  
})
   
   
.controller('openOrderPageCtrl', function($scope,$state,$odataresource,urlService,orderService,doctorService) {
	
	Order = $odataresource(urlService.baseURL  + 'Orders', 'ID');
    $scope.results = Order.odata()
		.filter("CTDoctor/ID",doctorService.currentDoctorID)
		.filter("IsArchived", "N")
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
	$state.go('d-sm.orderDetailPage');
  }
})
   
.controller('100345Ctrl', function($scope) {

})
   
.controller('createOrderPageCtrl', function($scope, $state, $odataresource, $stateParams, urlService, orderService, doctorService) {
	if(orderService.isExistingOrder){
		$scope.pageTitle = "编辑咨询";
		$scope.buttonLabel = "提交";
		tempOrder = orderService.currentOrder;
	}else{
		$scope.pageTitle = "新建咨询";
		$scope.buttonLabel = "创建";
		tempOrder = {};
		tempOrder.CTPatient = {};
		tempOrder.CTDoctor = {};
		tempOrder.CTDoctor.ID = 5;
		tempOrder.CTDetail = {};
	}
	$scope.currentOrder = tempOrder;
	
	$scope.createOrder =function(tempOrder){
	    Order = $odataresource(urlService.baseURL  + 'Orders', 'ID');
		
		var myOrder = new Order();
		myOrder.CTPatient = {};
		myOrder.CTPatient.ID = doctorService.currentDoctorID;
		myOrder.CTDetail = {};
		myOrder.CTDetail.Description = tempOrder.CTDetail.Description;
		myOrder.CTDoctor = {};
		myOrder.CTDoctor.ID = tempOrder.CTDoctor.ID;
		myOrder.Status = 'new';
		
		myOrder.$save(
		    function(myOrder){
			orderService.currentOrder = myOrder;
			$state.go('d-sm.orderDetailPage');
			},function(myOrder){
			
			}
		
		);
	}
})


   
.controller('createOrderConvPageCtrl', function($scope, $state, $odataresource, $stateParams, urlService, orderService) {
	$scope.currentOrder=orderService.currentOrder;
	tempOrderConv = {};
	$scope.currentOrderConv = tempOrderConv;
	
	$scope.createOrderConv =function(tempOrderConv){
	    OrderConv = $odataresource(urlService.baseURL  + 'Orders(' + orderService.currentOrder.ID + ')/OrderConvs', 'ID');
		
		var myOrderConv = new OrderConv();
		myOrderConv.Type = 'TEXT';
		myOrderConv.Owner = 'D';
		myOrderConv.Description = tempOrderConv.Description;
		
		myOrderConv.$save(
		    function(myOrderConv){
				$state.go('d-sm.orderConvsPage');
			},function(myOrderConv){
			}
		);
	}
})


.controller('chatCtrl', function($scope, $rootScope, $state, $stateParams, MockService,
    $ionicActionSheet,
    $ionicPopup, $ionicScrollDelegate, $timeout, $interval,orderService,urlService,$odataresource) {
		
	OrderConv = $odataresource(urlService.baseURL  + 'Orders(' + orderService.currentOrder.ID + ')/OrderConvs', 'id');
		
    orderConvs = OrderConv.odata().query();
		
	currentOrder = orderService.currentOrder;
	
  page = {};
  page.title = '咨询互动';
  page.enableNewMessage = true;
  page.newMessageHolder = '发消息给'+ currentOrder.CTPatient.Name +'：';
  if(orderService.currentOrder && orderService.currentOrder.IsArchived === 'Y'){
	page.title = '互动历史';
	page.enableNewMessage = false;
	page.newMessageHolder = '已停止互动';
  } else if(orderService.currentOrder && orderService.currentOrder.Status === 'new'){
	page.enableNewMessage = false;
	page.newMessageHolder = '待病人付款后互动';
  }
    $scope.page = page;
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

    $scope.sendMessage = function(sendMessageForm) {

		var myOrderConv = new OrderConv();
		myOrderConv.Type = 'TEXT';
		myOrderConv.Owner = 'D';
		myOrderConv.Description = $scope.input.message;
		
		myOrderConv.$save(
		    function(myOrderConv){
			},function(myOrderConv){
			}
		);

      // if you do a web service call this will be needed as well as before the viewScroll calls
      // you can't see the effect of this in the browser it needs to be used on a real device
      // for some reason the one time blur event is not firing in the browser but does on devices
      keepKeyboardOpen();
      
      //MockService.sendMessage(message).then(function(data) {
      $scope.input.message = '';

      $scope.messages.push(myOrderConv);

      $timeout(function() {
        keepKeyboardOpen();
        viewScroll.scrollBottom(true);
      }, 0);

      $timeout(function() {
        $scope.messages.push(MockService.getMockMessage());
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

 