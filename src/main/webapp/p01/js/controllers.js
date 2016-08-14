angular.module('app.controllers', [])

.service('urlService', [function(){
	this.baseURL = "/mc01/McService.svc/";
}])

.service('patientService', [function($odataresource){
	this.currentPatientID = 10;
	this.currentPatient;
}])

.service('doctorService', [function($odataresource){
	this.allDoctorList;
}])

.service('orderService', [function(){
	this.currentOrder;
	this.isExistingOrder;
	this.isDoctorFixed;
}])
.controller('indexPageCtrl', function($scope, $state, urlService, $odataresource, $ionicModal, $ionicActionSheet, orderService, doctorService) {
	
	$scope.createOrder = function() {
		$scope.hideSheet = $ionicActionSheet.show({
		  buttons: [
       { text: '<b>不指定医生</b>' },
       { text: '<b>指定医生</b>' }
		  ],
		  titleText: '新建咨询',
		  cancelText: '取消',
		  buttonClicked: function(index) {
			  orderService.isExistingOrder = false;
			  orderService.isDoctorFixed = (1===index);
			  $state.go('p-sm.createOrderPage');
		  }
		});
	}
	
  $scope.showImage=function(){
	  $scope.imageSrc  = 'http://localhost:28080/mc/img/k9n0gpcbRiFIUhLF7S8Q_zuhuai2.jpg';
      $scope.openModal();
  }
  
  
  $scope.selectables = [
    1, 2, 3
  ];

  $scope.longList  = [];
  for(var i=0;i<1000; i++){
    $scope.longList.push(i);
  }

  $scope.selectableNames =  [
    { name : "Mauro", role : "black hat"}, 
    { name : "Silvia", role : "pineye"},
    { name : "Merlino", role : "little canaglia"},
  ];

  $scope.someSetModel = 'Mauro';

  $scope.getOpt = function(option){
    return option.name + ":" + option.role;
  };
  
  $scope.shoutLoud = function(newValuea, oldValue){
    alert("changed from " + JSON.stringify(oldValue) + " to " + JSON.stringify(newValuea));
  };
  
  $scope.shoutReset = function(){
    alert("value was reset!");
  };
  

})
   
.controller('historyOrderPageCtrl', function($scope,$odataresource,urlService,orderService,patientService) {
	Order = $odataresource(urlService.baseURL  + 'Orders', 'ID');
    $scope.results = Order.odata()
		.filter("CTPatient/ID",patientService.currentPatientID)
		.filter("IsArchived", "Y")
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('orderDetailPageCtrl', function($scope,$ionicActionSheet, $state, $odataresource, $stateParams, urlService,orderService) {		
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
  } else if(orderService.currentOrder && orderService.currentOrder.Status === 'unpaid'){
	page.title = '待付款';
	page.orderType = 'unpaid';
  }
  
  $scope.page = page;
  $scope.getConvs=function(ObjectData){
	orderService.currentOrder=ObjectData;
	$state.go('p-sm.orderConvsPage');
  }
  
  
  
  $scope.pay=function(order){
	actionSheetTiyle = "请确认";
	pickupButtonLabel = "确认付款";
	$scope.hideSheet = $ionicActionSheet.show({
      buttons: [
        { text: pickupButtonLabel }
      ],
      titleText: actionSheetTiyle,
      cancelText: '我再想想',
      buttonClicked: function(index) {
		  order.Status = 'ongoing';
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
      
.controller('patientInfoPageCtrl', function($scope,$odataresource, $stateParams, urlService,patientService) {
		patientService.currentPatient = $odataresource(urlService.baseURL  + "Patients("+ patientService.currentPatientID +")")
        .odata()
        .single();
    $scope.patient = patientService.currentPatient;
})
   
.controller('patientUpdatePageCtrl', function($scope,$odataresource, $stateParams, urlService,patientService) {
	test = $odataresource(urlService.baseURL  + "Patients("+ patientService.currentPatientID +")")
        .odata()
        .single();
    $scope.patient = patientService.currentPatient;
	
	$scope.updatePatient=function(){
	   patientService.currentPatient.$update();
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
   
.controller('openOrderPageCtrl', function($scope,$odataresource,urlService,orderService,patientService) {
	Order = $odataresource(urlService.baseURL  + 'Orders', 'ID');
    $scope.results = Order.odata()
		.filter("CTPatient/ID",patientService.currentPatientID)
		.filter("IsArchived", "N")
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('createOrderPageCtrl', function($scope,$ionicModal, $state, $odataresource, $stateParams, urlService, orderService, doctorService, patientService) {
	$scope.isDoctorFixed = orderService.isDoctorFixed;

	
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
	    Order = $odataresource(urlService.baseURL  + 'Orders', 'id');
		
		var myOrder = new Order();
		myOrder.CTPatient = {};
		myOrder.CTPatient.ID = patientService.currentPatientID;
		myOrder.CTDetail = {};
		myOrder.CTDetail.Description = tempOrder.CTDetail.Description;
		if($scope.isDoctorFixed){
			myOrder.CTDoctor = {};
			myOrder.CTDoctor.ID = tempOrder.CTDoctor.ID;
		}
		myOrder.Status = 'new';
		
		myOrder.$save(
		    function(myOrder){
			orderService.currentOrder = myOrder;
			$state.go('p-sm.orderDetailPage',null,{reload:true});
			},function(myOrder){
			
			}
		
		);
	}
	
	if($scope.isDoctorFixed){
	
	allDoctors = 
        $odataresource(urlService.baseURL  + "Doctors")
        .odata()
        .query();
	
  $ionicModal.fromTemplateUrl('templates/doctorSelectionPage.html', {
    scope: $scope,
    animation: 'slide-in-up'
  }).then(function(modal) {
    $scope.modal = modal;
	$scope.modal.scope.doctors = allDoctors;
	$scope.modal.scope.pageTitle = '请选择一位医生';
	$scope.modal.scope.setOption = function(doctor) {
		$scope.currentOrder.CTDoctor = doctor;
		$scope.modal.hide();
	}
	$scope.modal.scope.returnClicked = function() {
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
  
    $scope.$on('$ionicView.enter', function() {
     // Code you want executed every time view is opened
     $scope.openModal();
  })
  
  
		
	}
  
})
   
.controller('createOrderConvPageCtrl', function($scope, $state, $odataresource, $stateParams, urlService, orderService) {
	$scope.currentOrder=orderService.currentOrder;
	tempOrderConv = {};
	$scope.currentOrderConv = tempOrderConv;
	
	$scope.createOrderConv =function(tempOrderConv){
	    OrderConv = $odataresource(urlService.baseURL  + 'Orders(' + orderService.currentOrder.ID + ')/OrderConvs', 'id');
		
		var myOrderConv = new OrderConv();
		myOrderConv.Type = 'TEXT';
		myOrderConv.Owner = 'P';
		myOrderConv.Description = tempOrderConv.Description;
		
		myOrderConv.$save(
		    function(myOrderConv){
				$state.go('p-sm.orderDetailPage');
			},function(myOrderConv){
			}
		);
	}
})

.controller('MainCtrl', ['$scope', function ($scope) {
  $scope.selectables = [
    1, 2, 3
  ];

  $scope.longList  = [];
  for(var i=0;i<1000; i++){
    $scope.longList.push(i);
  }

  $scope.selectableNames =  [
    { name : "Mauro", role : "black hat"}, 
    { name : "Silvia", role : "pineye"},
    { name : "Merlino", role : "little canaglia"},
  ];

  $scope.someSetModel = 'Mauro';

  $scope.getOpt = function(option){
    return option.name + ":" + option.role;
  };
  
  $scope.shoutLoud = function(newValuea, oldValue){
    alert("changed from " + JSON.stringify(oldValue) + " to " + JSON.stringify(newValuea));
  };
  
  $scope.shoutReset = function(){
    alert("value was reset!");
  };
  
}])



.controller('chatCtrl', function($scope, $rootScope, $state, $stateParams, MockService,
    $ionicActionSheet,
    $ionicPopup, $ionicScrollDelegate, $timeout, $interval,orderService,urlService,$odataresource) {
		
		/*
    orderConvs = 
        $odataresource(urlService.baseURL  + "Orders("+ 40+")/OrderConvs")
        .odata()
        .query();
	currentOrder=$odataresource(urlService.baseURL  + "Orders("+40 +")")
        .odata()
        .single();

    // mock acquiring data via $stateParams
    $scope.toUser = {
      _id: '534b8e5aaa5e7afc1b23e69b',
      pic: currentOrder.CTDoctor.Avatar,
      username: currentOrder.CTDoctor.Name
    }

    // this could be on $rootScope rather than in $stateParams
    $scope.user = {
      _id: '534b8fb2aa5e7afc1b23e69c',
      pic: currentOrder.CTPatient.Avatar,
      username: currentOrder.CTPatient.Name
    };
	*/
	
	$scope.user = {
      _id: '534b8e5aaa5e7afc1b23e69b',
      Avatar: 'patient_default01.png',
      Name: '老王'
    }

    // this could be on $rootScope rather than in $stateParams
    $scope.toUser = {
      _id: '534b8fb2aa5e7afc1b23e69c',
      Avatar: 'doctor_default01.png',
      Name: '张医师'
    };

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
      MockService.getUserMessages({
        toUserId: $scope.toUser._id
      }).then(function(data) {
        $scope.doneLoading = true;
        $scope.messages = data.messages;

        $timeout(function() {
          viewScroll.scrollBottom();
        }, 0);
      });
    }

    $scope.$watch('input.message', function(newValue, oldValue) {
      console.log('input.message $watch, newValue ' + newValue);
      if (!newValue) newValue = '';
      localStorage['userMessage-' + $scope.toUser._id] = newValue;
    });

    $scope.sendMessage = function(sendMessageForm) {
      var message = {
        toId: $scope.toUser._id,
        text: $scope.input.message
      };

      // if you do a web service call this will be needed as well as before the viewScroll calls
      // you can't see the effect of this in the browser it needs to be used on a real device
      // for some reason the one time blur event is not firing in the browser but does on devices
      keepKeyboardOpen();
      
      //MockService.sendMessage(message).then(function(data) {
      $scope.input.message = '';

      message._id = new Date().getTime(); // :~)
      message.date = new Date();
      message.username = $scope.user.username;
      message.userId = $scope.user._id;
      message.pic = $scope.user.picture;

      $scope.messages.push(message);

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

function getMockMessages() {
  return {"messages":[{"_id":"535d625f898df4e80e2a125e","text":"Ionic has changed the game for hybrid app development.","userId":"534b8fb2aa5e7afc1b23e69c","date":"2014-04-27T20:02:39.082Z","read":true,"readDate":"2014-12-01T06:27:37.944Z"},{"_id":"535f13ffee3b2a68112b9fc0","text":"I like Ionic better than ice cream!","userId":"534b8e5aaa5e7afc1b23e69b","date":"2014-04-29T02:52:47.706Z","read":true,"readDate":"2014-12-01T06:27:37.944Z"},{"_id":"546a5843fd4c5d581efa263a","text":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","userId":"534b8fb2aa5e7afc1b23e69c","date":"2014-11-17T20:19:15.289Z","read":true,"readDate":"2014-12-01T06:27:38.328Z"},{"_id":"54764399ab43d1d4113abfd1","text":"Am I dreaming?","userId":"534b8e5aaa5e7afc1b23e69b","date":"2014-11-26T21:18:17.591Z","read":true,"readDate":"2014-12-01T06:27:38.337Z"},{"_id":"547643aeab43d1d4113abfd2","text":"Is this magic?","userId":"534b8fb2aa5e7afc1b23e69c","date":"2014-11-26T21:18:38.549Z","read":true,"readDate":"2014-12-01T06:27:38.338Z"},{"_id":"547815dbab43d1d4113abfef","text":"Gee wiz, this is something special.","userId":"534b8e5aaa5e7afc1b23e69b","date":"2014-11-28T06:27:40.001Z","read":true,"readDate":"2014-12-01T06:27:38.338Z"},{"_id":"54781c69ab43d1d4113abff0","text":"I think I like Ionic more than I like ice cream!","userId":"534b8fb2aa5e7afc1b23e69c","date":"2014-11-28T06:55:37.350Z","read":true,"readDate":"2014-12-01T06:27:38.338Z"},{"_id":"54781ca4ab43d1d4113abff1","text":"Yea, it's pretty sweet","userId":"534b8e5aaa5e7afc1b23e69b","date":"2014-11-28T06:56:36.472Z","read":true,"readDate":"2014-12-01T06:27:38.338Z"},{"_id":"5478df86ab43d1d4113abff4","text":"Wow, this is really something huh?","userId":"534b8fb2aa5e7afc1b23e69c","date":"2014-11-28T20:48:06.572Z","read":true,"readDate":"2014-12-01T06:27:38.339Z"},{"_id":"54781ca4ab43d1d4113abff1","text":"Create amazing apps - ionicframework.com","userId":"534b8e5aaa5e7afc1b23e69b","date":"2014-11-29T06:56:36.472Z","read":true,"readDate":"2014-12-01T06:27:38.338Z"}],"unread":0};
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

 